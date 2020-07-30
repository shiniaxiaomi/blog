package com.lyj.blog.controller;

import com.lyj.blog.config.Message;
import com.lyj.blog.model.Blog;
import com.lyj.blog.model.User;
import com.lyj.blog.service.BlogService;
import com.lyj.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/7/27 7:27 下午
 */
@Controller
@RequestMapping("blog")
public class BlogController {

    @Autowired
    BlogService blogService;


    @ResponseBody
    @GetMapping("md")
    public Message getMD(int id){
        String md = blogService.getMD(id);
        return Message.success(null,md);
    }

    @ResponseBody
    @PostMapping("update")
    public Message update(Blog blog){
        blogService.update(blog);
        return Message.success("更新成功");
    }




}
