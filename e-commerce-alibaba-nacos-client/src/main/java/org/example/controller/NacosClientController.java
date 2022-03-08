package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.service.NacosClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/nacos-client")
public class NacosClientController {

    @Autowired
    private NacosClientService nacosClientService;

    @GetMapping("/service-instance")
    public List<ServiceInstance> logNacosClientInfo(@RequestParam(defaultValue = "e-commerce-nacos-client") String serviceId){
        log.info("service: [{}]",serviceId);
        return nacosClientService.getNacosClienInfo(serviceId);
    }

}
