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

    @NeedLogin
    @ResponseBody
    @PostMapping("deleteRelation")
    public Message deleteRelation(@RequestParam("name") String name,@RequestParam("blogId") int blogId){
        fileService.deleteRelation(name,blogId);
        return Message.success("删除成功");
    }

    @NeedLogin
    @ResponseBody
    @PostMapping("delete")
    public Message delete(@RequestParam("id") int id){
        fileService.delete(id);
        return Message.success("删除成功");
    }


}
