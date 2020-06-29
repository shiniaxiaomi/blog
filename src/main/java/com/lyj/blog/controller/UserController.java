package com.lyj.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyj.blog.mapper.BlogMapper;
import com.lyj.blog.mapper.UserMapper;
import com.lyj.blog.model.Blog;
import com.lyj.blog.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {


    @Autowired
    UserMapper userMapper;

    @Autowired
    BlogMapper blogMapper;

    @RequestMapping("getData")
    public List<Blog> getTreeData(){
        return blogMapper.selectList(new QueryWrapper<Blog>().select("id", "name", "pid"));
        //查询id为1的user
//        User user = userMapper.selectById(1);
//        return user.getTree();
//        return "[{\n" +
//                "        \"value\": \"Magazines\",\n" +
//                "        \"id\": \"Magazines\",\n" +
//                "        \"open\": true,\n" +
//                "        \"items\": [\n" +
//                "            {\n" +
//                "                \"value\": \"Sport\",\n" +
//                "                \"id\": \"Sport\"\n" +
//                "            }\n" +
//                "        ]\n" +
//                "    }]";
    }


//    public void test(){
//        List<Blog> blogs = ;
//        //构建json树
//
//
//    }
}
