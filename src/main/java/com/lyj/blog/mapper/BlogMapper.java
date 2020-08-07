package com.lyj.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyj.blog.model.Blog;
import com.lyj.blog.model.req.FilingResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlogMapper extends BaseMapper<Blog> {

    List<FilingResult> filing();

    Page<Blog> selectBlogItemsByTagId(@Param("isPrivate") boolean isPrivate, @Param("tagId") int tagId, Page<?> page);
}