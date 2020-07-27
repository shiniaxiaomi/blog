package com.lyj.blog.service;

import com.lyj.blog.mapper.BlogMapper;
import com.lyj.blog.model.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/7/27 7:27 下午
 */
@Service
public class BlogService {


    @Autowired
    BlogMapper blogMapper;


    public void insert(Blog blog) {
        blogMapper.insert(blog);
    }
}
