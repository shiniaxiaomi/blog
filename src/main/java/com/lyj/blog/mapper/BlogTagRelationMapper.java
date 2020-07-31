package com.lyj.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyj.blog.model.BlogTagRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlogTagRelationMapper extends BaseMapper<BlogTagRelation> {

    void insertBatch(@Param("blogId") int blogId, @Param("tagIds") List<Integer> tagIds);
}