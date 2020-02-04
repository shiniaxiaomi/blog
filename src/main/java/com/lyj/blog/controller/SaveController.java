package com.lyj.blog.controller;


import com.lyj.blog.annotation.NeedLogin;
import com.lyj.blog.model.Blog;
import com.lyj.blog.model.Message;
import com.lyj.blog.service.BlogAndTagService;
import com.lyj.blog.service.BlogService;
import com.lyj.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SaveController {

    @Autowired
    BlogService blogService;
    @Autowired
    TagService tagService;
    @Autowired
    BlogAndTagService blogAndTagService;


    /**
     * 缓存主要的删除点就在blog的创建和保存
     * @param blog
     * @return
     * @throws Exception
     */
    @NeedLogin
    @RequestMapping("saveBlog")
    @ResponseBody
    public Message saveBlog(Blog blog) throws Exception {
        Integer blogId = blog.getId();

        if(blogId==null){
            blogService.create(blog);//创建博客
        }else{
            blogService.save(blog);//更新博客
        }
        return Message.success("保存成功");
    }

    /**
     * 缓存主要的删除点就在blog的创建和保存
     * @param blog
     * @return
     * @throws Exception
     */
    @NeedLogin
    @RequestMapping("saveDesc")
    @ResponseBody
    public Message saveDesc(Blog blog) throws Exception {
        Integer blogId = blog.getId();

        if(blogId==null){
            return Message.error("错误:blogId不能为空");
        }

        blogService.save(blog);

        return Message.success("保存成功");
    }



}
