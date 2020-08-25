<!doctype html>
<html lang="en">
<#include "../common/head.ftl">
<#include "../common/body.ftl">
<#include "../common/left.ftl">
<#include "../common/right.ftl">
<#include "../common/sidebar.ftl">
<#include "../common/page.ftl">
<@head>
    <script src="https://cdn.jsdelivr.net/npm/layer-v3.0.3@1.0.1/layer.min.js"></script>
    <script src="/js/tag.js"></script>
</@head>
<@body>
    <@left class="col-md-1 col-lg-2" style="max-width: 150px">
        <@sidebar></@sidebar>
    </@left>
    <@right class="col-md-11 col-lg-10">
        <p class="h3">${title!}</p>
        <a href="javascript:void(0);" onclick="back(${blogId!})">返回</a>
        <table class="table table-hover">
            <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">文件名称</th>
                    <th scope="col">文件类型</th>
                    <th scope="col">预览</th>
                    <th scope="col">引用数</th>
                    <th scope="col">操作</th>
                </tr>
            </thead>
            <tbody>
                <#list fileList as file>
                    <tr id="tr_${file_index+1}">
                        <th scope="row">${file_index+1}</th>
                        <td>${file.name!}</td>
                        <td><#if file.type == 0>img<#else>file</#if></td>
                        <td>预览</td>
                        <td>${file.count!}</td>
                        <td><button type="button" class="btn btn-primary btn-sm" onclick="deleteFile(${file.id!},${file_index+1})">删除</button></td>
                    </tr>
                </#list>
            </tbody>
        </table>
        <!--分页组件-->
        <@page></@page>
    </@right>
</@body>
</html>

<script>

    // 删除引用和文件
    function deleteFile(fileId,tableIndex) {
        $.post("/file/delete",{id:fileId},function (data,status) {
            if(status==="success" && data.code){
                layer.msg(data.msg);
                // 删除掉这一行记录
                $("#tr_"+tableIndex).eq(0).remove();
                // 总数-1
                let span = $("#totalSpan").eq(0);
                span.text(span.text()-1);
            }else{
                layer.msg("删除失败");
            }
        })
    }

    function back(blogId){
        if(blogId===undefined){
            window.location="/admin/blog";
        }else{
            window.location="/admin/blog/"+blogId;
        }
    }

    $(function () {

    })
</script>



