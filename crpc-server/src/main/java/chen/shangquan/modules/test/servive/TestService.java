package chen.shangquan.modules.test.servive;

import chen.shangquan.crpc.model.po.ServerInfo;
import chen.shangquan.crpc.server.annotation.ServerRegister;
import org.springframework.stereotype.Component;

@Component("TestService")
@ServerRegister(className = "TestService", version = "V1")
public class TestService {
    public String test(String test) {
        String s = "Hello, " + test + "!";
        return s; // 返回一个字符串
    }

    public void testVoid(String test) {
        System.out.println("TestService + + testVoid +++");
    }

    public ServerInfo testPo(String test) {
        System.out.println("TestService + + testVoid +++");
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setArea("gduhao");
        return serverInfo;
    }
}
