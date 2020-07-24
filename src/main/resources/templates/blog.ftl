<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <link rel="stylesheet" href="/css/blog.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/vditor@3.3.2/dist/js/highlight.js/styles/github.css">


    <#include "common/headerCommon.ftl">
    <@header/>

    <style>
        /*保证toc左右能够显示全*/
        /*.vditor-outline__item{*/
        /*    overflow: inherit;*/
        /*}*/

        /*面包屑背景颜色*/
        .breadcrumb {
            background-color: #f8f9fa;
        }

        .vditor-outline__item{
            font-size: 80%;
        }
        .vditor-outline__item:hover {
            color: #007bff;
        }
        a{
            color: #3b86d8;
        }
    </style>
</head>
<body>

    <#--引入顶部导航栏-->
    <#include "common/navCommon.ftl">
    <@nav/>

    <div class="container-fluid" style="margin-top:85px;">

        <div class="row justify-content-center">
            <!--左（目录）-->
            <div class="col-md-3 d-none d-md-block" style="max-width: 250px;">
                <div id="tocContainer" class="sticky-top overflow-auto shadow-lg p-3 rounded" style="top: 95px;margin-left:10px;max-height: 590px">
<#--                    目录<hr style="margin: 10px 0">-->
                    <div id="toc" style="margin-left: -35px;"></div>
                </div>
            </div>

            <!--右（blog）-->
            <div class="col col-md-8" style="padding: 0 30px"><!--全部尺寸都设置为自动-->
                <!--面包屑导航-->
                <nav aria-label="breadcrumb">
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item">
                            <a href="javascript:void(0)" onclick="window.history.back()">返回</a>|<a href="/blog/edit?id=${id!}">编辑</a>|<a href="/">首页</a>
                        </li>
                        <li class="breadcrumb-item active" aria-current="page">
                            jdsfjdksfjds
                        </li>
                    </ol>

                </nav>
                <div id="vditor" class="vditor-reset">${mdHtml!""}</div>
            </div>

        </div>

    </div>

</body>

<#include "common/beianCommon.ftl">
<@beian/>


</html>

<script>

    // 当从编辑页面返回时，刷新页面
    if(window.localStorage.getItem("needReload") === "true"){
        window.localStorage.setItem("needReload","false");
        window.location.reload();
    }

    $(function () {
        var buf=document.getElementById("vditor");
        // Vditor.highlightRender({},buf);//渲染代码高亮
        // Vditor.codeRender(buf);//渲染代码复制
        Vditor.outlineRender(buf,document.getElementById("toc"));//渲染大纲到指定dom
        if($("#toc").html()===""){
            $("#toc").html("没有目录");
            $("#toc").css("margin-left",0);
        }

        //查找h1-h6
        $("#vditor :header").each(function(){
            let str="<a href='/blog/edit?id=${id!}#wysiwyg-"+$(this).attr("id")+"'>"+$(this).html()+"</a>";
            $(this).html(str);
        });
    })

</script>
