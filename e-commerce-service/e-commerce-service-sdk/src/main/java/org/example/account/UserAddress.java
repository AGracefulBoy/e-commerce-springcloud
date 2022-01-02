package org.example.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhoudashuai
 * @date 2021年12月21日 11:18 下午
 */
@ApiModel(description = "用户单个地址信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAddress {
    @ApiModelProperty(value = "用户姓名")
    private String username;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "详细的地址")
    private String addressDetail;
}
