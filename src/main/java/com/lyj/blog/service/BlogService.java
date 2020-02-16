package com.lyj.blog.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyj.blog.dao.BlogDao;
import com.lyj.blog.exception.MessageException;
import com.lyj.blog.model.Blog;
import com.lyj.blog.model.BlogAndTag;
import com.lyj.blog.model.BlogExample;
import com.lyj.blog.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BlogService {

    @Autowired
    BlogDao blogDao;
    @Autowired
    TagService tagService;
    @Autowired
    BlogAndTagService blogAndTagService;
    //需要自调用代理后的方法
    @Autowired
    BlogService blogService;

    @Autowired
    RedisTemplate redisTemplate;


    //=========================新增,更改操作=======================
    /**
     * blog相关的注入口,需要在全部操作完成后,清空缓存
     * 创建blog(完成blog数据插入,tag生成,blogAndTag中间表的关系维护)
     *
     * 在传入的blog中是没有blog.id的,但是在方法执行完成之后,会将blog.id设置到blog对象中,
     * 又因为缓存实在方法之后执行的,所以把key设置成blog.id是没有问题的
     * @param blog
     * @return
     * @throws MessageException
     */
    @Transactional
    @CacheEvict(value = "cache",allEntries = true)
    public void create(Blog blog) throws MessageException {
        //插入数据
        this.insertBlog(blog);

        //生成并保存tag
        List<Integer> tagIds = tagService.save(blog.getTagNames());



        //维护中间表blog和tag之间的关系
        blogAndTagService.save(blog.getId(),tagIds);
    }

    /**
     * blog相关的注入口,需要在全部操作完成后,清空缓存
     * 有选择性的进行更新
     * @param blog
     * @throws MessageException
     */
    @Transactional
    @CacheEvict(value = "cache",allEntries = true)
    public void save(Blog blog) throws MessageException {
        blog.setUpdateTime(new Date());// 更新修改日期

        //如果tagName为null,则说明是更新描述
        if(blog.getTagNames()==null){
            //保存描述
            this.updateBlog(blog);
        }else{
            //保存blog内容
            this.updateBlog(blog);
            //生成并保存tag
            List<Integer> tagIds = tagService.save(blog.getTagNames());

            //维护中间表blog和tag之间的关系
            blogAndTagService.save(blog.getId(),tagIds);
        }
    }

    //创建blog并缓存
    @CachePut(value = "cache",key = "'blogIdToBlog:'+#blog.id")
    public Blog insertBlog(Blog blog) throws MessageException {
        //设置blog一些初始值
        Date date = new Date();
        blog.setCreateTime(date);
        blog.setUpdateTime(date);
        blog.setHot(0);

        //插入blog数据
        int insert = blogDao.insert(blog);
        if(insert==0){
            throw new MessageException("添加失败");
        }

        return blog;
    }

    //更新blog
    public void updateBlog(Blog blog) throws MessageException {
        int i = blogDao.updateByPrimaryKeySelective(blog);
        if(i==0){
            throw new MessageException("更新失败");
        }
    }

    //=======================查询操作============================
    //根据blogId查询blog
    @Cacheable(value = "cache",key = "'blogIdToBlog:'+#blogId",unless = "#result==null")
    public Blog selectBlogById(Integer blogId){
        Blog blog = blogDao.selectByPrimaryKey(blogId);
        if(blog==null) return null;

        List<Tag> tags = blogAndTagService.selectTagsByBlogId(blogId);
        blog.setTags(tags);
        //创建所拥有的tagNames的json字符串
        String tagNames = TagService.buildTagNamesUtil(tags);
        blog.setTagNames(tagNames);
        return blog;
    }

    /**
     * 分页查询blog(去除掉置顶的blog),缓存分页的内容
     * @param page
     * @param size
     * @return
     */
    @Cacheable(value = "cache",key = "'blogSort:'+#page+'-'+#size")
    public List<Blog> getBlogs(int page, int size) {
        PageHelper.startPage(page, size);

        //排除置顶blog
        BlogExample blogExample = new BlogExample();
        BlogExample.Criteria criteria = blogExample.createCriteria();
        List<Integer> excludeBlogIds = this.excludeBlogByTagNames(Arrays.asList("置顶", "草稿","介绍"));
        for(Integer id:excludeBlogIds){
            criteria.andIdNotEqualTo(id);
        }

        //查询所有不是置顶的blog和不是草稿的blog
        blogExample.setOrderByClause("updateTime desc");//按照更新时间降序排列
        List<Blog> blogs = blogDao.selectByExample(blogExample);
        PageInfo<Blog> pageInfo = new PageInfo<>(blogs);

        List<Blog> list = pageInfo.getList();
        for(Blog blog:list){
            //设置blog对应的tags
            List<Tag> tags = blogAndTagService.selectTagsByBlogId(blog.getId());
            blog.setTags(tags);
            //设置blog对应的访问次数
            Integer visitCount = this.selectVisitCount(blog.getId());
            blog.setHot(visitCount);
        }

        return list;
    }

    //根据tagName分页的查询blog
    @Cacheable(value = "cache",key = "'blogSortByTagName:tagName:'+#tagName+':'+#page+'-'+#size")
    public List<Blog> getBlogsByTagName(String tagName, int page, int size) {
        Tag tag = tagService.selectTagByTagName(tagName);
        if(tag ==null) return new ArrayList<>();

        List<Blog> blogs = blogAndTagService.selectBlogsByTagId(tag.getId(), page, size);
        return blogs;
    }


    //根据tagNames排除查询出的blog
    public List<Integer> excludeBlogByTagNames(List<String> tagNames){
        List<Integer> excludeBlogIds=new ArrayList<>();
        for(String tagName:tagNames){
            Tag tag = tagService.selectTagByTagName(tagName);
            if(tag==null) continue;
            List<BlogAndTag> blogAndTags = blogAndTagService.selectBlogAndTagByTagId(tag.getId());
            for(BlogAndTag blogAndTag:blogAndTags){
                if(blogAndTag==null) continue;
                excludeBlogIds.add(blogAndTag.getBlogId());
            }
        }
        return excludeBlogIds;
    }

    //清除cache缓存的空方法
    @CacheEvict(value = "cache",allEntries = true)
    public void cleanBlogCache(){}



    //=============用于查询和更新blog的访问次数=====================
    //查询对应blog的访问次数,如果缓存有,则从缓存中获取,如果没有,则查询数据库
    @Cacheable(value = "visitCount",key = "'blogId:'+#id",unless = "#result==null")
    public Integer selectVisitCount(Integer id) {
        Blog blog = blogDao.selectByPrimaryKey(id);
        return blog.getHot();
    }
    @CachePut(value = "visitCount",key = "'blogId:'+#id")
    public Integer updateVisitCount(Integer id, Integer visitCount){
        return visitCount;
    }
    //用于查询redis中对应blog的访问次数,并递增(函数并不访问数据库,数据库信息会每天定时的进行更新)
    public Integer selectAndIncrVisitCount(Integer id){
        Integer visitCount = blogService.selectVisitCount(id);
        blogService.updateVisitCount(id, ++visitCount);//自增,并返回给redis更新
        return visitCount;
    }

    //真正的将blog的访问次数更新到数据库中(每天凌晨4点更新)
    public void updateVisitCountToDataBase() throws MessageException {

        Set keys = redisTemplate.keys("visitCount*");

        ValueOperations valueOperations = redisTemplate.opsForValue();
        Integer length="visitCount::blogId:".length();

        Iterator iterator = keys.iterator();
        while(iterator.hasNext()){
            String next = (String) iterator.next();
            Integer visitCount = (Integer) valueOperations.get(next);
            Integer id = Integer.valueOf(next.substring(length));
            boolean isNeedClean = this.updateVisitCountDao(id, visitCount);//更新数据库
            if(isNeedClean){
                //清除key对应的缓存
                redisTemplate.delete(next);
            }
        }
    }

    //更新blog的访问次数的数据操作
    public boolean updateVisitCountDao(Integer id,Integer visitCount) throws MessageException {
        Blog blog = new Blog();
        blog.setId(id);
        blog.setHot(visitCount);
        int i = blogDao.updateByPrimaryKeySelective(blog);

        //如果更新数为0,则表明该blog已经被删除,那么,我们就要清空缓存中对应的key
        if(i==0){
            return true;//表示需要清除缓存中对应的key
        }else{
            return false;//表示成功,不需要清除缓存
        }
    }

    //==================草稿相关===================
    //获取所有的线上草稿
    @Cacheable(value = "cache",key = "'draftByPage:'+#page")
    public List<Blog> selectDraft(Integer page){
        Tag tag = tagService.selectTagByTagName("草稿");
        if(tag ==null) return new ArrayList<>();
        List<Blog> draftBlogs = blogAndTagService.selectBlogsByTagId(tag.getId(),page,10);
        return draftBlogs;
    }


    //根据年份查询blog的数量
    @Cacheable(value = "cache",key = "'blogCountByYear'")
    public List<Map> selectBlogCountByYear(){
        List<Map> list = new ArrayList();
        Map map=null;
        //查询总计
        Integer blogCount = blogDao.selectBlogCount();
        map =new HashMap(2);
        map.put("year","总计");
        map.put("count",blogCount);
        list.add(map);
        //每个年份的总计
        List<Map> maps = blogDao.selectBlogCountByYear();
        for(Map item:maps){
            map=new HashMap(2);
            map.put("year",item.get("year"));
            map.put("count",item.get("blogCount"));
            list.add(map);
        }

        return list;
    }

    //按照year进行分页查询
    @Cacheable(value = "cache",key = "'selectBlogByYear:year:'+#year+'-'+#page+'-'+#size")
    public List<Blog> selectBlogByYear(Integer year, Integer page, Integer size) throws ParseException {
        PageHelper.startPage(page, size);

        BlogExample blogExample = new BlogExample();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        blogExample.createCriteria().andCreateTimeGreaterThan(simpleDateFormat.parse(String.valueOf(year)))
            .andCreateTimeLessThanOrEqualTo(simpleDateFormat.parse(String.valueOf((year+1))));
        List<Blog> blogs = blogDao.selectByExample(blogExample);
        PageInfo<Blog> pageInfo = new PageInfo<>(blogs);

        List<Blog> list = pageInfo.getList();
        for(Blog blog:list){
            //设置blog对应的tags
            List<Tag> tags = blogAndTagService.selectTagsByBlogId(blog.getId());
            blog.setTags(tags);
            //设置blog对应的访问次数
            Integer visitCount = this.selectVisitCount(blog.getId());
            blog.setHot(visitCount);
        }

        return list;
    }

    //根据blogId进行删除blog,同时清空所有的缓存
    //需要维护tag中的blog的数量,blogAndTag中间表中的数据
    @CacheEvict(value = "cache",allEntries = true)
    public void deleteBlogById(Integer blogId) throws MessageException {
        //删除对应的blog
        int i = blogDao.deleteByPrimaryKey(blogId);
        if(i==0){
            throw new MessageException("删除blog失败");
        }


        //将blog所对应的所有tag的blog数量递减
        List<Tag> tags = blogAndTagService.selectTagsByBlogId(blogId);
        for(Tag tag:tags){
            if(tag==null) continue;
            tagService.decrBlogCountByTagId(tag.getId());//对应的tag的数量递减
        }

        //维护blogAndTag中间表
        blogAndTagService.deleteByBlogId(blogId);

    }

    //查询所有的blogNames
    @Cacheable(value = "cache",key = "'allBlogNames'")
    public List<Map> selectAllBlogNames() {
        List<Map> blogNames = blogDao.selectAllBlogNames();
        return blogNames;
    }
}
