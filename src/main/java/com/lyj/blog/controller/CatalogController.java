package com.lyj.blog.controller;

import com.lyj.blog.config.Message;
import com.lyj.blog.model.Catalog;
import com.lyj.blog.service.BlogService;
import com.lyj.blog.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/7/30 11:56 上午
 */
@Controller
@RequestMapping("catalog")
public class CatalogController {

    @Autowired
    CatalogService catalogService;

    // 插入文件夹或者文件
    @ResponseBody
    @PostMapping("insert")
    public Message insert(Catalog catalog){
        catalogService.insert(catalog);
        return Message.success("添加成功",catalog);
    }

    // 删除文件夹或者文件
    @ResponseBody
    @PostMapping("delete")
    public Message delete(Catalog catalog){
        catalogService.delete(catalog);
        return Message.success("删除成功");
    }

    // 删除文件夹或者文件
    @ResponseBody
    @PostMapping("updateName")
    public Message updateName(Catalog catalog){
        catalogService.updateName(catalog);
        return Message.success("更新成功");
    }

    // 移动文件夹或者文件
    @ResponseBody
    @PostMapping("move")
    public Message move(Catalog catalog){
        catalogService.updatePid(catalog);
        return Message.success("移动成功");
    }


    @ResponseBody
    @GetMapping
    public Message selectCatalog(){
        List<Catalog> list= catalogService.selectCatalog();
        return Message.success(null,list);
    }

}
