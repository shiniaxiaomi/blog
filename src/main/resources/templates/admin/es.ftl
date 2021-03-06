<!doctype html>
<html lang="en">
<#include "../common/head.ftl">
<#include "../common/body.ftl">
<#include "../common/left.ftl">
<#include "../common/right.ftl">
<#include "../common/sidebar.ftl">
<#include "../common/page.ftl">
<@head>
    <script src="https://cdn.jsdelivr.net/npm/layer-v3.0.3@1.0.1/layer.min.js"></script>
    <link href="/jsonview/jquery.jsonview.min.css" rel="stylesheet">
    <script src="/jsonview/jquery.jsonview.min.js"></script>
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
        .jsonview .string {
            word-break: break-all; /*换行*/
        }
    </style>
</@head>
<@body>
    <@left class="col-md-1 col-lg-2" style="max-width: 150px">
        <@sidebar></@sidebar>
    </@left>
    <@right class="col-md-11 col-lg-10">
            <div class="overflow-auto">
                <a class="mr-2" href="javascript:void(0);" onclick="setValue('/_cat/indices?format=JSON','get')">查询所有index</a>
                <a class="m-2" href="javascript:void(0);" onclick="setValue('/blog?format=JSON','get')">查询blog字段</a>
                <a class="m-2" href="javascript:void(0);" onclick="deleteAllData()">删除所有数据</a>
                <a class="m-2" href="javascript:void(0);" onclick="deleteDataByBlogId()">删除数据根据blogId</a>
                <a class="m-2" href="javascript:void(0);" onclick="updateDataByBlogId()">更新数据根据BlogId</a>
                <a class="m-2" href="javascript:void(0);" onclick="queryAllData()">分页查询所有内容</a>
                <a class="m-2" href="javascript:void(0);" onclick="compoundQueries()">混合查询</a>
                <a class="m-2" href="javascript:void(0);" onclick="searchHeadingName()">按照标题搜索</a>
                <a class="m-2" href="javascript:void(0);" onclick="searchBlogName()">按照博客名称搜索</a>
                <form id="form" onsubmit="return false;">
                    <textarea id="searchParam" name="json" style="height: 350px"></textarea>
                    <select id="method" name="method">
                        <option id="get" value ="get">get</option>
                        <option id="post" value ="post">post</option>
                    </select>
                    <button onclick="search();">搜索</button>
                    <button onclick="update();">更新</button>
                    <button onclick="deleteBtn();">删除</button>
                    <button onclick="$('#searchParam').val('')">清空</button>
                    <button onclick="beautifyClick()">格式化</button>
                    <button onclick="serializeClick()">序列化</button> |
                    <button onclick="initEsData()">初始化es数据</button>
                </form>
            </div>
            搜索结果:
            <div id="searchResult"></div>
    </@right>
</@body>
</html>

<script>

    // 询问框工具方法
    function layerConfirm(msg,func) {
        let index = layer.confirm(msg, {
            btn: ['确定','取消'] //按钮
        }, function(){
            func();
            layer.close(index); //如果设定了yes回调，需进行手工关闭
        }, function(){
            layer.close(index); //如果设定了yes回调，需进行手工关闭
        });
    }

    // 初始化es中的数据
    function initEsData() {
        layerConfirm("是否需要将es数据初始化？",function () {
            let layerMsg = layer.msg("数据正在初始化，请稍后...",{time:0});
            $.get("/admin/initEsData",function (data,status) {
                layer.close(layerMsg);
                if(status==="success" && data.code){
                    layer.msg("数据初始化成功");
                }else{
                    layer.msg("数据初始化失败");
                }
            })
        });
    }

    // 更新指定数据
    function updateDataByBlogId() {
        // 将blogId为130的公有数据修改为私有数据
        setValue(`
        {
          "script": {
            "source": "ctx._source.isPrivate=true",
            "lang": "painless"
          },
          "query": {
            "term": {
              "blogId": "130"
            }
          }
        }
        `,"post");
    }

    // 删除所有数据
    function deleteAllData() {
        setValue(`
        {
          "query": {
            "match_all": {  }
          }
        }
        `,"post");
    }

    // 删除指定的数据
    function deleteDataByBlogId() {
        // 删除blogId为130的数据
        setValue(`
        {
          "query": {
            "term": {
              "blogId": "130"
            }
          }
        }
        `,"post");
    }

    // 将json串序列化成一个行字符串
    function serializeClick() {
        // 校验json格式是否正确
        let $searchParam = $("#searchParam");
        try {
            let parse = JSON.parse($searchParam.val());
            $searchParam.val(JSON.stringify(parse))
        } catch(e) {
            layer.msg("json格式不正确");
            return undefined;
        }
    }

    // 按照标题名称搜索
    function searchHeadingName() {
        setValue(`
        {
          "query": {
            "bool" : {
              "should" : {
                "match" : { "headingName" : "正则" }
              },
              "boost" : 1.0
            }
          }
        }
        `,"post");//match是模糊匹配，term是必须精准匹配
    }

    // 按照博客名称搜索
    function searchBlogName() {
        setValue(`
        {
          "query": {
            "bool" : {
              "should" : {
                "match" : { "blogName" : "正则" }
              },
              "boost" : 1.0
            }
          }
        }
        `,"post");//match是模糊匹配，term是必须精准匹配
    }

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

    // 删除事件
    function deleteBtn() {
        // 校验json格式是否正确
        if($("#method").val()==="post"){
            try {
                JSON.parse($("#searchParam").val());
            } catch(e) {
                layer.msg("json格式不正确");
                return;
            }
        }

        $.post("/es/delete",$("#form").serialize(),function (data,status) {
            if(status==="success" && data.code){
                $("#searchResult").JSONView(data.data);
            }else{
                console.log(data.msg);
            }
            layer.msg(data.msg);
        })
    }

    // 更新
    function update() {
        // 校验json格式是否正确
        if($("#method").val()==="post"){
            try {
                JSON.parse($("#searchParam").val());
            } catch(e) {
                layer.msg("json格式不正确");
                return;
            }
        }

        $.post("/es/update",$("#form").serialize(),function (data,status) {
            if(status==="success" && data.code){
                $("#searchResult").JSONView(data.data);
            }else{
                console.log(data.msg);
            }
            layer.msg(data.msg);
        })
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



