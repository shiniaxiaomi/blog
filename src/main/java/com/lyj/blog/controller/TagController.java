package com.lyj.blog.controller;

import com.lyj.blog.config.Message;
import com.lyj.blog.model.Tag;
import com.lyj.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/7/27 11:22 下午
 */
@Controller
@RequestMapping("tag")
public class TagController {

    @Autowired
    TagService tagService;

    @ResponseBody
    @PostMapping("update")
    public Message update(Tag tag){
        tagService.update(tag);
        return Message.success("更新成功");
    }

    @ResponseBody
    @PostMapping("insert")
    public Message insert(Tag tag){
        tagService.insert(tag);
        return Message.success("添加成功");
    }

    @ResponseBody
    @GetMapping
    public Message select(){
        List<Tag> tags=tagService.select();
        return Message.success(null,tags);
    }

    // 支持批量删除
    @ResponseBody
    @PostMapping("delete")
    public Message deleteBatch(@RequestParam("tags") Integer[] tagIds){
        tagService.deleteBatch(tagIds);
        return Message.success("删除成功");
    }

}
