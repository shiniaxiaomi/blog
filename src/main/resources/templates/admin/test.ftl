<!doctype html>
<html lang="en">
<#include "../common/head.ftl">
<#include "../common/body.ftl">
<#include "../common/left.ftl">
<#include "../common/right.ftl">
<@head>
    <!-- vditor -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/vditor@3.4.1/dist/index.css" />
    <script src="https://cdn.jsdelivr.net/npm/vditor@3.4.1/dist/index.min.js" defer></script>
    <!-- layer -->
    <script src="https://cdn.jsdelivr.net/npm/layer-v3.0.3@1.0.1/layer.min.js"></script>
    <style>
        .fontRed{
            color: rgb(166, 0, 0);
            font-weight: bold;
        }
    </style>
</@head>
<@body>
    <@left class="col-2" style="max-width: 150px">
        <div id="toc"></div>
    </@left>
    <@right class="col-10" style="padding-left: 200px">
        <button onclick="hideAll()">隐藏所有</button>
        <button onclick="showAll()">显示所有</button>
        <div id="vditor" class="vditor-reset">
            jdskfjdskfjdkfjsk
        </div>
    </@right>
</@body>
</html>

<script>

    function hideAll(){
        let detail = $("#vditor detail");
        detail.css("display","none");
        detail.each(function(){
            if(!$(this).next().hasClass("tip")){
                $(this).after("<span class='tip'>隐藏了细节,点击标题展示细节</span>");
            }
        });
    }

    function showAll(){
        $("#vditor detail").css("display","inline");
        $("#vditor .tip").remove();
    }

    $(function () {
        Vditor.highlightRender({
            lineNumber:true
        },document.getElementById("vditor"));//渲染代码高亮
        Vditor.codeRender(document.getElementById("vditor"));//渲染代码复制按钮
        Vditor.outlineRender(document.getElementById("vditor"), document.getElementById("toc"));//生成toc目录

        //查找h1-h6,并添加点击隐藏或显示内容的功能
        $("#vditor :header").each(function(){
            $(this).click(function () {
                let next = $(this).next();
                next.toggle();//展示或隐藏内容
                // 显示提示字符
                if(next.css("display")==="none"){
                    next.after("<span class='tip'>隐藏了细节,点击标题展示细节</span>");
                }else if(next.css("display")==="inline"){
                    if(next.next().hasClass("tip")){
                        next.next().remove();
                    }
                }
            });
        });
    })
</script>
