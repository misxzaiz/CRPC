package chen.shangquan.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 对所有接口路径都允许跨域
                .allowedOriginPatterns("*")  // 允许指定来源的跨域请求
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 允许的请求方法
                .allowCredentials(true);  // 允许请求携带凭据（如 Cookies）
    }
}
