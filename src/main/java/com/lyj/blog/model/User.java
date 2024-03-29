package com.lyj.blog.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * user
 *
 * @author
 */
@Accessors(chain = true)
@Data
public class User implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String userName;

    private String password;

    /**
     * 文件目录信息
     */
    private String tree;

    private static final long serialVersionUID = 1L;
}