package bak.controller;

import bak.annotation.NeedLogin;
import bak.model.Blog;
import bak.service.BlogAndTagService;
import bak.service.BlogService;
import bak.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class EditController {

    @Autowired
    BlogService blogService;
    @Autowired
    TagService tagService;
    @Autowired
    BlogAndTagService blogAndTagService;


    //编辑线上博客描述
    @NeedLogin
    @RequestMapping("/editDesc")
    public ModelAndView editDesc(int blogId){

        ModelAndView edit = new ModelAndView("edit");

        Blog blog = blogService.selectBlogById(blogId);
        edit.addObject("blog",blog);
        edit.addObject("editFlag","editDesc");

        return edit;
    }

    //编辑线上博客
    @NeedLogin
    @RequestMapping("/editBlog")
    public ModelAndView editBlog(int blogId){

        ModelAndView edit = new ModelAndView("edit");

        //查询对应的blog
        Blog blog = blogService.selectBlogById(blogId);

        edit.addObject("blog",blog);
        edit.addObject("editFlag","editBlog");
        //查询所有要提示的tags
        String tipTags = tagService.getAllTags();
        edit.addObject("tipTags",tipTags);

        return edit;
    }

    /**
     * 维持心跳
     */
    @NeedLogin
    @ResponseBody
    @RequestMapping("/holdHeartbeat")
    public void holdHeartbeat(){}


    //编辑本地草稿
    @RequestMapping("/editLocalDraft")
    public ModelAndView editLocalDraft(String blogId){
        ModelAndView edit = new ModelAndView("edit");
        edit.addObject("editFlag","editLocalDraft");
        edit.addObject("blogId",blogId);

        //获取提示的tag
        String tipTags = tagService.getAllTags();
        edit.addObject("tipTags",tipTags);
        return edit;
    }

    //编辑本地草稿描述
    @RequestMapping("/editLocalDraftDesc")
    public ModelAndView editLocalDraftDesc(String blogId){
        ModelAndView edit = new ModelAndView("edit");
        edit.addObject("editFlag","editLocalDraftDesc");
        edit.addObject("blogId",blogId);

        return edit;
    }



}
