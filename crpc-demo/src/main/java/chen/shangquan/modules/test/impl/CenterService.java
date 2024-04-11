package chen.shangquan.modules.test.impl;

import chen.shangquan.crpc.model.po.ServerInfo;
import chen.shangquan.crpc.network.data.RpcRequest;
import chen.shangquan.crpc.server.annotation.ServerRegister;

@ServerRegister(serverName = "CrpcServer", className = "CenterService", area = "BJJ")
public interface CenterService {
    public Object dealMethod(RpcRequest request);
    public ServerInfo getServerBalanceForServerInfo(String serverName);
}
