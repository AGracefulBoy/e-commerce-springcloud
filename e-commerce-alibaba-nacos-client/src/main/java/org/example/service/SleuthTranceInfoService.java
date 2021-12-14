package org.example.service;

import brave.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 使用代码更直观的看到Sleuth 生成的相关跟踪信息
 * @author zhoudashuai
 * @date 2021年12月13日 10:48 下午
 */
@Slf4j
@Service
public class SleuthTranceInfoService {

    /** brave.Tracer 跟踪对象*/
    private final Tracer tracer;

    public SleuthTranceInfoService(Tracer tracer) {
        this.tracer = tracer;
    }

    /**
     * 打印当前的跟踪信息到日志中
     * @author zhoudashuai
     * @date 2021/12/13 10:50 下午
     */

    public void logCurrentTraceInfo(){
        log.info("Sleuth trace id : [{}]",tracer.currentSpan().context().traceId());
        log.info("Sleuth span id: [{}]",tracer.currentSpan().context().spanId());
    }

}
