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
    private List<ServerInfo> list;
    private final Map<String, List<Integer>> regionExecutionOrder = new HashMap<>();
    private final Map<String, AtomicInteger> regionIndexMap = new HashMap<>();
    private final AtomicInteger globalIndex = new AtomicInteger(0);
    public WeightAreaLoadBalancing(List<ServerInfo> list) {
        this.list = list;
        setExecutionOrder();
    }

    private void setExecutionOrder() {
        Map<String, Integer> totalRegionWeight = new HashMap<>();

        // 计算每个地区的总权重
        for (ServerInfo server : list) {
            String region = server.getArea();
            int weight = server.getWeight();
            totalRegionWeight.put(region, totalRegionWeight.getOrDefault(region, 0) + weight);
        }

        // 初始化每个地区的执行顺序和索引
        for (String region : totalRegionWeight.keySet()) {
            int totalWeight = totalRegionWeight.get(region);
            List<Integer> executionOrder = new ArrayList<>();
            int serverIndex = 0;
            for (ServerInfo server : list) {
                if (server.getArea().equals(region)) {
                    for (int i = 0; i < server.getWeight(); i++) {
                        executionOrder.add(serverIndex);
                    }
                }
                serverIndex++;
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
            int i = globalIndex.getAndIncrement();
            if (i >= list.size() - 2) {
                globalIndex.set(0);
            }
            ServerInfo serverInfo = list.get(i);
            log.info("WeightLoadBalancing.loadBalancing serverInfo:{}", serverInfo);
            return serverInfo;
        } else {
            int i = index.getAndIncrement();
            if (i >= executionOrder.size() - 2) {
                index.set(0);
            }
            return list.get(executionOrder.get(i));
        }
    }
}
