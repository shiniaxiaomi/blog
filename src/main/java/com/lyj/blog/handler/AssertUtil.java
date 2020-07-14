package com.lyj.blog.handler;

/**
 * @author Yingjie.Lu
 * @description 断言类
 * @date 2020/7/13 8:49 下午
 */
public class AssertUtil {

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new GlobalException(message);
        }
    }
}
