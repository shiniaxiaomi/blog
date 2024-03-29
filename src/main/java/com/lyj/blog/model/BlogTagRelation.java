package com.lyj.blog.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * blog_tag_relation
 *
 * @author
 */
@Data
public class BlogTagRelation implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 博客id
     */
    private Integer blogId;

    /**
     * 标签id
     */
    private Integer tagId;

    private static final long serialVersionUID = 1L;
}