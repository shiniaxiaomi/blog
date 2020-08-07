<!doctype html>
<html lang="en">
<#include "../common/head.ftl">
<#include "../common/body.ftl">
<#include "../common/left.ftl">
<#include "../common/right.ftl">
<@head>
    <!-- vditor -->
    <link rel="stylesheet" href="/vditor/index.css" />
    <script src="/vditor/index.min.js" defer></script>
    <!-- layer -->
    <script src="/layer/layer.js"></script>
    <style>
        .fontRed{
            color: rgb(166, 0, 0);
            font-weight: bold;
        }
        .vditor-outline__item:hover{
            color: #2e72e2;
        }
        .hljs{
            background-color: #e9ecefa8 !important;
        }
    </style>
    <script>
        // 当从编辑页面返回时，刷新页面
        if(window.localStorage.getItem("needReload") === "true"){
            window.localStorage.setItem("needReload","false");
            setTimeout(function () {
                window.location.reload();
            },100);
        }
    </script>
</@head>
<@body style="max-width: 1300px">
    <@left class="col-4" style="max-width: 300px">
        <!--公共导航栏-->
        <div class="sticky-top" style="top: 100px;z-index: 1000">
            <div class=" input-group-sm">
                <input id="header-search" class="form-control w-100 mb-1" type="text">
            </div>
            <div id="toc" class="overflow-auto" style="height: 545px;font-size: 14px"></div>
        </div>
    </@left>
    <@right class="col-8 px-5" style="border-left: 2px solid #e9ecef">
        <!--面包屑导航-->
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item">
                    <a href="/">首页</a>|<a href="/admin/blog/${blogId!}">编辑</a>
                </li>
                <li class="breadcrumb-item active" aria-current="page">${blogName!}</li>
            </ol>
        </nav>
        <!--文章内容-->
        <div id="vditor" class="vditor-reset">
            ${html!}
        </div>
    </@right>
</@body>
</html>

<script>

    // 搜索heading
    function searchHeading(){
        let header = $("#header-search");
        let val = header.val();
        if(val===""){
            //恢复
            $("#toc div").each(function () {
                $(this).show();
            });
        }else{
            //搜索
            $("#toc div").each(function () {
                if($(this).text().toLowerCase().indexOf(val.toLowerCase())===-1){
                    $(this).hide();
                }
            });
            //清空输入框
            header.val("");
        }
    }

    $(function () {
        Vditor.highlightRender({lineNumber:true},document.getElementById("vditor"));//渲染代码高亮
        Vditor.codeRender(document.getElementById("vditor"));//渲染代码复制按钮
        Vditor.outlineRender(document.getElementById("vditor"), document.getElementById("toc"));//生成toc目录

        // 绑定回车搜索heading事件
        $("#header-search").keypress(function(e) {
            if (e.keyCode === 13) {
                searchHeading();
            }
        });

        setTimeout(function () {
            if(location.hash!==""){
                $("#toc div[data-id^="+location.hash.substr(1)+"]").click();//刷新页面锚点
            }
        },200);

        //查找h1-h6,添加点击编辑的按钮
        $("#vditor :header").each(function(){
            $(this).append("<span style='font-size: 15px'><a href='/admin/blog/${blogId!}#"+$(this).attr("text")+"'> 编辑 </a></span>");
            $(this).prepend($(this)[0].tagName+": ");
        });
    })
</script>