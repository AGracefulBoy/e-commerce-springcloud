package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.example.goods.GoodsInfo;
import org.example.service.async.AsyncTaskManager;
import org.example.vo.AsyncTaskInfo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 异步服务对外提供的api接口
 * @author zhoudashuai
 * @date 2022年04月06日 9:27 下午
 */
@RestController
@Api(tags = "商品异步入库服务")
@Slf4j
@RequestMapping("/async-goods")
public class AsyncGoodsController {

    private final AsyncTaskManager asyncTaskManager;

    public AsyncGoodsController(AsyncTaskManager asyncTaskManager) {
        this.asyncTaskManager = asyncTaskManager;
    }

    @PostMapping("/import-goods")
    @ApiOperation(value = "导入商品",notes = "导入商品进入到商品表",httpMethod = "POST")
    public AsyncTaskInfo importGoods(@RequestBody List<GoodsInfo> goodsInfos){
        return asyncTaskManager.submit(goodsInfos);
    }

    @GetMapping("/task-info")
    @ApiOperation(value = "查询状态",notes = "查询异步任务的执行状态",httpMethod = "GET")
    public AsyncTaskInfo getTaskInfo(@RequestParam String taskId){
        return asyncTaskManager.getTaskInfo(taskId);
    }
}
