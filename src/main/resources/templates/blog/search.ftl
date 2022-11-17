<!doctype html>
<html lang="en">
<#include "../common/head.ftl">
<#include "../common/body.ftl">
<#include "../common/left.ftl">
<#include "../common/right.ftl">
<#include "../common/info.ftl">
<@head>
    <link rel="stylesheet" href="/vditor/index.css" />
    <script src="/vditor/js/lute/lute.min.js"></script>
    <script src="/layer/layer.min.js"></script>
    <style>
        .vditor-reset {
            font-size: 10px;
        }
        .vditor-reset p {
            margin-bottom: 0px;
        }
        a{
            word-break: break-all;/*换行，防止溢出*/
        }
        /* 去掉样式 */
        .h1, h1, .h2, h2, .h3, h3, .h4, h4, .h5, h5, .h6, h6 {
            font-size: revert;
        }
        code {
            color: black;
        }
    </style>
</@head>
<@body>
    <!--先设置为全部尺寸隐藏，然后再设置大于lg时显示-->
    <@left class="col-lg-3 d-none d-lg-block">
        <@info/>
    </@left>
    <!--全部尺寸都设置为自动-->
    <@right class="col-sm-12 col-md-10 col-lg-9 px-4">
        <form class="form-inline mb-3" id="form" onsubmit="return false;">
            <div class="form-group input-group input-group-sm mr-3">
                关键字
                <input type="text" class="form-control" id="keyword" autocomplete="off" name="keyword" value="${keyword!}">
            </div>
            <div class="form-group input-group input-group-sm mr-3">
                标签
                <input type="text" class="form-control" id="tagKeyword" autocomplete="off" name="tagKeyword">
            </div>
            <button class="btn btn-primary btn-sm" onclick="searchBlog()">搜索</button>
        </form>
        <div id="result"></div>
        <!--分页组件-->
        <nav aria-label="Page navigation example">
            <ul id="page" class="pagination justify-content-center" style="flex-flow: row wrap;"></ul>
        </nav>
    </@right>
</@body>
</html>

