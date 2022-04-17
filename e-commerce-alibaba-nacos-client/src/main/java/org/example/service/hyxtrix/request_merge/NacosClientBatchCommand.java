package org.example.service.hyxtrix.request_merge;

import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import lombok.extern.slf4j.Slf4j;
import org.example.service.NacosClientService;
import org.springframework.cloud.client.ServiceInstance;

import java.util.Collections;
import java.util.List;

/**
 * 批量请求Hystrix command
 * @author zhoudashuai
 * @date 2022年04月17日 3:16 下午
 */
@Slf4j
public class NacosClientBatchCommand extends HystrixCommand<List<List<ServiceInstance>>> {

    private final NacosClientService nacosClientService;

    private final List<String> serviceIds;

    protected NacosClientBatchCommand(NacosClientService nacosClientService,List<String> serviceIds) {
        super(
                HystrixCommand.Setter.withGroupKey(
                        HystrixCommandGroupKey.Factory.asKey("NacosClientBatchCommandGroup")
                )
        );
        this.nacosClientService = nacosClientService;
        this.serviceIds = serviceIds;
    }

    @Override
    protected List<List<ServiceInstance>> run() throws Exception {
        log.info("use nacos client batch command to get result: [{}]",
                JSON.toJSONString(serviceIds));
        return nacosClientService.getNacosClientInfos(serviceIds);
    }

    @Override
    protected List<List<ServiceInstance>> getFallback() {
        log.info("use nacos client batch command to get result failure use fallback");
        return Collections.emptyList();
    }
}
