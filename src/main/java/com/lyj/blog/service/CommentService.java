package com.lyj.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyj.blog.mapper.CommentMapper;
import com.lyj.blog.model.Comment;
import com.lyj.blog.model.CommentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/8/10 1:40 下午
 */
@Service
public class CommentService {

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    CommentUserService commentUserService;

    public Page<Comment> selectByBlogId(int blogId, int page, int size){
        return commentMapper.selectPage(new Page<>(page, size),
                new QueryWrapper<Comment>().select("id", "html").eq("blog_id",blogId)
                        .orderByDesc("create_time"));
    }

    public String selectUserNameByEmail(String email){
        CommentUser commentUser = commentUserService.selectByEmail(email);
        return commentUser.getGithubName();
    }


    @Transactional
    public void insert(int blogId,int replyId,String email, String github_username,String html) {
        // 1.判断用户是否已经存在
        CommentUser dbUser = commentUserService.selectByEmail(email);
        if(dbUser!=null){
            //如果数据库存在用户，且传入的github_username不为空，则更新对应的用户名
            if(github_username!=null && !"".equals(github_username)){
                //更新用户名
                CommentUser update = new CommentUser().setId(dbUser.getId()).setGithubName(github_username);
                commentUserService.updateUserNameById(update);
            }
        }else{ // 2.如果不存在，则创建user
            dbUser = new CommentUser().setEmail(email).setGithubName(github_username);
            commentUserService.insert(dbUser);
        }

        // 插入评论
        Comment comment = new Comment().setBlogId(blogId).setReplyId(replyId)
                .setCommentUserId(dbUser.getId()).setHtml(html);
        commentMapper.insert(comment);
    }
}
