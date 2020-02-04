package com.lyj.blog.model;

import lombok.Data;

import java.io.Serializable;

/**
 * tag
 * @author 
 */
@Data
public class Tag implements Serializable {
    private Integer id;

    private String name;

    /**
     * 属于该tag的博客数量
     */
    private Integer count;

    private static final long serialVersionUID = 1L;
}