package org.example.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 品牌分类
 */
@Getter
@AllArgsConstructor
public enum BrandCategory {

    BRAND_A("20001","平牌A"),
    BRAND_B("20002","平牌B"),
    BRAND_C("20003","平牌C"),
    BRAND_D("20004","平牌D"),
    BRAND_E("20005","平牌E"),
    ;

    /**
     * 品牌分类编码
     */
    private final String code;

    /**
     * 品牌分类描述
     */
    private final String description;

    public static BrandCategory of(String code){
        Objects.requireNonNull(code);
        return Stream.of(values()).filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(
                        () -> new IllegalArgumentException(code + "not exists")
                );
    }
}
