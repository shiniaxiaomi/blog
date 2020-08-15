package com.lyj.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyj.blog.model.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    Page<Comment> selectByBlogId(Page<Object> page, @Param("blogId") int blogId);

    Comment selectLeftJoinCommentUserById(@Param("commentId") int commentId);

    void incrById(@Param("commentId") int commentId);
}