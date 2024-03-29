package com.lyj.blog.controller;

import com.lyj.blog.config.Constant;
import com.lyj.blog.handler.Util;
import com.lyj.blog.model.Blog;
import com.lyj.blog.model.req.Message;
import com.lyj.blog.service.BlogService;
import com.lyj.blog.service.CommentService;
import com.lyj.blog.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

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

    @Autowired
    CommentService commentService;

    @Autowired
    HttpSession session;

    @Autowired
    RedisService redisService;

    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("index");
        // 查询置顶非私有博客列表
        List<Blog> stickBlogList = blogService.selectIndexBlogs(true, Util.getIsPrivate(session), 1, Constant.SIZE + 5);
        // 查询非置顶非私有博客列表
        List<Blog> newestBlogList = blogService.selectIndexBlogs(false, Util.getIsPrivate(session), 1, Constant.SIZE + 5);
        mav.addObject("stickBlogList", stickBlogList);
        mav.addObject("newestBlogList", newestBlogList);
        return mav;
    }

    @ResponseBody
    @RequestMapping("test")
    public String test() {
        return "admin/test";
    }

    @RequestMapping("index/about")
    public ModelAndView about() {
        ModelAndView mav = new ModelAndView("blog/index");
        Blog blog = null;
        try {
            blog = blogService.selectHTMLAndNameByName("关于");
        } catch (Exception ignore) {
        }
        if (blog == null) {
            mav.addObject("html", "请先创建该博客");
            return mav;
        }
        redisService.incrVisitCountByBlogId(blog.getId());//自增blog的访问次数
        mav.addObject("html", blog.getMdHtml());
        mav.addObject("blogId", blog.getId());
        mav.addObject("blogName", blog.getName());

        return mav;
    }

    @RequestMapping("index/todo")
    public ModelAndView todo() {
        ModelAndView mav = new ModelAndView("blog/index");
        // 如果未登入，直接返回没有权限
        if (!Util.isLogin(session)) {
            mav.addObject("html", "请先登入");
            return mav;
        }

        Blog blog = null;
        try {
            blog = blogService.selectHTMLAndNameByName("待办");
        } catch (Exception ignore) {
        }
        if (blog == null) {
            mav.addObject("html", "请先创建该博客");
            return mav;
        }
        redisService.incrVisitCountByBlogId(blog.getId());//自增blog的访问次数
        mav.addObject("html", blog.getMdHtml());
        mav.addObject("blogId", blog.getId());
        mav.addObject("blogName", blog.getName());

        return mav;
    }

    @ResponseBody
    @PostMapping("feedback")
    public Message feedback(String email, String content) {
        commentService.feedback(email, content);
        return Message.success("反馈成功");
    }
}
