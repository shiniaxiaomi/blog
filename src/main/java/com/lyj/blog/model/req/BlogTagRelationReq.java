package com.lyj.blog.model.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/7/14 10:10 下午
 */
@Data
public class BlogTagRelationReq {

    Integer blogId; //可以为空

    @NotNull
    String tagName;

    Integer tagId; //可以为空，因为标签可能为被创建

    @NotNull
    Boolean isNew;

}
