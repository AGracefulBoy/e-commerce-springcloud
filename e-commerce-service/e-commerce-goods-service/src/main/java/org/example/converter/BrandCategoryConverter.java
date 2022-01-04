package org.example.converter;

import org.example.constant.BrandCategory;

import javax.persistence.AttributeConverter;

/**
 * @author zhoudashuai
 * @date 2022年01月03日 7:45 下午
 */
public class BrandCategoryConverter implements AttributeConverter<BrandCategory,String> {
    @Override
    public String convertToDatabaseColumn(BrandCategory brandCategory) {
        return brandCategory.getCode();
    }

    @Override
    public BrandCategory convertToEntityAttribute(String code) {
        return BrandCategory.of(code);
    }
}
