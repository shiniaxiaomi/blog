package com.lyj.blog.model;

import java.io.Serializable;
import lombok.Data;

/**
 * tag
 * @author 
 */
@Data
public class Tag implements Serializable {
    private Integer id;

    /**
     * 标签名称
     */
    private String name;

    /**
     * 标签下的blog数量
     */
    private Integer count;

    private static final long serialVersionUID = 1L;
}