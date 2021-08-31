package com.lyj.blog.parser;

import com.lyj.blog.model.Blog;
import com.lyj.blog.parser.attr.TodoListAttributeProvider;
import com.lyj.blog.parser.model.EsHeading;
import com.lyj.blog.parser.render.HeadingRenderer;
import com.lyj.blog.parser.visitor.HeadingContentVisitor;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.task.list.items.TaskListItemsExtension;
import org.commonmark.renderer.NodeRenderer;
import org.commonmark.renderer.html.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 解析blog的核心抽象类
 */
public abstract class Parser {

    HeadingContentVisitor headingContentVisitor = new HeadingContentVisitor();//创建标题内容的处理器
    List<Extension> extensions = Arrays.asList(TablesExtension.create(), TaskListItemsExtension.create()); //加载插件
    org.commonmark.parser.Parser parser = org.commonmark.parser.Parser.builder().extensions(extensions).build();//创建markdown解析器
    HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensions)
            .attributeProviderFactory(new AttributeProviderFactory() {
                public AttributeProvider create(AttributeProviderContext context) {
                    return new TodoListAttributeProvider();
                }
            }).nodeRendererFactory(new HtmlNodeRendererFactory() {
                public NodeRenderer create(HtmlNodeRendererContext context) {
                    return new HeadingRenderer(context);
                }
            }).build();//创建html渲染器
    //============================================================
    Pattern headingPattern = Pattern.compile("^#+ .*\n");//用于匹配标题
    public static final String HeadingSplitLine = "=======------=======";//内容分割线

    private static StringBuilder sb = new StringBuilder();
    private static List<String> headingIdList = new ArrayList<>();
    private static int headingIdIndex = 0;

    // 添加ES搜索的展示文本
    public static void appendEsText(String text) {
        sb.append(text);
    }


    // 添加标题id
    public static void addHeadingId(String headingId) {
        headingIdList.add(headingId);
    }

    // 清空数据
    public static void clearData() {
        sb = new StringBuilder();
        headingIdList = new ArrayList<>();
        headingIdIndex = 0;
    }

    public static String getHeadingId() {
        return headingIdList.get(headingIdIndex++);
    }

    /**
     * 核心方法
     *
     * @param blog 传入需要解析的blog
     * @return 返回解析后的html
     */
    public String parse(Blog blog) {
        // 解析md并转化为html，收集heading信息（已经生成了headingId）
        String html = parseMdToHtml(blog.getMd());

        // 组装并生成ESHeading对象
        List<EsHeading> esHeadings = buildEsHeading(sb, headingIdList, blog);

        // 保存到ES中
        insertHeadingToEs(headingIdList, esHeadings, blog);

        // 最后一步，一定要清空数据
        clearData();

        return html;
    }

    protected abstract List<EsHeading> buildEsHeading(StringBuilder sb, List<String> headingIdList, Blog blog);

    protected abstract void insertHeadingToEs(List<String> headingIdList, List<EsHeading> esHeadings, Blog blog);

    // 解析md为html，并收集heading的相关数据
    abstract String parseMdToHtml(String md);


}
