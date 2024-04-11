package chen.shangquan.modules.test.service;

import chen.shangquan.crpc.model.po.ServerInfo;
import chen.shangquan.crpc.server.annotation.ServerRegister;
import chen.shangquan.modules.test.impl.CenterService;
import chen.shangquan.modules.test.impl.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@ServerRegister(className = "DemoService")
public class DemoService {
    @Resource
    private TestService testService;

    @Resource
    private CenterService centerService;

    public String test(String test) {
        String test1 = testService.test(test);
        testService.testVoid("uahjd");
        ServerInfo serverInfo = testService.testPo("sgdiuaoi");
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }

    public ServerInfo testBalance(String serverName) {
        return centerService.getServerBalanceForServerInfo(serverName);
    }
}
