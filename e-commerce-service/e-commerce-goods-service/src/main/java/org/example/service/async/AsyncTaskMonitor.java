package org.example.service.async;

import io.lettuce.core.dynamic.annotation.CommandNaming;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.constant.AsyncTaskStatusEnum;
import org.example.vo.AsyncTaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 异步任务执行监控切面
 * @author zhoudashuai
 * @date 2022年04月05日 7:05 下午
 */
@Slf4j
@Aspect
@Component
public class AsyncTaskMonitor {

    private final AsyncTaskManager asyncTaskManager;

    public AsyncTaskMonitor(AsyncTaskManager asyncTaskManager) {
        this.asyncTaskManager = asyncTaskManager;
    }

    /**
     * 异步任务执行的环绕切面
     * 环绕切换让我们可以再方法执行之前和执行之后做一些额外的操作
     * @param proceedingJoinPoint
     * @return
     */
    @Around("execution(* org.example.service.async.AsyncServiceImpl.*(..))")
    public Object taskHandle(ProceedingJoinPoint proceedingJoinPoint){

        //获取taskId，调用异步任务传入的第二个参数
        String taskId = proceedingJoinPoint.getArgs()[1].toString();

        //获取任务信息,在提交任务的时候就已经放入容器中了
        AsyncTaskInfo taskInfo = asyncTaskManager.getTaskInfo(taskId);

        log.info("AsyncTaskMonitor is monitoring async task: [{}]",taskId);

        taskInfo.setStatus(AsyncTaskStatusEnum.RUNNING);
        asyncTaskManager.setTaskInfo(taskInfo);//设置为运行状态，并重新放入容器

        AsyncTaskStatusEnum status;

        Object result;

        try {
            result = proceedingJoinPoint.proceed();
            status = AsyncTaskStatusEnum.SUCCESS;
        }catch (Throwable ex){
            result = null;
            status = AsyncTaskStatusEnum.FAILED;
            log.error("AsyncTaskMonitor: async task [{}] is failed,Error Info : [{}]",
                    taskId,ex);
        }
        //设置异步任务其他的信息，再次放入容器中
        taskInfo.setEndTime(new Date());
        taskInfo.setStatus(status);
        taskInfo.setTotalTime(String.valueOf(taskInfo.getEndTime().getTime() - taskInfo.getStartTime().getTime()));
        asyncTaskManager.setTaskInfo(taskInfo);
        return result;
    }
}
