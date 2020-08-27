package com.lyj.blog.handler;

import com.lyj.blog.exception.MessageException;
import com.lyj.blog.model.req.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/8/12 11:45 上午
 */
@Slf4j
@RestControllerAdvice
public class GlobelExceptionHandler {

    @ExceptionHandler(MessageException.class)
    public Message handleMessageException(Throwable e){
        return Message.error(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public void handleIllegalArgumentException(Throwable e){
        if(e.getMessage().contains("RedisCacheConfiguration")){
            log.error("redis不能存储null值，可忽略该报错");
        }
    }

    // 文件上传异常
    @ExceptionHandler(MultipartException.class)
    public Message handleFileUploadException(Throwable e){
        return Message.error(e.getMessage());
    }


}
