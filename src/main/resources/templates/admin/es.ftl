<!doctype html>
<html lang="en">
<#include "../common/head.ftl">
<#include "../common/body.ftl">
<#include "../common/left.ftl">
<#include "../common/right.ftl">
<#include "../common/sidebar.ftl">
<#include "../common/page.ftl">
<@head>
    <script src="/layer/layer.js"></script>
    <link href="https://cdn.bootcdn.net/ajax/libs/jquery-jsonview/1.2.3/jquery.jsonview.min.css" rel="stylesheet">
    <script src="https://cdn.bootcdn.net/ajax/libs/jquery-jsonview/1.2.3/jquery.jsonview.min.js"></script>
    <style>
        textarea{
            padding: 0.75em;
            display: block;
            -webkit-box-sizing: border-box;
            box-sizing: border-box;
            width: 100%;
            height: 500px;
            min-height: 5.125em;
            border-radius: 5px;
            border: 1px solid rgba(0,0,0,0.1);
            font-size: 0.875em;
            word-wrap: break-word;
            resize: vertical;
            background-color: #f6f6f6;
            outline: none;
            -webkit-transition: all 0.25s ease;
            transition: all 0.25s ease;
        }
    </style>
</@head>
<@body>
    <@left class="col-md-1 col-lg-2" style="max-width: 150px">
        <@sidebar></@sidebar>
    </@left>
    <@right class="col-md-11 col-lg-10">
            <div class="overflow-auto">
                <a class="m-2" href="javascript:void(0);" onclick="setValue('/_cat/indices?format=JSON','get')">查询所有index</a>
                <a class="m-2" href="javascript:void(0);" onclick="setValue('/blog?format=JSON','get')">查询blog字段</a>
                <a class="m-2" href="javascript:void(0);" onclick="queryAllData()">分页查询所有内容</a>
                <a class="m-2" href="javascript:void(0);" onclick="compoundQueries()">混合查询</a>
                <form id="form" onsubmit="return false;">
                    <textarea id="searchParam" name="json" style="height: 350px"></textarea>
                    <select id="method" name="method">
                        <option id="get" value ="get">get</option>
                        <option id="post" value ="post">post</option>
                    </select>
                    <button onclick="search();">搜索</button>
                    <button onclick="$('#searchParam').val('')">清空</button>
                    <button onclick="beautifyClick()">格式化</button>
                </form>
            </div>
            搜索结果:
            <div id="searchResult"></div>
    </@right>
</@body>
</html>

<script>

    // 混合查询
    function compoundQueries() {
        setValue(`
        {
          "query": {
            "bool" : {
              "must" : {
                "match" : { "blogName" : "开发记录" }
              },
              "filter": {
                "match" : { "content" : "的" }
              },
              "must_not" : {
                "match" : { "blogId" : "149" }
              },
              "should" : {
                "match" : { "headingName" : "正则" }
              },
              "boost" : 1.0
            }
          }
        }
        `,"post");//match是模糊匹配，term是必须精准匹配
    }

    // 分页查询所有数据
    function queryAllData() {
        setValue(`
        {
            "from" : 0,
            "size" : 10,
            "query": {
                "match_all": {  }
            }
        }
        `,"post");
    }

    // 设置内容
    function setValue(json,method) {
        let $method = $("#method");
        let $searchParam = $("#searchParam");

        // 设置方法
        $method.val(method);
        
        if($method.val()==="post"){
            let beautifyJsonData = beautifyJson(json);
            if(beautifyJsonData!==undefined){
                $searchParam.val(beautifyJsonData)
            }
        }else{
            $searchParam.val(json);
        }
    }

    // 美化json
    function beautifyJson(json) {
        // 校验json格式是否正确
        try {
            let parse = JSON.parse(json);
            return JSON.stringify(parse, null, 4);//4个空格
        } catch(e) {
            layer.msg("json格式不正确");
            return undefined;
        }
    }

    // 格式化按钮点击
    function beautifyClick() {
        setValue($("#searchParam").val(),"post");
    }

    // 查询
    function search(){
        // 校验json格式是否正确
        if($("#method").val()==="post"){
            try {
                JSON.parse($("#searchParam").val());
            } catch(e) {
                layer.msg("json格式不正确");
                return;
            }
        }

        $.post("/es/search",$("#form").serialize(),function (data,status) {
            if(status==="success" && data.code){
                $("#searchResult").JSONView(data.data);
            }else{
                console.log(data.msg);
                layer.msg(data.msg);
            }
        })
    }

    $(function () {
        $("#searchParam").on('keydown', function (e) {
            if (e.keyCode == 9) {
                e.preventDefault();
                let indent = '    ';
                let start = this.selectionStart;
                let end = this.selectionEnd;
                let selected = window.getSelection().toString();
                selected = indent + selected.replace(/\n/g, '\n' + indent);
                this.value = this.value.substring(0, start) + selected
                    + this.value.substring(end);
                this.setSelectionRange(start + indent.length, start
                    + selected.length);
            }
        })
    })
</script>



