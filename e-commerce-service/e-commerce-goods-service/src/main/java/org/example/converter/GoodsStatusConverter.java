package org.example.converter;

/**
 * @author zhoudashuai
 * @date 2022年01月03日 7:34 下午
 */

import org.example.constant.GoodsStatus;

import javax.persistence.AttributeConverter;

/**
 * 商品状态枚举类转换器
 */
public class GoodsStatusConverter implements AttributeConverter<GoodsStatus,Integer> {
    /**
     * 转换为可以存入数据表的基本类型
     * @param goodsStatus
     * @return
     */
    @Override
    public Integer convertToDatabaseColumn(GoodsStatus goodsStatus) {
        return goodsStatus.getStatus();
    }

    /**
     * 还原数据表中的字段的值到 java数据类型
     * @param status
     * @return
     */
    @Override
    public GoodsStatus convertToEntityAttribute(Integer status) {
        return GoodsStatus.of(status);
    }
}
