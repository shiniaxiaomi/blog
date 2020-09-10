package com.lyj.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyj.blog.config.Constant;
import com.lyj.blog.handler.Util;
import com.lyj.blog.interceptor.NeedLogin;
import com.lyj.blog.model.req.FilingResult;
import com.lyj.blog.model.req.Message;
import com.lyj.blog.model.Blog;
import com.lyj.blog.model.req.EsSearch;
import com.lyj.blog.service.BlogService;
import com.lyj.blog.service.BlogTagRelationService;
import com.lyj.blog.service.EsService;
import com.lyj.blog.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    HttpSession session;

    @Autowired
    RedisService redisService;

    @ResponseBody
    @GetMapping("md")
    public Message getMD(int id){
        String md = blogService.getMD(id);
        return Message.success(null,md);
    }

    /**
     * 更新blog内容
     * @param blog id，md
     * @return
     */
    @NeedLogin
    @ResponseBody
    @PostMapping("update")
    public Message update(Blog blog){
        blogService.update(blog);
        return Message.success("更新成功");
    }

    @ResponseBody
    @GetMapping("config")
    public Message selectConfig(int id){
        Blog config = blogService.getBlogConfig(id);
        List<Integer> checkedTagIds = blogTagRelationService.selectTagIdsByBlogId(id);

        // 组合结果
        HashMap<Object, Object> map = new HashMap<>();
        map.put("isPrivate",config.getIsPrivate());
        map.put("isStick",config.getIsStick());
        map.put("checkedTagIds",checkedTagIds);

        return Message.success(null,map);
    }

    @NeedLogin
    @ResponseBody
    @PostMapping("config")
    public Message updateConfig(int id,boolean isStick,Integer[] tags){
        Blog blog = new Blog().setId(id).setIsStick(isStick);
        blogService.updateConfig(blog,tags);
        return Message.success("更新成功");
    }

    @GetMapping("search/index")
    public ModelAndView searchIndex(String keyword){
        ModelAndView mav = new ModelAndView("blog/search");
        mav.addObject("keyword",keyword);
        return mav;
    }

    @ResponseBody
    @GetMapping("search/{page}")
    public Message search(EsSearch esSearch,@PathVariable("page") int page){
        Map map = esService.search(esSearch, page);
        return Message.success(null,map);
    }

    @GetMapping("{id}")
    public ModelAndView blog(@PathVariable("id") int id){
        ModelAndView mav = new ModelAndView("blog/index");
        Blog blog = blogService.selectHTMLAndName(id);
        redisService.incrVisitCountByBlogId(id);// 手动自增访问次数
        mav.addObject("html",blog.getMdHtml());
        mav.addObject("blogId",id);
        mav.addObject("blogName",blog.getName());
        return mav;
    }

    // 查询归档数据
    @ResponseBody
    @GetMapping("filing")
    public Message filing(){
        List<FilingResult> filing = blogService.filing();
        return Message.success(null,filing);
    }

    // 查询归档数据
    @GetMapping("filing/{year}/{page}")
    public ModelAndView filing(@PathVariable("year") int year,@PathVariable("page") int page){
        ModelAndView mav = new ModelAndView("blog/more");
        Page<Blog> blogPage = blogService.selectBlogItemsByYear(year,Util.getIsPrivate(session), page, Constant.SIZE);
        Util.renderPageParam(mav,blogPage,"/blog/filing/"+year+"/",year+" 归档");
        mav.addObject("moreBlogList",blogPage.getRecords());//分页数据
        return mav;
    }

    // 分页查询置顶数据
    @GetMapping("stick/{page}")
    public ModelAndView stick(@PathVariable("page") int page){
        ModelAndView mav = new ModelAndView("blog/more");
        Page<Blog> blogPage =  blogService.selectBlogItemsPage(true,Util.getIsPrivate(session),page,Constant.SIZE);
        Util.renderPageParam(mav,blogPage,"/blog/stick/","置顶博客 分页");
        mav.addObject("moreBlogList",blogPage.getRecords());//分页数据
        return mav;
    }

    // 分页查询最新数据
    @GetMapping("newest/{page}")
    public ModelAndView newest(@PathVariable("page") int page){
        ModelAndView mav = new ModelAndView("blog/more");
        Page<Blog> blogPage =  blogService.selectBlogItemsPage(false,Util.getIsPrivate(session),page,Constant.SIZE);
        Util.renderPageParam(mav,blogPage,"/blog/newest/","最新博客 分页");
        mav.addObject("moreBlogList",blogPage.getRecords());//分页数据
        return mav;
    }

    @NeedLogin
    @ResponseBody
    @PostMapping("upload/{blogId}")
    public Message uploadFile(@RequestParam("file") MultipartFile multipartFile,@PathVariable("blogId") int blogId){
        return blogService.uploadFile(multipartFile,blogId);
    }

    // 备份md文档
    @NeedLogin
    @ResponseBody
    @GetMapping("backups")
    public void backups(){
        blogService.backups();
    }

    // TODO: 2020/8/31 提供一个批量导入的接口（上传功能），选择对应的文件夹进行上传，可以选择多个文件上传，然后创建对应的blog

    // 获取博客的访问总数
    @ResponseBody
    @RequestMapping("visitCountAll")
    public Message visitCountAll(){
        return Message.success(null,redisService.selectVisitCountAll());
    }

}
