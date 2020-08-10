package com.lyj.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyj.blog.config.Constant;
import com.lyj.blog.model.Comment;
import com.lyj.blog.model.req.Message;
import com.lyj.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/8/10 1:39 下午
 */
@Controller
@RequestMapping("comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @ResponseBody
    @GetMapping("{blogId}/{page}")
    public Message list(@PathVariable("blogId") int blogId, @PathVariable("page") int page){
        Page<Comment> commentPage = commentService.selectByBlogId(blogId, page, Constant.SIZE);
        return Message.success(null,commentPage);
    }

    @ResponseBody
    @PostMapping
    public Message insert(
                        @RequestParam("blogId") int blogId,
                        @RequestParam("replyId") int replyId,
                        @RequestParam("email") String email,
                        @RequestParam("github_username") String github_username,
                        @RequestParam("html") String html){
        commentService.insert(blogId,replyId,email,github_username,html);
        return Message.success(null);
    }

//    @ResponseBody
//    @GetMapping("selectUserNameByEmail")
//    public Message selectUserNameByEmail(@RequestParam("email") String email){
//        String userName = commentService.selectUserNameByEmail(email);
//        return Message.success(null,userName);
//    }
}
