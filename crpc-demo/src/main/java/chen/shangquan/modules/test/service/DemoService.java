package chen.shangquan.modules.test.service;

import chen.shangquan.crpc.server.annotation.ServerRegister;
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

    public String testTime() {
        return "OK!";
    }

    public String show(String test) {
        String testResult = testService.show();
        return test + "  " + testResult + " " + LocalDateTime.now()
                .format(DateTimeFormatter
                        .ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                );
    }

    public String test(String test) {
        String testResult = testService.test(test);
        return testResult + " " + LocalDateTime.now()
                .format(DateTimeFormatter
                        .ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                );
    }

    public String testException(String test) {
        String testResult = testService.test(test);
        testService.testException(test);
        return testResult + " " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }

    public String testBalance() {
        testService.testVoid("");
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }
}
