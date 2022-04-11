package org.example.service.communication;

import com.alibaba.fastjson.JSON;
import com.netflix.loadbalancer.*;
import com.netflix.loadbalancer.reactive.LoadBalancerCommand;
import lombok.extern.slf4j.Slf4j;
import org.example.constant.CommonConstant;
import org.example.vo.JwtToken;
import org.example.vo.UserAndPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rx.Observable;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用ribbon实现微服务通信
 * * @author zhoudashuai
 * @date 2022年04月10日 2:27 下午
 */
@Service
@Slf4j
public class UseRibbonService {
    private final RestTemplate restTemplate;
    private final DiscoveryClient discoveryClient;

    public UseRibbonService(RestTemplate restTemplate, DiscoveryClient discoveryClient) {
        this.restTemplate = restTemplate;
        this.discoveryClient = discoveryClient;
    }

    public JwtToken getTokenFromAuthorityByRibbon(UserAndPassword userAndPassword){
        String requestUrl = String.format(
                "http:%s/ecommerce-authoriry-center/authority/token", CommonConstant.AUTHORITY_CENTER_SERVICE_ID
        );

        log.info("request url and body: [{}],[{}]",requestUrl,
                JSON.toJSONString(userAndPassword));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        return restTemplate.postForObject(requestUrl,
                new HttpEntity<>(JSON.toJSONString(userAndPassword),headers),JwtToken.class);
    }

    /**
     * <h2>使用原生的 Ribbon Api, 看看 Ribbon 是如何完成: 服务调用 + 负载均衡</h2>
     * */
    public JwtToken thinkingInRibbon(UserAndPassword usernameAndPassword) {

        String urlFormat = "http://%s/ecommerce-authority-center/authority/token";

        // 1. 找到服务提供方的地址和端口号
        List<ServiceInstance> targetInstances = discoveryClient.getInstances(
                CommonConstant.AUTHORITY_CENTER_SERVICE_ID
        );

        // 构造 Ribbon 服务列表
        List<Server> servers = new ArrayList<>(targetInstances.size());
        targetInstances.forEach(i -> {
            servers.add(new Server(i.getHost(), i.getPort()));
            log.info("found target instance: [{}] -> [{}]", i.getHost(), i.getPort());
        });

        // 2. 使用负载均衡策略实现远端服务调用
        // 构建 Ribbon 负载实例
        BaseLoadBalancer loadBalancer = LoadBalancerBuilder.newBuilder()
                .buildFixedServerListLoadBalancer(servers);
        // 设置负载均衡策略
        loadBalancer.setRule(new RetryRule(new RandomRule(), 300));

        String result = LoadBalancerCommand.builder().withLoadBalancer(loadBalancer)
                .build().submit(server -> {

                    String targetUrl = String.format(
                            urlFormat,
                            String.format("%s:%s", server.getHost(), server.getPort())
                    );
                    log.info("target request url: [{}]", targetUrl);

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);

                    String tokenStr = new RestTemplate().postForObject(
                            targetUrl,
                            new HttpEntity<>(JSON.toJSONString(usernameAndPassword), headers),
                            String.class
                    );

                    return Observable.just(tokenStr);

                }).toBlocking().first().toString();

        return JSON.parseObject(result, JwtToken.class);
    }

}
