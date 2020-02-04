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
//    @Scheduled(cron = "0 0 3 * * *")
    //废弃,因为tag数量可能不会很精准,所以如果按照tag所拥有的blog数量为0进行删除,不是很可靠(还是准备定期手动删除,最好搞一个接口进行删除)
    @Deprecated
    public void cleanNoUseTags(){
        try {
            tagService.deleteNoUseTags();
        } catch (Exception e) {
            log.error("定时删除tag失败:",e);
        }
    }



}
