package com.lyj.blog.timer;


import com.lyj.blog.controller.PageController;
import com.lyj.blog.service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * redis数据预热
 */
@Component
@Slf4j
public class ReloadTimer {

    @Autowired
    BlogService blogService;

    //只需要调用一下index()方法,来重新加载所有数据到缓存
    @Autowired
    PageController pageController;

    //定时更新blog的缓存数据(先清除,在加载)
    @Scheduled(cron = "0 0 4 * * *")
    public void reloadBlogCache(){
        try {
            //清除redis中blog相关的所有缓存
            blogService.cleanBlogCache();
            //加载所有数据到缓存
            pageController.index();
        } catch (Exception e) {
            log.error(String.valueOf(e.getCause()));
        }
    }



}
