package com.lyj.blog.timer;


import com.lyj.blog.controller.PageController;
import com.lyj.blog.model.Blog;
import com.lyj.blog.model.Tag;
import com.lyj.blog.service.BlogService;
import com.lyj.blog.service.TagService;
import com.lyj.blog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * redis数据预热
 */
@Component
@Slf4j
public class ReloadTimer {

    @Autowired
    BlogService blogService;

    @Autowired
    TagService tagService;

    @Autowired
    UserService userService;

    //定时更新blog的缓存数据(先清除,在加载)
    @Scheduled(cron = "0 0 4 * * *")
    public void reloadBlogCache(){
        try {
            //清除redis中blog相关的所有缓存
            blogService.cleanBlogCache();
            //加载所有数据到缓存
            this.preReloadData();
        } catch (Exception e) {
            log.error(String.valueOf(e.getCause()));
        }
    }

    //预热数据
    public void preReloadData(){
        //置顶博客
        tagService.getBlogsByTagName("置顶",1);//只要置顶数量不超过10个即可
        //最新博客(分页)
        blogService.getBlogs(0, 10);
        //查询所有的tag
        tagService.getMaxCountBySize(10);
        //查询首页访问次数
        userService.selectAndIncrHomePageVisitCount();
        //归档数据
        blogService.selectBlogCountByYear();
    }



}
