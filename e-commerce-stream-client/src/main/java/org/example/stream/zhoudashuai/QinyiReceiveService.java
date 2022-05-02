package org.example.stream.zhoudashuai;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.vo.QinyiMessage;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;

/**
 * 使用自定义输入信道
 *
 * @author zhoudashuai
 * @date 2022年05月02日 10:59 下午
 */
@Slf4j
@EnableBinding(QinyiSink.class)
public class QinyiReceiveService {

    /**
     * 使用自定义的输入信道接收消息
     *
     * @param payload
     */
    @StreamListener(QinyiSink.INPUT)
    public void receiveMessage(@Payload Object payload) {
        log.info("in QinyiReceiveService consume message start");

        QinyiMessage qinyiMessage = JSON.parseObject(payload.toString(), QinyiMessage.class);

        log.info("in qinyiReceiveService consume message success: [{}]", JSON.toJSONString(qinyiMessage));
    }
}
