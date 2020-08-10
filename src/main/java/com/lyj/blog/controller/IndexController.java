package com.lyj.blog.controller;

import com.lyj.blog.config.Constant;
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
        List<Blog> stickBlogList = blogService.selectIndexBlogs(true, false, 1, Constant.SIZE/2);
        // 查询非置顶非私有博客列表
        List<Blog> newestBlogList = blogService.selectIndexBlogs(false, false, 1, Constant.SIZE);
        mav.addObject("stickBlogList",stickBlogList);
        mav.addObject("newestBlogList",newestBlogList);
        return mav;
    }

    @RequestMapping("test")
    public String test(){
        return "admin/test";
    }

    @RequestMapping("index/about")
    public ModelAndView about(){
        ModelAndView mav = new ModelAndView("blog/index");
        Blog blog = blogService.selectHTMLAndNameByName("关于");
        if(blog==null){
            mav.addObject("html","请先创建该博客");
            return mav;
        }
        blogService.countIncr(blog.getId());//自增blog的访问次数
        mav.addObject("html",blog.getMdHtml());
        mav.addObject("blogId",blog.getId());
        mav.addObject("blogName",blog.getName());

        return mav;
    }

    @RequestMapping("index/todo")
    public ModelAndView todo(){
        ModelAndView mav = new ModelAndView("blog/index");
        Blog blog = blogService.selectHTMLAndNameByName("待办");
        if(blog==null){
            mav.addObject("html","请先创建该博客");
            return mav;
        }
        blogService.countIncr(blog.getId());//自增blog的访问次数
        mav.addObject("html",blog.getMdHtml());
        mav.addObject("blogId",blog.getId());
        mav.addObject("blogName",blog.getName());

        return mav;
    }

    @RequestMapping("index/toc")
    public String toc(){
        return "nav/toc";
    }

    @RequestMapping("index/tag")
    public String tag(){
        return "nav/tag";
    }
}
