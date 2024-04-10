package chen.shangquan.crpc.server.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author chenshangquan
 * @date 3/11/2024
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ServerRegister {
    /**
     * 服务名
     */
    String serverName() default "";

    /**
     * 类名
     */
    String className();

    /**
     * 版本
     */
    String version() default "V1";

    /**
     * 地区
     */
    String area() default "default";
}

