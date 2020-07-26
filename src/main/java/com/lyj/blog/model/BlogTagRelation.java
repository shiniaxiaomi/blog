package com.lyj.blog.model;

import java.io.Serializable;
import lombok.Data;

/**
 * blog_tag_relation
 * @author 
 */
@Data
public class BlogTagRelation implements Serializable {
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