package com.lyj.blog.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * config
 * @author 
 */
@Data
public class Config implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 腾讯翻译secretId
     */
    private String secretId;

    /**
     * 腾讯翻译secretKey
     */
    private String secretKey;

    private static final long serialVersionUID = 1L;
}