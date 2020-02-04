<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="/css/bootstrap.min.css">

    <title>所有线上草稿 | 是你啊小米-陆英杰关注后端Java技术</title>

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

<#--本地草稿-->
<script src="/js/websql.js"></script>
<script src="/js/LocalDraft.js"></script>
<script>
    $(function () {
        //开启提示工具
        $('[data-toggle="tooltip"]').tooltip();

        buildLoaclDraftHtml(${nowPage!"1"});

    })
</script>


</body>
</html>