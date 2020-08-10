package com.lyj.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyj.blog.mapper.UserMapper;
import com.lyj.blog.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/7/29 12:23 下午
 */
@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public int selectVisitCount() {
        User user = userMapper.selectOne(new QueryWrapper<User>().select("visit_count").eq("id", 1));
        return user.getVisitCount();
    }
}
