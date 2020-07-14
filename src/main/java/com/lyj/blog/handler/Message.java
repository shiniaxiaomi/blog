package com.lyj.blog.handler;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Message {

    public static final boolean ERROR=false;
    public static final boolean SUCCESS=true;


    boolean code;//true表示成功，false表示失败
    Object data;//返回的数据
    String msg;//提示消息

    // 返回错误消息
    public static Message error(Object data,String msg){
        return new Message().setCode(Message.ERROR).setData(data).setMsg(msg);
    }
    public static Message error(String msg){
        return new Message().setCode(Message.ERROR).setMsg(msg);
    }

    public static Message success(Object data,String msg){
        return new Message().setCode(Message.SUCCESS).setData(data).setMsg(msg);
    }

    public static Message success(Object data){
        return new Message().setCode(Message.SUCCESS).setData(data);
    }

    public static Message success(){
        return new Message().setCode(Message.SUCCESS);
    }

    public static Message success(String msg){
        return new Message().setCode(Message.SUCCESS).setMsg(msg);
    }

}
