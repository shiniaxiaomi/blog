package com.lyj.blog.handler;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/8/7 11:39 上午
 */
public class Util {

    public static void renderPageParam(ModelAndView mav, Page<?> pages, String url, String title) {
        mav.addObject("pages", pages.getPages() == 0 ? 1 : pages.getPages()); //总页数
        mav.addObject("currentPage", pages.getCurrent()); //当前页
        mav.addObject("total", pages.getTotal()); //总数
        mav.addObject("url", url);//组装url（不包括页数）
        mav.addObject("title", title);//页面标题
    }

    // 根据登入状态获取是否为私有的状态
    public static Boolean getIsPrivate(HttpSession session) {
        // 如果登入，则查询所有数据，所以isPrivate=null
        if (session.getAttribute("isLogin") != null) {
            return true;
        } else {
            // 如果未登入，则只查询非私有的数据，所以isPrivate=false
            return false;
        }
    }

    // 返回是否登入的判断
    public static boolean isLogin(HttpSession session) {
        return session.getAttribute("isLogin") != null;
    }
}
