package org.example.service.communication;

import feign.Feign;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * openFeign使用okhttp 配置类
 * @author zhoudashuai
 * @date 2022年04月11日 10:12 下午
 */
@Configuration
@ConditionalOnClass(Feign.class)
@AutoConfigureBefore(FeignAutoConfiguration.class)
public class FeignOkHttpConfig {
    /**
     * 注入okhttp，并自定义配置
     * @return
     */
    @Bean
    public okhttp3.OkHttpClient okHttpClient(){
        return new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)//设置连接超时
                .readTimeout(3,TimeUnit.SECONDS)
                .writeTimeout(5,TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)//是否自动重连
                //配置连接池中的最大空闲线程个数为10 并保持5分钟
                .connectionPool(new ConnectionPool(
                        10,5L,TimeUnit.MINUTES))
                .build();
    }
}
