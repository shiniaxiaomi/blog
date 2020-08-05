package com.lyj.blog.parser;

import com.lyj.blog.parser.attr.TodoListAttributeProvider;
import com.lyj.blog.parser.render.HeadingRenderer;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.task.list.items.TaskListItemsExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.NodeRenderer;
import org.commonmark.renderer.html.*;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/7/31 11:53 下午
 */
public class Test {
    public static void main(String[] args) {
        test1();
    }

    private static void test1() {
        String content = "# djsfkdsjk\n" +
                "\n" +
                "sdjfkdsj\n" +
                "\n" +
                "- jkdfsd\n" +
                "- dsfds\n" +
                "\n" +
                "---\n" +
                "\n" +
                "1. dsjfdsk\n" +
                "2. sdfds\n" +
                "\n" +
                "## jsdkfjsd\n" +
                "\n" +
                "```java\n" +
                "public void main(){\n" +
                "    int i=0;\n" +
                "}\n" +
                "```\n" +
                "\n" +
                "`dsjfkdsjfkdsjfdsk`\n" +
                "\n" +
                "dsfjkdsfds\n" +
                "\n" +
                "\n" +
                "| dsfsd | weq | col3qwewq |\n" +
                "| - | - | - |\n" +
                "| 1 | 2 | 3 |\n" +
                "| 4 | 5 | 6 |\n" +
                "\n" +
                "[dsfjdsk](dfjskfjsdk \"kdsfjks\")\n" +
                "\n" +
                "> jksjfkds\n" +
                ">\n" +
                "> dsfjkds\n" +
                "\n" +
                "* [X] dsfjdsjfds\n" +
                "* [ ] dsfdsfds";
        Pattern pattern = Pattern.compile("#+(.*\\n)*");
        // 现在创建 matcher 对象
        Matcher m = pattern.matcher(content);
        if (m.find()) {
            System.out.println("Found value: " + m.group(0) );
        }
    }

    public static void test(){
        List<Extension> extensions = Arrays.asList(TablesExtension.create(), TaskListItemsExtension.create()); //加载插件

        Parser parser = Parser.builder().extensions(extensions).build();//创建markdown解析器
        Node document = parser.parse("# djsfkdsjk\n" +
                "\n" +
                "sdjfkdsj\n" +
                "\n" +
                "- jkdfsd\n" +
                "- dsfds\n" +
                "\n" +
                "---\n" +
                "\n" +
                "1. dsjfdsk\n" +
                "2. sdfds\n" +
                "\n" +
                "## jsdkfjsd\n" +
                "\n" +
                "```java\n" +
                "public void main(){\n" +
                "    int i=0;\n" +
                "}\n" +
                "```\n" +
                "\n" +
                "`dsjfkdsjfkdsjfdsk`\n" +
                "\n" +
                "dsfjkdsfds\n" +
                "\n" +
                "\n" +
                "| dsfsd | weq | col3qwewq |\n" +
                "| - | - | - |\n" +
                "| 1 | 2 | 3 |\n" +
                "| 4 | 5 | 6 |\n" +
                "\n" +
                "[dsfjdsk](dfjskfjsdk \"kdsfjks\")\n" +
                "\n" +
                "> jksjfkds\n" +
                ">\n" +
                "> dsfjkds\n" +
                "\n" +
                "* [X] dsfjdsjfds\n" +
                "* [ ] dsfdsfds");//解析markdown，生成语法树

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
        String render = renderer.render(document);//渲染语法树，生成html
        System.out.println(render);
    }
}
