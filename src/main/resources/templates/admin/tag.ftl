<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="/css/bootstrap.min.css">

    <link rel="stylesheet" href="/css/blog.css">

</head>
<body>

<#--引入顶部导航栏-->
<#include "../common/nav.ftl">
<#include "../common/record.ftl">
<@nav/>

<#--主体-->
<div class="container-xl">
    <div class="row justify-content-center">
        <!--左（公共导航栏）-->
        <div class="col-2" style="max-width: 150px">
            <a class="d-block mb-2" href="/admin/blog">博客目录</a>
            <a class="d-block mb-2" href="/admin/tag">标签管理</a>
        </div>

        <!--中（blog）-->
        <div class="col-10">
            <div>
                <input>
                <button>搜索</button>
                <button>删除</button>
                <button>添加</button>
            </div>
            <div>
                <span class="badge badge-secondary">java</span>
                <span class="badge badge-secondary">test</span>
                <span class="badge badge-secondary">linux</span>
            </div>
        </div>

        <#--备案信息-->
        <@record/>
    </div>
</div>

<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="/js/jquery.min.js"></script>
<script src="/js/popper.min.js"></script>
<script src="/js/bootstrap.min.js"></script>

<script>

    $(function () {

    })

</script>


</body>
</html>
