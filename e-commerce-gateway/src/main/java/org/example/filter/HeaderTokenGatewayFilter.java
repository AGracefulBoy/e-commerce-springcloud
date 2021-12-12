package org.example.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author zhoudashuai
 * @date 2021年12月06日 11:18 下午
 * http头部携带token的验证过滤器
 */
public class HeaderTokenGatewayFilter implements GatewayFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //从http header中寻找key为token，value为imooc的键值对
        String name = exchange.getRequest().getHeaders().getFirst("imooc");
        if ("imooc".equals(name)){
            return chain.filter(exchange);
        }

        //标记此次请求没有权限并结束此次请求
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE+2;
    }
}
