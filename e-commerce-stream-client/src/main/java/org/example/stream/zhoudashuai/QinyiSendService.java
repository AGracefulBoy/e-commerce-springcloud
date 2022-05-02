package org.example.stream.zhoudashuai;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.vo.QinyiMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

/**
 * 使用自定义的通信信道 QinyiSource 实现消息的发送
 *
 * @author zhoudashuai
 * @date 2022年05月02日 10:55 下午
 */
@Slf4j
@EnableBinding(QinyiSource.class)
public class QinyiSendService {

    private final QinyiSource qinyiSource;

    public QinyiSendService(QinyiSource qinyiSource) {
        this.qinyiSource = qinyiSource;
    }

    /**
     * 使用自定义的输出信道发送消息
     *
     * @param message
     */
    public void sendMessage(QinyiMessage message) {
        String _message = JSON.toJSONString(message);
        log.info("in QinyiSendService send message: [{}]", _message);
        qinyiSource.qinyiOutput().send(MessageBuilder.withPayload(_message).build());
    }
}
