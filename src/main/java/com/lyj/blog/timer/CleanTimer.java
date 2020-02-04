package com.lyj.blog.timer;

import com.lyj.blog.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CleanTimer {

    @Autowired
    TagService tagService;

    //定时删除没有用的tag(凌晨3点,该操作需要在定时加载redis操作之前完成)
    @Scheduled(cron = "0 0 3 * * *")
    public void cleanNoUseTags(){
        try {
            tagService.deleteNoUseTags();
        } catch (Exception e) {
            log.error("定时删除tag失败:",e);
        }
    }

}
