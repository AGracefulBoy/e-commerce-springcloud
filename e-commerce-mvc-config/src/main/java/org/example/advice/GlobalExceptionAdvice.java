package org.example.advice;

import cn.hutool.http.server.HttpServerRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.vo.CommonResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常捕获处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {
    @ExceptionHandler(value = Exception.class)
    public CommonResponse<String> handlerCommerceException(HttpServerRequest req,Exception exception){
        CommonResponse<String> response = new CommonResponse<>(-1,"business error");
        response.setData(exception.getMessage());
        log.error("commerce service has errorL: [{}]",exception.getMessage(),exception);
        return response;
    }
}
