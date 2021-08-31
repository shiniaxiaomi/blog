package com.lyj.blog.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Yingjie.Lu
 * @description 自定义错误页面
 * @date 2020/8/16 4:04 下午
 */
@Controller
public class ErrorPageController implements ErrorController {

    @Override
    public String getErrorPath() {
        return null;
    }

    @RequestMapping("error")
    public String error() {
        return "common/error";
    }
}
