package chen.shangquan.crpc.bean;

import chen.shangquan.agent.ServiceAgent;
import chen.shangquan.crpc.runner.ServerApplicationRunner;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AppConfig implements ApplicationRunner {
    @Resource
    ServerApplicationRunner serverApplicationRunner;
    @Autowired
    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String springbootApplicationPackageName = serverApplicationRunner.getSpringbootApplicationPackageName();
        List<Class<?>> serverRegisterClasses = ServerApplicationRunner.getServerRegisterClasses(springbootApplicationPackageName);
        List<Class<?>> interfaceList = serverRegisterClasses.stream().filter(Class::isInterface).collect(Collectors.toList());
        for (Class<?> clazz : interfaceList) {
            ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
            proxyFactoryBean.setInterfaces(clazz);
            // 根据接口动态设置目标对象
            proxyFactoryBean.setTarget(ServiceAgent.createService(clazz));
            // 注册为Bean
            beanFactory.registerSingleton(clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1), proxyFactoryBean);
        }
    }
}
