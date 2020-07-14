package com.lyj.blog.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;


@Accessors(chain = true)
@Data
public class Tag {


    @TableId(type = IdType.AUTO)
    Integer id;
    @JSONField(name="value")
    String name;//tag名称

}
