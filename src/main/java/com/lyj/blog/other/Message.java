package com.lyj.blog.other;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Message {

    int code;//0表示成功，1表示失败
    Object data;

}
