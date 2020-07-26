package com.lyj.blog.model;

import java.io.Serializable;
import lombok.Data;

/**
 * user
 * @author 
 */
@Data
public class User implements Serializable {
    private Integer id;

    private String userName;

    private String password;

    /**
     * 博客的总访问次数
     */
    private Integer visitCount;

    private static final long serialVersionUID = 1L;
}