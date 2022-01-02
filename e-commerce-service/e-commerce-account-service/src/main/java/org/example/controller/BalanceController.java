package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.example.account.BalanceInfo;
import org.example.service.IBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhoudashuai
 * @date 2021年12月25日 10:54 下午
 */
@Api(tags = "获取用户余额服务")
@Slf4j
@RestController
@RequestMapping("/balance")
public class BalanceController {
    private final IBalanceService balanceService;

    public BalanceController(IBalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @ApiOperation(value = "当前用户",notes = "获取当前用户余额信息",httpMethod = "GET")
    @GetMapping("current-balance")
    public BalanceInfo getCurrentUserBalanceInfo(){
        return balanceService.getCurrentUserBalanceInfo();
    }

    @ApiOperation(value = "扣减",notes = "扣减用户余额",httpMethod = "PUT")
    @PutMapping("/deduct-balance")
    public BalanceInfo deductBalance(@RequestBody BalanceInfo balanceInfo){
        return balanceService.deductBalance(balanceInfo);
    }
}
