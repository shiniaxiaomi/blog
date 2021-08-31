package com.lyj.blog.controller;

import com.lyj.blog.interceptor.NeedLogin;
import com.lyj.blog.model.req.Message;
import com.lyj.blog.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/8/7 10:32 下午
 */
@Controller
@RequestMapping("file")
public class FileController {

    @Autowired
    FileService fileService;

    // 删除blog和文件的关联关系，并将file被引用的总数-1，但不删除真实文件
    @NeedLogin
    @ResponseBody
    @PostMapping("deleteRelationByFileName")
    public Message deleteRelationByFileNameAndBlogId(@RequestParam("name") String name, @RequestParam("blogId") int blogId) {
        fileService.deleteRelation(name, blogId);
        return Message.success("删除成功");
    }

    // 删除blog和文件的关联关系，并将file被引用的总数-1，但不删除真实文件
    @NeedLogin
    @ResponseBody
    @PostMapping("deleteRelationByFileId")
    public Message deleteRelationByFileIdAndBlogId(@RequestParam("fileId") int fileId, @RequestParam("blogId") int blogId) {
        fileService.deleteRelation(fileId, blogId);
        return Message.success("删除成功");
    }

    // 删除真实文件，并删除blog和文件的关联关系，并将file表中的记录删除
    @NeedLogin
    @ResponseBody
    @PostMapping("delete")
    public Message deleteFile(@RequestParam("id") int id) {
        fileService.delete(id);
        return Message.success("删除成功");
    }

}
