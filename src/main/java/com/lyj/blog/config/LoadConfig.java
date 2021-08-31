package com.lyj.blog.config;

import com.lyj.blog.mapper.ConfigMapper;
import com.lyj.blog.model.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/7/27 9:52 上午
 */
@Component
public class LoadConfig {

    @Autowired
    ConfigMapper configMapper;

    static Config config;

    // 加载配置
    @PostConstruct
    public void loadConfig() {
        config = configMapper.selectById(1);
    }

    public static Config getInstance() {
        return config;
    }


}
