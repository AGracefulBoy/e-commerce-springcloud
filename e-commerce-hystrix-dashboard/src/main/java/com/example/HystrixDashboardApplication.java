package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * http://127.0.0.1:9999/ecommerce-hystrix-dashboard/hystrix
 * http://127.0.0.1:8000/ecommerce-nacos-client/actuator/hystrix.stream
 * @author zhoudashuai
 * @date 2022年04月17日 5:16 下午
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableHystrixDashboard
public class HystrixDashboardApplication {
    public static void main(String[] args) {
        SpringApplication.run(HystrixDashboardApplication.class, args);
    }
}
