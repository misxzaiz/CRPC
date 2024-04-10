package chen.shangquan.utils.balance.impl;

import chen.shangquan.crpc.model.po.ServerInfo;
import chen.shangquan.utils.balance.LoadBalancing;

import java.util.List;

public class AreaLoadbalancing implements LoadBalancing {
    private List<ServerInfo> list = null;
    public AreaLoadbalancing(List<ServerInfo> list) {
        this.list = list;
    }
    @Override
    public ServerInfo loadBalancing() {
        return null;
    }
}
