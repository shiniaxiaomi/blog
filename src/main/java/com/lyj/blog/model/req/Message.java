package com.lyj.blog.model.req;

import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/7/29 5:15 下午
 */
@Data
public class Message {
    boolean code;// true为成功，false为失败
    Object data;// 数据对象
    String msg;

    public Message() {
    }

    public Message(boolean code, String msg, Object data) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public static Message success(String msg, Object data) {
        return new Message(true, msg, data);
    }

    public static Message success(String msg) {
        return new Message(true, msg, null);
    }

    public static Message error(String msg, Object data) {
        return new Message(false, msg, data);
    }

    public static Message error(String msg) {
        return new Message(false, msg, null);
    }

    //直接转化为json
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
