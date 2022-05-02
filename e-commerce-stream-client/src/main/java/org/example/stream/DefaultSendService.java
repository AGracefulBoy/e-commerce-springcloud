package org.example.stream;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.vo.QinyiMessage;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * 使用默认的通信信道发送消息
 *
 * @author zhoudashuai
 * @date 2022年05月02日 10:34 下午
 */
@Slf4j
@EnableBinding(Source.class)
public class DefaultSendService {

    private final Source source;

    public DefaultSendService(Source source) {
        this.source = source;
    }

    /**
     * 使用默认的输出信道发送消息
     *
     * @param qinyiMessage
     */
    public void sendMessage(QinyiMessage qinyiMessage) {
        String _message = JSON.toJSONString(qinyiMessage);
        log.info("in DefaultSendService send message: [{}]", _message);
        //steam 基于 spring message实现的，统一消息的编程模型，是stream组件的重要组成部分之一
        source.output().send(MessageBuilder.withPayload(_message).build());
    }

}
