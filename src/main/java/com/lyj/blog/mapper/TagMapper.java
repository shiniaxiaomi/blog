package com.lyj.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyj.blog.model.Tag;
import com.lyj.blog.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TagMapper extends BaseMapper<Tag> {
    List<Tag> selectByBlogId(@Param("blogId") Integer blogId);

    List<Integer> selectBlogIds(@Param("tagName") String tagName);
}
