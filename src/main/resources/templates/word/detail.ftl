<!doctype html>
<html lang="en">
<#include "../common/head.ftl">
<#include "../common/body.ftl">
<#include "../common/right.ftl">
<@head>
    <script src="https://cdn.jsdelivr.net/npm/layer-v3.0.3@1.0.1/layer.min.js"></script>
</@head>
<@body>
    <@right>
        <form id="form" onsubmit="save();return false;">
            <button class="btn btn-primary" type="submit">提交</button>
            <input hidden name="id" value="${id!0}">
            <div id="content"></div>
        </form>
    </@right>
</@body>
</html>

<script>
    // 保存
    function save(){
        $.ajax({
            type: "POST",//方法类型
            url: "/word/saveIndex" ,//url
            data: $('#form').serialize(),
            success: function(data) {
                layer.msg(data.msg);
            },
        });
    }

    $(function () {

        $.get("/word/content/${id!0}",function (data,status) {
            let parse = JSON.parse(data.data.context);
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
            let index = JSON.parse(data.data.index);
            if(index!==null){
                for(let i=0;i<index.length;i++){
                    let checkedIndex = index[i];
                    $("#"+checkedIndex).attr("checked","checked");
                }
            }
        })

    })

</script>
