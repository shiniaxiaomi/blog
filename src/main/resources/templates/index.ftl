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
<#include "common/nav.ftl">
<#--引入介绍信息-->
<#include "common/info.ftl">

<@nav/>

<#--主体-->
<div class="container-xl">
    <div class="row justify-content-center">
        <!--左（简介）-->
        <!--先设置为全部尺寸隐藏，然后再设置大于lg时显示-->
        <div class="col-lg-3 d-none d-lg-block">
            <@info/>
        </div>

        <!--中（blog）-->
        <!--全部尺寸都设置为自动-->
        <div class="col-sm-12 col-md-10 col-lg-9">

        </div>
    </div>

    <#--备案号-->
    <div style="text-align: center;margin: 10px;">
        <a href="http://www.beian.miit.gov.cn" style="color: rgb(118, 118, 118); text-decoration: none;">
            ICP证 : 浙ICP备18021271号
        </a>
    </div>
</div>

<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="/js/jquery.min.js"></script>
<script src="/js/popper.min.js"></script>
<script src="/js/bootstrap.min.js"></script>

<script>

    $(function () {
        //开启提示工具
        $('[data-toggle="tooltip"]').tooltip();
    })

</script>


</body>
</html>
