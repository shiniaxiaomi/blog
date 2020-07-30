package com.lyj.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lyj.blog.mapper.BlogMapper;
import com.lyj.blog.model.Blog;
import com.lyj.blog.model.User;
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

    @Autowired
    UserService userService;

    // 添加blog
    public void insert(Blog blog) {
        blogMapper.insert(blog);
    }


    public void delete(int id) {
        blogMapper.deleteById(id);
    }

    public void updateName(Integer blogId, String name) {
        Blog blog = new Blog().setId(blogId).setName(name);
        blogMapper.updateById(blog);
    }

    public String getMD(int id) {
        Blog blog = blogMapper.selectOne(new QueryWrapper<Blog>().select("md").eq("id", id));
        return blog==null?"":blog.getMd();
    }

    public void update(Blog blog) {
        blogMapper.updateById(blog);
    }
}
