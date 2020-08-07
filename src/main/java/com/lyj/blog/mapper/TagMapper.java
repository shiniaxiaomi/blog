package com.lyj.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyj.blog.model.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    List<String> selectTagNameByBlogId(@Param("blogId") Integer blogId);

    List<Tag> selectTagByBlogIds(@Param("blogIds") List<Integer> blogIds);
}