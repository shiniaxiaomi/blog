package com.lyj.blog.controller;

import com.lyj.blog.interceptor.NeedLogin;
import com.lyj.blog.model.req.Message;
import org.springframework.beans.factory.annotation.Autowired;
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
    RestTemplate restTemplate;

    final String esHost="http://localhost:9200";

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
            try {
                String data = restTemplate.getForObject(esHost+json, String.class);
                return Message.success(null,data);
            }catch (Exception e){
                return Message.error("请确认是否是请求的方法不对:"+e.getMessage());
            }
        }

        // post请求
        HttpHeaders headers = new HttpHeaders();// 添加请求头
        headers.add("Content-Type","application/json");
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        try {
            String data = restTemplate.postForObject(esHost+"/_search?format=JSON&pretty", entity, String.class);
            return Message.success(null,data);
        }catch (Exception e){
            return Message.error(e.getMessage());
        }
    }

    @NeedLogin
    @ResponseBody
    @PostMapping("update")
    public Message update(String json) {
        // post请求
        HttpHeaders headers = new HttpHeaders();// 添加请求头
        headers.add("Content-Type","application/json");
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        try {
            String data = restTemplate.postForObject(esHost+"/blog/_update_by_query?format=JSON&pretty", entity, String.class);
            return Message.success("更新成功",data);
        }catch (Exception e){
            return Message.error(e.getMessage());
        }
    }

    //删除数据
    @NeedLogin
    @ResponseBody
    @PostMapping("delete")
    public Message delete(String json) {
        // post请求
        HttpHeaders headers = new HttpHeaders();// 添加请求头
        headers.add("Content-Type","application/json");
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        try {
            String data = restTemplate.postForObject(esHost+"/blog/_delete_by_query?format=JSON&pretty", entity, String.class);
            return Message.success("删除成功",data);
        }catch (Exception e){
            return Message.error(e.getMessage());
        }
    }
}
