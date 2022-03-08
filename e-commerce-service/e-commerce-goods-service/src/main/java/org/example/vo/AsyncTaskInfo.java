package org.example.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
import org.example.constant.AsyncTaskStatusEnum;

import java.util.Date;

/**
 * 异步任务执行信息
 * @author zhoudashuai
 * @date 2022年01月04日 11:38 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsyncTaskInfo {

    private String taskId;

    /**异步任务执行状态*/
    private AsyncTaskStatusEnum status;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 异步任务总耗时
     */
    private String totalTime;
}
