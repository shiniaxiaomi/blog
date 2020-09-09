package com.lyj.blog.parser;

import com.lyj.blog.model.Blog;
import com.lyj.blog.parser.attr.TodoListAttributeProvider;
import com.lyj.blog.parser.model.ESHeading;
import com.lyj.blog.parser.render.HeadingRenderer;
import com.lyj.blog.parser.visitor.HeadingContentVisitor;
import com.lyj.blog.service.BlogService;
import com.lyj.blog.service.EsService;
import com.lyj.blog.service.TagService;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.task.list.items.TaskListItemsExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.NodeRenderer;
import org.commonmark.renderer.html.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/8/3 5:05 下午
 */
@Component
public class ParserUtil {

    @Autowired
    EsService esService;

    @Autowired
    BlogService blogService;
    @Autowired
    TagService tagService;

    private List<Extension> extensions = Arrays.asList(TablesExtension.create(), TaskListItemsExtension.create()); //加载插件

    private Parser parser = Parser.builder().extensions(extensions).build();//创建markdown解析器

    private HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensions)
            .attributeProviderFactory(new AttributeProviderFactory() {
                public AttributeProvider create(AttributeProviderContext context) {
                    return new TodoListAttributeProvider();
                }
            }).nodeRendererFactory(new HtmlNodeRendererFactory() {
                public NodeRenderer create(HtmlNodeRendererContext context) {
                    return new HeadingRenderer(context);
                }
            }).build();//创建html渲染器

    /**
     * 将md转换成html（需要使用到id，md字段）
     * @param blog
     * @return
     */
    public String parseMdToHtml(Blog blog){
        if(blog.getId()==null){
            throw new RuntimeException("blog的id不能为空");
        }

        //解析md，转换为语法树
        Node document = parser.parse(blog.getMd()==null?"":blog.getMd());
        //创建标题内容的处理器
        HeadingContentVisitor headingContentVisitor = new HeadingContentVisitor();
        headingContentVisitor.setBlogId(blog.getId());//设置blogId
        //添加visitor，用于生成标题及标题内容
        document.accept(headingContentVisitor);
        //渲染语法树，生成html
        String html = renderer.render(document);

        //获取已经解析好的标题
        List<ESHeading> headingList = headingContentVisitor.getHandledContent();
        //组装blogName和tagName
        if(headingList.size()!=0){
            String blogName = blogService.selectNameById(blog.getId());
            String tagName = tagService.selectTagNameByBlogId(blog.getId());
            headingList.forEach(esHeading -> {
                // 如果标题为默认标题，则使用博客名称作为标题
                if(esHeading.getHeadingName().equals("# 默认标题")){
                    esHeading.setHeadingName(blogName);
                }
                esHeading.setBlogName(blogName);
                esHeading.setTagName(tagName);
            });
        }

        //清除es中blogId对应的数据
        esService.deleteHeadingByBlogIdInES("blog",String.valueOf(blog.getId()));//根据blogId字段进行删除数据
        //批量保存到ES中
        if(headingList.size()!=0){
            Boolean isPrivate = blogService.getIsPrivateByBlogId(blog.getId());
            // 将博客内容保存到es中
            esService.insertHeadingToESBatch("blog",headingList,isPrivate);
        }

        return html;
    }



}
