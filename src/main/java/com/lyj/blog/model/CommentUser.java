package com.lyj.blog.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * comment_user
 *
 * @author
 */
@Accessors(chain = true)
@Data
public class CommentUser implements Serializable {
    /**
     * 评论用户id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * github用户名（也可以随便输入，也可以为空）
     */
    private String githubName;

    /**
     * 头像链接
     */
    private String avatar;

    private static final long serialVersionUID = 1L;
}