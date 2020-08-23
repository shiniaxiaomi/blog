package com.lyj.blog.controller;

import com.lyj.blog.handler.Util;
import com.lyj.blog.interceptor.NeedLogin;
import com.lyj.blog.model.req.Message;
import com.lyj.blog.model.Catalog;
import com.lyj.blog.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
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

    @Autowired
    HttpSession session;

    // 插入文件夹或者文件
    @NeedLogin
    @ResponseBody
    @PostMapping("insert")
    public Message insert(Catalog catalog){
        catalogService.insert(catalog);
        return Message.success("添加成功",catalog);
    }

    // 删除文件夹或者文件
    @NeedLogin
    @ResponseBody
    @PostMapping("delete")
    public Message delete(Catalog catalog){
        catalogService.delete(catalog);
        return Message.success("删除成功");
    }

    // 更新文件夹或者文件
    @NeedLogin
    @ResponseBody
    @PostMapping("updateName")
    public Message updateName(Catalog catalog){
        catalogService.updateName(catalog);
        return Message.success("更新成功");
    }

    // 移动文件夹或者文件
    @NeedLogin
    @ResponseBody
    @PostMapping("move")
    public Message move(Catalog catalog){
        catalogService.updatePid(catalog);
        return Message.success("移动成功");
    }


    @ResponseBody
    @GetMapping
    public Message selectCatalog(){
        Boolean isPrivate = Util.getIsPrivate(session);
        List<Catalog> list= catalogService.selectCatalog(isPrivate);
        if(list.size()==0){
            // 如果没有目录，则创建默认目录
            int catalogPid = catalogService.insert(new Catalog().setName("/").setIsFolder(true));
            catalogService.insert(new Catalog().setName("_待整理").setIsFolder(true).setPid(catalogPid));
            list= catalogService.selectCatalog(isPrivate);
        }
        return Message.success(null,list);
    }

    // 查询文件夹的共享状态
    @ResponseBody
    @GetMapping("status/folder/{id}")
    public Message selectFolderStatus(@PathVariable("id") int id){
        boolean isPrivate = catalogService.selectIsPrivateById(id);
        return Message.success(null,isPrivate);
    }

    @ResponseBody
    @PostMapping("status/folder")
    public Message updateFolderStatus(int id,boolean isPrivate){
        catalogService.updateIsPrivateById(id,isPrivate);
        return Message.success(null);
    }


}
