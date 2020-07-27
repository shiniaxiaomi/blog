package com.lyj.blog.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * blog_folder_relation
 * @author 
 */
@Data
public class BlogFolderRelation implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 文件夹id
     */
    private Integer blogId;

    /**
     * blog所属的文件夹的id，即blog的pid
     */
    private Integer folderId;

    private static final long serialVersionUID = 1L;
}