package com.lyj.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyj.blog.handler.AssertUtil;
import com.lyj.blog.mapper.BlogTagRelationMapper;
import com.lyj.blog.model.Blog;
import com.lyj.blog.model.BlogTagRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/7/13 9:04 下午
 */
@Service
public class BlogTagRelationService {

    @Autowired
    BlogTagRelationMapper blogTagRelationMapper;

    //查询tagId下的是否还有blog
    public boolean isHasBlog(int tagId) {
        Integer count = blogTagRelationMapper.selectCount(
                new QueryWrapper<BlogTagRelation>().eq("tag_id", tagId));
        return count>0;
    }

    //删除tagId对应的关联表中的数据
    public void deleteByTagId(Integer tagId) {
        int i = blogTagRelationMapper.delete(new QueryWrapper<BlogTagRelation>()
                .eq("tag_id", tagId));
        AssertUtil.isTrue(i==1,"标签关系维护失败");
    }

    public void addRelation(Integer blogId, Integer tagId) {
        BlogTagRelation blogTagRelation = new BlogTagRelation().setBlogId(blogId).setTagId(tagId);
        int insert = blogTagRelationMapper.insert(blogTagRelation);
        AssertUtil.isTrue(insert==1,"标签关系添加失败");
    }

    public void deleteRelation(Integer blogId, Integer tagId) {
        int delete = blogTagRelationMapper.delete(new QueryWrapper<BlogTagRelation>()
                .eq("blog_id", blogId).eq("tag_id", tagId));
        AssertUtil.isTrue(delete==1,"标签关系删除失败");
    }
}
