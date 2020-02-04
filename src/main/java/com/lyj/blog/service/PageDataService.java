package com.lyj.blog.service;


import com.lyj.blog.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * 该类给不同的页面组件提供对应的页面数据
 */
@Service
public class PageDataService {

    @Autowired
    TagService tagService;

    @Autowired
    UserService userService;

    @Autowired
    BlogService blogService;

    //提供公共介绍组件的数据
    public void provideIntroduceData(ModelAndView modelAndView){
        //查询所有的tag
        List<Tag> tags = tagService.getMaxCountBySize(10);
        modelAndView.addObject("tags",tags);

        //查询首页访问次数
        Integer homePageVisitCount = userService.selectAndIncrHomePageVisitCount();
        modelAndView.addObject("homePageVisitCount",homePageVisitCount);//首页访问次数

        //归档数据
        List<Map> maps = blogService.selectBlogCountByYear();
        modelAndView.addObject("blogCounts",maps);

    }



}
