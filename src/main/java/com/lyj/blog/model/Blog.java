package com.lyj.blog.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Accessors(chain = true)
@Data
public class Blog {


    @TableId(type = IdType.AUTO)
    Integer id;
    String name;//博客名称
    @TableField("`desc`")
    String desc;//博客描述
    String md;//文档内容
    String mdHtml;//html文档内容

    Date createTime;
    Date updateTime;

    Integer pid;//blog的父节点的id

}
