package com.lyj.blog.controller;

import com.lyj.blog.model.Blog;
import com.lyj.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/7/24 9:25 下午
 */
@Controller
@RequestMapping
public class IndexController {

    @Autowired
    BlogService blogService;

    @RequestMapping("/")
    public ModelAndView index(){
        ModelAndView mav = new ModelAndView("index");
        // 查询置顶非私有博客列表
        List<Blog> stickBlogList = blogService.selectIndexBlogs(true, false, 1, 5);
        // 查询非置顶非私有博客列表
        List<Blog> newestBlogList = blogService.selectIndexBlogs(false, false, 1, 10);
        mav.addObject("stickBlogList",stickBlogList);
        mav.addObject("newestBlogList",newestBlogList);
        return mav;
    }

    @RequestMapping("/test")
    public String test(){
        return "admin/test";
    }

}
