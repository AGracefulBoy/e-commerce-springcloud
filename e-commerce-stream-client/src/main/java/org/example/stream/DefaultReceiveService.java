package org.example.stream;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.vo.QinyiMessage;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

/**
 * @author zhoudashuai
 * @date 2022年05月02日 10:44 下午
 */
@Slf4j
@EnableBinding(Sink.class)
public class DefaultReceiveService {

    /**
     * 使用默认的输入信道接收消息
     *
     * @param payload
     */
    @StreamListener(Sink.INPUT)
    public void receiveMessage(Object payload) {
        log.info("in defaultReceiveService consume message start");
        QinyiMessage qinyiMessage = JSON.parseObject(payload.toString(), QinyiMessage.class);
        //消费消息
        log.info("in default receive service consume message success: [{}]",
                JSON.toJSONString(qinyiMessage));

    }
}
