package org.example.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 主键ids
 * @author zhoudashuai
 * @date 2021年12月22日 10:47 下午
 */
@ApiModel(description = "通用id对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableId {

    @ApiModelProperty(value = "数据表记录主键")
    private List<Id> ids;

    @ApiModel(description = "数据表主键对象")
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Id{
        @ApiModelProperty(value = "数据表记录")
        private Long id;
    }
}
