package bak.model;

import lombok.Data;

import java.io.Serializable;

/**
 * blogandtag
 * @author 
 */
@Data
public class BlogAndTag implements Serializable {
    /**
     * 博客Id
     */
    private Integer blogId;

    /**
     * tagId
     */
    private Integer tagId;

    private static final long serialVersionUID = 1L;
}