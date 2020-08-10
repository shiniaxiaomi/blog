package com.lyj.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyj.blog.model.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}