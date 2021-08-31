package com.lyj.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyj.blog.config.Constant;
import com.lyj.blog.handler.Util;
import com.lyj.blog.interceptor.NeedLogin;
import com.lyj.blog.model.Blog;
import com.lyj.blog.model.req.Message;
import com.lyj.blog.model.Tag;
import com.lyj.blog.service.BlogService;
import com.lyj.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/7/27 11:22 下午
 */
@Controller
@RequestMapping("tag")
public class TagController {

    @Autowired
    TagService tagService;

    @Autowired
    BlogService blogService;

    @NeedLogin
    @ResponseBody
    @PostMapping("update")
    public Message update(Tag tag) {
        tagService.update(tag);
        return Message.success("更新成功");
    }

    @NeedLogin
    @ResponseBody
    @PostMapping("insert")
    public Message insert(Tag tag) {
        tagService.insert(tag);
        return Message.success("添加成功");
    }

    @ResponseBody
    @GetMapping
    public Message select() {
        List<Tag> tags = tagService.select();
        return Message.success(null, tags);
    }

    // 支持批量删除
    @NeedLogin
    @ResponseBody
    @PostMapping("delete")
    public Message deleteBatch(@RequestParam("tags") Integer[] tagIds) {
        tagService.deleteBatch(tagIds);
        return Message.success("删除成功");
    }

    // 查询归档数据
    @GetMapping("{id}/{page}")
    public ModelAndView selectBlogItemByTagId(@PathVariable("id") int id, @PathVariable("page") int page) {
        ModelAndView mav = new ModelAndView("blog/more");
        Page<Blog> blogPage = blogService.selectBlogItemsByTagId(id, page, Constant.SIZE);
        String tagName = tagService.selectTagNameById(id);
        Util.renderPageParam(mav, blogPage, "/tag/" + id + "/", tagName + "标签 分页");
        mav.addObject("moreBlogList", blogPage.getRecords());//分页数据
        return mav;
    }

}
