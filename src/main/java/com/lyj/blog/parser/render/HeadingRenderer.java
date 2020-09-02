package com.lyj.blog.parser.render;

import com.lyj.blog.parser.visitor.HeadingContentVisitor;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.renderer.NodeRenderer;
import org.commonmark.renderer.html.HtmlNodeRendererContext;
import org.commonmark.renderer.html.HtmlWriter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/8/1 1:39 下午
 */
public class HeadingRenderer implements NodeRenderer {

    private final HtmlWriter html;

    public HeadingRenderer(HtmlNodeRendererContext context) {
        this.html = context.getWriter();

    }

    @Override
    public Set<Class<? extends Node>> getNodeTypes() {
        return Collections.singleton(Heading.class);
    }

    @Override
    public void render(Node node) {
        Heading heading = (Heading) node;

        String text = handleHeading(heading);
        if(text==null) return;

        HashMap<String, String> map = new HashMap<>();
        map.put("text",text);
        map.put("id", HeadingContentVisitor.getHeadingId());
        html.tag("h"+heading.getLevel(),map);
        html.text(text);
        html.tag("/h"+heading.getLevel());
    }

    // 处理heading，返回标题内容
    public static String handleHeadingForES(Heading heading){
        String headingName=null;
        if(heading.getFirstChild()==null){
            return null;
        }
        switch (heading.getLevel()){
            case 1:
                headingName = "# " +((Text) heading.getFirstChild()).getLiteral();
                break;
            case 2:
                headingName = "## " +((Text) heading.getFirstChild()).getLiteral();
                break;
            case 3:
                headingName = "### " +((Text) heading.getFirstChild()).getLiteral();
                break;
            case 4:
                headingName = "#### " +((Text) heading.getFirstChild()).getLiteral();
                break;
            case 5:
                headingName = "##### " +((Text) heading.getFirstChild()).getLiteral();
                break;
            case 6:
                headingName = "###### " +((Text) heading.getFirstChild()).getLiteral();
                break;
        }
        return headingName;
    }

    // 处理heading，返回标题内容
    public static String handleHeading(Heading heading){
        if(heading.getFirstChild()==null){
            return null;
        }
        return ((Text) heading.getFirstChild()).getLiteral();
    }
}
