package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.example.common.TableId;
import org.example.goods.DeductGoodsInventory;
import org.example.goods.GoodsInfo;
import org.example.goods.SimpleGoodsInfo;
import org.example.service.IGoodsService;
import org.example.vo.PageSimpleGoodsInfo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品服务接口
 * @author zhoudashuai
 * @date 2022年04月09日 10:57 下午
 */
@Slf4j
@Api(tags = "商品微服务功能接口")
@RestController
@RequestMapping("/goods")
public class GoodsController {

    private final IGoodsService goodsService;

    public GoodsController(IGoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @ApiOperation(value = "详细信息",notes = "根据tableId查询详细商品信息",httpMethod = "POST")
    @PostMapping("/goods-info")
    public List<GoodsInfo> getGoodsInfoByTableId(@RequestBody TableId tableId){
        return goodsService.getGoodsInfoByTableId(tableId);
    }

    @ApiOperation(value ="简单商品信息",notes = "获取分页的建档商品信息",httpMethod = "GET")
    @GetMapping("/page-simple-goods-info")
    public PageSimpleGoodsInfo getSimpleGoodsInfoByPage(
            @RequestParam(required = false,defaultValue = "1") int page){
        return goodsService.getSimpleGoodsInfoByPage(page);
    }

    @ApiOperation(value = "简单商品信息",notes = "根据tableId查询简单商品信息",httpMethod = "POST")
    @PostMapping("/simple-goods-info")
    public List<SimpleGoodsInfo> getSimpleGoodsInfoByTableId(@RequestBody TableId tableId){
        return goodsService.getSimpleGoodsInfoByTableId(tableId);
    }

    @ApiOperation(value = "扣减商品库存",notes = "扣减商品信息",httpMethod = "PUT")
    @PutMapping("deduct-goods-inventory")
    public Boolean deductGoodsInventory(@RequestBody List<DeductGoodsInventory> deductGoodsInventories){
        return goodsService.deductGoodsInventory(deductGoodsInventories);
    }
}
