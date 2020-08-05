package com.lyj.blog.parser.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/8/3 5:54 下午
 */
@Accessors(chain = true)
@Data
public class ESHeading {

    String headingId;
    int blogId;
    String blogName;
    String headingName;
    String tagName;
    String content;

}
