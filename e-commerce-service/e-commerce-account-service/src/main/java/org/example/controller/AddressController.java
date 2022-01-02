package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.example.account.AddressInfo;
import org.example.common.TableId;
import org.example.service.IAddressService;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhoudashuai
 * @date 2021年12月25日 10:42 下午
 */
@Api(tags = "用户地址服务")
@RestController
@RequestMapping("/address")
@Slf4j
public class AddressController {

    private final IAddressService addressService;

    public AddressController(IAddressService addressService) {
        this.addressService = addressService;
    }

    @ApiOperation(value = "创建",notes = "创建当前用户地址信息",httpMethod = "POST")
    @PostMapping("/createAddress")
    public TableId createAddressInfo(@RequestBody AddressInfo addressInfo){
        return addressService.createAddressInfo(addressInfo);
    }

    @ApiOperation(value = "当前用户",notes = "获取当前用户地址信息",httpMethod = "GET")
    @GetMapping("/current-address")
    public AddressInfo getCurrentAddressInfo(){
        return addressService.getCurrentAddressInfo();
    }

    @ApiOperation(value = "获取用户地址信息",
            notes ="用户id获取用户地址信息,id是地址表的主键",httpMethod = "GET")
    @GetMapping("/address-info")
    public AddressInfo getAddressInfoById(@RequestParam("id") Long id){
        return addressService.getAddressInfoById(id);
    }

    @ApiOperation(value = "获取用户地址信息",notes = "通过tableId获取用户地址信息",
    httpMethod = "POST")
    @PostMapping("/address-info-by-table-id")
    public AddressInfo getAddressInfoByTableId(@RequestBody TableId tableId){
        return addressService.getAddressInfoByTableId(tableId);
    }
}
