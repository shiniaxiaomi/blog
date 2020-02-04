<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <title>更多博客 | 是你啊小米-陆英杰关注后端Java技术</title>
    <meta name="keywords" content="MoreBlog、Java、Java后端、JavaBlog、个人博客、技术博客、实用、陆英杰、是你啊小米"/>
    <meta name="description" content="陆英杰的个人博客,专注于Java后端技术的学习,记录和分享"/>
    <meta name="robots" content="all">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="/css/bootstrap.min.css">

    <#--blog-->
    <link rel="stylesheet" href="/css/blog.css">

</head>
<body>

<#--引入顶部导航栏-->
<#include "ftlTemplate/navTemplate.ftl">
<@header/>

<#--主体-->
<div class="container-xl">
    <div class="row">

        <#--左侧-->
        <div class="col-lg-9">
            <#--所有博客-->
            <div class="whiteBlock">
                <#include "ftlTemplate/blogListTemplate.ftl">
                <@blogList blogs=blogs tagColor="badge-warning">
                    <p class="text-muted" style="font-size: 20px;">所有博客</p>
                </@blogList>

                <#--分页-->
                <#include "ftlTemplate/pageTemplate.ftl">
                <@paging nowPage=nowPage blogSize=nowSize link="/moreBlog"/>
            </div>

        </div>

        <#--右侧-->
        <div class="col-lg-3 d-none d-lg-block" style="max-width: 300px;">
            <div class="sticky-top">
                <#--介绍信息-->
                <#include "ftlTemplate/introduceTemplate.ftl" >
                <@introduce/>
            </div>
        </div>
    </div>
</div>

<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="/js/jquery.min.js"></script>
<script src="/js/popper.min.js"></script>
<script src="/js/bootstrap.min.js"></script>
<!-- 弹窗 -->
<script src="/js/pop.js"></script>

<#--引入登入模板(该模板需要刚在jquery加载之后的body标签内)-->
<#include  "ftlTemplate/loginTemplate.ftl">

<script>
    $(function () {
        //开启提示工具
        $('[data-toggle="tooltip"]').tooltip();

    })
</script>


</body>
</html>