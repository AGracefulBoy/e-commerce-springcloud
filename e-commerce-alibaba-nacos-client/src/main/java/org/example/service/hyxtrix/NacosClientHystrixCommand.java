package org.example.service.hyxtrix;

import com.netflix.hystrix.*;
import lombok.extern.slf4j.Slf4j;
import org.example.service.NacosClientService;
import org.springframework.cloud.client.ServiceInstance;

import java.util.Collections;
import java.util.List;

import static com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE;
import static com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy.THREAD;

/**
 * 给nacosClientService实现包装
 *
 * @author zhoudashuai
 * @date 2022年04月13日 11:13 下午
 * 舱壁模式：
 * 1.线程池
 * 2.信号量：算法+数据结构，有限状态机
 */
@Slf4j
public class NacosClientHystrixCommand extends HystrixCommand<List<ServiceInstance>> {

    /**
     * 需要保护的服务
     */
    private final NacosClientService nacosClientService;

    /**
     * 方法需要传递的参数
     */
    private final String serviceId;

    public NacosClientHystrixCommand(NacosClientService nacosClientService, String serviceId) {
        super(
                Setter.withGroupKey(
                        HystrixCommandGroupKey.Factory.asKey("NacosClientService"))
                        .andCommandKey(HystrixCommandKey.Factory.asKey("NacosClientHystrixCommand"))
                        .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("NacosClientPool"))
                        .andCommandPropertiesDefaults(
                                HystrixCommandProperties.Setter()
                                        .withExecutionIsolationStrategy(THREAD) //线程池隔离策略
                                        .withFallbackEnabled(true) //开启降级
                                        .withCircuitBreakerEnabled(true) //开启熔断
                        )
        );

        //可以配置信号量隔离策略
        /*Setter semaphore = Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("NacosClientService"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("NacosClientHystrixCommand"))
                .andCommandPropertiesDefaults(
                        HystrixCommandProperties.Setter()
                                .withCircuitBreakerRequestVolumeThreshold(10)
                                .withCircuitBreakerSleepWindowInMilliseconds(5000)
                        .withCircuitBreakerErrorThresholdPercentage(50)
                        .withExecutionIsolationStrategy(SEMAPHORE)//使用信号量隔离策略
                );*/

        this.nacosClientService = nacosClientService;
        this.serviceId = serviceId;
    }

    @Override
    protected List<ServiceInstance> run() throws Exception {
        log.info("nacos client service in hystrix command to get service instance:[{}],[{}]",
                serviceId,Thread.currentThread().getName());
        return this.nacosClientService.getNacosClientInfo(this.serviceId);
    }

    /**
     * 降级处理策略
     *
     * @return
     */
    @Override
    protected List<ServiceInstance> getFallback() {
        log.warn("nacos client service run error: [{}],[{}]",
                this.serviceId,Thread.currentThread().getName());
        return Collections.EMPTY_LIST;
    }
}
