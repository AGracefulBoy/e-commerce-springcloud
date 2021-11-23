package org.example.conf;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * 配置安全认证以便其他的微服务可以注册
 */
@Configuration
public class SecuritySecureConfig extends WebSecurityConfigurerAdapter {

    /** 应用上下文路径*/
    private final String adminContextPath;

    public SecuritySecureConfig(AdminServerProperties adminServerProperties){
        this.adminContextPath = adminServerProperties.getContextPath();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler =
                new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(adminContextPath + "/");

        http.authorizeRequests()
                // 1.配置公共资源和登录页可以公开访问
                .antMatchers(adminContextPath+"/assets/**").permitAll()
                .antMatchers(adminContextPath+"/login").permitAll()
                // 2.其他请求必须经过验证
                .anyRequest().authenticated()
                .and()
                // 3.配置登录和登出
                .formLogin().loginPage(adminContextPath+"/login").successHandler(successHandler)
                .and()
                .logout().logoutUrl(adminContextPath+"/logout")
                .and()
                // 4.开启http basic 支持，其他的服务模块注册时需要使用
                .httpBasic()
                .and()
                // 5.开启基于cookie的csrf的保护
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                // 6.忽略这些路径的csrf保护，以便其他的模块可以注册
                .ignoringAntMatchers(
                        adminContextPath+"/instances",
                        adminContextPath+"/actuator/**"
                );
    }
}
