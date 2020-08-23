package com.lyj.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/8/14 9:42 上午
 */
@Configuration
public class EmailConfig {

    @DependsOn("loadConfig") //依赖于加载配置类，所以加载配置类会先加载
    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.qq.com");
        javaMailSender.setPort(465);//设置端口
        javaMailSender.setUsername(LoadConfig.getInstance().getEmail());
        javaMailSender.setPassword(LoadConfig.getInstance().getEmailPassword());
        javaMailSender.setDefaultEncoding("utf-8");
        return javaMailSender;
    }

}
