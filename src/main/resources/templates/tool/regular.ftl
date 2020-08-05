<!doctype html>
<html lang="en">
<#include "../common/head.ftl">
<#include "../common/body.ftl">
<#include "../common/right.ftl">
<@head></@head>
<@body>
    <@right>
        <form id="form" onsubmit="return false;">
            <div class="form-group">
                <label for="formGroupExampleInput">内容</label>
                <textarea name="content" type="text" class="form-control" style="height: 150px"></textarea>
            </div>
            <div class="form-group">
                <label for="formGroupExampleInput2">正则</label>
                <input name="regular" autocomplete="off" type="text" class="form-control" id="formGroupExampleInput2" placeholder="">
            </div>
            <div class="form-group">
                <button onclick="test()">测试</button>
            </div>
            <div class="form-group">
                <label for="formGroupExampleInput2">结果</label>
                <textarea id="result" type="text" class="form-control" style="height: 150px"></textarea>
            </div>
        </form>
    </@right>
</@body>
</html>


<script>
    function test(){
        $.post("/tool/regular",$("form").serialize(),function (data,status) {
            if(status==="success"){
                document.querySelector("#result").value=data;
            }
        })
    }
</script>
