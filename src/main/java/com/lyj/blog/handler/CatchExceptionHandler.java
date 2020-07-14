package com.lyj.blog.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Yingjie.Lu
 * @description 统一异常捕获
 * @date 2020/7/13 8:33 下午
 */
@Slf4j
@RestControllerAdvice
public class CatchExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public Message handleBusinessException(GlobalException ex) {
        ex.printStackTrace();
        return Message.error(ex,ex.getMessage());
    }
    @ExceptionHandler(DataAccessException.class)
    public Message handleRuntimeException(RuntimeException ex) {
        ex.printStackTrace();
        return Message.error(ex.getMessage(),"数据库异常，请检查！");
    }

}
