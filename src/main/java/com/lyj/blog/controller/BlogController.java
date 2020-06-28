package com.lyj.blog.controller;

import com.lyj.blog.mapper.BlogMapper;
import com.lyj.blog.model.Blog;
import com.lyj.blog.other.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Controller
public class BlogController {

    @Autowired
    BlogMapper blogMapper;

    @RequestMapping("blog")
    public ModelAndView blog(int id){
        ModelAndView modelAndView = new ModelAndView("blog");

        Blog blog = blogMapper.selectById(id);
        modelAndView.addObject("mdHtml",blog.getMdHtml());//拿到mdHtml信息
        modelAndView.addObject("id",id);

        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("blog/create")
    public Message create(String name){

        Date date = new Date();
        Blog blog = new Blog().setName(name).setCreateTime(date).setUpdateTime(date);
        blogMapper.insert(blog);

        return new Message().setData(blog.getId());//返回新生成blog的id
    }

    @RequestMapping("blog/edit")
    public ModelAndView edit(int id){
        ModelAndView modelAndView = new ModelAndView("edit");
        modelAndView.addObject("blogId",id);//设置本地浏览器缓存的id号

        Blog blog = blogMapper.selectById(id);
        modelAndView.addObject("md",blog.getMd());//拿到md信息

        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("blog/update")
    public void update(Blog blog){
        blog.setUpdateTime(new Date());
        blogMapper.updateById(blog);
    }

}
