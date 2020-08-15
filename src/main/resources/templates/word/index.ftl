<!doctype html>
<html lang="en">
<#include "../common/head.ftl">
<#include "../common/body.ftl">
<#include "../common/right.ftl">
<@head>
    <script src="/layer/layer.js"></script>
</@head>
<@body>
    <@right>
        <ul class="nav nav-pills mb-3" id="pills-tab" role="tablist">
            <li class="nav-item">
                <a class="nav-link active" id="pills-home-tab" data-toggle="pill" href="#pills-home"
                   role="tab" aria-controls="pills-home" aria-selected="true">分析</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" id="pills-profile-tab" data-toggle="pill" href="#wordList"
                   role="tab" aria-controls="wordList" aria-selected="false">查看</a>
            </li>
        </ul>

        <div class="tab-content" id="pills-tabContent">
            <div class="tab-pane fade show active" id="pills-home" role="tabpanel" aria-labelledby="pills-home-tab">
                <#--表单提交-->
                <form id="form" class="was-validated" onsubmit="analyze();return false;">
                    <div class="form-row">
                        <label for="name" class="col-form-label">文件名称</label>
                        <input class="form-control is-valid" name="name" required>
                        <label for="content" class="col-form-label">解析内容</label>
                        <textarea name="content" class="form-control is-valid" required
                                  style="height: 300px"></textarea>
                    </div>

                    <div class="form-row justify-content-center mt-3">
                        <button class="btn btn-primary" type="submit">提交</button>
                    </div>
                </form>

            </div>
            <div id="wordList" class="tab-pane fade" role="tabpanel" aria-labelledby="pills-profile-tab">

            </div>
        </div>
    </@right>
</@body>
</html>

<script>
    function analyze() {
        $.ajax({
            type: "POST",//方法类型
            url: "/word/analyze" ,//url
            data: $('#form').serialize(),
            success: function (data) {
                if(data.code){
                    window.location.href="/word/detail/"+data.data;//请求单词详情页
                }
                layer.msg(data.msg)
            },
        });
    }

    function deleteWord(id){
        $.post("/word/delete",{id:id},function (data,status) {
            layer.msg(data.msg)
        })
    }

    $(function () {
        $.get("/word/list",function (data,status) {
            let str="";
            for(let i=0;i<data.length;i++){
                str+= `
                    <div class="row justify-content-between">
                        <a class="col-auto" href='/word/detail/`+data[i].id+`'>`+data[i].name+`</a>
                        <a class="col-auto" href='#' onclick='deleteWord(`+data[i].id+`)'>删除</a>
                    </div>
                `;
            }
            $("#wordList").html(str);
        })
    })
</script>
