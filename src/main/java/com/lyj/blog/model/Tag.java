package com.lyj.blog.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * tag
 *
 * @author
 */
@Data
public class Tag implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 标签名称
     */
    private String name;

    /**
     * 标签下的blog数量
     */
    private Integer count;

    @TableField(exist = false)
    private Integer blogId;

    private static final long serialVersionUID = 1L;
}