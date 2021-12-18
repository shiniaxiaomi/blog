package com.lyj.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyj.blog.exception.MessageException;
import com.lyj.blog.mapper.CommentMapper;
import com.lyj.blog.model.Comment;
import com.lyj.blog.model.CommentUser;
import com.lyj.blog.model.req.CommentReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/8/10 1:40 下午
 */
@Slf4j
@Service
public class CommentService {

    @Value("${myConfig.host}")
    String host;

    @Autowired
    AsyncService asyncService;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    CommentUserService commentUserService;

    @CachePut(value = "CommentPage", key = "#blogId + ',' + #page")
    public Page<Comment> selectByBlogId(int blogId, int page, int size) {
        return commentMapper.selectByBlogId(new Page<>(page, size), blogId);
    }

    public Integer updateCommentUser(String userName, String email, String githubName) {
        CommentUser dbUser = null;

        if (email != null) {
            dbUser = commentUserService.selectByEmail(email);
            if (dbUser != null && userName != null && !userName.equals(dbUser.getUsername())) {
                throw new MessageException("邮箱已被占用，请输入正确的用户名");
            }
        }

        dbUser = commentUserService.selectByUserName(userName);
        if (dbUser == null) {
            // 创建用户
            CommentUser insert = new CommentUser().setUsername(userName).setEmail(email).setGithubName(githubName);
            commentUserService.insert(insert);
            return insert.getId();
        } else {
            CommentUser update = new CommentUser().setId(dbUser.getId());
            if (githubName != null && !githubName.equals(dbUser.getGithubName())) {
                update.setGithubName(githubName);
            }
            if (email != null && dbUser.getEmail() == null) {
                update.setEmail(email);
            }
            if (update.getGithubName() != null || update.getEmail() != null) {
                commentUserService.updateById(update);
            }
        }

        return dbUser.getId();
    }

    @Transactional
    @CacheEvict(value = "CommentPage", key = "#commentReq.blogId + ',' + 1") //当有评论时，去掉第一页的缓存
    public int insert(CommentReq commentReq) {
        String userName = "".equals(commentReq.getUsername()) ? null : commentReq.getUsername();
        String email = "".equals(commentReq.getEmail()) ? null : commentReq.getEmail();
        String githubName = "".equals(commentReq.getGithubUsername()) ? null : commentReq.getGithubUsername();
        Integer commentUserId = updateCommentUser(userName, email, githubName);

        // 插入评论
        Comment comment = new Comment().setBlogId(commentReq.getBlogId()).setReplyId(commentReq.getReplyId())
                .setCommentUserId(commentUserId).setContent(commentReq.getCommentContent());
        commentMapper.insert(comment);

        // 发送评论邮件通知
        asyncService.notifyEmail(commentReq.getBlogId(), comment.getContent());

        // 如果回复id不为null，并且被回复方有邮件的情况下，发送邮件通知被回复方
        if (commentReq.getReplyId() != null) {
            // 通过reply_id查询回复的用户email
            String toEmail = selectUserEmailByReplyId(commentReq.getReplyId());
            if (toEmail != null) {
                Comment comment_reply = commentMapper.selectOne(new QueryWrapper<Comment>().select("content").eq("id", commentReq.getReplyId()));
                Map<String, Object> model = new HashMap<>();
                model.put("originalComment", comment_reply.getContent());// 原始评论内容
                model.put("replyComment", commentReq.getCommentContent()); // 回复内容
                model.put("replyLink", "http://" + host + "/comment/reply/" + commentReq.getReplyId());// 回复链接
                asyncService.sendMail(toEmail, model);
            }
        }

        return comment.getId();
    }

    @Cacheable(value = "Comment", key = "#commentId")
    public Comment selectLeftJoinCommentUserById(int commentId) {
        return commentMapper.selectLeftJoinCommentUserById(commentId);
    }

    public String selectUserEmailByReplyId(int replyId) {
        Comment comment = commentMapper.selectOne(new QueryWrapper<Comment>().select("comment_user_id").eq("id", replyId));
        int userId = comment.getCommentUserId();
        return commentUserService.selectEmailById(userId);
    }

    public void incr(int commentId) {
        commentMapper.incrById(commentId);
    }

    // 反馈内容
    public void feedback(String email, String content) {
        asyncService.feedbackMail(email, content);
    }
}
