package com.lyj.blog.controller;


import com.lyj.blog.model.Message;
import com.lyj.blog.model.User;
import com.lyj.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("userLogin")
    @ResponseBody
    public Message userLogin(User user, HttpSession session) throws Exception {
        if(session.getAttribute("user")!=null){
            return Message.success("登入成功");
        }

        User loginUser = userService.login(user);
        session.setAttribute("user",loginUser);
        return Message.success("登入成功");
    }

}
