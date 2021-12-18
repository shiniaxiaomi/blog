package com.lyj.blog.model.req;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/8/12 11:15 上午
 */
@Accessors(chain = true)
@Data
public class CommentReq {
    Integer blogId;
    Integer replyId;
    @NotNull(message = "用户名必填")
    @NotBlank
    String username;
    String email;
    String githubUsername;
    String commentContent;
}
