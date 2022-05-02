package org.example.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息传递对象 springCloud stream + kafka
 *
 * @author zhoudashuai
 * @date 2022年05月02日 10:31 下午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QinyiMessage {

    private Integer id;
    private String projectName;
    private String org;
    private String author;
    private String version;

    /**
     * 返回一个默认的消息
     *
     * @return
     */
    public static QinyiMessage defaultMessage() {
        return new QinyiMessage(
                1,
                "e-commerce-stream-client",
                "zhoudashuai,com",
                "zhoudashuai",
                "1.0"
        );
    }

}
