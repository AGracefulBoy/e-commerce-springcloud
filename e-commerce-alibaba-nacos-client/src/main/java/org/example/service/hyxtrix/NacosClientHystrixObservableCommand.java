package org.example.service.hyxtrix;

import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;
import lombok.extern.slf4j.Slf4j;
import org.example.service.NacosClientService;
import org.springframework.cloud.client.ServiceInstance;
import rx.Observable;
import rx.Subscriber;

import java.util.Collections;
import java.util.List;

/**
 * 隔离策略是基于信号量实现的
 *
 * @author zhoudashuai
 * @date 2022年04月15日 10:06 下午
 */
@Slf4j
public class NacosClientHystrixObservableCommand
        extends HystrixObservableCommand<List<ServiceInstance>> {

    /**
     * 要保护的服务
     */
    private final NacosClientService nacosClientService;

    /**
     * 方法需要传递的参数
     */
    private final List<String> serviceIds;

    public NacosClientHystrixObservableCommand(NacosClientService nacosClientService, List<String> serviceIds) {
        super(
                HystrixObservableCommand.Setter
                        .withGroupKey(HystrixCommandGroupKey
                                .Factory.asKey("NacosClientService"))
                        .andCommandKey(HystrixCommandKey
                                .Factory.asKey("NacosClientHystrixObservableCommand"))
                        .andCommandPropertiesDefaults(
                                HystrixCommandProperties.Setter()
                                        .withFallbackEnabled(true)//开启降级
                                        .withCircuitBreakerEnabled(true)//开启熔断
                        )
        );
        this.nacosClientService = nacosClientService;
        this.serviceIds = serviceIds;
    }

    /**
     * 要保护的方法调用
     *
     * @return
     */
    @Override
    protected Observable<List<ServiceInstance>> construct() {
        return Observable.create(new Observable.OnSubscribe<List<ServiceInstance>>() {
            //Observable有三个关键的事件方法，分别是 onNext,onCompleted,onError
            @Override
            public void call(Subscriber<? super List<ServiceInstance>> subscriber) {
                try {
                    if (!subscriber.isUnsubscribed()){
                        log.info("subscriber command task: [{}],[{}]",
                                JSON.toJSONString(serviceIds),
                                Thread.currentThread().getName());
                        serviceIds.forEach(
                                s -> subscriber
                                        .onNext(nacosClientService.getNacosClientInfo(s))
                        );
                        subscriber.onCompleted();
                        log.info("command task completed: [{}],[{}]",JSON.toJSONString(serviceIds),
                                Thread.currentThread().getName());
                    }
                }catch (Exception ex){
                    subscriber.onError(ex);
                }
            }
        });
    }

    /**
     * 服务降级
     *
     * @return
     */
    @Override
    protected Observable<List<ServiceInstance>> resumeWithFallback() {
        return Observable.create(new Observable.OnSubscribe<List<ServiceInstance>>() {
            //Observable有三个关键的事件方法，分别是 onNext,onCompleted,onError
            @Override
            public void call(Subscriber<? super List<ServiceInstance>> subscriber) {
                try {
                    if (!subscriber.isUnsubscribed()){
                        log.info("(fallback) subscriber command task: [{}],[{}]",
                                JSON.toJSONString(serviceIds),
                                Thread.currentThread().getName());
                        subscriber.onNext(Collections.emptyList());
                        subscriber.onCompleted();
                        log.info("(fallback) command task completed: [{}],[{}]",JSON.toJSONString(serviceIds),
                                Thread.currentThread().getName());
                    }
                }catch (Exception ex){
                    subscriber.onError(ex);
                }
            }
        });
    }
}
