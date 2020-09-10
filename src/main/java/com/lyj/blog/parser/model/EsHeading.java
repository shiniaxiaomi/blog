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
public class EsHeading {

    String headingId;
    int blogId;
    String blogName;
    String headingName;
    String tagName;
    String content;
    boolean isPrivate;// 是否私有

}
