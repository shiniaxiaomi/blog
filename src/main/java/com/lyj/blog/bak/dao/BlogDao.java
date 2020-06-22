package com.lyj.blog.bak.dao;

import com.lyj.blog.bak.model.Blog;
import com.lyj.blog.bak.model.BlogExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface BlogDao {
    long countByExample(BlogExample example);

    int deleteByExample(BlogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Blog record);

    int insertSelective(Blog record);

    List<Blog> selectByExample(BlogExample example);

    Blog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Blog record, @Param("example") BlogExample example);

    int updateByExample(@Param("record") Blog record, @Param("example") BlogExample example);

    int updateByPrimaryKeySelective(Blog record);

    int updateByPrimaryKey(Blog record);

    //按照年份查询博客的总数(并按照年份的降序排列)
    @Select("select count(*) as blogCount,DATE_FORMAT(createTime,'%Y') as year from blog GROUP BY DATE_FORMAT(createTime,'%Y') order by year desc")
    List<Map> selectBlogCountByYear();

    //查询blog的总数
    @Select("select count(*) as blogCount from blog")
    Integer selectBlogCount();

    //查询所有blogNames(并且通过更新时间进行降序排列)
    @Select("select id,name from blog order by updateTime desc")
    List<Map> selectAllBlogNames();
}