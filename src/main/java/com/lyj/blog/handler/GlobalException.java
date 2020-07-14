package com.lyj.blog.handler;

/**
 * @author Yingjie.Lu
 * @description 全局异常
 * @date 2020/7/13 8:37 下午
 */
public class GlobalException extends RuntimeException {

    public GlobalException(String message) {
        super(message);
    }
}
