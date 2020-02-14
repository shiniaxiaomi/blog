package com.lyj.blog.model;

import lombok.Data;

@Data
public class Message {

    int code;//状态码
    String msg;//消息
    Object data;//数据

    public Message(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    //单纯传入消息字符串
    public static Message success(String msg){
        return new Message(200, msg,null);
    }

    public static Message error(String msg){
        return new Message(400, msg,null);
    }


    //传入消息和数据对象
    public static Message success(String msg,Object data){
        return new Message(200, msg,data);
    }

    public static Message error(String msg,Object data){
        return new Message(400, msg,data);
    }

}

