<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="/css/bootstrap.min.css">

</head>
<body>

<#--引入顶部导航栏-->
<#include "common/nav.ftl">
<@nav/>

<#--主体-->
<div class="container-xl">

    <div class="row justify-content-center">
        <form id="form" onsubmit="save();return false;">
            <button class="btn btn-primary" type="submit">提交</button>
            <input hidden name="id" value="${id!0}">
            <div id="content"></div>
        </form>
    </div>

    <#--备案号-->
    <div class="m-3 text-center">
        <a href="http://www.beian.miit.gov.cn" class="text-muted text-decoration-none">ICP证 : 浙ICP备18021271号</a>
    </div>
</div>

<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="/js/jquery.min.js"></script>
<script src="/js/popper.min.js"></script>
<script src="/js/bootstrap.min.js"></script>

<script src="/layer/layer.js"></script>


<script>


    // 保存
    function save(){
        $.ajax({
            type: "POST",//方法类型
            url: "/word/saveIndex" ,//url
            data: $('#form').serialize(),
            success: function(data) {
                console.log(data)
                layer.msg('保存成功');
            },
            error: function(request) {
                console.log(request)
                alert("Connection error");
            }
        });
    }

    $(function () {

        $.get("/word/content/${id!0}",function (data,status) {
            console.log(data)
            let parse = JSON.parse(data.context);
            console.log(parse)
            let str="";
            for(let i=0;i<parse.length;i++){
                let element = parse[i];
                let split = element.split(",");
                let word=split[0];
                let translate=split[1];
                let count=split[2];
                str+=`
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" name="index" value="`+i+`" id="`+i+`">
                        <label class="form-check-label" for="`+i+`">`+word+`:`+translate+`---`+count+`</label>
                        <a target="_blank" href="https://fanyi.baidu.com/#en/zh/`+word+`">查询详细</a>
                    </div>
                `;
            }
            $("#content").html(str);

            // 设置已经勾选的
            let index = JSON.parse(data.index);
            if(index!==null){
                for(let i=0;i<index.length;i++){
                    let checkedIndex = index[i];
                    $("#"+checkedIndex).attr("checked","checked");
                }
            }
        })

    })

</script>


</body>
</html>
