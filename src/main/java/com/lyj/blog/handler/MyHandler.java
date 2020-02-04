package com.lyj.blog.handler;

import com.lyj.blog.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异常统一处理类
 * @author yingjie.lu
 * @version 1.0
 * @date 2019/12/23 1:38 下午
 */

@ControllerAdvice
@Slf4j
public class MyHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody   //设置返回字符串,而非对应的错误页面
    public Message handler(HttpServletRequest request, HttpServletResponse response, Exception e){
        log.error("Controller请求异常：{},异常为：{}",request.getServletPath(),e);
        return Message.error(e.getMessage());
    }
}
