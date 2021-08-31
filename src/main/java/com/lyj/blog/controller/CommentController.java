package com.lyj.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyj.blog.config.Constant;
import com.lyj.blog.model.Blog;
import com.lyj.blog.model.Comment;
import com.lyj.blog.model.req.CommentReq;
import com.lyj.blog.model.req.Message;
import com.lyj.blog.service.BlogService;
import com.lyj.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/8/10 1:39 下午
 */
@Controller
@RequestMapping("comment")
@Validated
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    BlogService blogService;

    @ResponseBody
    @GetMapping("{blogId}/{page}")
    public Message list(@PathVariable("blogId") int blogId, @PathVariable("page") int page) {
        Page<Comment> commentPage = commentService.selectByBlogId(blogId, page, Constant.SIZE);
        return Message.success(null, commentPage);
    }

    @ResponseBody
    @GetMapping("{id}")
    public Message list(@PathVariable("id") int commentId) {
        Comment comment = commentService.selectLeftJoinCommentUserById(commentId);
        return Message.success(null, comment);
    }

    @ResponseBody
    @PostMapping
    public Message insert(@Validated CommentReq commentReq) {
        int commentId = commentService.insert(commentReq);
        return Message.success(null, commentId);
    }

    @GetMapping("reply/{id}")
    public ModelAndView reply(@PathVariable("id") int commentId) {
        ModelAndView mav = new ModelAndView("blog/reply");
        //根据评论id查询blog的id和name
        Blog blog = blogService.selectBlogByCommentId(commentId);
        mav.addObject("blogId", blog.getId());
        mav.addObject("blogName", blog.getName());
        mav.addObject("commentId", commentId);
        return mav;
    }

    @ResponseBody
    @PostMapping("like/incr")
    public Message incr(@RequestParam("id") int commentId) {
        commentService.incr(commentId);
        return Message.success(null);
    }
}
