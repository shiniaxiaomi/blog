package com.lyj.blog.controller;

import org.springframework.stereotype.Controller;
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

    @ResponseBody
    @PostMapping("regular")
    public String regularTest(String content, String regular) {
        Pattern pattern = null;
        try {
            pattern = Pattern.compile(regular);
        } catch (Exception e) {
            e.printStackTrace();
            return "正则表达式不正确：" + e.getMessage();
        }
        // 现在创建 matcher 对象
        Matcher matcher = pattern.matcher(content);
        // 打印
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            sb.append(matcher.group());
            sb.append("\n-------------\n");
        }

        return "".equals(sb.toString()) ? "没有匹配到结果" : sb.toString();
    }
}
