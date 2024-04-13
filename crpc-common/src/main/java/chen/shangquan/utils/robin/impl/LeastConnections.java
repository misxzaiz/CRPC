package chen.shangquan.utils.robin.impl;

import chen.shangquan.utils.robin.Robin;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 最少连接法
 */
public class LeastConnections implements Robin {
    private Map<String, AtomicInteger> connections;
    private Comparator<Map.Entry<String, AtomicInteger>> comparator;

    public LeastConnections() {
        connections = new ConcurrentHashMap<>();
        comparator = (entry1, entry2) -> entry1.getValue().get() - entry2.getValue().get();
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

    public String getServer() {
        return connections.entrySet().stream().min(comparator).map(Map.Entry::getKey).orElse(null);
    }

    @Override
    public String getServer(String param) {
        return null;
    }
}