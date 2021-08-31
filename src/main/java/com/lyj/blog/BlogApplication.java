package com.lyj.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/7/24 9:27 下午
 */
@SpringBootApplication
@EnableAspectJAutoProxy
@MapperScan("com.lyj.blog.mapper")
public class BlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class);
    }

}
