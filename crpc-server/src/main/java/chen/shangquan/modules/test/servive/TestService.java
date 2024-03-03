package chen.shangquan.modules.test.servive;

import chen.shangquan.crpc.server.annotation.ServerRegister;
import org.springframework.stereotype.Component;

@Component("TestService")
@ServerRegister(className = "TestService", version = "V1")
public class TestService {
    public String test(String test) {
        return "Hello, " + test + "!"; // 返回一个字符串
    }
}
