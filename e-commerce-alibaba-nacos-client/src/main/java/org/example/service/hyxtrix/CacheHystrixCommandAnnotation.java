package org.example.service.hyxtrix;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import lombok.extern.slf4j.Slf4j;
import org.example.service.NacosClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 使用注解方式开启Hystrix请求缓存
 * @author zhoudashuai
 * @date 2022年04月16日 4:35 下午
 */
@Slf4j
@Service
public class CacheHystrixCommandAnnotation {

    private final NacosClientService nacosClientService;


    public CacheHystrixCommandAnnotation(NacosClientService nacosClientService) {
        this.nacosClientService = nacosClientService;
    }

    //第一种hystrix cache注解的使用方法

    @CacheResult(cacheKeyMethod = "getCacheKey")
    @HystrixCommand(commandKey = "CacheHystrixCommandAnnotation")
    public List<ServiceInstance> useCacheByAnnotation01(String serviceId){
        log.info("use cache01 to get nacos client info:[{}]",serviceId);
        return nacosClientService.getNacosClientInfo(serviceId);
    }

    @CacheRemove(commandKey = "CacheHystrixCommandAnnotation",
            cacheKeyMethod = "getCacheKey")
    @HystrixCommand
    public void flushCacheByAnnotation01(String cacheId){
        log.info("flush hystrix cache key: [{}]",cacheId);
    }

    public String getCacheKey(String cacheId){
        return cacheId;
    }

    //第二种hystrix cache注解的使用方法
    @CacheResult
    @HystrixCommand(commandKey = "CacheHystrixCommandAnnotation")
    public List<ServiceInstance> useCacheByAnnotation02(@CacheKey String serviceId){
        log.info("use cache02 to get nacos client info:[{}]",serviceId);
        return nacosClientService.getNacosClientInfo(serviceId);
    }

    @CacheRemove(commandKey = "CacheHystrixCommandAnnotation")
    @HystrixCommand
    public void flushCacheByAnnotation02(@CacheKey String cacheId){
        log.info("flush hystrix cache key: [{}]",cacheId);
    }

    //第三种hystrix cache注解的使用方法
    @CacheResult
    @HystrixCommand(commandKey = "CacheHystrixCommandAnnotation")
    public List<ServiceInstance> useCacheByAnnotation03(String serviceId){
        log.info("use cache03 to get nacos client info:[{}]",serviceId);
        return nacosClientService.getNacosClientInfo(serviceId);
    }

    @CacheRemove(commandKey = "CacheHystrixCommandAnnotation")
    @HystrixCommand
    public void flushCacheByAnnotation03(String cacheId){
        log.info("flush hystrix cache key: [{}]",cacheId);
    }
}
