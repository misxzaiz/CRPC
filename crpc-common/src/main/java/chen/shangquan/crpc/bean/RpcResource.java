package chen.shangquan.crpc.bean;


import org.springframework.context.annotation.Lazy;

import javax.annotation.Resource;
import java.lang.annotation.*;

/**
 * TODO 暂时不起作用
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Resource
@Lazy
public @interface RpcResource {

}
