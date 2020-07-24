package com.lyj.blog.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyj.blog.handler.AssertUtil;
import com.lyj.blog.mapper.BlogMapper;
import com.lyj.blog.model.Blog;
import com.lyj.blog.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/7/11 11:36 上午
 */
@Service
public class BlogService {

    @Autowired
    BlogMapper blogMapper;

    @Autowired
    TagService tagService;

    // 分页查询，每次只查询3个（用于首页的滚动加载）
    public Page<Blog> selectByPage(int page,int size){
        return blogMapper.selectPage(new Page<>(page, size),
                new QueryWrapper<Blog>().select("id","name","`desc`","create_time","update_time")
                        .orderByDesc("update_time","create_time"));
    }


    public Blog selectById(int id) {
        return blogMapper.selectById(id);
    }

    public Blog insert(String name, Integer pid) {
        Date date = new Date();
        Blog blog = new Blog().setName(name).setCreateTime(date).setUpdateTime(date).setPid(pid);
        int insert = blogMapper.insert(blog);
        AssertUtil.isTrue(insert==1,"blog新增失败");
        return blog;
    }

    public List<Blog> selectList(Integer pid) {
        List<Blog> list = blogMapper.selectList(new QueryWrapper<Blog>().select("id","pid"));
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(pid);//添加上本身节点
        getIds(list,ids,pid);

        List<Blog> blogs = blogMapper.selectList(new QueryWrapper<Blog>()
                .select("id", "name", "`desc`", "create_time", "update_time").in("id", ids)
                .orderByDesc("update_time","create_time"));//直接指定所有的id

        return blogs;
    }

    // 工具方法
    public void getIds(List<Blog> list,List<Integer> ids,Integer pid){
        for (Blog blog : list) {
            if(blog.getPid().equals(pid)){
                ids.add(blog.getId());
                getIds(list,ids,blog.getId());
            }
        }
    }

    public void updateById(Blog blog) {
        blog.setUpdateTime(new Date());
        int update = blogMapper.updateById(blog);
        AssertUtil.isTrue(update==1,"blog更新失败");
    }

    public void delete(int id) {
        //删除blog时确认一下下面是否还有blog（不然目录树那边会显示不全）
        int count = blogMapper.selectCount(new QueryWrapper<Blog>().eq("pid", id));
        AssertUtil.isTrue(count==0,"blog下还存在blog，不允许删除该blog");

        //如果blog下不存在blog，则删除对应的blog
        blogMapper.deleteById(id);
    }

    //获取blog的树状数据
    public String getTreeData() {
        List<Blog> blogs = blogMapper.selectList(new QueryWrapper<Blog>().select("id", "name", "pid"));
        List<HashMap<String, Object>> list = buildTree(blogs);
        return JSON.toJSONString(list);
    }

    //构建树的工具方法
    private List<HashMap<String, Object>> buildTree(List<Blog> blogs){
        int[] size={0};
        return _buildTree(blogs, null, size);
    }

    //构建一个以id为父节点的数组
    private List<HashMap<String, Object>> _buildTree(List<Blog> blogs, Integer pid, int[] size) {
        ArrayList<HashMap<String, Object>> list=new ArrayList<>();
        for (Blog blog : blogs) {
            if (blog.getPid() == pid) {
                size[0]++;
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", blog.getId());
                map.put("value", blog.getName());
                List<HashMap<String, Object>> arr = _buildTree(blogs, blog.getId(), size);
                map.put("items", arr);
                if(arr.size()==0){
                    map.put("isLeaf",true);
                }
                list.add(map);
            }
            if(size[0]==blogs.size()){
                return list;
            }
        }
        return list;
    }

    // 根据标签名称查询所有的blog
    public Page<Blog> selectBlogByTagName(String tagName) {
        List<Integer> blogIds = tagService.selectBlogIds(tagName);
        if(blogIds.size()==0){
            return new Page<>();
        }
        List<Blog> blogs = blogMapper.selectBatchIds(blogIds);
        return new Page<Blog>().setRecords(blogs);
    }
}
