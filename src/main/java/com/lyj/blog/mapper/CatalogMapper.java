package com.lyj.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyj.blog.model.Catalog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CatalogMapper extends BaseMapper<Catalog> {

    @Select("select id,pid,is_private from catalog where is_folder = true")
    List<Catalog> selectFolders();

    @Select("select id,is_folder from catalog where pid = #{id}")
    List<Catalog> selectListByPid(Integer id);
}