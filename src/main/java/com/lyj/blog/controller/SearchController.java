package com.lyj.blog.controller;


import com.lyj.blog.model.Blog;
import com.lyj.blog.service.BlogService;
import com.lyj.blog.service.PageDataService;
import com.lyj.blog.util.MyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class SearchController {


    @Autowired
    BlogService blogService;

    @Autowired
    PageDataService pageDataService;


    //通过blogName查找对应的blog
    @RequestMapping("searchMore")
    public ModelAndView searchMore(String blogName, String page){

        ModelAndView modelAndView = new ModelAndView("searchMore");

        Integer intPage = MyUtil.pageCheck(page);//获取当前搜索的页数

        List<Integer> ids=new ArrayList<>();//符合条件的blogId

        blogName=blogName.toLowerCase();//转小写
        List<Map> blogNames = blogService.selectAllBlogNames();
        for(Map map:blogNames){
            String name = (String) map.get("name");
            //(转小写后)进行关键词匹配
            if(name.toLowerCase().contains(blogName)){
                ids.add((Integer) map.get("id"));
            }
        }

        //例如:总数为23个,分别查询第1页,第2页,第3页
        //下面进行手动的分页(第1页就是0-9,第2页就是10-19,第3页是20-29,第4页30-39)
        int pageSize=10;
        List<Blog> blogs=new ArrayList<>();
        int startIndex=(intPage-1)*pageSize;
        int endIndex=(intPage-1)*pageSize+pageSize-1;

        //中间页
        if(ids.size()>endIndex){
            for(int i=startIndex;i<=endIndex;i++){
                blogs.add(blogService.selectBlogById(ids.get(i)));
            }
        }
        //最后一页
        else if(ids.size()>=startIndex && ids.size()<=endIndex){
            for(int i=startIndex;i<ids.size();i++){
                blogs.add(blogService.selectBlogById(ids.get(i)));
            }
        }
        //空白页
        else if(ids.size()<=startIndex){
            //不操作,返回空数组
        }

        //搜索结果
        modelAndView.addObject("blogs",blogs);
        //搜索关键字
        modelAndView.addObject("keyword",blogName);
        //nowPage,nowSize
        modelAndView.addObject("nowPage",intPage);
        modelAndView.addObject("nowSize",blogs.size());

        pageDataService.provideIntroduceData(modelAndView,false);

        return modelAndView;
    }

}
