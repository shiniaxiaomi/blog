package com.lyj.blog.timer;


import com.lyj.blog.exception.MessageException;
import com.lyj.blog.service.BlogService;
import com.lyj.blog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CountTimer {

    @Autowired
    BlogService blogService;

    @Autowired
    UserService userService;

    //定时更新blog的访问数据
    @Scheduled(cron = "0 0 2 * * *")
    public void updateVisitCount() throws MessageException {
        blogService.updateVisitCountToDataBase();
    }

    //定时更新首页的访问数据
    @Scheduled(cron = "0 0 2 * * *")
    public void updateHomePageVisitCount() throws MessageException {
        userService.updateHomePageVisitCountToDataBase();
    }



}
