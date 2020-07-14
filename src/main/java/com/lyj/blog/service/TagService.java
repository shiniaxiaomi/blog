package com.lyj.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyj.blog.handler.AssertUtil;
import com.lyj.blog.mapper.TagMapper;
import com.lyj.blog.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/7/13 9:03 下午
 */
@Service
public class TagService {

    @Autowired
    TagMapper tagMapper;

    @Autowired
    BlogTagRelationService blogTagRelationService;


    public List<Tag> selectList() {
        return tagMapper.selectList(new QueryWrapper<>());
    }

    public Tag create(String name) {
        //检查tagName是否重复
        int count = tagMapper.selectCount(new QueryWrapper<Tag>().eq("name", name));
        AssertUtil.isTrue(count==0,"tagName已存在");

        //新增tag
        Tag tag = new Tag().setName(name);
        int insert = tagMapper.insert(tag);
        AssertUtil.isTrue(insert==1,"tag新增失败");

        return tag;
    }

    public void update(int tagId, String name) {
        // 查询name是否存在
        Tag tag = tagMapper.selectOne(new QueryWrapper<Tag>().eq("name", name));
        AssertUtil.isTrue(tag!=null && tag.getId()!=tagId,"tagName已存在");

        tagMapper.updateById(new Tag().setId(tagId).setName(name));// 根据id修改名称
    }


    @Transactional
    public void delete(Integer tagId) {
        // 删除tag
        int i = tagMapper.deleteById(tagId);
        AssertUtil.isTrue(i==1,"标签删除失败");

        // 维护关系表
        blogTagRelationService.deleteByTagId(tagId);
    }

    public List<Tag> getDataByBlogId(Integer blogId) {
        return tagMapper.selectByBlogId(blogId);
    }

    public List<Integer> selectBlogIds(String tagName) {
        List<Integer> blogIds = tagMapper.selectBlogIds(tagName);
        return blogIds;
    }
}
