package com.lyj.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/7/27 7:34 下午
 */
@Controller
@RequestMapping("admin")
public class AdminController {

    @GetMapping({"","blog"})
    public String blog(){
        return "admin/blog";
    }

    @GetMapping("tag")
    public String tag(){
        return "admin/tag";
    }


}
