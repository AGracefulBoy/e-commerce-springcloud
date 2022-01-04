package org.example.converter;

import org.example.constant.GoodsCategory;

import javax.persistence.AttributeConverter;

/**
 * @author zhoudashuai
 * @date 2022年01月03日 7:53 下午
 */
public class GoodsCategoryConverter implements AttributeConverter<GoodsCategory, String> {
    @Override
    public String convertToDatabaseColumn(GoodsCategory goodsCategory) {
        return goodsCategory.getCode();
    }

    @Override
    public GoodsCategory convertToEntityAttribute(String code) {
        return GoodsCategory.of(code);
    }
}
