package com.lyj.blog.model;

import lombok.Data;

@Data
public class Message {

    int code;//状态码
    Object data;//数据

    public Message(int code, Object data) {
        this.code = code;
        this.data = data;
    }

    public static Message success(Object data){
        return new Message(200, data);
    }

    public static Message error(Object data){
        return new Message(400,data);
    }
}
