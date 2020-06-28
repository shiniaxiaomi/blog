package com.lyj.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//@EnableScheduling   //开启定时任务
@MapperScan("com.lyj.blog.mapper") //开启mapper扫描
//@EnableCaching //开启缓存注解,默认使用concurrentHashMap
//@EnableAspectJAutoProxy //开启aop
//@EnableTransactionManagement //开启事务
public class BlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }

}
