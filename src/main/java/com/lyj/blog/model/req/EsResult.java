package com.lyj.blog.model.req;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/8/6 3:35 下午
 */
@Accessors(chain = true)
@Data
public class EsResult {
    float score;
    Map sourceAsMap;
}
