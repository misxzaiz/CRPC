package chen.shangquan.agent;

import java.lang.reflect.Proxy;

/**
 * @author chenshangquan
 * @date 12/28/2023
 */
public class ServiceAgent {
    public static <T> T createService(Class<T> serviceInterface) {
        // 创建代理对象
        return serviceInterface.cast(
                Proxy.newProxyInstance(
                        serviceInterface.getClassLoader(),
                        new Class<?>[]{serviceInterface},
                        new RemoteInvocationHandler()));
    }
}
