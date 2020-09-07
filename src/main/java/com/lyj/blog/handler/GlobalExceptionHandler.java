package com.lyj.blog.handler;

import com.lyj.blog.exception.MessageException;
import com.lyj.blog.model.req.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import java.util.Arrays;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/8/12 11:45 上午
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 捕获所有异常(兜底的异常处理)
    @ExceptionHandler(Exception.class)
    public Message handleException(Throwable e){
        log.error("Exception异常",e);
        return Message.error(e.getMessage());
    }

    @ExceptionHandler(MessageException.class)
    public Message handleMessageException(Throwable e){
        log.error("自定义异常",e);
        return Message.error(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public void handleIllegalArgumentException(Throwable e){
        if(e.getMessage().contains("RedisCacheConfiguration")){
            log.error("redis不能存储null值，可忽略该报错",e);
        }
    }

    // 文件上传异常
    @ExceptionHandler(MultipartException.class)
    public Message handleFileUploadException(Throwable e){
        log.error("文件上传异常",e);
        return Message.error(e.getMessage());
    }


}
