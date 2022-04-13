package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.service.hyxtrix.UseHystrixCommandAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhoudashuai
 * @date 2022年04月12日 11:44 下午
 */
@Slf4j
@RestController
@RequestMapping("/hystrix")
public class HystrixController {

    private final UseHystrixCommandAnnotation useHystrixCommandAnnotation;

    public HystrixController(UseHystrixCommandAnnotation useHystrixCommandAnnotation) {
        this.useHystrixCommandAnnotation = useHystrixCommandAnnotation;
    }

    @GetMapping("/hystrix-command-annotation")
    public List<ServiceInstance> getNacosClientInfoUseAnnotation(
            @RequestParam String serviceId) {
        log.info("request nacos client info use annotation: [{}],[{}]",
                serviceId,Thread.currentThread().getName());
        return useHystrixCommandAnnotation.getNacosClientInfo(serviceId);
    }
}
