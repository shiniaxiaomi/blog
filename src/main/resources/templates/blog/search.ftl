<!doctype html>
<html lang="en">
<#include "../common/head.ftl">
<#include "../common/body.ftl">
<#include "../common/left.ftl">
<#include "../common/right.ftl">
<#include "../common/info.ftl">
<@head>
    <script src="https://cdn.jsdelivr.net/npm/vditor@3.3.5/dist/js/lute/lute.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/layer-v3.0.3@1.0.1/layer.min.js"></script>
    <style>
        .vditor-reset {
            font-size: 10px;
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
            <button class="btn btn-primary btn-sm" onclick="searchInES()">搜索</button>
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

    function searchInES(page){
        if(page===undefined) page=1;
        buffArr=[];//清空缓存数据
        $.get("/blog/search/"+page,$("#form").serialize(),function (data,status) {
            if(status==="success" && data.code){
                if(data.data.result.length===0){
                    layer.msg("没有相应的结果");
                    return;
                }
                let splits = $("#keyword").val().match(/[\u4e00-\u9fa5]+|\w+/g);
                if(splits==null) splits=[];
                // 构造搜索结果
                let str="";
                for(let i=0;i<data.data.result.length;i++){
                    let result=data.data.result[i].sourceAsMap;
                    let md2HTML="";
                    // 如果内容不为空
                    if(result.content!=null){
                        // 过滤特殊符号
                        md2HTML = lute.Md2HTML(result.content.replace(/\</g,"&lt;").replace(/\>/g,"&gt;"));
                        // 高亮内容
                        for(let j=0;j<splits.length;j++){
                            md2HTML=md2HTML.replace(new RegExp(splits[j],"gi"),"<span style='color: red'>"+splits[j]+"</span>");
                        }
                        if(md2HTML.length>400){
                            buffArr.push({id:i,content:md2HTML});//缓存更多的显示内容
                            md2HTML = md2HTML.substring(0,400);
                            md2HTML+="<br>...<br>";
                            md2HTML+=`<button type="button" class="btn btn-outline-secondary btn-sm" onclick="showMore(`+i+`)">显示更多</button>`;
                        }
                    }
                    // 高亮标题
                    let highlightHeading=result.headingName;
                    for(let j=0;j<splits.length;j++){
                        highlightHeading=highlightHeading.replace(new RegExp(splits[j],"gi"),"<span style='color: red'>"+splits[j]+"</span>");
                    }
                    str+=`
                        <div>
                            <h5><a href="/blog/`+result.blogId+`#`+result.headingId+`">`+highlightHeading+`</a>
                            <span style="font-size: 12px">📒`+result.blogName+`</span>
                            <span style="font-size: 12px">🔖`+result.tagName+`</span>
                            </h5>
                            <div class="vditor-reset" id="`+i+`">`+md2HTML+`</div>
                        </div><hr>
                    `;
                }
                $("#result").html(str);

                // 构造分页组件
                str="";
                let json = data.data;
                let previousPage=json.currentPage-1<1?json.pages-1:json.currentPage-1;
                let nextPage=json.currentPage+1>json.pages-1?1:json.currentPage+1;
                str+=`
                    <li class="page-item">
                        <a class="page-link" href="javascript:void(0)" onclick="searchInES(`+previousPage+`)" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                            <span class="sr-only">Previous</span>
                        </a>
                    </li>
                `;
                for(let i=1;i<json.pages;i++){
                    if(json.currentPage===i){
                        str+=`
                            <li class="page-item active"><a class="page-link" href="javascript:void(0)" onclick="searchInES(`+i+`)">`+i+`</a></li>
                        `;
                    }else{
                        str+=`
                            <li class="page-item"><a class="page-link" href="javascript:void(0)" onclick="searchInES(`+i+`)">`+i+`</a></li>
                        `;
                    }
                }
                str+=`
                    <li class="page-item">
                        <a class="page-link" href="javascript:void(0)" onclick="searchInES(`+nextPage+`)" aria-label="Next">
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
                searchInES();
            }
        });
        $("#contentKeyword").keypress(function(e) {
            if (e.keyCode === 13) {
                searchInES();
            }
        });
        $("#tagKeyword").keypress(function(e) {
            if (e.keyCode === 13) {
                searchInES();
            }
        });


        searchInES();//默认搜索第一页
    })
</script>