<!doctype html>
<html lang="en">
<#include "../common/head.ftl">
<@head></@head>
<#include "../common/body.ftl">
<@body>
    <#--主体-->
    <div class="container-xl">
        <div class="row justify-content-center">
            <form id="form" class="w-75" onsubmit="return false;">
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
        </div>
    </div>
</@body>
</html>


<script>
    function test(){
        $.post("/regular/test",$("form").serialize(),function (data,status) {
            if(status==="success"){
                document.querySelector("#result").value=data;
            }
        })
    }
</script>
