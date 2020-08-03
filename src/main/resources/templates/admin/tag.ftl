<!doctype html>
<html lang="en">
<#include "../common/head.ftl">
<@head>
    <script src="/layer/layer.js"></script>
    <script src="/js/tag.js"></script>
</@head>
<#include "../common/body.ftl">
<@body>
    <#--主体-->
    <div class="container-fluid">
        <div class="row justify-content-center">
            <!--左（公共导航栏）-->
            <div class="col-2" style="max-width: 150px">
                <a class="d-block mb-2" href="/admin/blog">博客目录</a>
                <a class="d-block mb-2" href="/admin/tag">标签管理</a>
            </div>

            <!--中（blog）-->
            <div class="col-10" style="padding-left: 100px">
                <div>
                    <input id="searchTagInput" autocomplete="off">
                    <div>
                        <button type="button" class="btn btn-secondary btn-sm mr-1" onclick="insertTag()">添加</button>
                        <button type="button" class="btn btn-secondary btn-sm m-1" onclick="deleteTag()">删除</button>
                        <button type="button" class="btn btn-secondary btn-sm m-1" onclick="updateTag()">更改</button>
                        <button type="button" class="btn btn-secondary btn-sm m-1" onclick="searchTag(originalTagData)">搜索</button>
                    </div>
                </div>
                <form id="tags" class="mt-3"></form>
            </div>
        </div>
    </div>
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



