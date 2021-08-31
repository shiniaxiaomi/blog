package com.lyj.blog.controller;

import com.lyj.blog.interceptor.NeedLogin;
import com.lyj.blog.model.User;
import com.lyj.blog.model.req.Message;
import com.lyj.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/7/29 12:19 下午
 */
@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;

    @ResponseBody
    @PostMapping("login")
    public Message login(String username, String password) {
        User user = new User().setUserName(username).setPassword(password);
        userService.login(user);
        return Message.success("登入成功");
    }

    // 获取登入表单
    @GetMapping("login/form")
    public ModelAndView loginForm(HttpSession session) {
        ModelAndView mav = new ModelAndView("admin/login");
        mav.addObject("isLogin", session.getAttribute("isLogin") != null);
        return mav;
    }

    // 退出登入
    @NeedLogin
    @ResponseBody
    @GetMapping("exitLogin")
    public Message exitLogin(HttpSession session) {
        session.removeAttribute("isLogin");
        return Message.success("退出成功");
    }


}
