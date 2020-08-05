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
</@head>
<@body>
    <@left class="col-4" style="max-width: 300px">
        <!--公共导航栏-->
        <div class="sticky-top" style="top: 100px;z-index: 1000">
            <div class=" input-group-sm">
                <input id="header-search" class="form-control w-100 mb-1" type="text">
            </div>
            <div id="toc" class="overflow-auto" style="height: 545px;font-size: 14px"></div>
        </div>
    </@left>
    <@right class="col-8 px-5">
<#--        <button onclick="hideAll()">隐藏所有</button>-->
<#--        <button onclick="showAll()">显示所有</button>-->
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
            $(this).append("<span style='font-size: 15px'><a href='/admin/${blogId!}#"+$(this).attr("text")+"'> 编辑 </a></span>");
            $(this).prepend($(this)[0].tagName+": ");
        });
    })

    // function hideAll(){
    //     let detail = $("#vditor detail");
    //     detail.css("display","none");
    //     detail.each(function(){
    //         if(!$(this).next().hasClass("tip")){
    //             $(this).after("<span class='tip'>隐藏了细节,点击标题展示细节</span>");
    //         }
    //     });
    // }
    //
    // function showAll(){
    //     $("#vditor detail").css("display","inline");
    //     $("#vditor .tip").remove();
    // }
    //
    //查找h1-h6,并添加点击隐藏或显示内容的功能
    // $("#vditor :header").each(function(){
    //     $(this).click(function () {
    //         let next = $(this).next();
    //         next.toggle();//展示或隐藏内容
    //         // 显示提示字符
    //         if(next.css("display")==="none"){
    //             next.after("<span class='tip'>隐藏了细节,点击标题展示细节</span>");
    //         }else if(next.css("display")==="inline"){
    //             if(next.next().hasClass("tip")){
    //                 next.next().remove();
    //             }
    //         }
    //     });
    // });
</script>