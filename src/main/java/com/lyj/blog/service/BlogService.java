package com.lyj.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyj.blog.mapper.BlogMapper;
import com.lyj.blog.model.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    TagService tagService;

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

    public boolean getIsPrivate(int id) {
        Blog blog = blogMapper.selectOne(new QueryWrapper<Blog>().select("is_private").eq("id", id));
        return blog.getIsPrivate();
    }

    @Transactional
    public void updateConfig(int id,boolean isPrivate, Integer[] tags) {
        // 更新blog的公开状态
        Blog blog = new Blog().setId(id).setIsPrivate(isPrivate);
        blogMapper.updateById(blog);

        // 全量的同步更新标签关联
        tagService.updateRelation(id,tags);
    }
}
