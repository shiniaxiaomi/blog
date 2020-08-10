package com.lyj.blog.parser.visitor;

import com.lyj.blog.parser.model.ESHeading;
import com.lyj.blog.parser.render.HeadingRenderer;
import org.commonmark.node.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Yingjie.Lu
 * @description 用于统计标题和标题内容
 * @date 2020/8/3 5:15 下午
 */
public class HeadingContentVisitor extends AbstractVisitor {

    private Integer blogId;

    private static List<String> headingIdList;
    private static int headingIndex=0;

    //获取headingId
    public static String getHeadingId(){
        return headingIdList.get(headingIndex++);
    }

    public void setBlogId(Integer blogId) {
        this.blogId = blogId;
    }

    public static String HeadingSplitLine="=======------=======";//内容分割线

    Pattern pattern = Pattern.compile("^.*\n");


    private StringBuilder sb=new StringBuilder();

    // 返回已经处理的内容
    public List<ESHeading> getHandledContent(){
        String[] splits = sb.toString().split(HeadingSplitLine);
        ArrayList<ESHeading> list = new ArrayList<>();

        for(int i=0;i<splits.length;i++){
            String split = splits[i];
            if("".equals(split)){
                continue;
            }
            ESHeading esHeading = new ESHeading();
            // 匹配出标题
            Matcher matcher = pattern.matcher(split);
            if (matcher.find()) {
                String group = matcher.group();
                esHeading.setHeadingName(group.substring(0,group.length()-1));//去掉回车换行
            }else{ //如果没找到标题，设置默认标题
                esHeading.setHeadingName("默认标题");
            }
            esHeading.setContent(split);
            esHeading.setBlogId(blogId);//设置标题所属的博客id
            esHeading.setHeadingId(headingIdList!=null?headingIdList.get(i-1):"");//设置标题id

            list.add(esHeading);
        }

        //置空标题id列表
        headingIdList=null;
        headingIndex=0;

        return list;//返回标题及内容的list
    }

    //需要将左右尖括号进行转义

    @Override
    public void visit(Text text) {
        sb.append(text.getLiteral());//添加普通文本内容
        sb.append("\n");
    }

    @Override
    public void visit(Heading heading) {
        if(headingIdList==null){
            headingIdList=new ArrayList<>();
        }

        //统一处理并获取标题，并返回标题名称（统一生成html和生成ES的标题是一致的）
        String text = HeadingRenderer.handleHeading(heading);
        if(text==null) return;

        sb.append(HeadingSplitLine);//添加内容分割线
        sb.append(text);//添加标题
        sb.append("\n");

        //设置headingId
        String headingId = UUID.randomUUID().toString();
        headingIdList.add(headingId);
    }

    @Override
    public void visit(FencedCodeBlock fencedCodeBlock) {
        sb.append(fencedCodeBlock.getLiteral());//添加代码块
        sb.append("\n");
    }

    @Override
    public void visit(Code code) {
        sb.append(code.getLiteral());//添加内联代码块
        sb.append("\n");
    }



}
