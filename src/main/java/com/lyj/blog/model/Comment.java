package com.lyj.blog.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * comment
 * @author 
 */
@Accessors(chain = true)
@Data
public class Comment implements Serializable {
    /**
     * 评论id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 博客id
     */
    private Integer blogId;

    /**
     * 评论用户id
     */
    private Integer commentUserId;

    /**
     * 回复评论的id
     */
    private Integer replyId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 渲染后的html
     */
    private String html;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}