package com.lyj.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyj.blog.model.File;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FileMapper extends BaseMapper<File> {

    Page<File> selectPageByBlogId(@Param("blogId") Integer blogId, Page<?> page);
}