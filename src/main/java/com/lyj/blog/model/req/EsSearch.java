package com.lyj.blog.model.req;

import lombok.Data;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/8/4 1:55 下午
 */
@Data
public class EsSearch {
    String keyword;
    String tagKeyword;
    int page;

}
