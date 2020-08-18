<!doctype html>
<html lang="en">
<#include "../common/head.ftl">
<#include "../common/body.ftl">
<#include "../common/left.ftl">
<#include "../common/right.ftl">
<#include "../common/info.ftl">
<#include "../common/item.ftl">
<@head>
    <link rel="stylesheet" href="/vditor/index.css"/>

</@head>
<@body>
    <@left class="col-lg-3 d-none d-lg-block" >
        <@info/>
    </@left>

    <@right class="col-sm-12 col-md-10 col-lg-9">
        <div style="text-align: center;" class="mb-5 w-100">
            <img class="img-fluid" src="/img/404.png" style="width: 60%"><br>
            <h5 class="mt-2 mb-4">抱歉，你访问的页面不存在</h5>
            <button type="button" class="btn btn-outline-danger" onclick="window.location.href='/'">返回首页</button>
        </div>
    </@right>
</@body>
</html>
