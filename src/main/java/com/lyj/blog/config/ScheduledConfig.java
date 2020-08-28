package com.lyj.blog.config;

import com.lyj.blog.service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@EnableScheduling //开启定时任务
public class ScheduledConfig {

    @Autowired
    BlogService blogService;

    @Scheduled(cron = "0 0 0 1/2 * *") // 每两天的0点备份一次
    public void backups(){
        blogService.backups();
        log.info("md文档备份成功");
    }

}
