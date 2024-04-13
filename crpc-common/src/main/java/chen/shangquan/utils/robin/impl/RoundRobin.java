package chen.shangquan.utils.robin.impl;

import chen.shangquan.utils.robin.Robin;

import java.util.List;

/**
 * 轮询法
 */
public class RoundRobin implements Robin {
    private int currentIndex = 0;
    private final List<String> servers;

    public RoundRobin(List<String> servers) {
        this.servers = servers;
    }

    public String getServer() {
        String server = servers.get(currentIndex);
        currentIndex = (currentIndex + 1) % servers.size();
        return server;
    }

    @Override
    public String getServer(String param) {
        return null;
    }
}
