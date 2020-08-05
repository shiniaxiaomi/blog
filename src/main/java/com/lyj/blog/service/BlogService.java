package com.lyj.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyj.blog.mapper.BlogMapper;
import com.lyj.blog.model.Blog;
import com.lyj.blog.parser.ParserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/7/27 7:27 下午
 */
@Service
public class BlogService {

    // 解析md的正则表达式
//    private Pattern mdPattern = Pattern.compile("#+([^#].*(\\r\\n)+)*");
//    private Pattern headPattern = Pattern.compile("#+.*");

    @Autowired
    BlogMapper blogMapper;

    @Autowired
    UserService userService;

    @Autowired
    TagService tagService;

    @Autowired
    ParserUtil parserUtil;



    // 添加blog
    public void insert(Blog blog) {
        blogMapper.insert(blog);
    }


    public void delete(int id) {
        blogMapper.deleteById(id);
        //todo 清除es中的索引
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
        blogMapper.updateById(blog);

        // 清除es中blogId对应的数据
//        deleteDocumentInES("blog", blog.getId().toString(), uuid);//blog数据库，blogId表，以header的uuid为一行数据
        // 往es中添加blogId对应的数据
//        insertDocumentToEsBatch("blog",blog.getId().toString(),uuid,blog.getMd());
    }

//    public void insertDocumentToES(String id){
//        IndexRequest indexRequest = new IndexRequest("blog")
//                .id(id)
//                .source("blogId", id,
//                        "header", "标题名称",
//                        "tag", "标签名称"
//                        );
//    }

//    public void insertDocumentToEsBatch(String index,String type,String id,String md){
//        //创建批量请求
//        BulkRequest request = new BulkRequest();
//
//        Matcher matcher = mdPattern.matcher(md);//使用正则将标题的内容分离
//        while(matcher.find()) {
//            String group = matcher.group();//每个标题对应的内容
//            // 解析标题名称
//            Matcher head = headPattern.matcher(group);
//            if(head.find()){
//                String s = head.group();
//                int level=0;
//                String header = null;
//                for(int i=0;i<s.length();i++){
//                    if(s.charAt(i)=='#'){
//                        level++;
//                    }else if(s.charAt(i)==' '){
//                        header=s.substring(i);
//                        break;
//                    }
//                }
//                request.add(new IndexRequest(index, type, id).source("header", header,"content",group,"level",level));
//            }
//        }
//
//        try {
//            elasticClient.bulk(request, RequestOptions.DEFAULT);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    // 删除elasticSearch中的文档
//    public void deleteDocumentInES(String index,String type,String id){
//
//        GetRequest getRequest = new GetRequest(index, type, id);
//
//        //创建delete请求
//        DeleteRequest request = new DeleteRequest(index, type, id);
//        //通过client进行删除
//        try {
//            elasticClient.delete(request, RequestOptions.DEFAULT);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public boolean getIsPrivate(int id) {
        Blog blog = blogMapper.selectOne(new QueryWrapper<Blog>().select("is_private").eq("id", id));
        return blog.getIsPrivate();
    }

    @Transactional
    public void updateConfig(int id,boolean isPrivate, Integer[] tags) {
        // 更新blog的公开状态
        Blog blog = new Blog().setId(id).setIsPrivate(isPrivate);
        blogMapper.updateById(blog);

        // 全量的同步更新标签关联
        tagService.updateRelation(id,tags);
    }

    public String selectNameById(Integer blogId) {
        Blog blog = blogMapper.selectOne(new QueryWrapper<Blog>().select("name").eq("id", blogId));
        return blog.getName();
    }

    public String selectHTML(int id) {
        Blog blog = blogMapper.selectOne(new QueryWrapper<Blog>().select("md_html").eq("id", id));
        return blog.getMdHtml();
    }

    public List<Blog> selectIndexBlogs(boolean isStick, boolean isPrivate, int page, int size) {
        Page<Blog> result = blogMapper.selectPage(new Page<>(page, size), new QueryWrapper<Blog>()
                .select("id", "name", "`desc`","create_time","update_time")
                .eq("is_stick", isStick)     //是否指定
                .eq("is_private", isPrivate) //是否公有
                .orderByDesc("update_time") //按照更新时间降序排列
                .orderByDesc("create_time") //按照创建时间降序排列
        );

        return result.getRecords();

    }
}
