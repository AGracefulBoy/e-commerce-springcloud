package org.example.service.communication;

import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.collections4.CollectionUtils;
import org.example.vo.JwtToken;
import org.example.vo.UserAndPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Random;

/**
 * 使用Feign的原生api openFeign = Feign+Ribbon
 * Feign根据接口去调用
 * Ribbon实现负载均衡和服务发现
 * @author zhoudashuai
 * @date 2022年04月11日 10:21 下午
 */
@Slf4j
@Service
public class UseFeignApi {

    @Autowired
    private DiscoveryClient discoveryClient;


    /**
     * 使用原生API调用远端服务
     * Feign默认配置初始化，设置自定义配置，生成代理对象
     * @param userAndPassword
     * @return
     */
    public JwtToken thinkingInFeign(UserAndPassword userAndPassword){
        //通过发射拿到serviceId
        String serviceId = null;
        Annotation[] annotations = AuthorityFeignClient.class.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(FeignClient.class)){
                serviceId = ((FeignClient) annotation).value();
                log.info("get service id from AuthorityFeignClient: [{}]",serviceId);
            }
        }
        //如果服务Id不存在
        if (null == serviceId){
            throw new RuntimeException("can not get serviceId");
        }

        //通过serviceId去拿可用的实例
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
        if (CollectionUtils.isEmpty(instances)){
            throw new RuntimeException("can not get target instance from serviceId: "+serviceId);
        }

        //随机选择一个服务实例：实现的思想是负载均衡
        ServiceInstance randomInstance = instances.get(new Random().nextInt(instances.size()));
        log.info("choose service instanceL [{}],[{}],[{}]",serviceId,
                randomInstance.getHost(),randomInstance.getPort());

        //Feign客户端初始化，必须要配置encoder，decoder,contract
        AuthorityFeignClient feignClient = Feign.builder() //Feign默认配置初始化
        .encoder(new GsonEncoder()) //设置自定义配置
                .decoder(new GsonDecoder())
                .logLevel(Logger.Level.FULL)
                .contract(new SpringMvcContract())
                .target(AuthorityFeignClient.class,String.format(
                        "http://%s:%s",randomInstance.getHost(),randomInstance.getPort()
                ));
        return feignClient.getTokenByFeign(userAndPassword);
    }
}
