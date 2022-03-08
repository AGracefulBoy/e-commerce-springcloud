package org.example.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.goods.SimpleGoodsInfo;

import java.util.List;

/**
 * 分页商品信息
 * @author zhoudashuai
 * @date 2022年01月04日 11:26 下午
 */
@ApiModel(description = "分页商品信息对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageSimpleGoodsInfo {

    @ApiModelProperty(value = "分页简单商品信息")
    private List<SimpleGoodsInfo> simpleGoodsInfos;

    @ApiModelProperty(value = "是否有更多的商品")
    private Boolean hasMore;


}
