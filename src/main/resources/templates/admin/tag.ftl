<!doctype html>
<html lang="en">
<#include "../common/head.ftl">
<#include "../common/body.ftl">
<#include "../common/left.ftl">
<#include "../common/right.ftl">
<#include "../common/sidebar.ftl">
<@head>
    <script src="/layer/layer.js"></script>
    <script src="/js/tag.js"></script>
</@head>
<@body>
    <@left class="col-2" style="max-width: 150px">
        <@sidebar></@sidebar>
    </@left>
    <@right class="col-10" style="padding-left: 100px">
        <div>
            <input id="searchTagInput" autocomplete="off">
            <div>
                <button type="button" class="btn btn-secondary btn-sm mr-1" onclick="insertTag()">添加</button>
                <button type="button" class="btn btn-secondary btn-sm m-1" onclick="deleteTag()">删除</button>
                <button type="button" class="btn btn-secondary btn-sm m-1" onclick="updateTag()">更改</button>
                <button type="button" class="btn btn-secondary btn-sm m-1" onclick="openTag()">打开</button>
                <button type="button" class="btn btn-secondary btn-sm m-1" onclick="searchTag(originalTagData)">搜索</button>
            </div>
        </div>
        <form id="tags" class="mt-3"></form>
    </@right>
</@body>
</html>

<script>
    $(function () {
        // // 绑定搜索输入框的回车事件
        // $("#searchInput").keypress(function(e) {
        //     if (e.keyCode === 13) {
        //         searchTag(originalTagData);
        //     }
        // });
        //
        // initTags();
    })
</script>



