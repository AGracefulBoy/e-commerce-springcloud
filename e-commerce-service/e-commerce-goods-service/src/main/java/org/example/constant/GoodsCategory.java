package org.example.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 商品类别
 */
@Getter
@AllArgsConstructor
public enum GoodsCategory {

    DIAN_QI("1001","电器"),
    JIA_JU("1002","家具"),
    FU_SHI("1003","服饰"),
    MU_YIN("1004","母婴"),
    SHI_PIN("1005","食品"),
    TU_SHU("1006","图书"),
    ;
    /**
     * 上平分类编码
     */
    private final String code;

    /**
     * 商品分类描述
     */
    private final String description;

    public static GoodsCategory of(String code){
        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(
                        () -> new IllegalArgumentException(code + "not exists")
                );
    }
}
