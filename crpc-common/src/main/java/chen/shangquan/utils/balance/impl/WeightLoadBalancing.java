package chen.shangquan.utils.balance.impl;

import chen.shangquan.utils.balance.LoadBalancing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class WeightLoadBalancing implements LoadBalancing {

    private List<Node> nodeList = new ArrayList<>();

    private final AtomicInteger index = new AtomicInteger(0);

    private final List<Integer> executionOrder = new ArrayList<>();

    private class Node {
        String mark;
        int weight;
    }

    /**
     * key：mark：ip + port
     * value：weight
     */
    public WeightLoadBalancing(Map<String, Integer> map) {
        map.forEach((key, value) -> {
            Node node = new Node();
            node.mark = key;
            node.weight = value;
            nodeList.add(node);
        });
        setExecutionOrder();
    }

    private void setExecutionOrder() {
        Map<Integer, Integer> serviceCalls = new HashMap<>();
        // 计算总权重
        int totalWeight = 0;
        for (int i = 0; i < nodeList.size(); i++) {
            Node server = nodeList.get(i);
            serviceCalls.put(i, server.weight);
            totalWeight += server.weight;
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
            executionOrder.add(selectedService);

            // 更新调用次数
            serviceCalls.put(selectedService, maxCalls - 1);
        }
    }

    @Override
    public String loadBalancing() {
        int i = index.getAndIncrement();
        if (i > executionOrder.size() - 1) {
            index.set(0);
        }
        return nodeList.get(executionOrder.get(i)).mark;
    }
}
