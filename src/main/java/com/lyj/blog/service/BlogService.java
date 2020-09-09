package com.lyj.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyj.blog.exception.MessageException;
import com.lyj.blog.handler.FileUtil;
import com.lyj.blog.mapper.BlogMapper;
import com.lyj.blog.model.Blog;
import com.lyj.blog.model.Catalog;
import com.lyj.blog.model.Tag;
import com.lyj.blog.model.req.FilingResult;
import com.lyj.blog.model.req.Message;
import com.lyj.blog.parser.ParserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/7/27 7:27 下午
 */
@Slf4j
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

    @Autowired
    CatalogService catalogService;

    @Value("${myConfig.file.location}/file")
    String filePath;

    @Autowired
    HttpSession session;

    @Autowired
    RedisService redisService;

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
        // 更新es对应的blog的名称
        esService.updateBlogNameByBlogId(blogId,name);
    }

    public String getMD(int id) {
        String md=blogMapper.selectMdByBlogId(id);
        return md==null?"":md;
    }

    // 保存在渲染md的时候的线程安全
    @Transactional
    @CacheEvict(value = "Blog",key = "#blog.id")
    public synchronized void update(Blog blog) {

        // 解析md为html,并将heading批量保存到es中
        String html = parserUtil.parseMdToHtml(blog);

        // 保存blog
        blog.setMdHtml(html);
        blog.setDesc(blog.getDesc());
        blog.setUpdateTime(new Date());

        blogMapper.updateById(blog);

    }

    @Transactional
    public void updateConfig(Blog blog, Integer[] tags) {
        // 更新blog的置顶状态
        blogMapper.updateById(blog);

        // 全量的同步更新标签关联
        tagService.updateRelation(blog.getId(),tags);
    }

    public String selectNameById(Integer blogId) {
        return blogMapper.selectNameByBlogId(blogId);
    }

    @Cacheable(value = "Blog",key = "#id")
    public Blog selectHTMLAndName(int id) {
        // 根据是否登入，查询公有的blog
        if(session.getAttribute("isLogin")!=null){
            return blogMapper.selectOne(new QueryWrapper<Blog>().select("md_html","name").eq("id", id));
        }else{
            return blogMapper.selectOne(new QueryWrapper<Blog>().select("md_html","name").eq("id", id)
                    .eq("is_private",false));
        }
    }

    @Cacheable(value = "BlogIndexPage",key = "#isStick + ',' + #isPrivate + ',' + #page + ',' + #size")
    public List<Blog> selectIndexBlogs(Boolean isStick, Boolean isPrivate, int page, int size) {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<Blog>()
                .select("id", "name", "`desc`","is_private"
                        ,"visit_count", "create_time", "update_time")
                .orderByDesc("update_time") //按照更新时间降序排列
                .orderByDesc("create_time"); //按照创建时间降序排列
        if(isStick!=null){
            queryWrapper.eq("is_stick", isStick); //置顶
        }
        if(!isPrivate){
            queryWrapper.eq("is_private", false); //公有
        }
        Page<Blog> result = blogMapper.selectPage(new Page<>(page, size), queryWrapper);

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
        return blogMapper.selectOne(new QueryWrapper<Blog>()
                .select("is_private", "is_stick").eq("id", id));
    }





    @Cacheable(value = "Filing",cacheManager = "cacheManager") //归档数据
    public List<FilingResult> filing() {
        return blogMapper.filing();
    }

    //分页查询在指定年份的blogItem
    @Cacheable(value = "BlogByYear",key = "#year + ',' + #page + ',' + #size")
    public Page<Blog> selectBlogItemsByYear(int year,Boolean isPrivate,int page,int size) {
        Date startYear=Date.from(LocalDate.of(year,1,1)
                .atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date endYear=Date.from(LocalDate.of(year+1,1,1)
                .atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        QueryWrapper<Blog> queryWrapper = new QueryWrapper<Blog>()
                .select("id", "name", "`desc`", "visit_count", "create_time", "update_time")
                .between("create_time", startYear, endYear)
                .orderByDesc("update_time") //按照更新时间降序排列
                .orderByDesc("create_time");//按照创建时间降序排列
        if(!isPrivate){
            queryWrapper.eq("is_private", false); //公有
        }

        Page<Blog> result = blogMapper.selectPage(new Page<>(page, size), queryWrapper);
        renderBlogItems(result.getRecords());
        return result;
    }

    // 根据tagId分页查询blogItems
    @Cacheable(value = "BlogByTag",key = "#tagId + ',' + #page + ',' + #size")
    public Page<Blog> selectBlogItemsByTagId(int tagId, int page, int size) {
        Page<Blog> blogPage = blogMapper.selectBlogItemsByTagId(false, tagId, new Page<>(page, size));//公有
        renderBlogItems(blogPage.getRecords());
        return blogPage;
    }

    // 分页查询：根据是否置顶和私有
    @Cacheable(value = "BlogPage",key = "#isStick + ',' + #isPrivate + ',' + #page + ',' + #size")
    public Page<Blog> selectBlogItemsPage(Boolean isStick, Boolean isPrivate, int page, int size){
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<Blog>()
                .select("id", "name", "`desc`", "visit_count", "create_time", "update_time");
        if(isStick!=null){
            queryWrapper.eq("is_stick", isStick); //置顶
        }
        if(!isPrivate){
            queryWrapper.eq("is_private", false); //公有
        }
        queryWrapper.orderByDesc("update_time") //按照更新时间降序排列
                .orderByDesc("create_time");//按照创建时间降序排列
        Page<Blog> result = blogMapper.selectPage(new Page<>(page, size), queryWrapper);

        renderBlogItems(result.getRecords());
        return result;
    }

    @Cacheable(value = "Blog",key = "#name")
    public Blog selectHTMLAndNameByName(String name) {
        return blogMapper.selectOne(new QueryWrapper<Blog>().select("id","md_html", "name").eq("name", name));
    }

    public Blog selectBlogByCommentId(int commentId) {
        return blogMapper.selectBlogByCommentId(commentId);
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

    // 备份文件
    public void backups() {
        File folder = new File(filePath + "/MdFolder");
        // 文件夹存在，则删除文件夹
        if(folder.exists()){
            FileUtil.deleteFileOrDir(folder);
        }

        // 默认为文件夹不存在，创建文件夹
        boolean mkdir = folder.mkdir();
        if(!mkdir){
            throw new MessageException("文件夹创建失败，备份失败");
        }

        // 查询blog表的name和md字段，并生成md文件
        List<Blog> blogs = blogMapper.selectList(new QueryWrapper<Blog>().select("name", "md"));
        for (Blog blog:blogs){
            File mdFile = new File(folder.getAbsolutePath() + "/" + blog.getName()+".md");
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(mdFile));){
                writer.write(blog.getMd()!=null?blog.getMd():"");
            } catch (IOException e) {
                log.error("备份过程中，"+blog.getName()+"文件备份失败",e);
            }
        }

        // 创建zip压缩包文件
        String zipFileName = FileUtil.createZipFile(folder, filePath);
        // 将备份好的zip添加到file表中进行管理
        if(zipFileName!=null){
            com.lyj.blog.model.File backupFile = new com.lyj.blog.model.File();
            backupFile.setName(zipFileName).setType(1).setCount(0);//设置名称，文件类型，引用数为0
            fileService.insertFile(backupFile,-1);//blogId=-1的表示备份文件
        }
    }

    // 通过blogId和文件夹的私有情况，综合得出该blog是否为私有
    public boolean mergeAndGetBlogIsPrivate(int blogId){
        Catalog blog = catalogService.selectIsPrivateByBlogId(blogId);
        List<Catalog> folders = catalogService.selectFolder();

        // 首先先看blog，如果是私有的，则直接返回私有，如果是公有的，则继续查看他的父级，直到父级的pid为null
        if(blog.getIsPrivate()){
            return true;
        }

        // ========以下情况blog都是公有的，然后就接着判断他的父级的私有状态========
        // 如果文件夹个数为0，则直接返回公有
        if(folders.size()==0){
            return false;
        }

        // 如果folders个数不为0,则先找到一个父级节点
        Catalog folder=null;
        for (Catalog tempFolder : folders) {
            if (blog.getPid().equals(tempFolder.getId())) {
                // 如果父级直接是私有的，则直接返回私有
                if (tempFolder.getIsPrivate()) {
                    return true;
                } else {
                    // 保存父级节点
                    folder = tempFolder;
                    break;
                }
            }
        }

        // 如果没找到父节点，则直接返回公有
        if(folder==null){
            return false;
        }

        // 如果找到了父节点，在递归遍历其父节点，直到父级的pid为null
        while(folder.getPid()!=null){
            for (Catalog tempFolder : folders) {
                // 如果找到了父级
                if (blog.getPid().equals(tempFolder.getId())) {
                    // 如果父级直接是私有的，则直接返回私有
                    if (tempFolder.getIsPrivate()) {
                        return true;
                    }
                    // 如果父级为公有，则保存父级，且继续递归遍历
                    folder = tempFolder;
                    break;
                }
            }
        }

        // 如果都不是私有，则最终返回公有状态
        return false;
    }


    // 根据blogId查询该blog是否为私有状态
    public Boolean getIsPrivateByBlogId(Integer blogId) {
        return blogMapper.getIsPrivateByBlogId(blogId);
    }

    public List<Blog> selectIdAndMdList() {
        return blogMapper.selectList(new QueryWrapper<Blog>().select("id","md"));
    }
}
