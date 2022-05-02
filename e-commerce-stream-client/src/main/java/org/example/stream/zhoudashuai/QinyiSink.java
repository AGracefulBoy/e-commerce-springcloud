package org.example.stream.zhoudashuai;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * 自定义输入信道
 *
 * @author zhoudashuai
 * @date 2022年05月02日 10:58 下午
 */
public interface QinyiSink {
    String INPUT = "qinyiInput";

    /**
     * 输入信道的名称是qinyiInput
     *
     * @return
     */
    @Input(QinyiSink.INPUT)
    SubscribableChannel qinyiInput();
}
