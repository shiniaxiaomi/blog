package com.lyj.blog.controller;


import com.alibaba.fastjson.JSON;
import com.lyj.blog.annotation.NeedLogin;
import com.lyj.blog.model.Blog;
import com.lyj.blog.model.Message;
import com.lyj.blog.model.Tag;
import com.lyj.blog.service.*;
import com.lyj.blog.util.MyUtil;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Controller
public class PageController {

    @Value("${imgUploadPath}")
    String imgUploadPath;//上传图片的路径


    @Autowired
    BlogService blogService;
    @Autowired
    TagService tagService;

    @Autowired
    UserService userService;

    @Autowired
    BlogAndTagService blogAndTagService;

    @Autowired
    HttpSession session;

    @Autowired
    PageDataService pageDataService;

    @Autowired
    RedisTemplate redisTemplate;

    @RequestMapping("/")
    public ModelAndView index() throws Exception {

        ModelAndView index = new ModelAndView("home");

        //置顶博客
        List<Blog> stickBlogs = tagService.getBlogsByTagName("置顶",1);//只要置顶数量不超过10个即可
        index.addObject("stickBlogs",stickBlogs);
        //最新博客(分页)
        List<Blog> blogs = blogService.getBlogs(0, 10);
        index.addObject("blogs",blogs);
        //提供介绍数据
        pageDataService.provideIntroduceData(index,true);
        //标记访问类型
        index.addObject("blogType","blog");//类型编辑为blog

        return index;
    }

    //分页查看所有的blog(每页10条)
    @RequestMapping("moreBlog")
    public ModelAndView moreBlog(String page) throws Exception {

        ModelAndView modelAndView = new ModelAndView("moreBlog");

        Integer intPage = MyUtil.pageCheck(page);

        //分页查询blog
        List<Blog> blogs = blogService.getBlogs(intPage, 10);
        modelAndView.addObject("blogs",blogs);

        pageDataService.provideIntroduceData(modelAndView,false);

        //标记访问类型
        modelAndView.addObject("blogType","blog");//类型编辑为blog

        //当前是第几页
        modelAndView.addObject("nowPage",intPage);
        modelAndView.addObject("nowSize",blogs.size());

        return modelAndView;
    }

    //分页查看所有的草稿(每页10条)
    @RequestMapping("moreDraft")
    public ModelAndView moreDraft(String page){

        ModelAndView modelAndView = new ModelAndView("moreDraft");

        Integer intPage = MyUtil.pageCheck(page);

        //分页查询blog
        List<Blog> blogs = blogService.getBlogsByTagName("草稿", intPage, 10);
        modelAndView.addObject("blogs",blogs);

        pageDataService.provideIntroduceData(modelAndView,false);

        //标记访问类型
        modelAndView.addObject("blogType","blog");//类型编辑为blog

        //当前是第几页
        modelAndView.addObject("nowPage",intPage);
        modelAndView.addObject("nowSize",blogs.size());

        return modelAndView;
    }

    //分页查看所有的草稿(每页10条)
    @RequestMapping("moreLocalDraft")
    public ModelAndView moreLocalDraft(String page){

        ModelAndView modelAndView = new ModelAndView("moreLocalDraft");

        Integer intPage = MyUtil.pageCheck(page);

        pageDataService.provideIntroduceData(modelAndView,false);

        //标记访问类型
        modelAndView.addObject("blogType","localDraft");//类型编辑为本地草稿

        //当前是第几页
        modelAndView.addObject("nowPage",intPage);
        modelAndView.addObject("nowSize",0);

        return modelAndView;
    }




    //查看线上博客和线上草稿
    @RequestMapping("blog")
    public ModelAndView blog(Integer id) {

        ModelAndView modelAndView = new ModelAndView("blogContext");

        if(id==null){
            modelAndView.setViewName("forward:/");//转发到首页
            return modelAndView;
        }

        //查询对应的blog
        Blog blog = blogService.selectBlogById(id);//因为mybatis有缓存的问题,返回的结果可能为null
        if(blog!=null){
            Integer visitCount = blogService.selectAndIncrVisitCount(id);//查询对应博客的访问次数并递增
            blog.setHot(visitCount);
            modelAndView.addObject("blog",blog);
        }else{
            //记录次数,如果次数超过5次,则返回首页
            Long increment = redisTemplate.opsForValue().increment("refreshTimesByBlogId:" + id);
            if(increment>5){
                //返回首页,并清除次数
                redisTemplate.delete("refreshTimesByBlogId:" + id);
                modelAndView.setViewName("forward:/");
                return modelAndView;
            }else{
                //如果blog为null,则再次重定向到该链接,重新进行加载数据
                modelAndView.setViewName("redirect:/blog?id="+id);
                return modelAndView;
            }
        }

        //提供介绍数据
        pageDataService.provideIntroduceData(modelAndView,true);

        return modelAndView;
    }

