<!doctype html>
<html lang="en">
<#include "../common/head.ftl">
<#include "../common/body.ftl">
<#include "../common/left.ftl">
<#include "../common/right.ftl">
<#include "../common/info.ftl">
<@head>
    <script src="https://cdn.jsdelivr.net/npm/layer-v3.0.3@1.0.1/layer.min.js"></script>
    <script src="/js/tag.js"></script>
</@head>
<@body active="标签">
    <@left class="col-lg-3 d-none d-lg-block" >
        <@info/>
    </@left>

    <@right class="col-sm-12 col-md-10 col-lg-9">
        <div>
            <input id="searchTagInput" autocomplete="off">
            <div>
<#--                <button type="button" class="btn btn-secondary btn-sm mr-1" onclick="insertTag()">添加</button>-->
<#--                <button type="button" class="btn btn-secondary btn-sm m-1" onclick="deleteTag()">删除</button>-->
<#--                <button type="button" class="btn btn-secondary btn-sm m-1" onclick="updateTag()">更改</button>-->
                <button type="button" class="btn btn-secondary btn-sm m-1" onclick="searchTag(originalTagData)">搜索</button>
                <button type="button" class="btn btn-secondary btn-sm m-1" onclick="openTag()">打开</button>
            </div>
        </div>
        <form id="tags" class="mt-3"></form>
    </@right>
</@body>
</html>

<script>
    $(function () {
        //开启提示工具
        $('[data-toggle="tooltip"]').tooltip();

        initTags(); // 初始化标签
    })
</script>