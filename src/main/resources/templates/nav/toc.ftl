<!doctype html>
<html lang="en">
<#include "../common/head.ftl">
<#include "../common/body.ftl">
<#include "../common/left.ftl">
<#include "../common/right.ftl">
<#include "../common/info.ftl">
<@head>
        <!--ztree-->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/ztree@3.5.24/css/zTreeStyle/zTreeStyle.css">
        <script src="https://cdn.jsdelivr.net/npm/ztree@3.5.24/js/jquery.ztree.all.min.js"></script>
        <script src="/js/catalog.js"></script>
</@head>
<@body active="目录">
    <@left class="col-lg-3 d-none d-lg-block" >
        <@info/>
    </@left>

    <@right class="col-sm-12 col-md-10 col-lg-9">
            <div class="form-inline">
                    <input class="mr-2" id="searchInput" autocomplete="off">
                <button type="button" class="btn btn-secondary btn-sm m-1" onclick="highlightSearch()">高亮搜索</button>
                <button type="button" class="btn btn-secondary btn-sm m-1" onclick="cancelHighlight()">取消高亮</button>
                <button type="button" class="btn btn-secondary btn-sm m-1" onclick="filterSearch()">过滤搜索</button>
                <button type="button" class="btn btn-secondary btn-sm m-1" onclick="cancelFilter()">取消过滤</button>
            </div>
            <div class="overflow-auto" style="height: 500px">
                    <ul id="treeDemo" class="ztree"></ul>
            </div>
    </@right>
</@body>
</html>

<script>
    $(function () {
        //开启提示工具
        $('[data-toggle="tooltip"]').tooltip();

        initTree(false); // 初始化目录
    })
</script>