package com.lyj.blog.parser.visitor;

import com.lyj.blog.parser.model.ESHeading;
import com.lyj.blog.parser.render.HeadingRenderer;
import org.commonmark.node.*;

import java.util.*;
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

    Pattern headingPattern = Pattern.compile("^#+ .*\n");//用于匹配标题


    private StringBuilder sb=new StringBuilder();

    // 返回已经处理的内容
    public List<ESHeading> getHandledContent(){
        String[] splits = sb.toString().split(HeadingSplitLine);
        ArrayList<ESHeading> list = new ArrayList<>();

        // 如果长度为1，可能为空白，可能没有标题
        if(splits.length==1){
            if("".equals(splits[0])){
                return Collections.emptyList();
            }else{
                ESHeading esHeading = new ESHeading();
                esHeading.setBlogId(blogId);
                esHeading.setContent(splits[0]);
                esHeading.setHeadingName("# 默认标题");
                list.add(esHeading);
                return list;
            }
        }

        // 如果第一个没有标题
        int i=0;
        if(!headingPattern.matcher(splits[0]).find()){
            ESHeading esHeading = new ESHeading();
            esHeading.setBlogId(blogId);
            esHeading.setContent(splits[0]);
            esHeading.setHeadingName("# 默认标题");
            list.add(esHeading);
            i=1;// 如果第一个没有标题，则下面从第二个开始处理
        }

        try {
            for(;i<splits.length;i++){
                String split = splits[i];

                ESHeading esHeading = new ESHeading();
                // 匹配并设置标题
                Matcher matcher = headingPattern.matcher(split);
                if (matcher.find()) {
                    String group = matcher.group();
                    esHeading.setHeadingName(group.substring(0, group.length() - 1));//去掉回车换行
                } else { //如果没找到标题，设置默认标题
                    esHeading.setHeadingName("# 默认标题");
                }
                // 设置标题uuid
                esHeading.setHeadingId(headingIdList!=null?headingIdList.get(i-1):"");
                // 设置内容（去掉标题部分）
                String[] arr = split.split("^#+ .*\n");
                if (arr.length == 2) {
                    esHeading.setContent(arr[1]);
                } else if (arr.length == 1) {
                    esHeading.setContent(arr[0]);
                }
                // 设置标题所属的博客id
                esHeading.setBlogId(blogId);
                list.add(esHeading);
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            //置空标题id列表(保证在抛出异常后，还能置空标题列表和index)
            headingIdList=null;
            headingIndex=0;
        }

        return list;//返回标题及内容的list
    }

    // 需要将左右尖括号进行转义(普通内容不会出现# 的情况，因为如果出现，那么就称为标题了)
    @Override
    public void visit(Text text) {
        sb.append(text.getLiteral());//添加普通文本内容
        sb.append("\n");
    }

    // 标题
    @Override
    public void visit(Heading heading) {
        if(headingIdList==null){
            headingIdList=new ArrayList<>();
        }

        //统一处理并获取标题，并返回标题名称（统一生成html和生成ES的标题是一致的）
        String text = HeadingRenderer.handleHeadingForES(heading);
        if(text==null) return;

        sb.append(HeadingSplitLine);//添加内容分割线
        sb.append(text);//添加标题
        sb.append("\n");

        //设置headingId
        headingIdList.add(UUID.randomUUID().toString());
    }

    // 代码块
    @Override
    public void visit(FencedCodeBlock fencedCodeBlock) {
        sb.append(fencedCodeBlock.getLiteral().replaceAll("#",""));//添加代码块，并去掉#
        sb.append("\n");
    }

    // 内联代码块
    @Override
    public void visit(Code code) {
        sb.append(code.getLiteral().replaceAll("#",""));//添加内联代码块，并去掉#
        sb.append("\n");
    }



}
