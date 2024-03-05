package chen.shangquan.utils.balance;

import chen.shangquan.crpc.model.po.ServerInfo;

import java.util.List;

public interface LoadBalancing {
    ServerInfo loadBalancing();
}
