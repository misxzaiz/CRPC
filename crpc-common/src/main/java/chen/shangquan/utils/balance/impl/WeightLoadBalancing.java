package chen.shangquan.utils.balance.impl;

import chen.shangquan.crpc.model.po.ServerInfo;
import chen.shangquan.crpc.network.data.RpcRequest;
import chen.shangquan.crpc.network.thread.RpcRequestLocalThread;
import chen.shangquan.utils.balance.LoadBalancing;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class WeightLoadBalancing implements LoadBalancing {

    private List<ServerInfo> list;

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
    public WeightLoadBalancing(List<ServerInfo> list) {
        this.list = list;
        setExecutionOrder();
    }

    private void setExecutionOrder() {
        Map<Integer, Integer> serviceCalls = new HashMap<>();
        // 计算总权重
        int totalWeight = 0;
        for (int i = 0; i < list.size(); i++) {
            ServerInfo server = list.get(i);
            serviceCalls.put(i, server.getWeight());
            totalWeight += server.getWeight();
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
    public ServerInfo loadBalancing() {
        RpcRequest rpcRequest = RpcRequestLocalThread.getRpcRequest();
        log.info("WeightLoadBalancing.loadBalancing:{}",rpcRequest);
        int i = index.getAndIncrement();
        // 因为 getAndIncrement 会加一
        if (i > executionOrder.size() - 2) {
            index.set(0);
        }
        return list.get(executionOrder.get(i));
    }
}
