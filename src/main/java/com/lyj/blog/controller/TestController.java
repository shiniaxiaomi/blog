package com.lyj.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyj.blog.mapper.BlogMapper;
import com.lyj.blog.model.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class TestController {

    @Autowired
    BlogMapper blogMapper;

    @RequestMapping("/")
    public ModelAndView test(){
        ModelAndView modelAndView = new ModelAndView("index");


        blogMapper.selectList(new QueryWrapper<Blog>().select("id","name","`desc`","create_time","update_time")); //直接指定字段
        List<Blog> blogs = blogMapper.selectList(new QueryWrapper<>());
        modelAndView.addObject("blogs",blogs);

        return modelAndView;
    }

    @RequestMapping("test")
    public String test1(){
        return "test";
    }

    //草稿页面
    @RequestMapping("draft")
    public String draft(){
        // 使用草稿时需要先创建好，并指定草稿对应的id
        return "forward:/blog/edit?id=21";
    }

    //草稿页面
    @RequestMapping("aboutMe")
    public String aboutMe(){
        // 使用草稿时需要先创建好，并指定草稿对应的id
        return "forward:/blog?id=22";
    }

}
