package chen.shangquan.utils.balance.impl;

import chen.shangquan.crpc.model.po.ServerInfo;
import chen.shangquan.crpc.network.data.RpcRequest;
import chen.shangquan.crpc.network.thread.RpcRequestLocalThread;
import chen.shangquan.utils.balance.LoadBalancing;
import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class WeightAreaLoadBalancing implements LoadBalancing {
    private final List<ServerInfo> list;
    private final List<Integer> globalExecutionOrder = new ArrayList<>();
    private final AtomicInteger globalIndex = new AtomicInteger(0);
    private final Map<String, List<Integer>> regionExecutionOrder = new HashMap<>();
    private final Map<String, AtomicInteger> regionIndexMap = new HashMap<>();
    public WeightAreaLoadBalancing(List<ServerInfo> list) {
        this.list = list;
        setExecutionOrder();
    }

    private void setExecutionOrder() {

        // 每个服务的权重
        Map<Integer, Integer> serviceCalls = new HashMap<>();
        // 所有服务的总权重
        int totalWeight = 0;
        // 每个地区每个服务的权重
        Map<String, Map<Integer, Integer>> regionServiceCalls = new HashMap<>();
        // 每个地区的总权重
        Map<String, Integer> regionTotalWeight = new HashMap<>();
        // 计算每个地区的总权重
        for (int i = 0; i < list.size(); i++) {
            ServerInfo server = list.get(i);
            String region = server.getArea();
            int weight = server.getWeight();
            serviceCalls.put(i, server.getWeight());
            totalWeight += server.getWeight();
            regionTotalWeight.put(region, regionTotalWeight.getOrDefault(region, 0) + weight);
            Map<Integer, Integer> map = regionServiceCalls.get(region);
            if (map == null) {
                map = new HashMap<>();
            }
            map.put(i, weight);
            regionServiceCalls.put(region, map);
        }

        // 循环生成执行顺序
        for (int i = 0; i < totalWeight; i++) {
            int selectedService = 1;
            int maxCalls = 0;

            // 找到当前调用次数最多的服务
            for (Map.Entry<Integer, Integer> entry : serviceCalls.entrySet()) {
                int service = entry.getKey();
                int calls = entry.getValue();

                if (calls > maxCalls) {
                    maxCalls = calls;
                    selectedService = service;
                }
            }

            // 将选中的服务添加到执行顺序列表中
            globalExecutionOrder.add(selectedService);

            // 更新调用次数
            serviceCalls.put(selectedService, maxCalls - 1);
        }

        for (String region : regionTotalWeight.keySet()) {
            Integer innerTotalWeight = regionTotalWeight.get(region);
            Map<Integer, Integer> innerServiceCalls = regionServiceCalls.get(region);
            List<Integer> executionOrder = new ArrayList<>();
            for (int i = 0; i < innerTotalWeight; i++) {
                int selectedService = 1;
                int maxCalls = 0;
                // 找到当前调用次数最多的服务
                for (Map.Entry<Integer, Integer> entry : innerServiceCalls.entrySet()) {
                    int service = entry.getKey();
                    int calls = entry.getValue();

                    if (calls > maxCalls) {
                        maxCalls = calls;
                        selectedService = service;
                    }
                }
                executionOrder.add(selectedService);

                // 更新调用次数
                innerServiceCalls.put(selectedService, maxCalls - 1);
            }
            regionExecutionOrder.put(region, executionOrder);
            regionIndexMap.put(region, new AtomicInteger(0));
        }
    }

    @Override
    public ServerInfo loadBalancing() {
        RpcRequest rpcRequest = RpcRequestLocalThread.getRpcRequest();
        log.info("WeightLoadBalancing.loadBalancing:{}", rpcRequest);
        RpcRequest request = null;
        if (rpcRequest != null) {
            request = BeanUtil.toBean(rpcRequest.getData(), RpcRequest.class);
        }

        assert request != null;
        String region = request.getArea() != null ? request.getArea() : rpcRequest.getArea();
        List<Integer> executionOrder = regionExecutionOrder.getOrDefault(region, Collections.emptyList());
        AtomicInteger index = regionIndexMap.getOrDefault(region, new AtomicInteger(0));

        if (executionOrder.isEmpty()) {
            log.info("全局负载均衡");
            int i = globalIndex.getAndIncrement();
            if (i >= list.size() - 1) {
                globalIndex.set(0);
            }
            ServerInfo serverInfo = list.get(globalExecutionOrder.get(i));
            log.info("WeightLoadBalancing.loadBalancing serverInfo:{}", serverInfo);
            return serverInfo;
        } else {

            int i = index.getAndIncrement();
            if (i >= executionOrder.size() - 1) {
                index.set(0);
            }
            ServerInfo serverInfo = list.get(executionOrder.get(i));
            log.info("区域负载均衡 serverInfo:{}", serverInfo);
            return serverInfo;
        }
    }
}
