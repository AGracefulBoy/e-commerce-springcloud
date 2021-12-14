package org.example.sampler;

import brave.sampler.RateLimitingSampler;
import brave.sampler.Sampler;
import org.springframework.cloud.sleuth.sampler.ProbabilityBasedSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 使用配置的方式设置抽样率
 * @author zhoudashuai
 * @date 2021年12月14日 10:51 下午
 */
@Configuration
public class SamplerConfig {

    /**
     * 限速采集
     * @author zhoudashuai
     * @date 2021/12/14 10:52 下午
     * * @return brave.sampler.Sampler
     */

    @Bean
    public Sampler sampler(){
        return RateLimitingSampler.create(100);
    }


    /**
     * 概率采集，默认的采样策略，默认值是 0.1
     * @return
     */
    /*@Bean
    public Sampler defaultSampler(){
        return ProbabilityBasedSampler.create(0.5f);
    }*/
}
