package chen.shangquan.utils.balance;

import chen.shangquan.crpc.model.po.ServerInfo;

public interface LoadBalancing {
    ServerInfo loadBalancing();
}
