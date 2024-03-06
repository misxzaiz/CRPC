package chen.shangquan.modules.test.service;

import chen.shangquan.agent.ServiceAgent;
import chen.shangquan.crpc.model.po.ServerInfo;
import chen.shangquan.crpc.server.annotation.ServerRegister;
import chen.shangquan.modules.test.impl.TestService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@ServerRegister(className = "DemoService", version = "V1")
public class DemoService {
    public String test(String test) {
        TestService service = ServiceAgent.createService(TestService.class);
        String test1 = service.test(test);
        service.testVoid("uahjd");
        ServerInfo serverInfo = service.testPo("sgdiuaoi");
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }
}
