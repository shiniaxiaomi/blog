package com.lyj.blog.bak.util;

import java.util.Date;

/**
 * 主要用作做参数的校验
 */
public class MyUtil {

    //校验传入的page参数,如果不符合规则,则返回1
    public static Integer pageCheck(String page){
        //校验page参数
        Integer intPage=null;
        //如果为空,则跳转到第一页
        if(page==null || "".equals(page)){
            intPage=1;
        }else{
            //如果转换数字异常
            try {
                intPage = Integer.parseInt(page);
            }catch (Exception e){
                intPage=1;
            }
        }

        if(intPage<1) intPage=1;//最小值校验

        return intPage;
    }

    //校验传入的year参数,如果不符合规则,则返回当前的年份
    public static Integer yearCheck(String year){
        //校验page参数
        Integer intYear=null;
        //如果为空,则跳转到第一页
        if(year==null || "".equals(year)){
            intYear=new Date().getYear();
        }else{
            //如果转换数字异常
            try {
                intYear = Integer.parseInt(year);
            }catch (Exception e){
                intYear=new Date().getYear();
            }
        }

        return intYear;
    }

}
