package org.example.filter;

import lombok.extern.slf4j.Slf4j;
import org.example.constant.GateWayConstant;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author zhoudashuai
 * @date 2021年12月06日 11:34 下午
 * 缓存请求body的全局过滤器
 */
@Slf4j
@Component
@SuppressWarnings("all")
public class GlobalCacheRequestBodyFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        boolean isLoginOrRegister =
                exchange.getRequest().getURI().getPath().contains(GateWayConstant.LOGIN_URI)
                        || exchange.getRequest().getURI().getPath().contains(GateWayConstant.REGISTER_URI);

        if (null == exchange.getRequest().getHeaders().getContentType() || !isLoginOrRegister) {
            return chain.filter(exchange);
        }

        // DataBufferUtils.join 拿到http请求中的数据  就是 dataBuffer
        return DataBufferUtils.join(exchange.getRequest().getBody()).flatMap(dataBuffer -> {
            //确保数据缓冲区不被释放，必须要 DataBufferUtils.retain
            DataBufferUtils.retain(dataBuffer);
            // defer、just 都是去创建数据源，得到当前数据副本
            Flux<DataBuffer> cachedFlux = Flux.defer(() ->
                Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));
                //重新包装 serverHttpRequest ,重写 getBody 方法，能够返回请求数据
                ServerHttpRequest mutatedRequest =
                        new ServerHttpRequestDecorator(exchange.getRequest()) {
                            @Override
                            public Flux<DataBuffer> getBody() {
                                Flux<DataBuffer> cachedFlux1 = cachedFlux;
                                return cachedFlux1;
                            }
                        };
                //包装之后的serverHttpRequest 向下传递
                return chain.filter(exchange.mutate().request(mutatedRequest).build());
            });
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE+1;
    }
}
