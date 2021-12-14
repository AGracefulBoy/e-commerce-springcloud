package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * 用户账户微服务启动入口
 * @author zhoudashuai
 * @date 2021年12月15日 12:36 上午
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing
public class AccountApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccountApplication.class,args);
    }
}
