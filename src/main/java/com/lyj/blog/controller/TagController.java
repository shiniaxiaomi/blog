package com.lyj.blog.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyj.blog.handler.GlobalException;
import com.lyj.blog.mapper.BlogTagRelationMapper;
import com.lyj.blog.mapper.TagMapper;
import com.lyj.blog.model.BlogTagRelation;
import com.lyj.blog.model.Tag;
import com.lyj.blog.handler.Message;
import com.lyj.blog.model.req.BlogTagRelationReq;
import com.lyj.blog.service.BlogTagRelationService;
import com.lyj.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("tag")
public class TagController {

    @Autowired
    TagService tagService;

    @Autowired
    BlogTagRelationService blogTagRelationService;

    //添加标签和blog的关系
    @PostMapping("addRelation")
    public Message addRelation(@Validated BlogTagRelationReq blogTagRelationReq){
        // 如果需要新增，则添加tag
        if(blogTagRelationReq.getIsNew()){
            Tag tag=tagService.create(blogTagRelationReq.getTagName());
            if(blogTagRelationReq.getTagId()==null) {
                blogTagRelationReq.setTagId(tag.getId());
            }
        }

        // 添加blog和tag的关系
        blogTagRelationService.addRelation(blogTagRelationReq.getBlogId(),blogTagRelationReq.getTagId());

        return Message.success("标签关系添加成功");
    }

    //删除标签和blog的关系
    @PostMapping("deleteRelation")
    public Message deleteRelation(@RequestParam Integer blogId,@RequestParam Integer tagId){

        // 删除blog和tag的关系
        blogTagRelationService.deleteRelation(blogId,tagId);

        return Message.success("标签关系删除成功");
    }


    //获取所有tag
    @GetMapping("getData")
    public String getData(){
        List<Tag> tags = tagService.selectList();
        return JSON.toJSONString(tags);
    }

    //获取所有tag
    @GetMapping("getDataByBlogId")
    public String getDataByBlogId(@NotNull Integer blogId){
        List<Tag> tags = tagService.getDataByBlogId(blogId);
        return JSON.toJSONString(tags);
    }

    @PostMapping("create")
    public Message create(@NotNull String name){
        tagService.create(name);
        return Message.success("标签创建成功");
    }

    /**
     * 更新tag名称，先查询数据库是否已经存在该名称
     * @param tagId 要修改的tag对象的id
     * @param name 要更改的名称
     * @return
     */
    @PostMapping("update")
    public Message update(@NotNull Integer tagId,@NotNull String name){
        tagService.update(tagId,name);
        return Message.success("标签更新成功");
    }

    /**
     * 查询对应的tag下是否有blog
     * @param tagId
     * @return
     */
    @GetMapping("isHasBlog")
    public Message isHasBlog(@NotNull Integer tagId){
        // 查询该tag下是否有blog
        boolean hasBlog = blogTagRelationService.isHasBlog(tagId);

        if(hasBlog){
            return Message.success(true,"标签下还有博客");//还有blog
        }else{
            return Message.success(false);//没有blog
        }
    }

    @Transactional
    @PostMapping("delete")
    public Message delete(@NotNull Integer tagId){
        tagService.delete(tagId);
        return Message.success("标签删除失败");
    }


}
