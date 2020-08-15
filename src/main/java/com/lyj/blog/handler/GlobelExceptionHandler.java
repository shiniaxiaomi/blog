package com.lyj.blog.handler;

import com.lyj.blog.exception.MessageException;
import com.lyj.blog.model.req.Message;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/8/12 11:45 上午
 */
@RestControllerAdvice
public class GlobelExceptionHandler {

    @ExceptionHandler(MessageException.class)
    public Message handleMessageException(Throwable e){
        return Message.error(e.getMessage());
    }

}
