package com.lyj.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyj.blog.model.Blog;
import com.lyj.blog.model.req.FilingResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface BlogMapper extends BaseMapper<Blog> {

    List<FilingResult> filing();

    Page<Blog> selectBlogItemsByTagId(@Param("isPrivate") boolean isPrivate, @Param("tagId") int tagId, Page<?> page);

    Blog selectBlogByCommentId(@Param("commentId") int commentId);

    Integer selectSum();

    @Select("select is_private from blog where id = #{blogId}")
    Boolean getIsPrivateByBlogId(Integer blogId);

    @Select("select visit_count from blog where id = #{blogId}")
    Integer getVisitCountByBlogId(int blogId);

    @Select("select md from blog where id = #{blogId}")
    String selectMdByBlogId(int blogId);

    @Select("select name from blog where id = #{blogId}")
    String selectNameByBlogId(Integer blogId);

    @Update("update blog set visit_count = #{visitCount} where id = #{blogId}")
    void updateVisitCountByBlogId(Integer blogId, Integer visitCount);

    @Update("update blog set md_html = #{html} where id = #{BlogId}")
    void updateHtmlById(String html, Integer BlogId);
}