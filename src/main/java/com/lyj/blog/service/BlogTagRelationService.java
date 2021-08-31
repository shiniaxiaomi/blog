package com.lyj.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyj.blog.mapper.BlogTagRelationMapper;
import com.lyj.blog.model.BlogTagRelation;
import com.lyj.blog.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/7/31 1:41 下午
 */
@Service
public class BlogTagRelationService {

    @Autowired
    BlogTagRelationMapper blogTagRelationMapper;

    public void deleteBatchByTagIds(List<Integer> tagIds) {
        if (tagIds.size() == 0) {
            return;
        }
        blogTagRelationMapper.delete(new QueryWrapper<BlogTagRelation>().in("tag_id", tagIds));
    }

    public List<Integer> selectTagIdsByBlogId(int id) {
        List<BlogTagRelation> blogTagRelations = blogTagRelationMapper
                .selectList(new QueryWrapper<BlogTagRelation>().select("tag_id").eq("blog_id", id));
        List<Integer> tagIds = blogTagRelations.stream().map(BlogTagRelation::getTagId).collect(Collectors.toList());
        return tagIds;
    }


    public void insertBatch(int blogId, List<Integer> tagIds) {
        if (tagIds.size() == 0) {
            return;
        }
        blogTagRelationMapper.insertBatch(blogId, tagIds);
    }

//    public Page<BlogTagRelation> selectBlogIdByTagId(int tagId,int page,int size){
//        return blogTagRelationMapper.selectPage(new Page<>(page, size),
//                new QueryWrapper<BlogTagRelation>().select("blog_id").eq("tag_id", tagId));
//    }

    public void deleteByBlogId(int blogId) {
        blogTagRelationMapper.delete(new QueryWrapper<BlogTagRelation>().eq("blog_id", blogId));
    }
}
