package com.lyj.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/7/24 9:25 下午
 */
@Controller
@RequestMapping
public class IndexController {

    @RequestMapping("/")
    public String index(){
        return "index";
    }


    @RequestMapping("/test")
    public String test(){
        return "admin/test";
    }





}
