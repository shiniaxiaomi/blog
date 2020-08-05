<!doctype html>
<html lang="en">
<#include "../common/head.ftl">
<#include "../common/body.ftl">
<#include "../common/left.ftl">
<#include "../common/right.ftl">
<#include "../common/info.ftl">
<@head></@head>
<@body>
    <!--先设置为全部尺寸隐藏，然后再设置大于lg时显示-->
    <@left class="col-lg-3 d-none d-lg-block">
        <@info/>
    </@left>
    <!--全部尺寸都设置为自动-->
    <@right class="col-sm-12 col-md-10 col-lg-9 px-4">
        <form id="form" onsubmit="return false;">
            关键字：<input id="keyword" autocomplete="off" name="keyword" value="${keyword!}">
            标签：<input id="tagKeyword" autocomplete="off" name="tagKeyword">
            <button onclick="searchInES()">搜索</button>
        </form>
        <div id="result"></div>
    </@right>
</@body>
</html>

<script>

    function searchInES(){
        $.get("/blog/search",$("#form").serialize(),function (data,status) {
            if(status==="success" && data.code){
                let str="";
                for(let i=0;i<data.data.length;i++){
                    let result=data.data[i].sourceAsMap;
                    str+=`
                        <div>
                            <h5><a href="/blog/`+result.blogId+`#`+result.headingId+`">`+result.blogName+": "+result.headingName+`</a></h5>
                            `+result.content.replace(/</g, '&lt;').replace(/>/g, '&gt;')+`
                        </div>
                    `;
                }
                $("#result").html(str);
            }
        })
    }

    $(function () {
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


        searchInES();
    })
</script>