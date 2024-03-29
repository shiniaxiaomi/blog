package com.lyj.blog.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * catalog
 *
 * @author
 */
@Accessors(chain = true)
@Data
public class Catalog implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 名称（如果是blog，需要同步去blog表更新名称）
     */
    private String name;

    /**
     * 父级的id
     */
    private Integer pid;

    /**
     * 是否为文件夹
     */
    private Boolean isFolder;

    /**
     * 记录blog的id，如果是文件夹，则为空（这个要带给前端，点击的时候可以打开对应的blog）
     */
    private Integer blogId;

    /**
     * 记录是否为私有
     */
    private Boolean isPrivate;

    private static final long serialVersionUID = 1L;
}