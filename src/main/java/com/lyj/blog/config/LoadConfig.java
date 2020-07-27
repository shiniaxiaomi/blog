package com.lyj.blog.config;

import com.lyj.blog.mapper.ConfigMapper;
import com.lyj.blog.model.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/7/27 9:52 上午
 */
@Component
public class LoadConfig {

    static Config config;

    @Autowired
    ConfigMapper configMapper;

    @PostConstruct
    public void loadConfig(){
        config = configMapper.selectById(1);
    }

    public static String getSecretId(){
        return config.getSecretId();
    }

    public static String getSecretKey(){
        return config.getSecretKey();
    }

}
