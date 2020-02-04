package com.lyj.blog.controller;


import com.lyj.blog.model.Message;
import com.lyj.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DeleteController {

    @Autowired
    BlogService blogService;


    //根据blogId删除对应的blog
    @RequestMapping("deleteBlog")
    @ResponseBody
    public Message deleteBlog(Integer blogId) throws Exception {
        blogService.deleteBlogById(blogId);
        return Message.success("删除成功");
    }


}
