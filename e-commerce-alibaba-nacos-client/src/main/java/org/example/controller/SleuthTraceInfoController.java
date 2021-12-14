package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.service.SleuthTranceInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhoudashuai
 * @date 2021年12月13日 10:53 下午
 */
@Slf4j
@RequestMapping("/sleuth")
@RestController
public class SleuthTraceInfoController {
    private final SleuthTranceInfoService tranceInfoService;

    public SleuthTraceInfoController(SleuthTranceInfoService tranceInfoService) {
        this.tranceInfoService = tranceInfoService;
    }

    /**
     * 打印Trace 日志
     * @author zhoudashuai
     * @date 2021/12/13 10:55 下午
     */

    @GetMapping("/trace-info")
    public void logCurrentTraceInfo(){
        tranceInfoService.logCurrentTraceInfo();
    }
}
