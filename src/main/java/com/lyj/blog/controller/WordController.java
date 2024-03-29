package com.lyj.blog.controller;

import com.lyj.blog.interceptor.NeedLogin;
import com.lyj.blog.model.Word;
import com.lyj.blog.model.req.Message;
import com.lyj.blog.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/7/26 11:40 下午
 */
@Controller
@RequestMapping("word")
public class WordController {

    @Autowired
    WordService wordService;

    @NeedLogin
    @ResponseBody
    @PostMapping("/analyze")
    public Message analyze(String name, String content) {
        Integer id = wordService.analyze(name, content);
        return Message.success(null, id);//返回id
    }

    @GetMapping("/detail/{id}")
    public ModelAndView detail(@PathVariable int id) {
        ModelAndView mav = new ModelAndView("word/detail");
        mav.addObject("id", id);
        return mav; //返回单词详情页
    }

    @ResponseBody
    @GetMapping("/content/{id}")
    public Message content(@PathVariable int id) {
        Word word = wordService.content(id);
        return Message.success(null, word);
    }

    @ResponseBody
    @GetMapping("/list")
    public Message list() {
        List<Word> list = wordService.list();//返回id和名称
        return Message.success(null, list);
    }

    @NeedLogin
    @ResponseBody
    @PostMapping("/saveIndex")
    public Message saveIndex(int id, Integer[] index) {
        wordService.save(id, index);
        return Message.success("保存成功");
    }

    @NeedLogin
    @ResponseBody
    @PostMapping("/delete")
    public Message delete(int id) {
        wordService.delete(id);
        return Message.success("删除成功");
    }


}
