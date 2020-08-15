package com.lyj.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyj.blog.mapper.BlogMapper;
import com.lyj.blog.model.Blog;
import com.lyj.blog.model.Tag;
import com.lyj.blog.model.req.FilingResult;
import com.lyj.blog.model.req.Message;
import com.lyj.blog.parser.ParserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/7/27 7:27 下午
 */
@Service
public class BlogService {

    @Autowired
    BlogMapper blogMapper;

    @Autowired
    UserService userService;

    @Autowired
    TagService tagService;

    @Autowired
    ParserUtil parserUtil;

    @Autowired
    EsService esService;

    @Autowired
    FileService fileService;

    @Autowired
    BlogTagRelationService blogTagRelationService;

    @Value("${file.location}/file")
    String filePath;

    // 添加blog
    public void insert(Blog blog) {
        blogMapper.insert(blog);
    }


    @Transactional
    public void delete(int id) {
        blogMapper.deleteById(id);
        // 删除博客与标签的关系
        blogTagRelationService.deleteByBlogId(id);
        // 清除es中的索引
        esService.deleteHeadingByBlogIdInES("blog",String.valueOf(id));
    }

    public void updateName(Integer blogId, String name) {
        Blog blog = new Blog().setId(blogId).setName(name);
        blogMapper.updateById(blog);
    }

    public String getMD(int id) {
        Blog blog = blogMapper.selectOne(new QueryWrapper<Blog>().select("md").eq("id", id));
        return blog==null?"":blog.getMd();
    }

    // 保存在渲染md的时候的线程安全
    @Transactional
    public synchronized void update(Blog blog) {

        // 解析md为html
        String html = parserUtil.parseMdToHtml(blog.getId(),blog.getMd());

        // 保存blog
        blog.setMdHtml(html);
        blog.setDesc(blog.getDesc());

        blogMapper.updateById(blog);

    }

    @Transactional
    public void updateConfig(Blog blog, Integer[] tags) {
        // 更新blog的公开状态
        blogMapper.updateById(blog);

        // 全量的同步更新标签关联
        tagService.updateRelation(blog.getId(),tags);
    }

    public String selectNameById(Integer blogId) {
        Blog blog = blogMapper.selectOne(new QueryWrapper<Blog>().select("name").eq("id", blogId));
        return blog.getName();
    }

    public Blog selectHTMLAndName(int id) {
        return blogMapper.selectOne(new QueryWrapper<Blog>().select("md_html","name").eq("id", id));
    }

    public List<Blog> selectIndexBlogs(boolean isStick, boolean isPrivate, int page, int size) {
        Page<Blog> result = blogMapper.selectPage(new Page<>(page, size), new QueryWrapper<Blog>()
                .select("id", "name", "`desc`","visit_count","create_time","update_time")
                .eq("is_stick", isStick)     //是否指定
                .eq("is_private", isPrivate) //是否公有
                .orderByDesc("update_time") //按照更新时间降序排列
                .orderByDesc("create_time") //按照创建时间降序排列
        );

        return renderBlogItems(result.getRecords());
    }

    // 渲染blog列表，组装对应的tag
    public List<Blog> renderBlogItems(List<Blog> list){
        // 收集blogId
        List<Integer> blogIds = list.stream().map(Blog::getId).collect(Collectors.toList());
        // 组装tag
        List<Tag> tags = tagService.selectTagByBlogIds(blogIds);
        list.forEach(blog -> {
            List<Tag> tagList = new ArrayList<>();
            for(Tag tag:tags){
                if(blog.getId().equals(tag.getBlogId())){
                    tagList.add(tag);
                }
            }
            blog.setTags(tagList);
        });
        return list;
    }

    public Blog getBlogConfig(int id) {
        return blogMapper.selectOne(new QueryWrapper<Blog>().select("is_private", "is_stick").eq("id", id));
    }

    // 自增博客的访问次数
    public void countIncr(int id) {
        Blog blog = blogMapper.selectOne(new QueryWrapper<Blog>().select("visit_count").eq("id", id));
        blog.setId(id);
        Integer visitCount = blog.getVisitCount();
        blog.setVisitCount(++visitCount);
        blogMapper.updateById(blog);
    }

    public List<FilingResult> filing() {
        return blogMapper.filing();
    }

