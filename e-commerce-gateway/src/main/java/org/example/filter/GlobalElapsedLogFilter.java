package org.example.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

/**
 * @author zhoudashuai
 * @date 2021年12月12日 3:16 下午
 */
@Slf4j
@Component
public class GlobalElapsedLogFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        StopWatch started = StopWatch.createStarted();
        String uri = exchange.getRequest().getURI().getPath();
        return chain.filter(exchange).then(
                Mono.fromRunnable(
                        () ->{
                            log.info("[{}] elapsed: [{}] ms",uri,started.getTime(TimeUnit.MILLISECONDS));
                        }
                )
        );
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
