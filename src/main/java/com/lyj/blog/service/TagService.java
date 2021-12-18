package com.lyj.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyj.blog.mapper.TagMapper;
import com.lyj.blog.model.Blog;
import com.lyj.blog.model.BlogTagRelation;
import com.lyj.blog.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/7/31 11:51 上午
 */
@Service
public class TagService {

    @Autowired
    TagMapper tagMapper;

    @Autowired
    BlogTagRelationService blogTagRelationService;

    @CacheEvict(value = "Tag", allEntries = true)
    public void update(Tag tag) {
        tagMapper.updateById(tag);
    }

    @CacheEvict(value = "Tag", allEntries = true)
    public void insert(Tag tag) {
        tagMapper.insert(tag);
    }

    @Cacheable("Tag")
    public List<Tag> select() {
        return tagMapper.selectList(new QueryWrapper<>());
    }

    @Transactional
    @CacheEvict(value = "Tag", allEntries = true)
    public void deleteBatch(Integer[] tagIds) {
        List<Integer> ids = Arrays.asList(tagIds);
        // 删除对应tag
        tagMapper.deleteBatchIds(ids);
        // 删除关联数据
        blogTagRelationService.deleteBatchByTagIds(ids);

    }

    /**
     * 全量的同步标签关联状态
     *
     * @param tags 目前blog所关联的tag
     */
    @Transactional
    public void updateRelation(int blogId, Integer[] tags) {
        if (tags == null) {
            return;
        }

        List<Integer> originalTagIds = blogTagRelationService.selectTagIdsByBlogId(blogId);

        // 如果原始数据存在，但是目前不存在，则需要删除
        List<Integer> deleteList = originalTagIds.stream().filter(originalTagId -> {
            for (Integer id : tags) {
                if (id.equals(originalTagId)) {
                    return false;//如果找到了，则不返回id
                }
            }
            return true; //如果没找到，则将id添加到删除集合中
        }).collect(Collectors.toList());

        // 如果目前存在，原始数据不存在，则需要添加
        List<Integer> insertList = Arrays.stream(tags).filter(tagId -> {
            for (Integer originalTagId : originalTagIds) {
                if (originalTagId.equals(tagId)) {
                    return false;//如果找到了，则不返回id
                }
            }
            return true;//如果没找到，则将id添加到新增集合中
        }).collect(Collectors.toList());

        // 批量删除标签关系
        blogTagRelationService.deleteBatchByTagIds(deleteList);

        // 批量新增标签关系
        blogTagRelationService.insertBatch(blogId, insertList);
    }


    public String selectTagNameByBlogId(Integer blogId) {
        List<String> tagNames = tagMapper.selectTagNameByBlogId(blogId);
        StringBuilder sb = new StringBuilder();
        tagNames.forEach(tagName -> sb.append(tagName).append(","));
        return sb.toString();
    }

    public List<Tag> selectTagByBlogIds(List<Integer> blogIds) {
        if (blogIds.size() == 0) {
            return Collections.emptyList();
        }
        List<Tag> tags = tagMapper.selectTagByBlogIds(blogIds);
        return tags;
    }

//    public Page<BlogTagRelation> selectBlogIdByTagId(int tagId, int page, int size) {
//         return blogTagRelationService.selectBlogIdByTagId(tagId,page,size);
//    }

    public String selectTagNameById(int id) {
        Tag tag = tagMapper.selectOne(new QueryWrapper<Tag>().select("name").eq("id", id));
        return tag.getName();
    }
}
