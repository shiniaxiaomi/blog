package com.lyj.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

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
        int port = 465;

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.qq.com");
        javaMailSender.setUsername(LoadConfig.getInstance().getEmail());
        javaMailSender.setPassword(LoadConfig.getInstance().getEmailPassword());
        javaMailSender.setDefaultEncoding("utf-8");

        // 配置端口和ssl（使用在阿里云和本地环境）
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", "true");//开启认证
        properties.setProperty("mail.smtp.timeout", "1000");//设置链接超时
        properties.setProperty("mail.smtp.port", Integer.toString(port));//设置端口
        properties.setProperty("mail.smtp.socketFactory.port", Integer.toString(port));//设置ssl端口
        properties.setProperty("mail.smtp.socketFactory.fallback", "false");
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        javaMailSender.setJavaMailProperties(properties);
        return javaMailSender;
    }

}
