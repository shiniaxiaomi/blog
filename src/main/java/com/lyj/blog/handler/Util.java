package com.lyj.blog.handler;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/8/7 11:39 上午
 */
public class Util {

    public static void renderPageParam(ModelAndView mav, Page<?> pages, String url,String title){
        mav.addObject("pages",pages.getPages()==0?1:pages.getPages()); //总页数
        mav.addObject("currentPage",pages.getCurrent()); //当前页
        mav.addObject("total",pages.getTotal()); //总数
        mav.addObject("url",url);//组装url（不包括页数）
        mav.addObject("title",title);//页面标题
    }
}
