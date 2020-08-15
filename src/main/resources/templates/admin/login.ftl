<!doctype html>
<html lang="en">
<#include "../common/head.ftl">
<#include "../common/body.ftl">
<#include "../common/right.ftl">
<@head>
    <script src="/layer/layer.js"></script>
</@head>
<@body>
    <@right class="">
        <#if isLogin!=true>
            <form id="form" onsubmit="login();return false;">
                <div class="form-group">
                    <label for="username">用户名</label>
                    <input name="username" id="username" autocomplete="off" type="text" class="form-control">
                </div>
                <div class="form-group">
                    <label for="password">密码</label>
                    <input  name="password" id="password" autocomplete="off" type="password" class="form-control">
                </div>
                <div style="text-align: center">
                    <button type="submit" class="btn btn-primary mb-2 mx-2">登入</button>
                    <button type="reset" onclick="history.back()" class="btn btn-primary mb-2 mx-2">返回</button>
                </div>
            </form>
        <#else>
            已登入<br>
            <button class="btn btn-primary mb-2 mx-2" onclick="exitLogin()">退出登入</button>
        </#if>

    </@right>
</@body>
</html>


<script>

    function loadUserName() {
        let username = window.localStorage.getItem("loginUserName");
        if(username!==undefined){
            $("#username").val(username);
        }
    }

    // 登入
    function login() {
        if($("#username").val()===""){
            layer.msg("用户名不能为空",{time:1000});
            return;
        }
        if($("#password").val()===""){
            layer.msg("密码不能为空",{time:1000});
            return;
        }
        $.post("/user/login",$("#form").serialize(),function (data,status) {
            if(status==="success" && data.code){
                // 缓存用户名
                window.localStorage.setItem("loginUserName",$("#username").val());
                // 1s后返回原始页面
                setTimeout(function () {
                    history.back();
                },500)
            }
            layer.msg(data.msg)
        })
    }

    // 退出登入
    function exitLogin(){
        $.get("/user/exitLogin",function (data,status) {
            if(status==="success" && data.code){
                // 1s后返回原始页面
                setTimeout(function () {
                    window.location.href="/"
                },500)
            }
            layer.msg(data.msg)
        })
    }

    $(function () {
        loadUserName();
    });

</script>
