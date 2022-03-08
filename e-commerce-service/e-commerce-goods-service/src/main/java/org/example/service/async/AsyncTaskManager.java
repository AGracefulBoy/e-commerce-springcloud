package org.example.service.async;

import lombok.extern.slf4j.Slf4j;
import org.example.constant.AsyncTaskStatusEnum;
import org.example.goods.GoodsInfo;
import org.example.vo.AsyncTaskInfo;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 异步任务执行管理器
 * 对异步任务进行包装管理，记录并塞入异步任务的执行信息
 * @author zhoudashuai
 * @date 2022年01月06日 11:48 下午
 */
@Slf4j
@Component
public class AsyncTaskManager {

    /** 异步任务执行信息容器 */
    private final Map<String , AsyncTaskInfo> taskContainer =
            new HashMap<>(16);

    private final IAsyncService asyncService;

    public AsyncTaskManager(IAsyncService asyncService) {
        this.asyncService = asyncService;
    }

    /**
     * 初始化异步任务
     * @return
     */
    public AsyncTaskInfo initTask(){
        AsyncTaskInfo taskInfo = new AsyncTaskInfo();
        //设置一个唯一的异步任务id，只要唯一即可
        taskInfo.setTaskId(UUID.randomUUID().toString());
        taskInfo.setStatus(AsyncTaskStatusEnum.STARED);
        taskInfo.setStartTime(new Date());
        //异步任务执行信息，放入到容器中
        taskContainer.put(taskInfo.getTaskId(),taskInfo);
        return taskInfo;
    }

    /**
     * 提交异步任务
     * @param goodsInfos
     * @return
     */
    public AsyncTaskInfo submit(List<GoodsInfo> goodsInfos){
        // 初始化一个异步任务的监控信息
        AsyncTaskInfo taskInfo = initTask();
        asyncService.asyncImportGoods(goodsInfos, taskInfo.getTaskId());
        return taskInfo;
    }

    /**
     * 设置异步任务执行状态信息
     * @param taskInfo
     */
    public void setTaskInfo(AsyncTaskInfo taskInfo){
        taskContainer.put(taskInfo.getTaskId(),taskInfo);
    }

    /**
     * 获取异步任务执行信息
     * @param taskId
     * @return
     */
    public AsyncTaskInfo getTaskInfo(String taskId){
        return taskContainer.get(taskId);
    }
}
