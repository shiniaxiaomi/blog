let originalTagData;

function deleteTag() {
    // 只能勾选一个tag，然后再添加
    let checkedTags = $("#tags").serializeArray();
    if(checkedTags.length!==1){
        layer.msg("至少选择一个进行删除");
        return;
    }

    // 获取都选的id，进行删除
    layer.confirm('删除标签后会删除其关联数据，确定删除吗', {
        btn: ['确定','取消'] //按钮
    }, function(){
        $.post("/tag/delete",$("#tags").serialize(),function (data,status) {
            if(status==="success" && data.code){
                layer.msg("删除成功");
                initTags();
            }
        });
    });

}

function insertTag() {
    // 获取输入框的值，然后创建一个tag并插入
    let value=$("#searchTagInput").val();
    if(value===""){
        layer.msg("名称不能为空");
        return;
    }
    $.post("/tag/insert",{name:value},function (data,status) {
        if(status==="success" && data.code){
            layer.msg("添加成功");
            // 清空输入框
            $("#searchTagInput").val("");
            // 刷新tag
            initTags();
        }else{
            layer.msg("添加失败");
        }
    })
}

function updateTag() {
    // 只能勾选一个tag，然后再添加
    let checkedTags = $("#tags").serializeArray();
    if(checkedTags.length!==1){
        layer.msg("只能选择一个进行更改");
        return;
    }

    //弹出一个输入框，并回显数据
    layer.prompt({
        value: $("#label_"+checkedTags[0].value).text(),
        title: '标签名称',
    },function(val, index){
        $.post("/tag/update",{id:checkedTags[0].value,name:val},function (data,status) {
            if(status==="success" && data.code){
                // 回显数据
                $("#label_"+checkedTags[0].value).text(val);
                layer.msg("更改成功")
                initTags();
            }else{
                layer.msg("更改失败")
            }
        });
        layer.close(index);
    });
}

function openTag() {
    // 只能勾选一个tag，然后再添加
    let checkedTags = $("#tags").serializeArray();
    if(checkedTags.length!==1){
        layer.msg("至少选择一个进行打开");
        return;
    }

    window.location="/tag/"+checkedTags[0].value+"/1";
}

// 渲染tag
function initTags(){
    $.get("/tag",function (data,status) {
        if(status==="success" && data.code){
            originalTagData=data.data;
            let tagData=data.data;
            let str="";
            for(let i=0;i<tagData.length;i++){
                str+=`
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="checkbox" name="tags" id="`+tagData[i].id+`" value="`+tagData[i].id+`">
                    <label class="form-check-label" id="label_`+tagData[i].id+`" for="`+tagData[i].id+`">`+tagData[i].name+`</label>
                </div>
            `;
            }
            $("#tags").html(str);
        }
    });
}
// 根据标签进行渲染
function renderTags(tags) {
    // 在渲染前重置
    resetTags();
    // 渲染
    for(let i=0;i<tags.length;i++){
        $("#label_"+tags[i].id).addClass("fontRed");
    }
}
function resetTags() {
    for(let i=0;i<originalTagData.length;i++){
        $("#label_"+originalTagData[i].id).removeClass("fontRed");
    }
}
// 搜索标签
function searchTag(tags){
    let searchTagInput = $("#searchTagInput");
    let value=$(searchTagInput).val().toLocaleLowerCase();
    $(searchTagInput).val("");//清空输入框
    if(value===""){
        resetTags();// 重制标签样式
        return;
    }
    // 返回需要渲染的标签id
    let result = tags.filter(item=>{
        return item.name.toLocaleLowerCase().indexOf(value)!==-1;
    });
    renderTags(result);
}

$(function () {
    // 绑定搜索输入框的回车事件
    $("#searchTagInput").keypress(function(e) {
        if (e.keyCode === 13) {
            searchTag(originalTagData);
        }
    });

    initTags();
});