<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="/css/bootstrap.min.css">

    <link rel="stylesheet" href="/css/blog.css">

    <style>
        .fontRed{
            color: rgb(166, 0, 0);
            font-weight: bold;
        }
    </style>

</head>
<body>

<#--引入顶部导航栏-->
<#include "../common/nav.ftl">
<#include "../common/record.ftl">
<@nav/>

<#--主体-->
<div class="container-fluid">
    <div class="row justify-content-center">
        <!--左（公共导航栏）-->
        <div class="col-2" style="max-width: 150px">
            <a class="d-block mb-2" href="/admin/blog">博客目录</a>
            <a class="d-block mb-2" href="/admin/tag">标签管理</a>
        </div>

        <!--中（blog）-->
        <div class="col-10" style="padding-left: 200px">

                <div class="custom-control custom-checkbox">
                    <input type="checkbox" class="custom-control-input" id="customCheck1">
                    <label class="custom-control-label" id="test" for="customCheck1">Check this custom checkbox</label>
                </div>

                <button onclick="checked()">勾选</button>
                <button onclick="cancel()">取消</button>


        </div>

        <#--备案信息-->
        <@record/>
    </div>
</div>

<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="/js/jquery.min.js"></script>
<script src="/js/popper.min.js"></script>
<script src="/js/bootstrap.min.js"></script>

<script src="/layer/layer.js"></script>

<script>
    function cancel(){
        $("#customCheck1").prop('checked', false)

    }

    function checked(){
        $("#customCheck1").prop('checked', true)
    }

    $(function () {

    })

</script>


</body>
</html>
