<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="/css/bootstrap.min.css">

    <!-- ⚠️生产环境请指定版本号，如 https://cdn.jsdelivr.net/npm/vditor@x.x.x/dist... -->
    <link rel="stylesheet" href="/vditor/index.css" />
    <#--    <script src="/vditor/index.min.js" defer></script>-->
    <script src="/vditor/js/method.min.js"></script>

    <link rel="stylesheet" href="/css/blog.css">

    <style>
        /*保证toc左右能够显示全*/
        /*.vditor-outline__item{*/
        /*    overflow: inherit;*/
        /*}*/

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
                    目录<hr style="margin: 10px 0">
                    <div id="toc" style="margin-left: -30px;"></div>
                </div>
            </div>
            <!--右（blog）-->
            <div class="col col-md-8" style="padding: 0 40px"><!--全部尺寸都设置为自动-->
                <div id="vditor" class="vditor-reset">${mdHtml!""}</div>
            </div>

        </div>

    </div>

</body>

<#include "common/beianCommon.ftl">
<@beian/>


</html>

<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="/js/jquery.min.js"></script>
<script src="/js/bootstrap.min.js"></script>
<script src="/layer/layer.js"></script>


<script>

    // 当从编辑页面返回时，刷新页面
    if(window.localStorage.getItem("needReload") === "true"){
        window.localStorage.setItem("needReload","false");
        window.location.reload();
    }

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
    }

    $(function () {

        var buf=document.getElementById("vditor");
        Vditor.highlightRender({},buf);//渲染代码高亮
        Vditor.codeRender(buf);//渲染代码复制
        Vditor.outlineRender(buf,document.getElementById("toc"));//渲染大纲到指定dom


        //查找h1-h6
        $(":header").each(function(){
            let str="<a href='/blog/edit?id=${id!}#wysiwyg-"+$(this).attr("id")+"' style='font-size: 16px'>编辑</a>";
            $(this).append(str);
        });
    })

</script>