    //分页查询在指定年份的blogItem
    public Page<Blog> selectBlogItemsByYear(int year,int page,int size) {
        Date startYear=Date.from(LocalDate.of(year,1,1)
                .atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date endYear=Date.from(LocalDate.of(year+1,1,1)
                .atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        Page<Blog> result = blogMapper.selectPage(new Page<>(page, size), new QueryWrapper<Blog>()
                .select("id", "name", "`desc`","visit_count","create_time","update_time")
                .eq("is_private", false) //公有
                .between("create_time",startYear,endYear)
                .orderByDesc("update_time") //按照更新时间降序排列
                .orderByDesc("create_time") //按照创建时间降序排列
        );

        renderBlogItems(result.getRecords());
        return result;
    }

    // 根据tagId分页查询blogItems
    public Page<Blog> selectBlogItemsByTagId(int tagId, int page, int size) {
        Page<Blog> blogPage = blogMapper.selectBlogItemsByTagId(false, tagId, new Page<>(page, size));//公有
        renderBlogItems(blogPage.getRecords());
        return blogPage;
    }

    // 分页查询：根据是否置顶和私有
    public Page<Blog> selectBlogItemsPage(boolean isStick, boolean isPrivate, int page, int size){
        Page<Blog> result = blogMapper.selectPage(new Page<>(page, size), new QueryWrapper<Blog>()
                .select("id", "name", "`desc`","visit_count","create_time","update_time")
                .eq("is_private", isPrivate) //公有
                .eq("is_stick",isStick) //置顶
                .orderByDesc("update_time") //按照更新时间降序排列
                .orderByDesc("create_time") //按照创建时间降序排列
        );

        renderBlogItems(result.getRecords());
        return result;
    }

    // 上传文件的处理
    @Transactional
    public Message uploadFile(MultipartFile multipartFile,int blogId) {
        if(multipartFile.isEmpty()){
            return Message.error("文件不能为空");
        }
        if(multipartFile.getOriginalFilename()==null){
            return Message.error("文件名称不能为空");
        }
        //获取并处理文件名
        String originalFilename = multipartFile.getOriginalFilename()
            .replaceAll("[^(a-zA-Z0-9\\u4e00-\\u9fa5\\.)]","")
                .replaceAll("[\\?\\\\\\/:\\|<>\\*\\[\\]\\$%\\{\\}@~\\(\\)\\s]","");
        String[] split = originalFilename.split("\\.");
        String filename;
        boolean isImg=false;
        if(split.length==2){
            filename = split[0]+"-"+ DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss").format(LocalDateTime.now())+"."+split[1];
            if("png".equals(split[1]) || "jpg".equals(split[1]) || "jpeg".equals(split[1]) || "gif".equals(split[1])){
                isImg=true;
            }
        }else{
            filename = split[0]+"-"+ LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss"));
        }
        //获取文件存放路径
        File file = new File(filePath,filename);

        //判断路径是否存在，如果不存在就创建一个
        if (!file.getParentFile().exists()) {
            boolean mkdirs = file.getParentFile().mkdirs();
            if(!mkdirs){
                return Message.error("文件夹创建失败");
            }
        }

        //将文件名称和关系数据库保存一份
        com.lyj.blog.model.File dbFile = new com.lyj.blog.model.File();
        dbFile.setName(filename).setType(isImg?0:1);
        fileService.insertFile(dbFile,blogId);

        try(
            //自动关闭流
            BufferedInputStream bis = new BufferedInputStream(multipartFile.getInputStream());
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        ){
            byte[] b=new byte[100];//也可以使用默认的缓冲区大小
            int len=0;
            while((len=bis.read(b))!=-1){
                bos.write(b,0,len);//输出到文件
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Message.error("文件上传失败");
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("fileName",filename);
        map.put("isImg",isImg);
        return Message.success("上传成功",map);
    }

    public Blog selectHTMLAndNameByName(String name) {
        return blogMapper.selectOne(new QueryWrapper<Blog>().select("id","md_html", "name").eq("name", name));
    }

    public Blog selectBlogByCommentId(int commentId) {
        return blogMapper.selectBlogByCommentId(commentId);
    }

    public int selectVisitCount() {
        return blogMapper.selectSum();
    }
}
