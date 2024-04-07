package chen.shangquan.crpc.runner;

import chen.shangquan.crpc.center.CrpcRegisterCenter;
import chen.shangquan.crpc.model.po.ServerInfo;
import chen.shangquan.crpc.network.NetworkServer;
import chen.shangquan.crpc.server.ServerRegister;
import chen.shangquan.utils.resource.CrpcConfigUtils;
import chen.shangquan.utils.type.ClassTypeUtils;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
public class ServerApplicationRunner implements ApplicationRunner {
    @Resource
    private ApplicationContext context;

    public static List<Class<?>> getServerRegisterClasses(String packageName) throws Exception {
        List<Class<?>> classes = new ArrayList<>();
        String packagePath = packageName.replace(".", "/");
        URL url = Thread.currentThread().getContextClassLoader().getResource(packagePath);
        if (url == null) {
            throw new RuntimeException("Package " + packageName + " not found.");
        }

        File packageDir = new File(url.toURI());
        scanClasses(packageName, packageDir, classes);

        return classes;
    }

    private static void scanClasses(String packageName, File dir, List<Class<?>> classes) throws ClassNotFoundException {
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().substring(0, file.getName().lastIndexOf("."));
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(chen.shangquan.crpc.server.annotation.ServerRegister.class)) {
                    classes.add(clazz);
                }
            } else if (file.isDirectory()) {
                scanClasses(packageName + "." + file.getName(), file, classes);
            }
        }
    }

    public String getSpringbootApplicationPackageName() {
        String[] springbootApplication = context.getBeanNamesForAnnotation(SpringBootApplication.class);
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition(springbootApplication[0]);
        String beanClassName = beanDefinition.getBeanClassName();
        String s1 = null;
        assert beanClassName != null;
        if (!beanClassName.contains(springbootApplication[0])) {
            String tmp = springbootApplication[0];
            s1 = ClassTypeUtils.setFirstToUpperCase(tmp);
        } else {
            s1 = springbootApplication[0];
        }
        String[] split = beanClassName.split("." + s1);
        String s = split[0];
        if (StrUtil.isBlank(s)) {
            s = "com.example";
        }
        return s;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 所有包含 ServerRegister 注解的类和注解
        String springbootApplicationPackageName = getSpringbootApplicationPackageName();
        try {
            List<Class<?>> serverRegisterClasses = getServerRegisterClasses(springbootApplicationPackageName);
            ServerInfo server = CrpcConfigUtils.getServerInfo();
            // TODO 从 CrpcConfigUtils 读取 zookeeper 的连接
            CrpcRegisterCenter.connect();
            ServerRegister.registerServerClass(server, serverRegisterClasses);
            NetworkServer.start(server.getPort());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
