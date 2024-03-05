package chen.shangquan.modules.test.service;

import chen.shangquan.agent.ServiceAgent;
import chen.shangquan.crpc.model.po.ServerInfo;
import chen.shangquan.crpc.server.annotation.ServerRegister;
import chen.shangquan.modules.test.impl.TestService;
import org.springframework.stereotype.Component;

@Component
@ServerRegister(className = "DemoService", version = "V1")
public class DemoService {
    public String test(String test) {
        TestService service = ServiceAgent.createService(TestService.class);
        String test1 = service.test(test);
        service.testVoid("uahjd");
        ServerInfo serverInfo = service.testPo("sgdiuaoi");
        return test1 + serverInfo;
    }
}
