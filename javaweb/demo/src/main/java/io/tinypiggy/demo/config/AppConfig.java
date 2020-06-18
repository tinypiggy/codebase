package io.tinypiggy.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    // 跨域资源共享(CORS)
    // Access-Control-Allow-Origin 值写成http://example.com这种形式指明谁可以访问，或者传*表示谁都可以
    // Access-Control-Expose-Headers 哪些Header能在响应中列出，默认只有6个Header可以。服务器其实可以返回很多Header，但是浏览器暴露给js的Header是经过筛选的
    // Access-Control-Allow-Credentials 了Access-Control-Allow-Credentials: true，就不能设置Access-Control-Allow-Origin:'*'了
    // Access-Control-Max-Age 预检请求的结果的缓存时间，单位为秒
    // Access-Control-Allow-Methods 预检请求中指明允许的HTTP方法
    // Access-Control-Allow-Headers 预检请求中指明允许的Header
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        //Spring还支持使用@CrossOrigin注解来更精细地控制跨域的粒度，
        // 比如加在@Controller类上，控制对应路径的跨域设置，
        // 更可以添加到具体的控制器方法上
        registry.addMapping("/")
                .allowedOrigins("api.ejiayou.com")
                .allowedMethods("*")
                .allowedHeaders("header1", "header2")
                .exposedHeaders("header1", "header2")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
