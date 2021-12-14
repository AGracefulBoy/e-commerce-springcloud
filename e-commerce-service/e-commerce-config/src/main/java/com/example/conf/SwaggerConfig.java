package com.example.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger 配置类
 * 原生 : /swagger-ui.html
 * 美化： /doc.html
 * @author zhoudashuai
 * @date 2021年12月14日 11:43 下午
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * swagger 实例Bean 是Docket ，所以通过配置Docket 实例来配置 Swagger
     * @return
     */
    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                //展示在swagger页面上的自定义工程信息
                .apiInfo(apiInfo())
                //选择展示哪些接口
                .select()
                //只有 com.example 包内的才去展示
                .apis(RequestHandlerSelectors.basePackage("com.example"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 配置swagger的描述信息
     * @return
     */
    public ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("zgb-micre-service")
                .description("e-commerce-spring-cloud-service")
                .contact(new Contact("name","www.zhoudashuai.com","17691018268@163.com"))
                .version("1.0")
                .build();
    }
}
