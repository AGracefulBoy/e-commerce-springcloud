package org.example.service.communication;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.constant.CommonConstant;
import org.example.vo.JwtToken;
import org.example.vo.UserAndPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 使用restTemplate实现微服务通信
 * @author zhoudashuai
 * @date 2022年04月10日 1:59 下午
 */
@Slf4j
@Service
public class UserRestTemplateService {

    private final LoadBalancerClient loadBalancerClient;

    public UserRestTemplateService(LoadBalancerClient loadBalancerClient) {
        this.loadBalancerClient = loadBalancerClient;
    }

    public JwtToken getTokenFromAuthorityService(UserAndPassword userAndPassword){
        String requestUrl = "http://127.0.0.1:7000/ecommerce-authoriry-center/authority/token";
        log.info("request url: [{}]",requestUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new RestTemplate().postForObject(requestUrl,new HttpEntity<>(
                JSON.toJSONString(userAndPassword),headers
        ),JwtToken.class);
    }

    public JwtToken getTokenFromAuthorityServiceWithLoadBalance(UserAndPassword userAndPassword){
        HttpHeaders headers = new HttpHeaders();

        ServiceInstance serviceInstance = loadBalancerClient.choose(
                CommonConstant.AUTHORITY_CENTER_SERVICE_ID
        );

        log.info("Nacos Client Info : [{}],[{}],[{}]",
                serviceInstance.getServiceId(),serviceInstance.getInstanceId(),
                JSON.toJSONString(serviceInstance.getMetadata()));
        String requestUrl = String.format("http:%s:%s/ecommerce-authoriry-center/authority/token",
                serviceInstance.getHost(),serviceInstance.getPort());

        headers.setContentType(MediaType.APPLICATION_JSON);
        return new RestTemplate().postForObject(requestUrl,new HttpEntity<>(
                JSON.toJSONString(userAndPassword),headers
        ),JwtToken.class);
    }
}
