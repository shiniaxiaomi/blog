package com.lyj.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/8/3 1:10 下午
 */
@Controller
@RequestMapping("tool")
public class ToolController {

    @GetMapping("regular")
    public String regular(){
        return "tool/regular";
    }

    @ResponseBody
    @PostMapping("regular")
    public String regularTest(String content,String regular){
        Pattern pattern = Pattern.compile(regular);
        // 现在创建 matcher 对象
        Matcher matcher = pattern.matcher(content);
        // 打印
        StringBuilder sb = new StringBuilder();
        while(matcher.find()) {
            sb.append(matcher.group());
            sb.append("\n-------------\n");
        }
        return sb.toString();
    }
}
