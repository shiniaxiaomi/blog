package com.lyj.blog.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyj.blog.dao.BlogAndTagDao;
import com.lyj.blog.exception.MessageException;
import com.lyj.blog.model.Blog;
import com.lyj.blog.model.BlogAndTag;
import com.lyj.blog.model.BlogAndTagExample;
import com.lyj.blog.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class BlogAndTagService {

    @Autowired
    BlogAndTagDao blogAndTagDao;

    @Autowired
    TagService tagService;

    @Autowired
    BlogService blogService;

    @Autowired
    BlogAndTagService blogAndTagService;//需要自调用

    //===========================增加,删除,更改操作============================
    //缓存中间表
    @CachePut(value = "cache",key = "'blogAndTag:'+#blogId+'-'+#tagId")
    public void insertBlogAndTag(Integer blogId,Integer tagId) throws MessageException {
        //往数据库中插入数据
        BlogAndTag blogAndTag = new BlogAndTag();
        blogAndTag.setBlogId(blogId);
        blogAndTag.setTagId(tagId);

        int insert = blogAndTagDao.insert(blogAndTag);
        if(insert==0){
            throw new MessageException("BlogAndTag中间表数据插入失败");
        }
    }

    /**
     * 维护一个blogId和多个tagId的关系
     * 以传入的参数为标准: 以'blogId-tagId'拼接的字符串为唯一键
     *      如果传入存在,数据库不存在,则创建
     *      如果传入存在,数据库已存在,则不操作
     *      如果传入不存在,数据库不存在,则不操作
     *      如果传入不存在,数据库存在,则删除数据库数据
     * @param blogId
     * @param tagIds
     * @throws MessageException
     */
    public void save(Integer blogId, List<Integer> tagIds) throws MessageException {

        if(blogId==null){
            throw new MessageException("blogId不能为null");
        }

        //遍历tagId,不存在则创建
        for(Integer tagId:tagIds){
            BlogAndTag blogAndTag = selectBlogAndTag(blogId, tagId);
            if(blogAndTag==null){
                //不存在,则创建
                blogAndTagService.insertBlogAndTag(blogId,tagId);
                //维护tag所对应的blog的个数
                tagService.incrBlogCountByTagId(tagId);
            }else{
                //存在,则不操作
            }
        }

        //查询blogId原来对应的tag, 检查原来存在,传入不存在的情况
        List<Tag> tags = blogAndTagService.selectTagsByBlogId(blogId);
        Iterator<Tag> iterator = tags.iterator();
        while(iterator.hasNext()){
            Tag next = iterator.next();
            if(!tagIds.contains(next.getId())){
                //删除数据库中间表的数据
                blogAndTagService.deleteBlogAndTag(blogId,next.getId());
                //维护tag所对应的blog的个数
                tagService.decrBlogCountByTagId(next.getId());
            }
        }
    }

    //删除对应的中间表数据
    public void deleteBlogAndTag(Integer blogId,Integer tagId) throws MessageException {
        //往数据库中删除数据
        BlogAndTagExample blogAndTagExample = new BlogAndTagExample();
        blogAndTagExample.createCriteria().andBlogIdEqualTo(blogId).andTagIdEqualTo(tagId);

        int delete = blogAndTagDao.deleteByExample(blogAndTagExample);
        if(delete==0){
            throw new MessageException("BlogAndTag中间表数据删除失败!");
        }
    }

    //========================查询操作=========================
    //先查询缓存,在查询数据库(如果数据库返回null,则不进行缓存)
    @Cacheable(value = "cache",key = "'blogAndTag:'+#blogId+'-'+#tagId",unless = "#result == null")
    public BlogAndTag selectBlogAndTag(Integer blogId, Integer tagId){

        BlogAndTagExample blogAndTagExample = new BlogAndTagExample();
        blogAndTagExample.createCriteria().andBlogIdEqualTo(blogId).andTagIdEqualTo(tagId);

        List<BlogAndTag> blogAndTags = blogAndTagDao.selectByExample(blogAndTagExample);
        //因为blogId加tagId的组合是唯一的
        if(blogAndTags.size()==0){
            //不存在
            return null;
        }else{
            return blogAndTags.get(0);
        }
    }

    //从缓存中查询blogId对应的所有tags
    @Cacheable(value = "cache",key = "'blogIdToTags:'+#blogId")
    public List<Tag> selectTagsByBlogId(Integer blogId){
        List<BlogAndTag> blogAndTags = this.selectBlogAndTagByBlogId(blogId);
        List<Tag> tags=new ArrayList<>();
        for(BlogAndTag blogAndTag:blogAndTags){
            Tag tag = tagService.selectTagByTagId(blogAndTag.getTagId());
            tags.add(tag);
        }
        return tags;
    }

    //根据blogId查询所有的tags
    public List<BlogAndTag> selectBlogAndTagByBlogId(Integer blogId){
        BlogAndTagExample blogAndTagExample = new BlogAndTagExample();
        blogAndTagExample.createCriteria().andBlogIdEqualTo(blogId);

        List<BlogAndTag> blogAndTags = blogAndTagDao.selectByExample(blogAndTagExample);
        return blogAndTags;
    }

    //根据tagId查询所有的blogs的id
    @Cacheable(value = "cache",key = "'tagIdToBlogsId:tagId:'+#tagId")
    public List<BlogAndTag> selectBlogAndTagByTagId(Integer tagId){
        BlogAndTagExample blogAndTagExample = new BlogAndTagExample();
        blogAndTagExample.createCriteria().andTagIdEqualTo(tagId);
        List<BlogAndTag> blogAndTags = blogAndTagDao.selectByExample(blogAndTagExample);
        return blogAndTags;
    }

    @Cacheable(value = "cache",key = "'tagIdToBlogs:tagId:'+#tagId+'-'+#page+'-'+#size")
    public List<Blog> selectBlogsByTagId(Integer tagId, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<BlogAndTag> blogAndTags = blogAndTagService.selectBlogAndTagByTagId(tagId);
        PageInfo<BlogAndTag> pageInfo = new PageInfo<>(blogAndTags);

        List<Blog> blogs=new ArrayList<>();
        List<BlogAndTag> list = pageInfo.getList();
        for(BlogAndTag blogAndTag:list){
            //根据blogId获取blog
            Blog blog = blogService.selectBlogById(blogAndTag.getBlogId());
            blogs.add(blog);
            //设置blog对应的访问次数
            Integer visitCount = blogService.selectVisitCount(blog.getId());
            blog.setHot(visitCount);
        }

        return blogs;
    }


    //根据blogId删除对应的数据
    public void deleteByBlogId(int blogId) {
        BlogAndTagExample blogAndTagExample = new BlogAndTagExample();
        blogAndTagExample.createCriteria().andBlogIdEqualTo(blogId);

        blogAndTagDao.deleteByExample(blogAndTagExample);

    }

    //根据tagId删除对应的数据
    public void deleteByTagId(Integer id) {
        BlogAndTagExample blogAndTagExample = new BlogAndTagExample();
        blogAndTagExample.createCriteria().andTagIdEqualTo(id);
        int i = blogAndTagDao.deleteByExample(blogAndTagExample);
    }
}
