package chen.shangquan.utils.robin.impl;

import chen.shangquan.utils.robin.Robin;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 加权最少连接法
 */
public class WeightedLeastConnections implements Robin {
    private Map<String, AtomicInteger> connections;
    private Map<String, Integer> weights;
    private Comparator<Map.Entry<String, AtomicInteger>> comparator;

    public WeightedLeastConnections(Map<String, Integer> weights) {
        connections = new ConcurrentHashMap<>();
        this.weights = weights;
        comparator = (entry1, entry2) -> {
            int cmp = entry1.getValue().get() * weights.get(entry1.getKey()) - entry2.getValue().get() * weights.get(entry2.getKey());
            if (cmp != 0) {
                return cmp;
            } else {
                return entry1.getKey().compareTo(entry2.getKey());
            }
        };
    }

    public void addServer(String server) {
        connections.putIfAbsent(server, new AtomicInteger(0));
    }

    public void removeServer(String server) {
        connections.remove(server);
    }

    public void incrementConnection(String server) {
        connections.get(server).incrementAndGet();
    }

    public void decrementConnection(String server) {
        connections.get(server).decrementAndGet();
    }

    @Override
    public String getServer() {
        return connections.entrySet().stream().min(comparator).map(Map.Entry::getKey).orElse(null);
    }

    @Override
    public String getServer(String param) {
        return null;
    }
}