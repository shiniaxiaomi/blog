package com.lyj.blog.dao;

import com.lyj.blog.model.BlogAndTag;
import com.lyj.blog.model.BlogAndTagExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlogAndTagDao {
    long countByExample(BlogAndTagExample example);

    int deleteByExample(BlogAndTagExample example);

    int insert(BlogAndTag record);

    int insertSelective(BlogAndTag record);

    List<BlogAndTag> selectByExample(BlogAndTagExample example);

    int updateByExampleSelective(@Param("record") BlogAndTag record, @Param("example") BlogAndTagExample example);

    int updateByExample(@Param("record") BlogAndTag record, @Param("example") BlogAndTagExample example);
}