package chen.shangquan.center.modules.test.service;

import chen.shangquan.crpc.model.po.ServerInfo;
import chen.shangquan.crpc.server.annotation.ServerRegister;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("TestService")
@ServerRegister(className = "TestService", version = "V1")
public class TestService {
    public String test(String test) {
        log.info("TestService.test test:{}", test);
        return "Hello, " + test + "!"; // 返回一个字符串
    }

    public void testVoid(String test) {
        log.info("TestService.testVoid test:{}", test);
    }

    public ServerInfo testPo(String test) {
        log.info("TestService.testPo test:{}", test);
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setArea("testPo");
        return serverInfo;
    }

    public String testException(String test) {
        log.info("TestService.testException test:{}", test);
        String s = "Hello, " + test + "!";
        int i = 1 / 0;
        if (true) {
            throw new RuntimeException();
        }
        return s; // 返回一个字符串
    }
}
