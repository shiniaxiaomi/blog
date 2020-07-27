package com.lyj.blog.controller;

import com.lyj.blog.model.Blog;
import com.lyj.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @PostMapping("insert")
    public String insert(@RequestBody Blog blog){
        blogService.insert(blog);
        return "添加成功";
    }





}
