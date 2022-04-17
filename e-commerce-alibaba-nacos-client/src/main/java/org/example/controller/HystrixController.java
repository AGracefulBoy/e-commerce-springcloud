package org.example.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.service.NacosClientService;
import org.example.service.hyxtrix.*;
import org.example.service.hyxtrix.request_merge.NacosClientCollapseCommand;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rx.Observable;
import rx.Observer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author zhoudashuai
 * @date 2022年04月12日 11:44 下午
 */
@Slf4j
@RestController
@RequestMapping("/hystrix")
public class HystrixController {

    private final UseHystrixCommandAnnotation useHystrixCommandAnnotation;

    private final NacosClientService nacosClientService;

    private final CacheHystrixCommandAnnotation cacheHystrixCommandAnnotation;

    public HystrixController(UseHystrixCommandAnnotation useHystrixCommandAnnotation, NacosClientService nacosClientService, CacheHystrixCommandAnnotation cacheHystrixCommandAnnotation) {
        this.useHystrixCommandAnnotation = useHystrixCommandAnnotation;
        this.nacosClientService = nacosClientService;
        this.cacheHystrixCommandAnnotation = cacheHystrixCommandAnnotation;
    }

    @GetMapping("/hystrix-command-annotation")
    public List<ServiceInstance> getNacosClientInfoUseAnnotation(
            @RequestParam String serviceId) {
        log.info("request nacos client info use annotation: [{}],[{}]",
                serviceId, Thread.currentThread().getName());
        return useHystrixCommandAnnotation.getNacosClientInfo(serviceId);
    }

    @GetMapping("/simple-hystrix-command")
    public List<ServiceInstance> getServiceInstanceByServiceId(@RequestParam String serviceId) throws ExecutionException, InterruptedException {
        //第一种
        List<ServiceInstance> serviceInstances01 = new NacosClientHystrixCommand(
                nacosClientService, serviceId
        ).execute();
        log.info("use execute to get service instances: [{}],[{}]",
                JSON.toJSONString(serviceInstances01), Thread.currentThread().getName());
        //第二种
        List<ServiceInstance> serviceInstances02;
        Future<List<ServiceInstance>> future = new NacosClientHystrixCommand(
                nacosClientService, serviceId
        ).queue();
        //这里可以做一些别的事
        serviceInstances02 = future.get();
        log.info("use queue to get service instances: [{}],[{}]",
                JSON.toJSONString(serviceInstances02), Thread.currentThread().getName());

        //第三种
        Observable<List<ServiceInstance>> observable = new NacosClientHystrixCommand(
                nacosClientService, serviceId
        ).observe();//热响应

        List<ServiceInstance> serviceInstances03 = observable.toBlocking().single();
        log.info("use Observable to get service instances: [{}],[{}]",
                JSON.toJSONString(serviceInstances03), Thread.currentThread().getName());

        //第四种方式
        Observable<List<ServiceInstance>> toObservable = new NacosClientHystrixCommand(
                nacosClientService, serviceId
        ).toObservable();//异步冷响应
        List<ServiceInstance> serviceInstances04 = toObservable.toBlocking().single();
        log.info("use Observable to get service instances: [{}],[{}]",
                JSON.toJSONString(serviceInstances04), Thread.currentThread().getName());

        return serviceInstances01;
    }

