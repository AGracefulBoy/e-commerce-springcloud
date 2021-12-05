package org.example.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;


/**
 * @author zhoudashuai
 * @date 2021年12月05日 7:20 下午动态路由配置，监听nacos中路由配置变更
 * 通过 nacos 下发don
 */
@Slf4j
@Component
@DependsOn({"gatewayConfig"})
public class DynamicRouteServiceImplByNacos {

    // nacos配置服务客户端
    private ConfigService configService;

    private final DynamicRouteServiceImpl dynamicRouteService;

    public DynamicRouteServiceImplByNacos(DynamicRouteServiceImpl dynamicRouteService) {
        this.dynamicRouteService = dynamicRouteService;
    }

    //连接到nacos之后的事
    //Bean在容器中构造完成之后会执行当前的init方法
    @PostConstruct
    public void init() {
        log.info("gateway route init....");
        try {
            //初始化 nacos客户端
            configService = initConfigService();
            if (null == configService) {
                log.error("init config service fail");
                return;
            }
            //通过nacos config 并指定路由配置去获取路由配置
            String configInfo = configService.getConfig(
                    GatewayConfig.NACOS_ROUTE_DATA_ID,
                    GatewayConfig.NACOS_ROUTE_GROUP,
                    GatewayConfig.DEFAULT_TIMEOUT
            );
            log.info("get current gateway config: [{}]", configInfo);
            List<RouteDefinition> definitionList = JSON.parseArray(configInfo, RouteDefinition.class);
            if (CollectionUtils.isEmpty(definitionList)) {
                for (RouteDefinition definition : definitionList) {
                    log.info("init gateway config:[{}]", definition.toString());
                    dynamicRouteService.addRouteDefinition(definition);
                }
            }
        } catch (Exception ex) {
            log.error("gateway route into has some error: [{}]", ex.getMessage(), ex);
        }
        dynamicRouteByNacosListener(GatewayConfig.NACOS_ROUTE_DATA_ID, GatewayConfig.NACOS_ROUTE_GROUP);
    }

    //初始化 nacos config
    private ConfigService initConfigService() {
        try {
            Properties properties = new Properties();
            properties.setProperty("serverAddr", GatewayConfig.NACOS_SERVER_ADDR);
            properties.setProperty("namespace", GatewayConfig.NACOS_NAMESPACE);
            return configService = NacosFactory.createConfigService(properties);
        } catch (NacosException e) {
            log.error("into gateway nacos config error: [{}]", e.getMessage(), e);
            return null;
        }
    }

    //实现对naocs的监听
    private void dynamicRouteByNacosListener(String dataId, String group) {
        try {
            //给nacos config客户端一个监听器
            configService.addListener(dataId, group, new Listener() {
                //自己提供一个线程池执行操作
                @Override
                public Executor getExecutor() {
                    return null;
                }

                // 接受配置的变更信息
                @Override
                public void receiveConfigInfo(String configInfo) {
                    log.info("start to update config: [{}]", configInfo);
                    List<RouteDefinition> definitionList = JSON.parseArray(configInfo, RouteDefinition.class);
                    log.info("update route: [{}]", definitionList.toString());
                    dynamicRouteService.updateList(definitionList);
                }
            });
        } catch (NacosException ex) {
            log.error("dynamic update gateway config error: [{}]", ex.getMessage(), ex);
        }
    }
}
