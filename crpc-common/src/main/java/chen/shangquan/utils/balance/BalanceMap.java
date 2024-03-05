package chen.shangquan.utils.balance;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class BalanceMap {
    private static final Map<String, WeakReference<LoadBalancing>> BALANCING_MAP = new WeakHashMap<>();
    private static final Map<String, WeakReference<LoadBalancing>> BALANCING_SERVER_MAP = new WeakHashMap<>();

    /**
     * 如果需要更新负载均衡，执行重新 put 一个就行
     */
    public static void put(String serviceName, LoadBalancing loadBalancing) {
        BALANCING_MAP.put(serviceName, new WeakReference<>(loadBalancing));
    }

    public static LoadBalancing get(String serviceName) {
        WeakReference<LoadBalancing> reference = BALANCING_MAP.get(serviceName);
        if (reference != null) {
            return reference.get();
        } else {
            return null;
        }
    }

    /**
     * 如果需要更新负载均衡，执行重新 put 一个就行
     */
    public static void putServer(String serviceName, LoadBalancing loadBalancing) {
        BALANCING_SERVER_MAP.put(serviceName, new WeakReference<>(loadBalancing));
    }

    public static LoadBalancing getServer(String serviceName) {
        WeakReference<LoadBalancing> reference = BALANCING_SERVER_MAP.get(serviceName);
        if (reference != null) {
            return reference.get();
        } else {
            return null;
        }
    }
}
