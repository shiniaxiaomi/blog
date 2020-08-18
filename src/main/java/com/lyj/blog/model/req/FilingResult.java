package com.lyj.blog.model.req;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/8/6 5:31 下午
 */
@Data
public class FilingResult implements Serializable {
    String year;
    int count;
}
