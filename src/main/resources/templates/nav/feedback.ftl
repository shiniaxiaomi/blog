<!doctype html>
<html lang="en">
<#include "../common/head.ftl">
<#include "../common/body.ftl">
<#include "../common/left.ftl">
<#include "../common/right.ftl">
<#include "../common/info.ftl">
<#include "../common/item.ftl">
<@head>
    <script src="/layer/layer.js"></script>
    <style>
        .gt-header-textarea {
            padding: 0.75em;
            display: block;
            -webkit-box-sizing: border-box;
            box-sizing: border-box;
            width: 100%;
            min-height: 5.125em;
            max-height: 15em;
            border-radius: 5px;
            border: 1px solid rgba(0, 0, 0, 0.1);
            font-size: 0.875em;
            word-wrap: break-word;
            resize: vertical;
            background-color: #f6f6f6;
            outline: none;
            -webkit-transition: all 0.25s ease;
            transition: all 0.25s ease;
        }

        .gt-header-input {
            padding: 0.3em 0.5em;
            border-radius: 5px;
            border: 1px solid rgba(0, 0, 0, 0.1);
            font-size: 0.875em;
            background-color: #f6f6f6;
            outline: none;
            margin-bottom: 10px;
            margin-right: 5px;
            width: 100%;
        }
    </style>
</@head>
<@body>
    <@left class="col-lg-3 d-none d-lg-block" >
        <@info/>
    </@left>

    <@right class="col-sm-12 col-md-10 col-lg-9">
        <div class="p-4 mb-5 shadow-lg bg-white rounded">
            <form id="form" onsubmit="commit();return false;">
                <div class="form-group row">
                    <label for="staticEmail" class="col-sm-2 col-form-label">邮箱</label>
                    <div class="col-sm-10">
                        <input type="text" autocomplete="off" class="form-control gt-header-input"
                               id="email" name="email" placeholder="用于接收回复（选填）">
                    </div>
                </div>
                <div class="form-group row">
                    <label for="inputPassword" class="col-sm-2 col-form-label">反馈</label>
                    <div class="col-sm-10">
                         <textarea id="content" name="content" class="gt-header-textarea form-control "
                                   placeholder="说点什么" style="overflow-wrap: break-word; resize: none; height: 300px;"></textarea>
                        <small class="text-muted ml-2">可以是使用过程中出现的问题，改进建议 等等</small>
                    </div>
                </div>
            </form>
            <div style="text-align: center" class="mt-4">
                <button type="button" class="btn btn-primary m-1" onclick="commit()">提交</button>
                <button type="button" class="btn btn-primary m-1" onclick="history.back()">取消</button>
            </div>
        </div>
    </@right>
</@body>
</html>

<script>
    function commit() {
        let email = $("#email").val();
        let content = $("#content").val();

        if(email!==""){
            //校验email是否合法
            let reg = /^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,5}$/;
            if(!reg.test(email)){
                layer.msg("邮箱格式不正确");
                return;
            }
        }

        if(content===""){
            layer.msg("反馈内容不能为空");
            return;
        }

        // 发送请求
        $.post("/feedback",$("#form").serialize(),function (data,status) {
            if(status==="success" && data.code){
                layer.msg(data.msg+",感谢您的反馈！");
                setTimeout(function () {
                    window.location.href="/";
                },1000)
            }else{
                layer.msg("发生错误，请联系管理员");
            }
        })
    }
    $(function () {
        //开启提示工具
        $('[data-toggle="tooltip"]').tooltip();
    })
</script>