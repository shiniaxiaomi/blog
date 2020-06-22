package com.lyj.blog.bak.aop;

import com.lyj.blog.bak.exception.MessageException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
@Aspect
@Slf4j
public class LoginAOP {


    @Autowired
    HttpSession session;

    @Autowired
    HttpServletRequest request;

    //保存和编辑的控制器的切点
    @Pointcut("@annotation(com.lyj.blog.bak.annotation.NeedLogin)")
    public void needLoginPointcut(){}


    //登入校验
    @Before(value = "needLoginPointcut()")
    public void needLogin() throws MessageException {
        Object user = session.getAttribute("user");
        if(user==null){
            throw new MessageException("请先登入");
        }
    }


    //切所有的控制器,然后根据返回值为ModelAndView来添加是否登入的信息
    @Pointcut("execution(* com.lyj.blog.bak.controller..* (..))")
    public void isLoginPointcut(){}


    //在ModelAndView来添加是否登入的信息
    @Around(value = "isLoginPointcut()")
    public Object isLogin(ProceedingJoinPoint pjp) throws Throwable {
        //记录访问请求
        log.info("请求:"+request.getServletPath());

        //============在业务方法执行之前
        Object proceed = pjp.proceed();//执行业务方法
        //============在业务方法执行之后

        if(proceed instanceof ModelAndView){
            ModelAndView modelAndView = (ModelAndView) proceed;
            //查看是否已经登入
            modelAndView.addObject("isLogin",session.getAttribute("user")!=null);

            return modelAndView;
        }else{
            return proceed;
        }
    }

}
