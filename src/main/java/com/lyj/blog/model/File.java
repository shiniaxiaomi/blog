package com.lyj.blog.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * file
 * @author 
 */
@Accessors(chain = true)
@Data
public class File implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件类型
     */
    private Integer type;

    /**
     * 被博客的引用数（缓存）
     */
    private Integer count;

    private static final long serialVersionUID = 1L;
}