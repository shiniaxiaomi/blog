package com.lyj.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyj.blog.model.Blog;
import com.lyj.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class IndexController {

    @Autowired
    BlogService blogService;

    @GetMapping("/")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("index");

        Page<Blog> stickBlogs = blogService.selectBlogByTagName("置顶"); //置顶博客
        Page<Blog> newestBlogs = blogService.blogByPage(1); //最新博客

        modelAndView.addObject("stickBlogs",stickBlogs.getRecords());
        modelAndView.addObject("newestBlogs",newestBlogs.getRecords());

        return modelAndView;
    }

    //草稿页面
    @GetMapping("draft")
    public String draft(){
        // 使用草稿时需要先创建好，并指定草稿对应的id
        return "forward:/blog/edit?id=51";
    }

    //草稿页面
    @GetMapping("todo")
    public String todo(){
        // 使用草稿时需要先创建好，并指定草稿对应的id
        return "forward:/blog/edit?id=62";
    }

    //草稿页面
    @GetMapping("aboutMe")
    public String aboutMe(){
        // 使用草稿时需要先创建好，并指定草稿对应的id
        return "forward:/blog?id=22";
    }

}
