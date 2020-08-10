package com.lyj.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyj.blog.mapper.BlogFileRelationMapper;
import com.lyj.blog.model.BlogFileRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/8/7 11:36 下午
 */
@Service
public class BlogFileRelationService {
    @Autowired
    BlogFileRelationMapper blogFileRelationMapper;

    public void insert(BlogFileRelation blogFileRelation){
        blogFileRelationMapper.insert(blogFileRelation);
    }

    public void deleteRelationByBlogIdAndFileId(int blogId, int fileId) {
        blogFileRelationMapper.delete(new QueryWrapper<BlogFileRelation>()
                .eq("blog_id",blogId).eq("file_id",fileId));
    }

    public void deleteRelationByFileId(int fileId) {
        blogFileRelationMapper.delete(new QueryWrapper<BlogFileRelation>().eq("file_id",fileId));
    }
}
