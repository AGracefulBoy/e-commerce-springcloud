package org.example.conf;

import org.example.filter.LoginUserInfoInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * mvc 配置
 * @author zhoudashuai
 * @date 2021年12月14日 11:35 下午
 */
@Configuration
public class ImoocWebMvcConfig extends WebMvcConfigurationSupport {
    /**
     * 添加拦截器配置
     * @param registry
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        //添加用户身份统一登录拦截的拦截器
        registry.addInterceptor(new LoginUserInfoInterceptor())
                .addPathPatterns("/**").order(0);
    }

    /**
     * 让mvc加载swagger的静态资源
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INFO/resources/");
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INFO/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INFO/resources/webjars");

        super.addResourceHandlers(registry);
    }
}
