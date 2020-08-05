package com.lyj.blog.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * blog
 * @author 
 */
@Accessors(chain = true)
@Data
public class Blog implements Serializable  {
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 博客名称
     */
    private String name;

    /**
     * 博客描述
     */
    @TableField("`desc`")
    private String desc;

    /**
     * 博客md原文
     */
    private String md;

    /**
     * 博客html
     */
    private String mdHtml;

    /**
     * 创建时间
     */
    private Date createTime;

    private Date updateTime;

    /**
     * 是否置顶，0（false不置顶），1（true置顶），默认为不置顶
     */
    private Boolean isStick;

    /**
     * 是否私有，0（false公有），1（true私有），默认为公有
     */
    private Boolean isPrivate;

    private static final long serialVersionUID = 1L;
}