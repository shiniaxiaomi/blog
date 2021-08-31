package com.lyj.blog.interceptor;

import java.lang.annotation.*;

/**
 * @author Yingjie.Lu
 * @description 标记注解, 标记该注解的方法需要登入才能访问
 * @date 2020/8/14 2:42 下午
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface NeedLogin {
}
