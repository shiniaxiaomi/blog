package com.lyj.blog.controller;

import com.lyj.blog.model.req.Message;
import com.lyj.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    // 登入

    // 退出

    @ResponseBody
    @RequestMapping("visitCount")
    public Message visitCount(){
        return Message.success(null,userService.selectVisitCount());
    }



}
