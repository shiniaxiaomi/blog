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

    // 保存目录
    public void updateCatalog(int id,String tree){
        User user = new User().setId(id).setTree(tree);
        userMapper.updateById(user);
    }

    // 查询目录
    public String selectCatalog(int id){
        User user = userMapper.selectById(id);
        String tree = user.getTree();
        if(tree!=null && !"".equals(tree)){
            return tree;
        }else{
            // 如果为空的话，则返回默认根节点
            return "[{\"id\":0,\"pId\":null,\"name\":\"/\",\"isFolder\":true,\"icon\":\"/ztree/img/folder.png\"}]";
        }
    }

}
