package chen.shangquan.modules.test.impl;

import chen.shangquan.crpc.network.data.RpcRequest;
import chen.shangquan.crpc.server.annotation.ServerRegister;

@ServerRegister(serverName = "CrpcServer", className = "CenterService", version = "V1")
public interface CenterService {
    public Object dealMethod(RpcRequest request);
}
