package com.lyj.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyj.blog.mapper.TagMapper;
import com.lyj.blog.model.BlogTagRelation;
import com.lyj.blog.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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


    public void update(Tag tag){
        tagMapper.updateById(tag);
    }

    public void insert(Tag tag) {
        tagMapper.insert(tag);
    }

    public List<Tag> select() {
        return tagMapper.selectList(new QueryWrapper<>());
    }

    @Transactional
    public void deleteBatch(Integer[] tagIds) {
        // TODO: 2020/7/31 删除每个tagId对应的tag，并且还要删除blog_tag_relation关联表中对应tag的数据
        List<Integer> ids = Arrays.asList(tagIds);
        // 删除对应tag
        tagMapper.deleteBatchIds(ids);
        // 删除关联数据
        blogTagRelationService.deleteBatchByTagIds(ids);

    }

    /**
     * 全量的同步标签关联状态
     * @param tags 目前blog所关联的tag
     */
    @Transactional
    public void updateRelation(int blogId,Integer[] tags) {

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
        blogTagRelationService.insertBatch(blogId,insertList);

    }
}
