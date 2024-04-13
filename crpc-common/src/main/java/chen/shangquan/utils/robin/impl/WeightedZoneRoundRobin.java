package chen.shangquan.utils.robin.impl;

import chen.shangquan.utils.robin.Robin;

import java.util.List;

/**
 * 加权区域轮询法
 */
public class WeightedZoneRoundRobin implements Robin {
    private int currentIndex = 0;
    private List<String> zones;
    private List<List<String>> serversInZones;
    private List<Integer> weights;
    private int totalWeight;

    public WeightedZoneRoundRobin(List<String> zones, List<List<String>> serversInZones, List<Integer> weights) {
        this.zones = zones;
        this.serversInZones = serversInZones;
        this.weights = weights;
        this.totalWeight = weights.stream().mapToInt(Integer::intValue).sum();
    }

    public String getServer() {
        int weightSum = 0;
        for (int i = 0; i < zones.size(); i++) {
            weightSum += weights.get(i);
            if (currentIndex < weightSum) {
                int zoneIndex = i;
                int serverIndex = currentIndex % serversInZones.get(zoneIndex).size();
                String server = serversInZones.get(zoneIndex).get(serverIndex);
                currentIndex /= serversInZones.get(zoneIndex).size();
                currentIndex -= weightSum - weights.get(zoneIndex);
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
