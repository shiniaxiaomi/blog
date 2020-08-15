package com.lyj.blog.exception;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/8/12 11:47 上午
 */
public class MessageException extends RuntimeException {
    public MessageException(String message) {
        super(message);
    }
}
