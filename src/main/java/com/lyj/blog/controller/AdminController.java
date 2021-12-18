package com.lyj.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyj.blog.config.Constant;
import com.lyj.blog.handler.Util;
import com.lyj.blog.interceptor.NeedLogin;
import com.lyj.blog.mapper.BlogMapper;
import com.lyj.blog.model.Blog;
import com.lyj.blog.model.File;
import com.lyj.blog.model.req.Message;
import com.lyj.blog.parser.Parser;
import com.lyj.blog.service.BlogService;
import com.lyj.blog.service.FileService;
import com.lyj.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/7/27 7:34 下午
 */
@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    FileService fileService;

    @Autowired
    BlogService blogService;

    @Autowired
    HttpSession session;

    @Autowired
    Parser parser;

    @Autowired
    BlogMapper blogMapper;

    @Autowired
    TagService tagService;

    // 在编辑页面时保持心跳
    @ResponseBody
    @NeedLogin
    @GetMapping("keepHeartbeat")
    public Message keepHeartbeat() {
        return Message.success(null);
    }

    @NeedLogin
    @GetMapping({"", "blog"})
    public String blog() {
        return "admin/blog";
    }

    @NeedLogin
    @GetMapping("blog/{id}")
    public ModelAndView editBlog(@PathVariable("id") int id) {
        ModelAndView mav = new ModelAndView("admin/blog");
        mav.addObject("blogId", id);
        return mav;
    }

    @NeedLogin
    @GetMapping("blog/private/{page}")
    public ModelAndView privateBlogList(@PathVariable("page") int page) {
        ModelAndView mav = new ModelAndView("blog/more");
        Page<Blog> blogPage = blogService.selectBlogItemsPage(null, true, page, Constant.SIZE);
        Util.renderPageParam(mav, blogPage, "/admin/blog/private/", "私有博客 分页");
        mav.addObject("moreBlogList", blogPage.getRecords());//分页数据
        return mav;
    }

    @NeedLogin
    @GetMapping("tag")
    public String tag() {
        return "admin/tag";
    }

    // 分页查询所有的文件
    @NeedLogin
    @GetMapping("file/{page}")
    public ModelAndView fileDashboard(@PathVariable("page") int page) {
        ModelAndView mav = new ModelAndView("admin/file");
        Page<File> filePage = fileService.selectPageByBlogId(null, page, Constant.SIZE);
        Util.renderPageParam(mav, filePage, "/admin/file/", "所有文件 分页");
        mav.addObject("fileList", filePage.getRecords());
        return mav;
    }

    // 分页查询具体某个blog的所有的文件
    @NeedLogin
    @GetMapping("file/{blogId}/{page}")
    public ModelAndView fileDashboard(@PathVariable("blogId") int blogId, @PathVariable("page") int page) {
        ModelAndView mav = new ModelAndView("admin/file");
        Page<File> filePage = fileService.selectPageByBlogId(blogId, page, Constant.SIZE);
        String blogName = blogService.selectNameById(blogId);
        Util.renderPageParam(mav, filePage, "/admin/file/" + blogId + "/", blogName + "文件 分页");
        mav.addObject("fileList", filePage.getRecords());
        mav.addObject("blogId", blogId);
        return mav;
    }

}
