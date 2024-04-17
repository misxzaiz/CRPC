package chen.shangquan.modules.test.impl;

import chen.shangquan.crpc.model.po.ServerInfo;
import chen.shangquan.crpc.server.annotation.ServerRegister;

@ServerRegister(serverName = "CrpcServer", className = "TestService")
public interface TestService {
    public String test(String test);
    public void testVoid(String test);
    public ServerInfo testPo(String test);
    public String testException(String test);
}
