package chen.shangquan.utils.balance.impl;

import chen.shangquan.crpc.model.po.ServerInfo;
import chen.shangquan.crpc.network.data.RpcRequest;
import chen.shangquan.crpc.network.thread.RpcRequestLocalThread;
import chen.shangquan.crpc.runner.ServerApplicationRunner;
import chen.shangquan.utils.balance.LoadBalancing;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class WeightAreaLoadBalancing implements LoadBalancing {
    private final List<ServerInfo> list;
    private final List<Integer> globalExecutionOrder = new ArrayList<>();
    private final AtomicInteger globalIndex = new AtomicInteger(0);
    private final Map<String, List<Integer>> regionExecutionOrder = new HashMap<>();
    private final Map<String, AtomicInteger> regionIndexMap = new HashMap<>();
    public void show() {
        System.out.println("服务列表：" + list);
        System.out.println("全服务执行顺序：" + globalExecutionOrder);
        System.out.println("各区域服务执行顺序：" + regionExecutionOrder);
    }

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

        // 计算每个地区的总权重
        List<Integer> globalPreServices = new ArrayList<>();
        // 循环生成执行顺序
        for (int i = 0; i < totalWeight; i++) {
            Map.Entry<Integer, Integer> integerIntegerEntry1 = serviceCalls.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get();
            Integer value = integerIntegerEntry1.getValue();
            List<Map.Entry<Integer, Integer>> maxEntries = serviceCalls.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(value))
                    .toList();

            Map.Entry<Integer, Integer> integerIntegerEntry = null;
            if (maxEntries.size() == 1 || globalPreServices.size() == 0) {
                integerIntegerEntry = maxEntries.get(0);
            } else {
                for (Integer preService : globalPreServices) {
                    List<Map.Entry<Integer, Integer>> collect = maxEntries.stream().filter(e -> Objects.equals(e.getKey(), preService)).collect(Collectors.toList());
                    if (collect.size() != 0) {
                        Map.Entry<Integer, Integer> integerIntegerEntry2 = collect.get(0);
                        if (globalExecutionOrder.size() != 0) {
                            Integer integer = globalExecutionOrder.get(globalExecutionOrder.size() - 1);
                            Integer key = integerIntegerEntry2.getKey();
                            if (Objects.equals(integer, key)) {
                                continue;
                            }
                        }
                        integerIntegerEntry = collect.get(0);
                        break;
                    }
                }
                if (integerIntegerEntry == null) {
                    integerIntegerEntry = maxEntries.stream().filter(e -> !globalPreServices.contains(e.getKey())).toList().get(0);
                }
            }

            globalPreServices.add(integerIntegerEntry.getKey());
            // 将选中的服务添加到执行顺序列表中
            globalExecutionOrder.add(integerIntegerEntry.getKey());

            // 更新调用次数
            serviceCalls.put(integerIntegerEntry.getKey(), integerIntegerEntry.getValue() - 1);
        }

        for (String region : regionTotalWeight.keySet()) {
            Integer innerTotalWeight = regionTotalWeight.get(region);
            Map<Integer, Integer> innerServiceCalls = regionServiceCalls.get(region);
            List<Integer> executionOrder = new ArrayList<>();
            List<Integer> regionPreServices = new ArrayList<>();
            for (int i = 0; i < innerTotalWeight; i++) {
                Map.Entry<Integer, Integer> integerIntegerEntry1 = innerServiceCalls.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get();
                Integer value = integerIntegerEntry1.getValue();
                List<Map.Entry<Integer, Integer>> maxEntries = innerServiceCalls.entrySet().stream()
                        .filter(entry -> entry.getValue().equals(value))
                        .toList();

                Map.Entry<Integer, Integer> integerIntegerEntry = null;
                if (maxEntries.size() == 1 || regionPreServices.size() == 0) {
                    integerIntegerEntry = maxEntries.get(0);
                } else {
                    for (Integer preService : regionPreServices) {
                        List<Map.Entry<Integer, Integer>> collect = maxEntries.stream().filter(e -> Objects.equals(e.getKey(), preService)).collect(Collectors.toList());
                        if (collect.size() != 0) {
                            Map.Entry<Integer, Integer> integerIntegerEntry2 = collect.get(0);
                            if (regionPreServices.size() != 0) {
                                Integer integer = regionPreServices.get(regionPreServices.size() - 1);
                                Integer key = integerIntegerEntry2.getKey();
                                if (Objects.equals(integer, key)) {
                                    continue;
                                }
                            }
                            integerIntegerEntry = collect.get(0);
                            break;
                        }
                    }
                    if (integerIntegerEntry == null) {
                        integerIntegerEntry = maxEntries.stream().filter(e -> !regionPreServices.contains(e.getKey())).toList().get(0);
                    }
                }

                regionPreServices.add(integerIntegerEntry.getKey());
                // 将选中的服务添加到执行顺序列表中
                executionOrder.add(integerIntegerEntry.getKey());

                // 更新调用次数
                innerServiceCalls.put(integerIntegerEntry.getKey(), integerIntegerEntry.getValue() - 1);
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
        String region = null;
        if (request != null) {
            region = request.getArea() != null ? request.getArea() : rpcRequest.getArea();
        }

        List<Integer> executionOrder = regionExecutionOrder.getOrDefault(region, Collections.emptyList());
        AtomicInteger index = regionIndexMap.getOrDefault(region, new AtomicInteger(0));

        if (executionOrder.isEmpty()) {
            int i = globalIndex.getAndIncrement();
//            log.info("全局负载均衡 i:{}", i);
            if (i >= globalExecutionOrder.size() - 1) {
                globalIndex.set(0);
            }
            ServerInfo serverInfo = list.get(globalExecutionOrder.get(i));
//            log.info("WeightLoadBalancing.loadBalancing serverInfo:{}", serverInfo);
            return serverInfo;
        } else {

            int i = index.getAndIncrement();
            if (i >= executionOrder.size() - 1) {
                index.set(0);
            }
            Integer integer = executionOrder.get(i);
            ServerInfo serverInfo = list.get(integer);
//            log.info("区域负载均衡 serverInfo:{}", serverInfo);
            return serverInfo;
        }
    }
}