    @GetMapping("/hystrix-observable-command")
    public List<ServiceInstance> getServiceInstancesByServiceIdObservable(@RequestParam String serviceId) {
        List<String> servicesIds = Arrays.asList(serviceId, serviceId, serviceId);
        List<List<ServiceInstance>> result = new ArrayList<>(servicesIds.size());

        NacosClientHystrixObservableCommand observableCommand = new NacosClientHystrixObservableCommand(
                nacosClientService, servicesIds
        );
        //异步执行命令
        Observable<List<ServiceInstance>> observe = observableCommand.observe();
        observe.subscribe(new Observer<List<ServiceInstance>>() {
            @Override
            public void onCompleted() {
                log.info("all tasks is complete :[{}],[{}]",
                        serviceId, Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(List<ServiceInstance> serviceInstances) {
                result.add(serviceInstances);
            }
        });
        log.info("observable command result is : [{}],[{}]", JSON.toJSONString(result), Thread.currentThread().getName());
        return result.get(0);
    }

    @GetMapping("/cache-hystrix-command")
    public void cacheHystrixCommand(@RequestParam String serviceId) {
        // 使用缓存command发起两次请求
        CacheHystrixCommand command1 = new CacheHystrixCommand(nacosClientService, serviceId);
        CacheHystrixCommand command2 = new CacheHystrixCommand(nacosClientService, serviceId);

        List<ServiceInstance> execute = command1.execute();
        List<ServiceInstance> execute2 = command2.execute();

        log.info("result01,result03: [{}],[{}]",
                JSON.toJSONString(execute), JSON.toJSONString(execute2));

        CacheHystrixCommand.flushRequestCache(serviceId);

        // 使用缓存command发起两次请求
        CacheHystrixCommand command3 = new CacheHystrixCommand(nacosClientService, serviceId);
        CacheHystrixCommand command4 = new CacheHystrixCommand(nacosClientService, serviceId);

        List<ServiceInstance> execute3 = command1.execute();
        List<ServiceInstance> execute4 = command2.execute();

        log.info("result01,result03: [{}],[{}]",
                JSON.toJSONString(execute3), JSON.toJSONString(execute4));

    }

    @GetMapping("/cache-annotation-01")
    public List<ServiceInstance> useCache01(@RequestParam String serviceId) {
        log.info("use cache by annotation01 to get nacos client info: [{}]", serviceId);
        List<ServiceInstance> serviceInstances01 = cacheHystrixCommandAnnotation.useCacheByAnnotation01(serviceId);
        List<ServiceInstance> serviceInstances02 = cacheHystrixCommandAnnotation.useCacheByAnnotation01(serviceId);

        cacheHystrixCommandAnnotation.flushCacheByAnnotation01(serviceId);
        List<ServiceInstance> serviceInstances03 = cacheHystrixCommandAnnotation.useCacheByAnnotation01(serviceId);
        return cacheHystrixCommandAnnotation.useCacheByAnnotation01(serviceId);
    }

    @GetMapping("/cache-annotation-02")
    public List<ServiceInstance> useCache02(@RequestParam String serviceId) {
        log.info("use cache by annotation02 to get nacos client info: [{}]", serviceId);
        List<ServiceInstance> serviceInstances01 = cacheHystrixCommandAnnotation.useCacheByAnnotation02(serviceId);
        List<ServiceInstance> serviceInstances02 = cacheHystrixCommandAnnotation.useCacheByAnnotation02(serviceId);

        cacheHystrixCommandAnnotation.flushCacheByAnnotation01(serviceId);
        List<ServiceInstance> serviceInstances03 = cacheHystrixCommandAnnotation.useCacheByAnnotation02(serviceId);
        return cacheHystrixCommandAnnotation.useCacheByAnnotation02(serviceId);
    }

    @GetMapping("/cache-annotation-03")
    public List<ServiceInstance> useCache03(@RequestParam String serviceId) {
        log.info("use cache by annotation03 to get nacos client info: [{}]", serviceId);
        List<ServiceInstance> serviceInstances01 = cacheHystrixCommandAnnotation.useCacheByAnnotation03(serviceId);
        List<ServiceInstance> serviceInstances02 = cacheHystrixCommandAnnotation.useCacheByAnnotation03(serviceId);

        cacheHystrixCommandAnnotation.flushCacheByAnnotation01(serviceId);
        List<ServiceInstance> serviceInstances03 = cacheHystrixCommandAnnotation.useCacheByAnnotation03(serviceId);
        return cacheHystrixCommandAnnotation.useCacheByAnnotation03(serviceId);
    }

    @GetMapping("/request-merge")
    public void requestMerge() throws Exception {
        //前三个请求会被合并
        NacosClientCollapseCommand nacosClientCollapseCommand01 =
                new NacosClientCollapseCommand(nacosClientService, "e-commerce-nacos-client1");
        NacosClientCollapseCommand nacosClientCollapseCommand02 =
                new NacosClientCollapseCommand(nacosClientService, "e-commerce-nacos-client2");
        NacosClientCollapseCommand nacosClientCollapseCommand03 =
                new NacosClientCollapseCommand(nacosClientService, "e-commerce-nacos-client3");

        Future<List<ServiceInstance>> future01 = nacosClientCollapseCommand01.queue();
        Future<List<ServiceInstance>> future02 = nacosClientCollapseCommand02.queue();
        Future<List<ServiceInstance>> future03 = nacosClientCollapseCommand03.queue();

        future01.get();
        future02.get();
        future03.get();

        Thread.sleep(2000);

        //过了合并的时间窗口，第四个单独发起请求
        NacosClientCollapseCommand nacosClientCollapseCommand04 =
                new NacosClientCollapseCommand(nacosClientService, "e-commerce-nacos-client4");
        Future<List<ServiceInstance>> future04 = nacosClientCollapseCommand04.queue();
        future04.get();

    }

    /**
     * 注解的方式实现请求合并
     * @throws Exception
     */
    @GetMapping("/request-merge-annotation")
    public void requestMergeAnnotation() throws Exception{
        Future<List<ServiceInstance>> future01 = nacosClientService.findNacosClientInfo(
                "e-commerce-nacos-client1"
        );

        Future<List<ServiceInstance>> future02 = nacosClientService.findNacosClientInfo(
                "e-commerce-nacos-client2"
        );

        Future<List<ServiceInstance>> future03 = nacosClientService.findNacosClientInfo(
                "e-commerce-nacos-client3"
        );

        future01.get();
        future02.get();
        future03.get();

        Thread.sleep(2000);

        Future<List<ServiceInstance>> future04 = nacosClientService.findNacosClientInfo(
                "e-commerce-nacos-client4"
        );

        future04.get();
    }
}
