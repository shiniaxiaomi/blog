<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <link rel="stylesheet" href="/css/blog.css">

    <#include "common/headerCommon.ftl">
    <@header/>

    <style>
        .list-group-item {
            padding: 6px 0px;
            cursor: pointer;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }
    </style>

</head>
<body>

<#--引入顶部导航栏-->
<#include "common/navCommon.ftl">
<@nav/>

<div class="container" style="margin-top:95px;max-width: 1300px">
    <div id="test" class="row justify-content-center">

        <!--左（简介）-->
        <div class="col-lg-3 d-none d-lg-block" style="max-width: 255px"><!--先设置为全部尺寸隐藏，然后再设置大于lg时显示-->
            <div class="sticky-top whiteBlock shadow-lg p-3 mb-5 bg-white rounded" style="top: 95px">
                <#--引入介绍信息-->
                <#include "common/infoCommon.ftl">
                <@info/>
            </div>
        </div>

        <!--中（blog）-->
        <div class="col-sm-10 col-lg-7"><!--全部尺寸都设置为自动-->
            <div class="whiteBlock shadow-lg p-3 mb-5 bg-white rounded">
                <div class="text-muted d-inline" style="font-size: 20px;">最新博客</div>
                <hr>

                <#include "common/blogItem.ftl" />
                <!--每篇置顶博客-->
                <#if blogs??>
                    <#list blogs as blog>
                        <@blogItem blog=blog/>
                    </#list>
                </#if>
            </div>
        </div>

        <!--右（目录）-->
        <div class="col-md-2 d-none d-md-block" style="max-width: 120px"><!--先设置为全部尺寸隐藏，然后再设置大于sm时显示-->
            <div class=" sticky-top whiteBlock shadow-lg mb-5 bg-white rounded" style="top: 95px;padding: 10px 11px 3px 11px">
                <ul class="list-group list-group-flush" style="text-align: center">
                    <a class="list-group-item" style="font-size: 80%" href="#">计算机基础</a>
                    <a class="list-group-item" style="font-size: 80%" href="#">mybatis</a>
                    <a class="list-group-item" style="font-size: 80%" href="#">待整理</a>
                    <a class="list-group-item" style="font-size: 80%" href="#">测试</a>
                </ul>
            </div>
        </div>

    </div>
</div>


<#include "common/beianCommon.ftl">
<@beian/>


</body>
</html>



<script>
    $(function () {
        //开启提示工具
        $('[data-toggle="tooltip"]').tooltip();

        console.log("!1111")

    })



    window.onresize =function resizeFresh() {
        if(window.innerWidth>=1200){
            console.log("xl");
        }else if(window.innerWidth>=992){
            console.log("lg");
        }else if(window.innerWidth>=768){
            console.log("md");
        }else if(window.innerWidth>=576){
            console.log("sm");
        }else{
            console.log("col");
        }
        var buf=window.innerWidth/12;
        console.log("列占比",buf,buf*8,buf*3);
        var arrs=$("#test").children();
        console.log("实际占比",$(arrs[0]).innerWidth(),$(arrs[1]).innerWidth(),$(arrs[2]).innerWidth())
    }



</script>