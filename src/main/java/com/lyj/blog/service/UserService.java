package com.lyj.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyj.blog.exception.MessageException;
import com.lyj.blog.mapper.UserMapper;
import com.lyj.blog.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/7/29 12:23 下午
 */
@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    BlogService blogService;

    @Autowired
    HttpSession session;

    public int selectVisitCount() {
        return blogService.selectVisitCount();
    }

    public boolean login(User user) {
        User dbUser = userMapper.selectOne(new QueryWrapper<User>().select("password").eq("user_name", user.getUserName()));
        if(dbUser==null){
            throw new MessageException("用户名不存在");
        }
        if(!dbUser.getPassword().equals(user.getPassword())){
            throw new MessageException("密码错误");
        }
        session.setAttribute("isLogin",true);
        return true;
    }
}
