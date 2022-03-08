package org.example.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异步任务状态枚举
 */
@Getter
@AllArgsConstructor
public enum AsyncTaskStatusEnum {

    STARED(0,"已经启动"),
    RUNNING(1,"正在运行状态"),
    SUCCESS(2,"执行成功"),
    FAILED(3,"执行失败"),
    ;

    /**执行状态编码 */
    private final int state;
    /**执行状态描述*/
    private final String stateInfo;
}
