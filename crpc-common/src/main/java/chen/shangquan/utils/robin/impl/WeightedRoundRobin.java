package chen.shangquan.utils.robin.impl;

import chen.shangquan.utils.robin.Robin;

import java.util.List;

/**
 * 加权轮询法
 */
public class WeightedRoundRobin implements Robin {
    private int currentIndex = 0;
    private List<String> servers;
    private List<Integer> weights;
    private int totalWeight;

    public WeightedRoundRobin(List<String> servers, List<Integer> weights) {
        this.servers = servers;
        this.weights = weights;
        this.totalWeight = weights.stream().mapToInt(Integer::intValue).sum();
    }

    public String getServer() {
        int weightSum = 0;
        for (int i = 0; i < servers.size(); i++) {
            weightSum += weights.get(i);
            if (currentIndex < weightSum) {
                String server = servers.get(i);
                currentIndex -= weightSum - weights.get(i);
                return server;
            }
        }
        return null;
    }

    @Override
    public String getServer(String param) {
        return null;
    }
}
