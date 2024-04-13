package chen.shangquan.utils.robin.impl;

import chen.shangquan.utils.robin.Robin;

import java.util.List;
import java.util.Random;

/**
 * 随机法
 */
public class RandomLoadBalancer implements Robin {
    private List<String> servers;
    private Random random;

    public RandomLoadBalancer(List<String> servers) {
        this.servers = servers;
        this.random = new Random();
    }

    public String getServer() {
        int index = random.nextInt(servers.size());
        return servers.get(index);
    }

    @Override
    public String getServer(String param) {
        return null;
    }
}
