package chen.shangquan.modules.test.service;

import chen.shangquan.crpc.server.annotation.ServerRegister;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ServerRegister(className = "TestService")
public class TestService {
    public String show() {
        return "这里是TestService";
    }
}
