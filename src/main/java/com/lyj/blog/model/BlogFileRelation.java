package com.lyj.blog.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * blog_file_relation
 *
 * @author
 */
@Accessors(chain = true)
@Data
public class BlogFileRelation implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 博客id
     */
    private Integer blogId;

    /**
     * 文件id
     */
    private Integer fileId;

    private static final long serialVersionUID = 1L;
}