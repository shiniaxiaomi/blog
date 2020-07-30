package com.lyj.blog.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * catalog
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

    private static final long serialVersionUID = 1L;
}