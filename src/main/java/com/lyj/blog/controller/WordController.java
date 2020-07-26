package com.lyj.blog.controller;

import com.lyj.blog.model.Word;
import com.lyj.blog.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

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

    @GetMapping
    public String index(){
        return "word";
    }

    @ResponseBody
    @PostMapping("/analyze")
    public Integer analyze(String name,String content){
        Integer id = wordService.analyze(name, content);
        return id; //返回id
    }

    @GetMapping("/detail/{id}")
    public ModelAndView detail(@PathVariable int id){
        ModelAndView mav=new ModelAndView("word_detail");
        mav.addObject("id",id);
        return mav; //返回单词详情页
    }

    @ResponseBody
    @GetMapping("/content/{id}")
    public Word content(@PathVariable int id){
        return wordService.content(id);
    }

    @ResponseBody
    @GetMapping("/list")
    public List<Word> list(){
        return wordService.list(); //返回id和名称
    }

    @ResponseBody
    @PostMapping("/save")
    public String save(int id,Integer[] index){
        wordService.save(id,index);
        return "保存成功";
    }


}
