<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <#include "ftlTemplate/SEOTemplate.ftl">
    <@title>线上博客草稿</@title>
    <@keywords>线上博客草稿,Draft</@keywords>
    <@description></@description>

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="/static/css/bootstrap.min.css">

    <#--blog-->
    <link rel="stylesheet" href="/static/css/blog.css">

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

            <#--本地草稿(不管是否登入都显示)-->
            <div class="whiteBlock">
                <div>
                    <p class="text-muted d-inline" style="font-size: 20px;">所有本地草稿</p>
                </div>
                <div id="localDraft"></div>

                <#--分页-->
                <#include "ftlTemplate/pageTemplate.ftl">
                <@paging nowPage=nowPage blogSize=nowSize link="/moreLocalDraft"/>
            </div>

        </div>

        <#--右侧-->
        <div class="col-lg-3 d-none d-lg-block">
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
<script src="/static/js/jquery.min.js"></script>
<script src="/static/js/popper.min.js"></script>
<script src="/static/js/bootstrap.min.js"></script>
<!-- 弹窗 -->
<script src="/static/js/pop.js"></script>

<#--引入登入模板(该模板需要刚在jquery加载之后的body标签内)-->
<#include  "ftlTemplate/loginTemplate.ftl">

<#--本地草稿-->
<script src="/static/js/websql.js"></script>
<script src="/static/js/LocalDraft.js"></script>
<script>
    $(function () {
        //开启提示工具
        $('[data-toggle="tooltip"]').tooltip();

        buildLoaclDraftHtml(${nowPage!"1"});

    })
</script>


</body>
</html>