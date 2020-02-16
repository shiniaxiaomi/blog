package com.lyj.blog.service;


import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyj.blog.dao.TagDao;
import com.lyj.blog.exception.MessageException;
import com.lyj.blog.model.Blog;
import com.lyj.blog.model.Tag;
import com.lyj.blog.model.TagExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TagService {

    @Autowired
    TagDao tagDao;
    @Autowired
    BlogAndTagService blogAndTagService;

    @Autowired
    BlogService blogService;

    @Autowired
    TagService tagService;

    //==================增加,删除,保存操作==================
    //根据tagName进行缓存
    @Transactional
    @CachePut(value = "cache",key = "'tagNameToTagId:'+#tagName")
    public Integer createTag(String tagName) throws MessageException {
        Tag tag = new Tag();
        tag.setName(tagName);
        tag.setCount(0);//被创建就是因为有一个blog进行关联
        int insert = tagDao.insert(tag);
        if(insert==0){
            throw new MessageException("tag创建失败");
        }
        return tag.getId();
    }

    //保存多个tag,并返回创建的id
    public List<Integer> save(String tagNames) throws MessageException {

        if(tagNames==null){
            throw new MessageException("tagNames不能为null");
        }

        String[] names = tagNames.split(",");
        List<Integer> tagIds=new ArrayList<>();

        //判断tag是否已经存在
        Integer tagId=null;//保存tagId
        for(String tagName:names){
            //如果是空字符串则不进行操作
            if("".equals(tagName)){
                continue;
            }

            Tag tag = tagService.selectTagByTagName(tagName);
            if(tag==null){
                //数据库不存在,则创建
                tagId = tagService.createTag(tagName);
            }else{
                //缓存中已存在,则不操作
                tagId=tag.getId();
            }
            //将tagId保存到tagIds中,用于返回
            tagIds.add(tagId);
        }

        return tagIds;
    }


    //==================查询操作======================
    //先从缓存中获取,如果没有,则从数据库中获取(如果返回值为null,则不缓存)
    @Cacheable(value = "cache",key = "'tagNameToTag:'+#tagName",unless = "#result == null")
    public Tag selectTagByTagName(String tagName){
        TagExample tagExample = new TagExample();
        tagExample.createCriteria().andNameEqualTo(tagName);
        List<Tag> tags = tagDao.selectByExample(tagExample);
        if(tags.size()==0){
            return null;//如果没有,则返回null,则不进行缓存
        }else{
            return tags.get(0);//如果有,只返回第一个(因为tagName是唯一的)
        }
    }

    //根据tagName查询对应的blog,并缓存(当结果的个数为0时不进行缓存)
    @Cacheable(value = "cache",key = "'tagNameToBlogsByPage:tagName:'+#tagName+'-'+#page",unless = "#result.size()==0")
    public List<Blog> getBlogsByTagName(String tagName, Integer page) {

        Tag tag = tagService.selectTagByTagName(tagName);
        if(tag==null){
            //不存在
            return new ArrayList<>();
        }else{
            //存在
            return blogAndTagService.selectBlogsByTagId(tag.getId(),page,10);
        }
    }

    //通过tagId获取到tag
    @Cacheable(value = "cache",key = "'tagIdToTag:'+#tagId",unless = "#result==null")
    public Tag selectTagByTagId(int tagId){
        return tagDao.selectByPrimaryKey(tagId);
    }

    //获取博客数量最多的tag
    @Cacheable(value ="cache",key = "'tagSort:'+#size")
    public List<Tag> getMaxCountBySize(int size) {
        TagExample tagExample = new TagExample();

        PageHelper.startPage(0, size);
        tagExample.setOrderByClause("count desc");//根据数量进行降序排序(默认是升序)
        List<Tag> tags = tagDao.selectByExample(tagExample);
        PageInfo<Tag> pageInfo = new PageInfo<>(tags);

        return pageInfo.getList();
    }

    //查询所有的tags(当返回的结果为空数组或者为null时不缓存结果,因为可以是mybatis有缓存,需要再查询一次)
    @Cacheable(value = "cache",key = "'tipTags'",unless = "#result==null || '[]'.equals(#result)")
    public String getAllTags() {
        List<Tag> tags = tagDao.selectByExample(new TagExample());
        return buildTagNamesUtil(tags);
    }

    //构建TagNames的Json字符串
    public static String buildTagNamesUtil(List<Tag> tags){
        List tagsList=new ArrayList();
        Map<String,String> map=null;
        for(Tag tag:tags){
            map=new HashMap<>(2);
            map.put("id",String.valueOf(tag.getId()));
            map.put("name",tag.getName());
            tagsList.add(map);
        }
        return JSON.toJSONString(tagsList);//将list转换成json对象
    }


    //自增tag所对应的blog的个数
    public void incrBlogCountByTagId(Integer tagId) {
        Tag tag = tagService.selectTagByTagId(tagId);
        Tag update = new Tag();
        update.setCount(tag.getCount()==null?0:tag.getCount()+1);
        update.setId(tagId);
        tagDao.updateByPrimaryKeySelective(update);
    }

    //递减tag所对应的blog的个数
    public void decrBlogCountByTagId(Integer tagId) {
        Tag tag = tagService.selectTagByTagId(tagId);
        Tag update = new Tag();
        update.setCount(tag.getCount()-1);
        update.setId(tagId);
        tagDao.updateByPrimaryKeySelective(update);
    }

    //获取到所有的tags
    @Cacheable(value = "cache",key = "'allTags'")
    public List<Tag> selectAllTags() {

        TagExample tagExample = new TagExample();
        tagExample.setOrderByClause("count desc");

        List<Tag> tags = tagDao.selectByExample(tagExample);
        return tags;
    }

    //该操作需要定时的检查是否存在没有用的tag,定时的进行删除
    @CacheEvict(value = "cache",allEntries = true)
    public void deleteNoUseTags() throws MessageException {
        List<Tag> tags = tagService.selectAllTags();
        for(Tag tag:tags){
            //需要删除的tag
            if(tag.getCount()==null || (tag.getCount()!=null && tag.getCount()==0)){
                tagService.deleteTag(tag.getId());
            }
        }
    }

    //根据tagId删除tag.并且维护好了blogAndTag中间表的关系
    @Transactional
    public void deleteTag(Integer id) throws MessageException {
        int i = tagDao.deleteByPrimaryKey(id);
        if(i==0){
            throw new MessageException("tag删除失败");
        }

        //维护blogAndTag数据(按照tagId进行删除)
        blogAndTagService.deleteByTagId(id);
    }

}
