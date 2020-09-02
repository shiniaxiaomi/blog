package com.lyj.blog.interceptor;

import com.lyj.blog.model.req.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


/**
 * @author Yingjie.Lu
 * @description 请求拦截
 * @date 2020/8/14 2:45 下午
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("请求路径:{}",request.getServletPath());

        // 如果登入，直接放行
        if(request.getSession().getAttribute("isLogin")!=null){
            return true;
        }

        //如果未登入，但是没有标记了NeedLogin注解，直接放行
        if((handler instanceof HandlerMethod) && ((HandlerMethod)handler).getMethodAnnotation(NeedLogin.class)==null){
            return true;
        }

        String accept = request.getHeader("Accept");
        if(accept.equals("*/*")){ //当请求json对象时
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.println(Message.error("请先登入"));
        }else{ //当请求页面时，转发到登入页
            request.getRequestDispatcher("/user/login/form").forward(request,response);//根据url进行转发
            //response.sendRedirect("/user/login/form");//根据url进行重定向
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 添加是否登入的状态信息
        if(modelAndView!=null){
            modelAndView.addObject("isLogin",request.getSession().getAttribute("isLogin")!=null);
        }
    }
}
