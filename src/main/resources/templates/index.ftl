<!doctype html>
<html lang="en">
<#include "common/head.ftl">
<#include "common/body.ftl">
<#include "common/left.ftl">
<#include "common/right.ftl">
<#include "common/info.ftl">
<#include "common/item.ftl">
<@head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/vditor@3.3.5/dist/index.css"/>
    <style>
        .vditor-reset {
            font-size: 13px;
            padding: 10px
        }
        .vditor-reset hr{
            margin: 4px 0 !important;
        }
        .vditor-reset p, .vditor-reset pre {
            margin: 0 !important;
            color: #6c757d!important;
        }
        .vditor-reset a{
            color: #6c757d;
            text-decoration:underline;
        }
        .vditor-reset a:hover,.tagLink:hover{
            color: #007bff;
        }
        .tagLink{
            color: #212529;
        }
        .moreLink{
            color: #6c757d;
            margin-top: 10px;
        }
        .moreLink:hover{
            color: #007bff;
        }
    </style>
</@head>
<@body>
    <@left class="col-lg-3 d-none d-lg-block" >
        <@info/>
    </@left>

    <@right class="col-md-12 col-lg-9">
        <div class="bg-white px-4 pt-4 shadow-lg rounded">
            <div>
                <p class="h3">置顶博客<span class="float-right h6"><a class="moreLink" href="/blog/stick/1">更多</a></span></p>
                <hr/>
                <@item stickBlogList/>
            </div>
        </div>
        <div class="bg-white px-4 pt-4 shadow-lg rounded">
            <div>
                <p class="h3">最新博客<span class="float-right h6"><a class="moreLink" href="/blog/newest/1">更多</a></span></p>
                <hr/>
                <@item newestBlogList/>
            </div>
        </div>
    </@right>
</@body>
</html>

<script>
    $(function () {
        //开启提示工具
        $('[data-toggle="tooltip"]').tooltip();
    })
</script>