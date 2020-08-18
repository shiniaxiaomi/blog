package com.lyj.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/8/16 3:46 下午
 */
@Controller
public class ViewController {

    @GetMapping("word")
    public String word(){
        return "word/index";
    }

    @GetMapping("tool/regular")
    public String regular(){
        return "tool/regular";
    }

    @GetMapping("index/toc")
    public String toc(){
        return "nav/toc";
    }

    @GetMapping("index/tag")
    public String tag(){
        return "nav/tag";
    }

    @GetMapping("index/feedback")
    public String feedback(){
        return "nav/feedback";
    }


}
