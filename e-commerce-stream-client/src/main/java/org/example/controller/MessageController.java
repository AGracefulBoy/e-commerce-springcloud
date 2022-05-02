package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.stream.DefaultSendService;
import org.example.stream.zhoudashuai.QinyiSendService;
import org.example.vo.QinyiMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhoudashuai
 * @date 2022年05月02日 11:06 下午
 */
@Slf4j
@RestController
@RequestMapping
public class MessageController {

    private final DefaultSendService defaultSendService;
    private final QinyiSendService qinyiSendService;


    public MessageController(DefaultSendService defaultSendService, QinyiSendService qinyiSendService) {
        this.defaultSendService = defaultSendService;
        this.qinyiSendService = qinyiSendService;
    }

    @GetMapping("/default")
    public void defaultSend() {
        defaultSendService.sendMessage(QinyiMessage.defaultMessage());
    }

    @GetMapping("/qinyi")
    public void qinyiSend() {
        qinyiSendService.sendMessage(QinyiMessage.defaultMessage());
    }
}
