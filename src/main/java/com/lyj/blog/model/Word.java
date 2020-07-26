package com.lyj.blog.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * word
 * @author 
 */
@Accessors(chain = true)
@Data
public class Word implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 分词后的单词数组（包含了单词，翻译和出现次数）
     */
    private String context;

    /**
     * 已经背过的index
     */
    @TableField("`index`")
    private String index;

    private static final long serialVersionUID = 1L;
}