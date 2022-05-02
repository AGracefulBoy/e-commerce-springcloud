package org.example.stream.zhoudashuai;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * 自定义输出信道
 *
 * @author zhoudashuai
 * @date 2022年05月02日 10:52 下午
 */
public interface QinyiSource {
    String OUTPUT = "qinyiOutput";

    /**
     * 输出信道的名称是 qinyiOutput 同时需要使用stream绑定器在配置文件中声明
     * @return
     */
    @Output(QinyiSource.OUTPUT)
    MessageChannel qinyiOutput();
}
