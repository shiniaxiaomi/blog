package com.lyj.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyj.blog.mapper.CommentUserMapper;
import com.lyj.blog.model.CommentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/8/10 4:53 下午
 */
@Service
public class CommentUserService {

    @Autowired
    CommentUserMapper commentUserMapper;


    public void insert(CommentUser commentUser) {
        commentUserMapper.insert(commentUser);
    }

    public CommentUser selectByEmail(String email) {
        return commentUserMapper.selectOne(new QueryWrapper<CommentUser>().eq("email", email));
    }

    public void updateById(CommentUser update){
        commentUserMapper.updateById(update);
    }

    public CommentUser selectByUserName(String username) {
        return commentUserMapper.selectOne(new QueryWrapper<CommentUser>().eq("username", username));
    }

    public String selectEmailById(int userId) {
        CommentUser commentUser = commentUserMapper.selectOne(new QueryWrapper<CommentUser>().select("email").eq("id", userId));
        if(commentUser==null){
            return null;
        }
        return commentUser.getEmail();
    }

    public CommentUser selectByUserNameAndEmail(String username, String email) {
        return commentUserMapper.selectOne(new QueryWrapper<CommentUser>().eq("username", username).eq("email", email));
    }
}
