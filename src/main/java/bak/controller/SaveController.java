package bak.controller;


import bak.annotation.NeedLogin;
import bak.exception.MessageException;
import bak.model.Blog;
import bak.model.Message;
import bak.service.BlogAndTagService;
import bak.service.BlogService;
import bak.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SaveController {

    @Autowired
    BlogService blogService;
    @Autowired
    TagService tagService;
    @Autowired
    BlogAndTagService blogAndTagService;


    /**
     * 缓存主要的删除点就在blog的创建和保存
     * @param blog
     * @return
     * @throws MessageException
     */
    @NeedLogin
    @RequestMapping("saveBlog")
    @ResponseBody
    public Message saveBlog(Blog blog) throws MessageException {
        Integer blogId = blog.getId();

        if(blogId==null){
            blogService.create(blog);//创建博客
        }else{
            blogService.save(blog);//更新博客
        }

        //发送保存成功的消息,并将保存后生成的blogId返回给前端,用于在编辑博客描述时定位博客
        return Message.success("保存成功",blog.getId());
    }

    /**
     * 缓存主要的删除点就在blog的创建和保存
     * @param blog
     * @return
     * @throws MessageException
     */
    @NeedLogin
    @RequestMapping("saveDesc")
    @ResponseBody
    public Message saveDesc(Blog blog) throws MessageException {
        Integer blogId = blog.getId();

        if(blogId==null){
            return Message.error("错误:blogId不能为空");
        }

        blogService.save(blog);

        return Message.success("保存成功");
    }



}
