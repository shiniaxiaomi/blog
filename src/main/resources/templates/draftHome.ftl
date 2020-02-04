<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="/css/bootstrap.min.css">

    <title>草稿 | 是你啊小米-陆英杰关注后端Java技术</title>

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

            <#--线上草稿(如果登入则显示)-->
            <#if isLogin==true>
                <div class="whiteBlock">
                    <#include "ftlTemplate/blogListTemplate.ftl">
                    <@blogList blogs=draftBlogs tagColor="badge-warning">
                        <p class="text-muted d-inline" style="font-size: 20px;">线上草稿</p>
                        <a class="text-muted" style="float: right" href="/moreDraft">更多</a>
                    </@blogList>
                </div>
            </#if>

            <#--本地草稿(不管是否登入都显示)-->
            <div class="whiteBlock">
                <div>
                    <p class="text-muted d-inline" style="font-size: 20px;">本地草稿</p>
                    <a class="text-muted" style="float: right" href="/moreLocalDraft">更多</a>
                </div>
                <div id="localDraft"></div>
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
<#--本地草稿-->
<script src="/js/websql.js"></script>
<script src="/js/LocalDraft.js"></script>

<#--引入登入模板(该模板需要刚在jquery加载之后的body标签内)-->
<#include  "ftlTemplate/loginTemplate.ftl">

<script>
    $(function () {
        //开启提示工具
        $('[data-toggle="tooltip"]').tooltip();

        buildLoaclDraftHtml(${nowPage});

    })

    //删除blog或者localDraft
    function deleteFunc(type,id){
        pop.confirm("是否要删除",function () {
            if(type=="blog"){
                $.post("/deleteBlog?blogId="+id,function (data, status) {
                    pop.prompt(data.data);
                    if(data.code==200){
                        setTimeout(function () {
                            window.location.reload();//刷新页面
                        },1000)
                    }
                })
            }
            else if(type=="localDraft"){
                //删除本地的草稿
                deleteDraftByName(id);
                //刷新页面
                pop.prompt("删除成功");
                setTimeout(function () {
                    window.location.reload();
                },1000)
            }
        })

    }
</script>


</body>
</html>