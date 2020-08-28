package com.lyj.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyj.blog.mapper.FileMapper;
import com.lyj.blog.model.BlogFileRelation;
import com.lyj.blog.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/8/7 10:34 下午
 */
@Service
public class FileService {

    @Value("${myConfig.file.location}/file/")
    String filePath;

    @Autowired
    FileMapper fileMapper;

    @Autowired
    BlogFileRelationService blogFileRelationService;


    public Page<File> selectPageByBlogId(Integer blogId, int page, int size) {
        return fileMapper.selectPageByBlogId(blogId, new Page<>(page, size));
    }

    @Transactional
    public void insertFile(File file,int blogId) {
        fileMapper.insert(file);
        //维护blog_file关系表
        BlogFileRelation blogFileRelation = new BlogFileRelation().setBlogId(blogId).setFileId(file.getId());
        blogFileRelationService.insert(blogFileRelation);
    }


    @Transactional
    public void deleteRelation(String name, int blogId) {
        File file = fileMapper.selectOne(new QueryWrapper<File>().eq("name", name));

        // 删除博客和文件的关联关系
        blogFileRelationService.deleteRelationByBlogIdAndFileId(blogId,file.getId());

        // 更新引用的数量(原始引用数量-1)
        File updateCount = new File().setId(file.getId()).setCount(file.getCount() - 1);
        fileMapper.updateById(updateCount);
    }

    @Transactional
    public void deleteRelation(int fileId, int blogId) {

        File file = fileMapper.selectById(fileId);

        // 删除博客和文件的关联关系
        blogFileRelationService.deleteRelationByBlogIdAndFileId(blogId,file.getId());

        // 更新引用的数量(原始引用数量-1)
        File updateCount = new File().setId(file.getId()).setCount(file.getCount() - 1);
        fileMapper.updateById(updateCount);
    }

    @Transactional
    public void delete(int id) {
        File file = fileMapper.selectById(id);

        //删除数据库文件记录
        fileMapper.deleteById(id);
        // 删除文件的引用关系
        blogFileRelationService.deleteRelationByFileId(id);

        // 删除真实文件
        java.io.File ioFile = new java.io.File(filePath + file.getName());
        if(ioFile.exists() && ioFile.delete()){
            System.out.println("文件删除成功");
        }else{
            System.out.println("文件不存在或文件删除失败");
        }
    }
}