    @RequestMapping("aboutMe")
    public ModelAndView aboutMe() throws Exception {
        Tag tag = tagService.selectTagByTagName("介绍");
        if(tag==null) return new ModelAndView("forward:/");//转发的首页

        List<Blog> blogs = blogAndTagService.selectBlogsByTagId(tag.getId(),1,10);
        //因为介绍只有一篇
        if(blogs.size()==0){
            throw new Exception("还没有介绍");
        }else{
            //转发到blog展示页面,展示介绍博客
            return new ModelAndView("forward:/blog?id="+blogs.get(0).getId());
        }
    }

    //查询tag对应的blog
    @RequestMapping("moreBlogByTag")
    public ModelAndView moreBlogByTag(Integer id,String page) {

        ModelAndView modelAndView = new ModelAndView("moreBlogByTag");

        if(id==null){
            modelAndView.setViewName("forward:/");//转发到首页
            return modelAndView;
        }

        Integer intPage = MyUtil.pageCheck(page);

        //tag对应的blogs
        List<Blog> blogs = blogAndTagService.selectBlogsByTagId(id,intPage,10);
        modelAndView.addObject("blogs",blogs);
        //对应的tag
        Tag tag = tagService.selectTagByTagId(id);
        modelAndView.addObject("tag",tag);

        pageDataService.provideIntroduceData(modelAndView,false);

        //当前是第几页
        modelAndView.addObject("nowPage",intPage);
        //总共的页数
        modelAndView.addObject("nowSize",blogs.size());

        return modelAndView;

    }

    //查询tag对应的blog
    @RequestMapping("moreBlogByYear")
    public ModelAndView moreBlogByYear(String year,String page) throws ParseException {

        ModelAndView modelAndView = new ModelAndView("moreBlogByYear");

        if(year==null){
            modelAndView.setViewName("forward:/");//转发到首页
            return modelAndView;
        }

        Integer intPage = MyUtil.pageCheck(page);
        Integer intYear = MyUtil.yearCheck(year);

        //year对应的blog
        List<Blog> blogs = blogService.selectBlogByYear(intYear, intPage, 10);
        modelAndView.addObject("blogs",blogs);


        pageDataService.provideIntroduceData(modelAndView,false);

        //当前是第几页
        modelAndView.addObject("nowPage",intPage);
        //总共的页数
        modelAndView.addObject("nowSize",blogs.size());

        modelAndView.addObject("year",String.valueOf(intYear));

        return modelAndView;

    }


    @RequestMapping("/edit")
    public ModelAndView edit(){
        ModelAndView edit = new ModelAndView("edit");
        edit.addObject("editFlag","edit");
        //查询所有要提示的tags
        String tipTags = tagService.getAllTags();
        edit.addObject("tipTags",tipTags);
        return edit;
    }

    @RequestMapping("/draftHome")
    public ModelAndView draftHome(String page){
        ModelAndView draft = new ModelAndView("draftHome");

        Integer intPage = MyUtil.pageCheck(page);

        List<Blog> draftBlogs = blogService.selectDraft(intPage);

        draft.addObject("blogType","blog");
        draft.addObject("draftBlogs",draftBlogs);

        //提供介绍数据
        pageDataService.provideIntroduceData(draft,false);

        //当前是第几页
        draft.addObject("nowPage",intPage);

        return draft;
    }

    //查看本地草稿
    @RequestMapping("/localDraft")
    public ModelAndView localDraft(String id){
        ModelAndView draft = new ModelAndView("localDraftContext");

        if(id==null){
            draft.setViewName("forward:/");//转发到首页
            return draft;
        }

        draft.addObject("blogId",id);
        draft.addObject("blogType","localDraft");//标记为本地草稿

        //提供介绍数据
        pageDataService.provideIntroduceData(draft,false);

        return draft;
    }

    //其实是获取到所有的tags,并按照文章数量降序排列
    @RequestMapping("/moreTags")
    @ResponseBody
    public Message moreTags(){
        List<Tag> tags = tagService.selectAllTags();
        return Message.success(null,tags);
    }

    /**
     * 上传图片
     * @param request
     * @return
     * @throws IOException
     * @throws FileUploadException
     * @throws ServletException
     */
    @NeedLogin
    @RequestMapping(value = "/uploadImg")
    @ResponseBody
    public String uploadImg(HttpServletRequest request){

        String fileName=null;
        Iterator<Part> iterator = null;
        try {
            iterator = request.getParts().iterator();
            while(iterator.hasNext()){
                Part next = iterator.next();
                fileName=next.getSubmittedFileName();
                File file = new File(imgUploadPath,fileName);
                if(!file.exists()){
                    file.createNewFile();
                }
                next.write(file.getAbsolutePath());
            }

            HashMap<String, Object> map = new HashMap<>();
            map.put("success",1);
            map.put("message","上传成功");
            map.put("url",fileName);

            return JSON.toJSONString(map);
        } catch (Exception e) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("success",0);
            map.put("message","上传失败");
            return JSON.toJSONString(map);
        }

    }


}
