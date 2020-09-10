package com.lyj.blog.parser.visitor;

import com.lyj.blog.parser.Parser;
import com.lyj.blog.parser.render.HeadingRenderer;
import org.commonmark.node.*;

import java.util.*;

/**
 * @author Yingjie.Lu
 * @description 用于统计标题和标题内容
 * @date 2020/8/3 5:15 下午
 */
public class HeadingContentVisitor extends AbstractVisitor {

    // 需要将左右尖括号进行转义(普通内容不会出现# 的情况，因为如果出现，那么就称为标题了)
    @Override
    public void visit(Text text) {
        Parser.appendEsText(text.getLiteral());//添加普通文本内容
        Parser.appendEsText("\n");
    }

    // 标题
    @Override
    public void visit(Heading heading) {
        //统一处理并获取标题，并返回标题名称（统一生成html和生成ES的标题是一致的）
        String text = HeadingRenderer.handleHeadingForES(heading);
        if(text==null) return;

        Parser.appendEsText(Parser.HeadingSplitLine);//添加普通文本内容
        Parser.appendEsText(text);//添加标题
        Parser.appendEsText("\n");

        //设置headingId
        Parser.addHeadingId(UUID.randomUUID().toString());
    }

    // 代码块
    @Override
    public void visit(FencedCodeBlock fencedCodeBlock) {
        Parser.appendEsText(fencedCodeBlock.getLiteral().replaceAll("#",""));//添加代码块，并去掉#
        Parser.appendEsText("\n");
    }

    // 内联代码块
    @Override
    public void visit(Code code) {
        Parser.appendEsText(code.getLiteral().replaceAll("#",""));//添加内联代码块，并去掉#
        Parser.appendEsText("\n");
    }



}
