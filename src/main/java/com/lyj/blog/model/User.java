package com.lyj.blog.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class User {
    @TableId(type = IdType.AUTO)
    Integer id;
    String userName;
    String password;
    Integer visitCount;
    String tree;
}
