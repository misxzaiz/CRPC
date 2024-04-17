package chen.shangquan.utils.balance.impl;

import chen.shangquan.crpc.model.po.ServerInfo;
import chen.shangquan.crpc.network.data.RpcRequest;
import chen.shangquan.crpc.network.thread.RpcRequestLocalThread;
import chen.shangquan.utils.balance.LoadBalancing;
import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class WeightLoadBalancing implements LoadBalancing {

    private List<ServerInfo> list;

    private final AtomicInteger index = new AtomicInteger(0);

    public final List<Integer> executionOrder = new ArrayList<>();

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

        List<Integer> preServices = new ArrayList<>();
        // 循环生成执行顺序
        for (int i = 0; i < totalWeight; i++) {
            // 找出权重最大的，并对比上一个是否和上一个相等，存在相等就选相等，不存在就随机

            Map.Entry<Integer, Integer> integerIntegerEntry1 = serviceCalls.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get();
            Integer value = integerIntegerEntry1.getValue();
            List<Map.Entry<Integer, Integer>> maxEntries = serviceCalls.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(value))
                    .toList();

            Map.Entry<Integer, Integer> integerIntegerEntry = null;
            if (maxEntries.size() == 1 || preServices.size() == 0) {
                integerIntegerEntry = maxEntries.get(0);
            } else {
                for (Integer preService : preServices) {
                    List<Map.Entry<Integer, Integer>> collect = maxEntries.stream().filter(e -> Objects.equals(e.getKey(), preService)).collect(Collectors.toList());
                    if (collect.size() != 0) {
                        Map.Entry<Integer, Integer> integerIntegerEntry2 = collect.get(0);
                        if (executionOrder.size() != 0) {
                            Integer integer = executionOrder.get(executionOrder.size() - 1);
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
                    integerIntegerEntry = maxEntries.stream().filter(e -> !preServices.contains(e.getKey())).toList().get(0);
                }
            }

            preServices.add(integerIntegerEntry.getKey());
            // 将选中的服务添加到执行顺序列表中
            executionOrder.add(integerIntegerEntry.getKey());

            // 更新调用次数
            serviceCalls.put(integerIntegerEntry.getKey(), integerIntegerEntry.getValue() - 1);
        }
    }

    @Override
    public ServerInfo loadBalancing() {
//        RpcRequest rpcRequest = RpcRequestLocalThread.getRpcRequest();
//        log.info("WeightLoadBalancing.loadBalancing:{}",rpcRequest);
//        RpcRequest request = null;
//        // rpcRequest.getData() 中的地区才是正确的地区，因为可以在@ServerRegister注解中指定具体服务的地区，所以按外层的不一定正确
//        if (rpcRequest != null) {
//            request = BeanUtil.toBean(rpcRequest.getData(), RpcRequest.class);
//        }
        int i = index.getAndIncrement();
        // 因为 getAndIncrement 会加一
        if (i > executionOrder.size() - 2) {
            index.set(0);
        }
        return list.get(executionOrder.get(i));
    }

}
