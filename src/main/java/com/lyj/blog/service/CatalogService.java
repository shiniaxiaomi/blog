package com.lyj.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lyj.blog.exception.MessageException;
import com.lyj.blog.handler.Util;
import com.lyj.blog.mapper.CatalogMapper;
import com.lyj.blog.model.Blog;
import com.lyj.blog.model.Catalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/7/30 1:17 下午
 */
@Service
public class CatalogService {

    @Autowired
    CatalogMapper catalogMapper;

    @Autowired
    BlogService blogService;


    @Transactional
    @CacheEvict(value = "Catalog",allEntries=true)
    public int insert(Catalog catalog){
        // 如果不是文件夹，则先添加blog，再添加目录item
        if(!catalog.getIsFolder()){
            Blog blog = new Blog().setName(catalog.getName());
            blogService.insert(blog);
            catalog.setBlogId(blog.getId()); //将blog的id回写
        }
        catalogMapper.insert(catalog);
        return catalog.getId();// 返回目录id
    }

    // 根据是否登入查询对应的数据
    @Cacheable(value = "Catalog")
    public List<Catalog> selectCatalog(Boolean isPrivate) {
        QueryWrapper<Catalog> queryWrapper = new QueryWrapper<Catalog>().orderByDesc("is_folder");
        if(isPrivate!=null){
            queryWrapper.eq("is_private",isPrivate);
        }
        return catalogMapper.selectList(queryWrapper);
    }

    @Transactional
    @CacheEvict(value = "Catalog",allEntries=true)
    public void delete(Catalog catalog) {
        // 如果是文件，则删除文件
        if(!catalog.getIsFolder()){
            blogService.delete(catalog.getBlogId());
        }

        // 删除目录item
        catalogMapper.deleteById(catalog.getId());
    }

    @Transactional
    @CacheEvict(value = "Catalog",allEntries=true)
    public void updateName(Catalog catalog) {
        // 如果是文件，更改blogName
        if(!catalog.getIsFolder()){
            blogService.updateName(catalog.getBlogId(),catalog.getName());
        }

        // 更新目录itemName
        Catalog update = new Catalog().setId(catalog.getId()).setName(catalog.getName());
        catalogMapper.updateById(update);
    }

    @CacheEvict(value = "Catalog",allEntries=true)
    public void updatePid(Catalog catalog) {
        // 判断校验pid是否为文件夹
        Catalog pidCatalog = catalogMapper.selectOne(new QueryWrapper<Catalog>().select("is_folder").eq("id", catalog.getPid()));
        if(pidCatalog==null){
            throw new MessageException("没有对应的文件夹");
        }else{
            if(!pidCatalog.getIsFolder()){
                throw new MessageException("移动失败，请选择对应的文件夹");
            }
        }
        catalogMapper.updateById(catalog);
    }

    public void updateIsPrivateByBlogId(Blog blog) {
        catalogMapper.update(new Catalog().setIsPrivate(blog.getIsPrivate()),
                new QueryWrapper<Catalog>().eq("blog_id",blog.getId()));
    }

    public boolean selectIsPrivateById(int id) {
        Catalog catalog = catalogMapper.selectOne(new QueryWrapper<Catalog>().select("is_private").eq("id", id));
        if(catalog!=null){
            return catalog.getIsPrivate();
        }
        return false;
    }

    @CacheEvict(value = "Catalog",allEntries=true)
    public void updateIsPrivateById(int id, boolean isPrivate) {
        catalogMapper.updateById(new Catalog().setId(id).setIsPrivate(isPrivate));
    }
}
