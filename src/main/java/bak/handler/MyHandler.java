package bak.handler;

import bak.exception.MessageException;
import bak.model.Message;
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

    //统一的Exception异常处理
//    @ExceptionHandler(Exception.class)
//    @ResponseBody   //设置返回字符串,而非对应的错误页面
//    public Message exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e){
//        log.error("\n异常请求:{}\n{}",request.getServletPath(),"异常信息:"+e.getClass().getName()+":\""+e.getMessage()+"\"\n堆栈信息:"+e.getStackTrace()[0]);
//        return Message.error(e.getMessage());
//    }

    //处理自定义的消息异常
    @ExceptionHandler(MessageException.class)
    @ResponseBody   //设置返回字符串,而非对应的错误页面
    public Message messageExceptionHandler(HttpServletRequest request, HttpServletResponse response, MessageException e){
        log.error("\n异常请求:{}\n{}",request.getServletPath(),"异常信息:"+e.getClass().getName()+":\""+e.getMessage()+"\"\n堆栈信息:"+e.getStackTrace()[0]);
        return Message.error(e.getMessage());
    }
}
