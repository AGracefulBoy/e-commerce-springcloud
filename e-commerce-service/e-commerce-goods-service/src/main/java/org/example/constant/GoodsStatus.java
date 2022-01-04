package org.example.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 商品状态枚举类
 */
@Getter
@AllArgsConstructor
public enum GoodsStatus {

    ONLINE(101,"上线"),
    OFFLINE(102,"下线"),
    STOCK_OUT(103,"缺货"),
    ;

    /**
     * 状态码
     */
    private final Integer status;

    /**
     * 状态描述
     */
    private final String description;


    /**
     * 根据code获取到 GoodsStatus
     * @param status
     * @return
     */
    public static GoodsStatus of(Integer status){
        Objects.requireNonNull(status);

        return Stream.of(values()).filter(bean -> bean.status.equals(status))
                .findAny()
                .orElseThrow(
                        () -> new IllegalArgumentException(status + "not exists")
                );
    }
}
