package com.lyj.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/8/14 9:42 上午
 */
@Configuration
public class EmailConfig {

    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.qq.com");
        javaMailSender.setUsername(LoadConfig.getInstance().getEmail());
        javaMailSender.setPassword(LoadConfig.getInstance().getEmailPassword());
        javaMailSender.setDefaultEncoding("utf-8");
        return javaMailSender;
    }

}
