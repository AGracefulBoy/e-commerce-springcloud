package org.example.service.hyxtrix.request_merge;

import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixCollapserKey;
import com.netflix.hystrix.HystrixCollapserProperties;
import com.netflix.hystrix.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.example.service.NacosClientService;
import org.springframework.cloud.client.ServiceInstance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 请求合并器
 *
 * @author zhoudashuai
 * @date 2022年04月17日 3:21 下午
 */
@Slf4j
public class NacosClientCollapseCommand
        extends HystrixCollapser<List<List<ServiceInstance>>, List<ServiceInstance>, String> {

    private final NacosClientService nacosClientService;
    private final String serviceId;

    public NacosClientCollapseCommand(NacosClientService nacosClientService, String serviceId) {
        super(
                HystrixCollapser.Setter.withCollapserKey(
                        HystrixCollapserKey.Factory.asKey("NacosClientCollapseCommand")
                ).andCollapserPropertiesDefaults(
                        HystrixCollapserProperties
                                .Setter().withTimerDelayInMilliseconds(300)
                )
        );
        this.nacosClientService = nacosClientService;
        this.serviceId = serviceId;
    }

    /**
     * 获取请求中的参数
     * @return
     */
    @Override
    public String getRequestArgument() {
        return this.serviceId;
    }

    /**
     * 创建批量请求 hystrix command
     * @param collapsedRequests
     * @return
     */
    @Override
    protected HystrixCommand<List<List<ServiceInstance>>> createCommand(Collection<CollapsedRequest<List<ServiceInstance>, String>> collapsedRequests) {
        List<String> serviceIds = new ArrayList<>(collapsedRequests.size());
        serviceIds.addAll(
          collapsedRequests.stream()
                  .map(CollapsedRequest::getArgument).collect(Collectors.toList())
        );

        return new NacosClientBatchCommand(nacosClientService,serviceIds);
    }

    /**
     * 响应分发给单独的请求
     * @param batchResponse
     * @param collapsedRequests
     */
    @Override
    protected void mapResponseToRequests(List<List<ServiceInstance>> batchResponse,
                                         Collection<CollapsedRequest<List<ServiceInstance>, String>> collapsedRequests) {
        int count = 0;
        for (CollapsedRequest<List<ServiceInstance>, String> collapsedRequest : collapsedRequests) {
            //从批量响应集合中按顺序取出结果
            List<ServiceInstance> serviceInstances = batchResponse.get(count++);
            //将结果放回原response中
            collapsedRequest.setResponse(serviceInstances);
        }
    }
}
