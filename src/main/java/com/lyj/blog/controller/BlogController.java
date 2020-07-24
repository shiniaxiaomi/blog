package com.lyj.blog.controller;

import com.lyj.blog.model.Blog;
import com.lyj.blog.handler.Message;
import com.lyj.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@Controller
@RequestMapping("blog")
public class BlogController {

    @Autowired
    BlogService blogService;

    //根据id查询blog
    @GetMapping
    public ModelAndView blog(@NotNull Integer id){
        ModelAndView modelAndView = new ModelAndView("blog");

        Blog blog = blogService.selectById(id);
        modelAndView.addObject("mdHtml",blog.getMdHtml());//拿到mdHtml信息
        modelAndView.addObject("id",id);

        return modelAndView;
    }

    //请求pid下的所有blog(平级结构)
    @GetMapping("list")
    public ModelAndView blogs(@NotNull Integer pid){
        ModelAndView modelAndView = new ModelAndView("index");
        List<Blog> blogs = blogService.selectList(pid);
        modelAndView.addObject("blogs",blogs);
        return modelAndView;
    }

    //分页查询blogs
    @GetMapping("page")
    @ResponseBody
    public Message blogByPage(@NotNull Integer index){
        return Message.success(blogService.selectByPage(index,5).getRecords());
    }


    //创建blog
    @ResponseBody
    @PostMapping("create")
    public Message create(@NotNull String name,@NotNull Integer pid){
        Blog blog = blogService.insert(name, pid);
        return Message.success(blog.getId(),"博客创建成功");//返回新生成blog的id
    }

    //编辑blog
    @GetMapping("edit")
    public ModelAndView edit(@NotNull Integer id){
        ModelAndView modelAndView = new ModelAndView("edit");
        modelAndView.addObject("blogId",id);//设置本地浏览器缓存的id号

//        Blog blog = blogService.selectById(id);
//        modelAndView.addObject("md",blog.getMd());//拿到md信息

        return modelAndView;
    }

    @ResponseBody
    @GetMapping("getMDByBlogId")
    public String getMDByBlogId(@NotNull Integer id){
        Blog blog = blogService.selectById(id);
        return blog.getMd();//拿到md信息
    }

    @ResponseBody
    @PostMapping("update")
    public Message update(Blog blog){
        blogService.updateById(blog);
        return Message.success("博客更新成功");
    }

    @ResponseBody
    @PostMapping("delete")
    public Message delete(int id){
        blogService.delete(id);
        return Message.success("博客删除成功");
    }

    @ResponseBody
    @GetMapping("getTreeData")
    public String getTreeData(){
        return blogService.getTreeData();
    }

}
