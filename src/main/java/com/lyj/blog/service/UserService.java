package com.lyj.blog.service;

import com.lyj.blog.dao.UserDao;
import com.lyj.blog.exception.MessageException;
import com.lyj.blog.model.User;
import com.lyj.blog.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    //主要被自调用
    @Autowired
    UserService userService;

    @Autowired
    RedisTemplate redisTemplate;

    //根据用户名获取用户信息
    @Cacheable(value = "user",key = "'name:'+#name",unless = "#result==null")
    public User getUserByName(String name){
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserNameEqualTo(name);

        List<User> users = userDao.selectByExample(userExample);
        //因为userName是唯一的
        if(users.size()==0){
            return null;
        }
        return users.get(0);
    }

    //查询admin用户的首页访问次数
    @Cacheable(value = "user",key = "'homePageVisitCount:admin'",unless = "#result==null")
    public Integer selectHomePageVisitCount(){
        User admin = userService.getUserByName("admin");
        if(admin==null) return 0;
        return admin.getVisitCount();
    }
    //更新admin用户的首页访问次数(redis)
    @CachePut(value = "user",key = "'homePageVisitCount:admin'",unless = "#result==null")
    public Integer updateHomePageVisitCount(Integer homePageCount){
        return homePageCount;//更新redis中的数据
    }
    //查询并自增首页的访问次数(redis)
    public Integer selectAndIncrHomePageVisitCount(){
        Integer homePageCount = userService.selectHomePageVisitCount();
        if(homePageCount==null){
            homePageCount=0;
        }
        userService.updateHomePageVisitCount(++homePageCount);
        return homePageCount;
    }
    //定时更新admin用户的首页访问次数(数据库)
    public void updateHomePageVisitCountToDataBase() throws MessageException {
        Integer homePageVisitCount = (Integer) redisTemplate.opsForValue().get("user::homePageVisitCount:admin");

        //将数据保存到数据库中
        User user = new User();
        user.setVisitCount(homePageVisitCount);

        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserNameEqualTo("admin");//更新admin用户
        int i = userDao.updateByExampleSelective(user, userExample);
        if(i==0){
            throw new MessageException("首页访问次数更新失败");
        }
    }

    //用户登入
    public User login(User user) throws MessageException {
        User userByName = userService.getUserByName(user.getUserName());
        if(userByName!=null && user.getPassword().equals(userByName.getPassword())){
            return userByName;
        }else{
            throw new MessageException("用户名或密码错误");
        }
    }
}
