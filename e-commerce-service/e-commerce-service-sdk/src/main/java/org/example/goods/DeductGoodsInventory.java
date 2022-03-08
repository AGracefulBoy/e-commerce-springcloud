package org.example.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 扣减商品库存
 * @author zhoudashuai
 * @date 2022年01月04日 11:30 下午
 */
@ApiModel(description = "扣减商品库存对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeductGoodsInventory {

    @ApiModelProperty(value = "商品的主键id")
    private Long goodsId;

    @ApiModelProperty(value = "扣减的个数")
    private Integer count;
}
