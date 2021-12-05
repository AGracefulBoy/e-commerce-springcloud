package org.example.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author zhoudashuai
 * @date 2021年12月05日 6:56 下午
 */
@Slf4j
@Service
@SuppressWarnings("all")
public class DynamicRouteServiceImpl implements ApplicationEventPublisherAware {
    private final RouteDefinitionWriter routeDefinitionWriter;

    private final RouteDefinitionLocator routeDefinitionLocator;

    private ApplicationEventPublisher publisher;

    public DynamicRouteServiceImpl(RouteDefinitionWriter routeDefinitionWriter,
                                   RouteDefinitionLocator routeDefinitionLocator) {
        this.routeDefinitionLocator = routeDefinitionLocator;
        this.routeDefinitionWriter = routeDefinitionWriter;
    }


    @Override
    public void setApplicationEventPublisher(
            ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    //添加路由
    public String addRouteDefinition(RouteDefinition definition) {
        log.info("gateway add route: [{}]", definition);
        //保存路由信息并发布
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        //发布时间通知给gateway，同步新增的路由定义
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
        return "success";
    }

    public String updateList(List<RouteDefinition> definitions) {
        log.info("gateway update route: [{}]", definitions);

        //先拿到当前gateway存储的路由定义
        List<RouteDefinition> routeDefinitionsExists = routeDefinitionLocator.getRouteDefinitions()
                .buffer().blockFirst();

        if (!CollectionUtils.isEmpty(routeDefinitionsExists)) {
            //需要清楚掉之前所有的 旧的 路由定义
            routeDefinitionsExists.forEach(rd -> {
                log.info("delete route definition: [{}]", rd);
                deleteById(rd.getId());
            });
        }
        // 更新的路由定义同步到gateway中
        definitions.forEach(definition -> updateByRouteDefinition(definition));
        return "success";
    }

    //根据路由id删除路由配置
    private String deleteById(String id) {
        try {
            log.info("gateway delete route id :[{}]", id);
            //删除并发布通知
            this.routeDefinitionWriter.delete(Mono.just(id)).subscribe();
            //发布时间通知给gateway更新路由定义
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
            return "delete success";
        } catch (Exception e) {
            log.error("gateway delete route fail: [{}]", e.getMessage(), e);
            return "delete fail";
        }
    }

    //更新路由  更新的实现策略 删除 + 新增 = 更新
    private String updateByRouteDefinition(RouteDefinition definition) {
        try {
            log.info("gateway update route: [{}]", definition);
            this.routeDefinitionWriter.delete(Mono.just(definition.getId()));
        } catch (Exception ex) {
            return "update fail,not find route routeId: " + definition.getId();
        }

        try {
            this.routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
            return "success";
        } catch (Exception ex) {
            return "update fail";
        }
    }
}
