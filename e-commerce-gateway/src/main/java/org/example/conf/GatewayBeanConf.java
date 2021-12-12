package org.example.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 网关需要注入到容器中的Bean
 * @author zhoudashuai
 * @date 2021年12月07日 11:22 下午
 */
@Configuration
public class GatewayBeanConf {
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
