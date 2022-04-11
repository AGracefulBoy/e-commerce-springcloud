package org.example.service.communication;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 使用ribbon之前的配置，增强restTemplate
 * @author zhoudashuai
 * @date 2022年04月10日 2:25 下午
 */
@Component
public class RibbonConfig {
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
