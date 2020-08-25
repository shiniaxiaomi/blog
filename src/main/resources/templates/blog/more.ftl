<!doctype html>
<html lang="en">
<#include "../common/head.ftl">
<#include "../common/body.ftl">
<#include "../common/left.ftl">
<#include "../common/right.ftl">
<#include "../common/info.ftl">
<#include "../common/item.ftl">
<#include "../common/page.ftl">
<@head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/vditor@3.4.1/dist/index.css"/>
    <style>
        .vditor-reset {
            font-size: 10px;
            padding: 10px
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

    <@right class="col-sm-12 col-md-10 col-lg-9">
        <div class="bg-white px-4 pt-4 shadow-lg rounded">
            <div>
                <p class="h3">${title!}</p>
                <hr/>
                <@item moreBlogList/>
            </div>
        </div>
        <!--分页组件-->
        <@page/>
    </@right>
</@body>
</html>

<script>
    $(function () {
        //开启提示工具
        $('[data-toggle="tooltip"]').tooltip();
    })
</script>