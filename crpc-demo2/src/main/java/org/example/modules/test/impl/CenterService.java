package org.example.modules.test.impl;

import chen.shangquan.crpc.network.data.RpcRequest;
import chen.shangquan.crpc.server.annotation.ServerRegister;

@ServerRegister(serverName = "CrpcServer", className = "CenterService")
public interface CenterService {
    public Object dealMethod(RpcRequest request);
}
