package com.lyj.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyj.blog.mapper.BlogMapper;
import com.lyj.blog.model.Blog;
import com.lyj.blog.other.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

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

    //请求pid下的所有blog
    @RequestMapping("blogs")
    public ModelAndView blogs(int pid){
        ModelAndView modelAndView = new ModelAndView("index");

        List<Blog> blogs = blogMapper.selectList(new QueryWrapper<Blog>().select("id", "name", "`desc`", "create_time", "update_time").eq("pid", pid));//直接指定字段
        modelAndView.addObject("blogs",blogs);

        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("blog/create")
    public Message create(String name,int pid){

        Date date = new Date();
        Blog blog = new Blog().setName(name).setCreateTime(date).setUpdateTime(date).setPid(pid);
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

    @ResponseBody
    @RequestMapping("blog/delete")
    public void delete(int id){

        blogMapper.deleteById(id);
    }


}
