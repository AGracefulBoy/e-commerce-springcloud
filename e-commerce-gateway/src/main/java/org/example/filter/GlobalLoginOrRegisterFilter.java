package org.example.filter;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.cms.PasswordRecipientId;
import org.checkerframework.checker.units.qual.A;
import org.example.constant.CommonConstant;
import org.example.constant.GateWayConstant;
import org.example.util.TokenParseUtil;
import org.example.vo.JwtToken;
import org.example.vo.LoginUserInfo;
import org.example.vo.UserAndPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.print.DocFlavor;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author zhoudashuai
 * @date 2021年12月07日 11:13 下午
 * 全局登录鉴权登录器
 */
@Slf4j
@Component
public class GlobalLoginOrRegisterFilter implements GlobalFilter, Ordered {

    private final LoadBalancerClient loadBalancerClient;

    private final RestTemplate restTemplate;

    public GlobalLoginOrRegisterFilter(LoadBalancerClient loadBalancerClient, RestTemplate restTemplate) {
        this.loadBalancerClient = loadBalancerClient;
        this.restTemplate = restTemplate;
    }

    /**
     * 登录注册鉴权
     * 1、如果是登录或注册，则去授权中心拿到token，并返回给客户端
     * 2、如果是访问其他的服务，则鉴权，没有权限则返回401
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // 1、如果是登录
        if (request.getURI().getPath().contains(GateWayConstant.LOGIN_URI)){
            String token = getTokenFromAuthorityCenter(
                    request,GateWayConstant.AUTHORITY_CENTER_TOKEN_URI_FORMAT
            );

            // header 中不能设置null
            response.getHeaders().add(
                    CommonConstant.JWT_USER_INFO_KEY, null==token ? "null" : token
            );
            response.setStatusCode(HttpStatus.OK);
            return response.setComplete();
        }

        // 2、如果是注册
        if (request.getURI().getPath().contains(GateWayConstant.REGISTER_URI)){
            // 去授权中心拿Token ： 先创建用户，再返回Token
            String token = getTokenFromAuthorityCenter(
                    request,GateWayConstant.AUTHORITY_CENTER_REGISTER_URI_FORMAT
            );
            response.getHeaders().add(
                    CommonConstant.JWT_USER_INFO_KEY,
                    null == token? "null" : token
            );
            response.setStatusCode(HttpStatus.OK);
            return response.setComplete();
        }

        // 3、去访问其他的服务，则鉴权校验是否从token中解析出用户信息
        HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst(CommonConstant.JWT_USER_INFO_KEY);
        LoginUserInfo loginUserInfo = null;

        try {
            loginUserInfo = TokenParseUtil.parseUserInfoFromToken(token);
        }catch (Exception e){
            log.error("parse user info from token error: [{}]",e.getMessage(),e);
        }
        // 获取不到登录信息,返回401
        /*if (null == loginUserInfo){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }*/

        //解析通过，则放行
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE+2;
    }

    /**
     * 从授权中心获取Token
     * @param request
     * @param uriFormat
     * @return
     */
    private String getTokenFromAuthorityCenter(ServerHttpRequest request,String uriFormat){
        ServiceInstance serviceInstance = loadBalancerClient.choose(
                CommonConstant.AUTHORITY_CENTER_SERVICE_ID
        );
        log.info("Nacos Client Info: [{}],[{}],[{}]",
                serviceInstance.getServiceId(),serviceInstance.getServiceId(),
                JSON.toJSONString(serviceInstance.getMetadata()));

        String requestUrl = String.format(
                uriFormat,serviceInstance.getHost(),serviceInstance.getPort());

        UserAndPassword requestBody = JSON.parseObject(
                parseBodyFromRequest(request),UserAndPassword.class
        );
        log.info("login request url and body: [{}],[{}]",requestUrl,JSON.toJSONString(requestBody));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JwtToken token = restTemplate.postForObject(requestUrl,new HttpEntity<>(JSON.toJSONString(requestBody),headers),
                JwtToken.class);

        if (null != token){
            return token.getToken();
        }
        return null;
    }

    /**
     * 从post请求中获取到请求数据
     * @param request
     * @return
     */
    private String parseBodyFromRequest(ServerHttpRequest request){
        //获取请求体
        Flux<DataBuffer> body = request.getBody();
        AtomicReference<String> bodyRef = new AtomicReference<>();

        //订阅缓冲区去消费请求体中的数据
        body.subscribe(buffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());

            // 一定要使用 DataBufferUtils.release 释放掉，否则会出现内存泄漏
            DataBufferUtils.release(buffer);
            bodyRef.set(charBuffer.toString());
        });

        return bodyRef.get();
    }
}
