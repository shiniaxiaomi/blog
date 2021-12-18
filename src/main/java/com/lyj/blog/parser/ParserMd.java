package com.lyj.blog.parser;


import com.lyj.blog.service.TagService;
import org.commonmark.node.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 解析md文档的核心类
 */
@Component
public class ParserMd extends Parser {

    @Autowired
    TagService tagService;

    @Override
    public String parseMdToHtml(String md) {
        //解析md，转换为语法树
        Node document = parser.parse(md == null ? "" : md);
        //添加visitor，用于生成标题及标题内容
        document.accept(headingContentVisitor);
        //渲染语法树，生成html
        return renderer.render(document);
    }
}
