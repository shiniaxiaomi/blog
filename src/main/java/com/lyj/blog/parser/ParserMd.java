package com.lyj.blog.parser;

import com.lyj.blog.model.Blog;
import com.lyj.blog.model.Tag;
import com.lyj.blog.parser.model.EsHeading;
import com.lyj.blog.service.EsService;
import com.lyj.blog.service.TagService;
import org.commonmark.node.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;

/**
 * 解析md文档的核心类
 */
@Component
public class ParserMd extends Parser{

    @Autowired
    EsService esService;

    @Autowired
    TagService tagService;

    // 制造EsHeading
    public EsHeading createHeading(Blog blog,String content,String headingName){
        EsHeading esHeading = new EsHeading();
        esHeading.setBlogId(blog.getId());
        esHeading.setBlogName(blog.getName());
        esHeading.setPrivate(blog.getIsPrivate());
        esHeading.setContent(content);
        esHeading.setTagName(blog.getTagNames());
        esHeading.setHeadingName( // 如果没有headingName，则使用blogName
                headingName==null||headingName.equals("")?"# "+blog.getName():headingName);
        return esHeading;
    }

    @Override
    protected List<EsHeading> buildEsHeading(StringBuilder sb, List<String> headingIdList, Blog blog) {
        ArrayList<EsHeading> list = new ArrayList<>();
        String[] splits = sb.toString().split(HeadingSplitLine);

        // 如果长度为1，可能为空白，可能没有标题
        if(splits.length==1){
            if("".equals(splits[0])){
                return Collections.emptyList();
            }else{
                EsHeading heading = createHeading(blog, splits[0], null);
                list.add(heading);
                return list;
            }
        }

        // 如果第一个没有标题
        int i=0;
        if(!headingPattern.matcher(splits[0]).find()){
            EsHeading heading = createHeading(blog, splits[0], null);
            list.add(heading);
            i=1;// 如果第一个没有标题，则下面从第二个开始处理
        }

        for(;i<splits.length;i++){
            String split = splits[i];
            EsHeading heading = createHeading(blog, "", null);
            // 匹配并设置标题
            Matcher matcher = headingPattern.matcher(split);
            if (matcher.find()) {
                String group = matcher.group();
                heading.setHeadingName(group.substring(0, group.length() - 1));//去掉回车换行
            }
            // 设置标题uuid
            heading.setHeadingId(headingIdList!=null?headingIdList.get(i-1):"");
            // 设置内容（去掉标题部分）
            String[] arr = split.split("^#+ .*\n");
            if (arr.length == 2) {
                heading.setContent(arr[1]);
            } else if (arr.length == 1) {
                heading.setContent(arr[0]);
            }
            list.add(heading);
        }

        //组装blogName和tagName
        if(list.size()!=0){
            list.forEach(esHeading -> {
                esHeading.setBlogName(blog.getName());
                esHeading.setTagName(blog.getTagNames());// 组装tagName
            });
        }
        return list;//返回标题及内容的list
    }

    @Override
    protected void insertHeadingToEs(List<String> headingIdList, List<EsHeading> esHeadings,Blog blog) {
        //清除es中blogId对应的数据
        esService.deleteHeadingByBlogIdInES("blog",String.valueOf(blog.getId()));//根据blogId字段进行删除数据
        if(esHeadings.size()==0){
            return;
        }
        //批量保存到ES中
        esService.insertHeadingToESBatch("blog",esHeadings);
    }

    @Override
    public String parseMdToHtml(String md) {
        //解析md，转换为语法树
        Node document = parser.parse(md==null?"":md);
        //添加visitor，用于生成标题及标题内容
        document.accept(headingContentVisitor);
        //渲染语法树，生成html
        return renderer.render(document);
    }
}
