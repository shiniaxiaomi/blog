package com.lyj.blog.controller;

import com.lyj.blog.config.Message;
import com.lyj.blog.model.Blog;
import com.lyj.blog.model.req.EsSearch;
import com.lyj.blog.service.BlogService;
import com.lyj.blog.service.BlogTagRelationService;
import com.lyj.blog.service.EsService;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/7/27 7:27 下午
 */
@Controller
@RequestMapping("blog")
public class BlogController {

    @Autowired
    BlogService blogService;

    @Autowired
    BlogTagRelationService blogTagRelationService;

    @Autowired
    EsService esService;


    @ResponseBody
    @GetMapping("md")
    public Message getMD(int id){
        String md = blogService.getMD(id);
        return Message.success(null,md);
    }

    @ResponseBody
    @PostMapping("update")
    public Message update(Blog blog){
        blogService.update(blog);
        return Message.success("更新成功");
    }

    @ResponseBody
    @GetMapping("config")
    public Message selectConfig(int id){
        boolean isPrivate = blogService.getIsPrivate(id);
        List<Integer> checkedTagIds = blogTagRelationService.selectTagIdsByBlogId(id);

        // 组合结果
        HashMap<Object, Object> map = new HashMap<>();
        map.put("isPrivate",isPrivate);
        map.put("checkedTagIds",checkedTagIds);

        return Message.success(null,map);
    }

    @ResponseBody
    @PostMapping("config")
    public Message updateConfig(int id,boolean isPrivate,Integer[] tags){
        blogService.updateConfig(id,isPrivate,tags);
        return Message.success("更新成功");
    }

    @GetMapping("search/index")
    public ModelAndView searchIndex(String keyword){
        ModelAndView mav = new ModelAndView("blog/search");
        mav.addObject("keyword",keyword);
        return mav;
    }

    @ResponseBody
    @GetMapping("search")
    public Message search(EsSearch esSearch){
        SearchHit[] search = esService.search(esSearch);
        return Message.success(null,search);
    }

    @GetMapping("{id}")
    public ModelAndView blog(@PathVariable("id") int id){
        ModelAndView mav = new ModelAndView("blog/index");
        String html = blogService.selectHTML(id);
        mav.addObject("html",html);
        mav.addObject("blogId",id);
        return mav;
    }

}
