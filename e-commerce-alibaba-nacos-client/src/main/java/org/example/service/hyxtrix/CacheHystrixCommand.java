package org.example.service.hyxtrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixRequestCache;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import lombok.extern.slf4j.Slf4j;
import org.example.service.NacosClientService;
import org.springframework.cloud.client.ServiceInstance;

import java.util.Collections;
import java.util.List;

/**
 * 带有缓存功能的hystrix
 *
 * @author zhoudashuai
 * @date 2022年04月16日 4:13 下午
 */
@Slf4j
public class CacheHystrixCommand extends HystrixCommand<List<ServiceInstance>> {

    //需要保护的服务
    private final NacosClientService nacosClientService;
    //方法需要传递的参数
    private final String serviceId;

    private static final HystrixCommandKey CACHED_KEY =
            HystrixCommandKey.Factory.asKey("CacheHystrixCommand");

    public CacheHystrixCommand(NacosClientService nacosClientService, String serviceId) {
        super(
                HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey
                        .Factory.asKey("CacheHystrixCommandGroup"))
                        .andCommandKey(CACHED_KEY)
        );
        this.nacosClientService = nacosClientService;
        this.serviceId = serviceId;
    }

    @Override
    protected List<ServiceInstance> run() throws Exception {
        log.info("CacheHystrixCommand in hystrix command to get service instance: " +
                "[{}],[{}]", serviceId, Thread.currentThread().getName());
        return this.nacosClientService.getNacosClientInfo(serviceId);
    }

    @Override
    protected List<ServiceInstance> getFallback() {
        return Collections.emptyList();
    }

    @Override
    protected String getCacheKey() {
        return serviceId;
    }

    /**
     * 根据缓存的key清理在一次Hystrix请求上下文的缓存
     */
    public static void flushRequestCache(String serviceId){
        HystrixRequestCache.getInstance(
                CACHED_KEY,
                HystrixConcurrencyStrategyDefault.getInstance()
        ).clear(serviceId);
        log.info("flush request cache in hystrix command :[{}],[{}]",serviceId,Thread.currentThread().getName());
    }
}
