package com.lyj.blog.parser.attr;

import org.commonmark.ext.task.list.items.TaskListItemMarker;
import org.commonmark.node.Node;
import org.commonmark.renderer.html.AttributeProvider;

import java.util.Map;

/**
 * @author Yingjie.Lu
 * @description 给todoList的li标签添加样式
 * @date 2020/8/1 12:37 下午
 */
public class TodoListAttributeProvider implements AttributeProvider { //AttributeProvider能够给节点添加额外的样式
    @Override
    public void setAttributes(Node node, String s, Map<String, String> map) {

        if(node.getFirstChild() instanceof TaskListItemMarker){
            map.put("class","vditor-task");
        }
    }
}
