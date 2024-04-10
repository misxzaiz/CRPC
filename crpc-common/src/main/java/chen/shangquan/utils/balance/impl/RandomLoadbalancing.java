package chen.shangquan.utils.balance.impl;

import chen.shangquan.crpc.model.po.ServerInfo;
import chen.shangquan.utils.balance.LoadBalancing;

import java.util.List;
import java.util.Random;

public class RandomLoadbalancing
        implements LoadBalancing
{
    /**
     * 元素：唯一标识，如 CRPC 中是 ip + port
     */
    private List<ServerInfo> list = null;

    private final Random random = new Random();

    public RandomLoadbalancing(List<ServerInfo> list) {
        this.list = list;
    }

    @Override
    public ServerInfo loadBalancing() {
        int randomNumber = random.nextInt(list.size());
        return list.get(randomNumber);
    }
}
