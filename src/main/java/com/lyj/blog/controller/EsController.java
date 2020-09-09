package com.lyj.blog.controller;

import com.lyj.blog.interceptor.NeedLogin;
import com.lyj.blog.model.req.Message;
import com.lyj.blog.service.EsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/8/17 4:23 下午
 */
@Controller
@RequestMapping("es")
public class EsController {

    @Autowired
    EsService esService;

    @NeedLogin
    @GetMapping
    public String es(){
        return "admin/es";
    }

    @NeedLogin
    @ResponseBody
    @PostMapping("search")
    public Message search(String json,String method) {
        // get请求
        if(method!=null && method.equals("get")){
            return esService.searchDataByGet(json);
        }
        return esService.searchDataByPost(json);
    }

    @NeedLogin
    @ResponseBody
    @PostMapping("update")
    public Message update(String json) {
        return esService.updateData(json);
    }

    //删除数据
    @NeedLogin
    @ResponseBody
    @PostMapping("delete")
    public Message delete(String json) {
        return esService.deleteData(json);
    }
}
