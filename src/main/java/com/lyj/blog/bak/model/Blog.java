package com.lyj.blog.bak.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * blog
 * @author 
 */
@Data
public class Blog implements Serializable {
    private Integer id;

    /**
     * 博客名称
     */
    private String name;

    /**
     * 博客的描述
     */
    private String desc;

    /**
     * 博客描述的html
     */
    private String descHtml;

    /**
     * 笔记源码
     */
    private String md;

    /**
     * 博客内容的html
     */
    private String mdHtml;

    /**
     * toc目录html
     */
    private String tocHtml;

    private Date createTime;

    private Date updateTime;

    /**
     * 观看人数
     */
    private Integer hot;

    /**
     * 缓存一个blog对应的所有tag名称
     */
    private String tagNames;

    private static final long serialVersionUID = 1L;

    //=========改动==============
    //博客所属的标签
    private List<Tag> tags;
}