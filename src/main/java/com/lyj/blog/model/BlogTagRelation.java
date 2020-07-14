package com.lyj.blog.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/7/9 10:51 下午
 */
@Accessors(chain = true)
@Data
public class BlogTagRelation {
    @TableId(type = IdType.AUTO)
    int id;

    int blogId;

    int tagId;

}
