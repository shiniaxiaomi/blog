package com.lyj.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lyj.blog.exception.MessageException;
import com.lyj.blog.mapper.BlogMapper;
import com.lyj.blog.mapper.CatalogMapper;
import com.lyj.blog.model.Blog;
import com.lyj.blog.model.Catalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayDeque;
import java.util.ArrayList;
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
    BlogMapper blogMapper;

    @Autowired
    BlogService blogService;

    @Autowired
    EsService esService;

    /**
     * 前端传入的值：name,pid,isFolder,icon
     */
    @Transactional
    @CacheEvict(value = "Catalog", allEntries = true)
    public int insert(Catalog catalog) {
        // 如果是创建文件夹
        if (catalog.getIsFolder()) {
            catalogMapper.insert(catalog);
            return catalog.getId();// 返回目录id
        }

        // 如果创建的是blog，先创建blog，再在目录中创建blog记录
        // 创建blog
        Blog blog = new Blog().setName(catalog.getName()).setIsPrivate(catalog.getIsPrivate());
        blogService.insert(blog);

        // 创建目录
        catalog.setBlogId(blog.getId()).setIsPrivate(catalog.getIsPrivate()); //将blog的id回写
        catalogMapper.insert(catalog);

        return catalog.getId();// 返回目录id
    }

    // 根据是否登入查询对应的数据
    @Cacheable(value = "Catalog", key = "#isPrivate")
    public List<Catalog> selectCatalog(Boolean isPrivate) {
        QueryWrapper<Catalog> queryWrapper = new QueryWrapper<Catalog>().orderByDesc("is_folder").orderByAsc("name");
        // 如果传入的是false，则用户未登入，查询公有内容（如果为true，则查询所有内容）
        if (!isPrivate) {
            queryWrapper.eq("is_private", false);
        }
        return catalogMapper.selectList(queryWrapper);
    }

    @Transactional
    @CacheEvict(value = "Catalog", allEntries = true)
    public void delete(Catalog catalog) {
        // 如果是文件，则删除文件
        if (!catalog.getIsFolder()) {
            blogService.delete(catalog.getBlogId());
        }

        // 删除目录item
        catalogMapper.deleteById(catalog.getId());
    }

    @Transactional
    @CacheEvict(value = "Catalog", allEntries = true)
    public void updateName(Catalog catalog) {
        // 如果是文件，更改blogName
        if (!catalog.getIsFolder()) {
            blogService.updateName(catalog.getBlogId(), catalog.getName());
        }

        // 更新目录itemName
        Catalog update = new Catalog().setId(catalog.getId()).setName(catalog.getName());
        catalogMapper.updateById(update);
    }

    @Transactional
    @CacheEvict(value = "Catalog", allEntries = true)
    public void updatePid(Catalog catalog) {
        // 判断校验pid是否为文件夹
        Catalog pidCatalog = catalogMapper.selectOne(new QueryWrapper<Catalog>().select("is_folder").eq("id", catalog.getPid()));
        if (pidCatalog == null) {
            throw new MessageException("没有对应的文件夹");
        }
        if (!pidCatalog.getIsFolder()) {
            throw new MessageException("移动失败，请选择对应的文件夹");
        }

        // 查询原始的catalog
        Catalog catalogDB = catalogMapper.selectById(catalog.getId());
        // 移动,并更新私有状态
        catalogMapper.updateById(catalog);
        // 如果原始的私有状态与修改的私有状态相等，则只修改pid即可，无需修改私有状态
        if (catalogDB.getIsPrivate() != catalog.getIsPrivate()) {
            List<Integer> fileIds = new ArrayList<>();// 保存文件ids
            List<Integer> folderIds = new ArrayList<>();// 保存文件夹ids
            getFileAndFolderByCatalog(catalog, fileIds, folderIds);
            // 批量更新文件的私有状态（包括了blog中缓存的私有状态）
            if (fileIds.size() != 0) {
                catalogMapper.update(new Catalog().setIsPrivate(catalog.getIsPrivate()),
                        new UpdateWrapper<Catalog>().in("id", fileIds.toArray()));
                blogMapper.update(new Blog().setIsPrivate(catalog.getIsPrivate()),
                        new UpdateWrapper<Blog>().in("id", fileIds.toArray()));
            }
            // 批量更新文件夹的私有状态
            if (folderIds.size() != 0) {
                catalogMapper.update(new Catalog().setIsPrivate(catalog.getIsPrivate()),
                        new UpdateWrapper<Catalog>().in("id", folderIds.toArray()));
            }
            // 批量更新es中文件的私有状态
            esService.updateIsPrivateByBlogIds(fileIds, catalog.getIsPrivate());
        }
    }

    // 根据catalog查询该节点下的所有文件和文件夹
    private void getFileAndFolderByCatalog(Catalog catalog, List<Integer> fileIds, List<Integer> folderIds) {
        // 如果当前移动节点是文件夹，则将其下面的所有节点都修改私有状态
        if (catalog.getIsFolder()) {
            List<Catalog> catalogs = catalogMapper.selectListByPid(catalog.getId());
            ArrayDeque<Integer> folderQueue = new ArrayDeque<>();
            for (Catalog item : catalogs) {
                if (!item.getIsFolder()) {
                    fileIds.add(item.getId());
                } else {
                    folderIds.add(item.getId());
                    folderQueue.push(item.getId());
                }
            }
            while (folderQueue.size() != 0) {
                Integer folderId = folderQueue.pop();
                List<Catalog> items = catalogMapper.selectListByPid(folderId);
                for (Catalog item : items) {
                    if (!item.getIsFolder()) {
                        fileIds.add(item.getId());
                    } else {
                        folderIds.add(item.getId());
                        folderQueue.push(item.getId());
                    }
                }
            }
        }
    }

//    @Transactional
//    @CacheEvict(value = "Catalog",allEntries=true)
//    public void updateIsPrivateByBlogId(Blog blog) {
//        catalogMapper.updateIsPrivateByBlogId(blog.getIsPrivate(),blog.getId());
//        // 更新es中的私有状态
//    }

    // 根据目录id查询数据库中的公开状态
//    public Boolean selectIsPrivateByIdInDB(int id){
//        Boolean isPrivate = catalogMapper.selectIsPrivateByIdInDB(id);
//        return isPrivate != null && isPrivate;
//    }

//    @CacheEvict(value = "Catalog",allEntries=true)
//    public void updateIsPrivateById(int id, boolean isPrivate) {
//        catalogMapper.updateById(new Catalog().setId(id).setIsPrivate(isPrivate));
//    }

    // 根据blogId查询是否是私有的
    @Cacheable(value = "Catalog", key = "#blogId")
    public Catalog selectIsPrivateByBlogId(int blogId) {
        return catalogMapper.selectOne(new QueryWrapper<Catalog>()
                .select("id", "pid", "is_private").eq("blog_id", blogId));
    }

    @Cacheable(value = "Catalog", key = "'folderList'")
    public List<Catalog> selectFolder() {
        return catalogMapper.selectFolders();
    }

}
