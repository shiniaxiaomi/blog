package com.lyj.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyj.blog.model.Catalog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CatalogMapper extends BaseMapper<Catalog> {

    @Select("select blog_id from catalog where id = #{id} ")
    Integer selectBlogIdById(Integer id);

    @Update("update catalog set is_private = #{isPrivate} where blog_id = #{blogId}")
    void updateIsPrivateByBlogId(Boolean isPrivate, Integer blogId);

    @Select("select is_private from catalog where id = #{id}")
    Boolean selectIsPrivateByIdInDB(int id);

    @Select("select id,pid,is_private from catalog where is_folder = true")
    List<Catalog> selectFolders();

    @Select("select id,is_folder from catalog where pid = #{id}")
    List<Catalog> selectListByPid(Integer id);
}