<script>

    let buffArr=[];

    // 直接在html高亮关键字
    function highlightHtml(html,keywords) {
        // 高亮内容
        for(let j=0;j<keywords.length;j++){
            // 正则表达式添加[^->], 为了避免替换掉html的>和后续生成的language-java等标识
            // 正则表达式添加{0}, 为了避免多替换掉一个字符
            html=html.replace(new RegExp("[^->]{0}"+keywords[j],"gi"),"<span style='color: red'>"+keywords[j]+"</span>");
        }
        return html;
    }

    // content: 传入的要转换的md内容
    function HandlerMd2Html(mdContent,keywords) {
        // 将markdown解析成html
        let md2HTML = lute.Md2HTML(mdContent);
        // 高亮内容
        for(let j=0;j<keywords.length;j++){
            // 正则表达式添加[^->], 为了避免替换掉html的>和后续生成的language-java等标识
            // 正则表达式添加{0}, 为了避免多替换掉一个字符
            md2HTML=md2HTML.replace(new RegExp("[^->]{0}"+keywords[j],"gi"),"<span style='color: red'>"+keywords[j]+"</span>");
        }
        return md2HTML;
    }

    function searchBlog(page){
        if(page===undefined) page=1;
        buffArr=[];//清空缓存数据
        console.log($("#form").serialize())
        $.get("/blog/search/"+page,$("#form").serialize(),function (data,status) {
            if(status==="success" && data.code){
                let resultList = data.data.records;
                if(resultList.length===0){
                    layer.msg("没有相应的结果");
                    return;
                }
                let keywords = $("#keyword").val().match(/[\u4e00-\u9fa5]+|\w+/g);
                if(keywords===undefined) keywords=[];
                // 构造搜索结果
                let str="";
                for(let i=0;i<resultList.length;i++){
                    let result=resultList[i];
                    let md2HTML="";
                    // 通过md渲染成html后高亮
                    // if(result.md.length>400){
                    //     // 缓存被隐藏的原始内容
                    //     buffArr.push({id:i,content:HandlerMd2Html(result.md,keywords)});
                    //     // 显示被截取的内容
                    //     md2HTML=HandlerMd2Html(result.md.substring(0,400),keywords);
                    //     md2HTML+="...<br>";
                    //     md2HTML+=`<button type="button" class="btn btn-outline-secondary btn-sm" onclick="showMore(`+i+`)">显示更多</button>`;
                    // }else{
                    //     md2HTML=HandlerMd2Html(result.md,keywords);
                    // }
                    // 直接在html中高亮
                    if(result.mdHtml.length>400){
                        // 缓存被隐藏的原始内容
                        buffArr.push({id:i,content:highlightHtml(result.mdHtml,keywords)});
                        // 显示被截取的内容
                        md2HTML=highlightHtml(result.mdHtml.substring(0,400),keywords);
                        md2HTML+="...<br>";
                        md2HTML+=`<button type="button" class="btn btn-outline-secondary btn-sm" onclick="showMore(`+i+`)">显示更多</button>`;
                    }else{
                        md2HTML=highlightHtml(result.mdHtml,keywords);
                    }
                    // 高亮标题
                    let highlightHeading=result.name;
                    for(let j=0;j<keywords.length;j++){
                        highlightHeading=highlightHeading.replace(new RegExp(keywords[j],"gi"),"<span style='color: red'>"+keywords[j]+"</span>");
                    }
                    str+=`
                        <div>
                            <h1><a href="/blog/`+result.id+`">`+highlightHeading+`</a>
                            <span style="font-size: 12px">🔖`+result.tagNames+`</span>
                            </h1>
                            <div class="vditor-reset" id="`+i+`">`+md2HTML+`</div>
                        </div><hr>
                    `;
                }
                $("#result").html(str);

                // 构造分页组件
                str="";
                let json = data.data;
                let previousPage=json.current-1<1?json.pages-1:json.current-1;
                let nextPage=json.current+1>json.pages-1?1:json.current+1;
                str+=`
                    <li class="page-item">
                        <a class="page-link" href="javascript:void(0)" onclick="searchBlog(`+previousPage+`)" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                            <span class="sr-only">Previous</span>
                        </a>
                    </li>
                `;
                for(let i=1;i<json.pages;i++){
                    if(json.current===i){
                        str+=`
                            <li class="page-item active"><a class="page-link" href="javascript:void(0)" onclick="searchBlog(`+i+`)">`+i+`</a></li>
                        `;
                    }else{
                        str+=`
                            <li class="page-item"><a class="page-link" href="javascript:void(0)" onclick="searchBlog(`+i+`)">`+i+`</a></li>
                        `;
                    }
                }
                str+=`
                    <li class="page-item">
                        <a class="page-link" href="javascript:void(0)" onclick="searchBlog(`+nextPage+`)" aria-label="Next">
                            <span aria-hidden="true">&raquo;</span>
                            <span class="sr-only">Next</span>
                        </a>
                    </li>
                    <li class="page-item disabled">
                        <a class="page-link" href="javascript:void(0)">
                            <span aria-hidden="true">总计：`+json.total+`</span>
                        </a>
                    </li>
                `;
                $("#page").html(str);
            }
        })
    }

    // 显示更多
    function showMore(id){
        for(let i=0;i<buffArr.length;i++){
            if(buffArr[i].id===id){
                $("#"+id).html(buffArr[i].content);
            }
        }
    }

    let lute;

    $(function () {
        lute=Lute.New();

        //开启提示工具
        $('[data-toggle="tooltip"]').tooltip();

        $("#searchInput").keypress(function(e) {
            if (e.keyCode === 13) {
                searchBlog();
            }
        });
        $("#contentKeyword").keypress(function(e) {
            if (e.keyCode === 13) {
                searchBlog();
            }
        });
        $("#tagKeyword").keypress(function(e) {
            if (e.keyCode === 13) {
                searchBlog();
            }
        });

        searchBlog();//默认搜索第一页
    })
</script>