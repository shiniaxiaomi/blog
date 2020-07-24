let tree; //保存blog树节点
let tagTree; //保存tag树节点
let isDropComplete=false; //是否拖拽完毕
let tokenField; //保存tag输入框对象
let tokenFlag=true; //如果是初始化，则不触发tag事件
let blogIdBuff;
let tagData; //tag数据
let blogData; //blog数据


$(function () {
    // 绑定回车操作
    $('#blogName').bind('keypress',function(event){
        if(event.keyCode === "13") {
            searchBlog();
        }
    });

    // initTagTree();
    initBlogTree();
    initTagField();
});

function initTagField() {
    $.get("/tag/getData",function (data,status) {
        data = JSON.parse(data);
        //开启标签
        tokenField = new Tokenfield({
            el: document.querySelector('#tagInput'),
            items: data,
            setItems: [],
            itemData:'value',
            itemLabel:'value',
        });
        // 删除前的回调
        tokenField.on("removeToken",function (event,data) {
            console.log(event,data)
            let response = $.ajax({
                type: "post",
                url: "/tag/deleteRelation",
                async: false,
                data: {
                    blogId: getBlogId(), //博客id
                    tagId:data.id, //标签id
                }
            });

            layer.msg(response.responseJSON.msg);
            if(response.responseJSON.code){
                return true;
            }else{
                return false;
            }
        });
        // 添加前的回调
        tokenField.on("addToken",function (event,data) {
            if(!tokenFlag){
                return true;
            }

            let response = $.ajax({
                type: "post",
                url: "/tag/addRelation",
                async: false,
                data: {
                    blogId: getBlogId(), //博客id
                    tagName:data.value, //标签名称
                    isNew:data.isNew !== undefined, //标签是否新增
                    tagId:data.id, //标签id
                }
            });

            layer.msg(response.responseJSON.msg);
            if(response.responseJSON.code){
                return true;
            }else{
                return false;
            }
        });

        // 创建可以点击添加标签的功能
        for(let i=0;i<data.length;i++){
            $("#tagsDiv").append(`
                        <a href="javascript:void(0)" class="badge badge-danger"  value='`+data[i].value+`' tagId='`+data[i].id+`'  onclick="insertTag(this)">`+data[i].value+`</a>
                    `);
        }
    })
}

// 获取当前操作的blogId
function getBlogId() {
    return blogIdBuff;
}

function initTagTree() {
    tagTree = new dhx.Tree("tagTreeParse");
    //加载数据
    tagTree.data.load("/tag/getData").then(function(){
        //创建右侧导航栏
        let buf="";
        tagTree.data.map(item=>{
            if(item.isLeaf===undefined){
                buf+="<a class='list-group-item' style='font-size: 80%' href='/tag?pid="+item.id+"'>"+item.value+"</a>";
            }
        })
        $("#tags").html(buf);
    });
    // tree.expandAll();//展开所有节点
    // tree.selection.add("Books"); //默认选中
}


//（在blog表中添加一个pid字段，记录父节点的id）
//todo 之后blog的关系就保存在json中，以tree的方式进行维护（及blog既是博客，又是文件夹），当tree发生修改时，保存一份到服务器
//如果是添加blog，则在blog表中添加
//如果修改的blog名称，则在blog表中更新名称
//如果blog的层级发生变化，则修改对应blog的pid
//在模态框关闭的时候保存当前的层级结构（之前修改的内容）
function initBlogTree() {

    tree = new dhx.Tree("blogTreeParse",{
        dragMode:"both"
    });

    //双击跳转
    // tree.events.on("ItemDblClick", function(id, e){
    //     // 拿到节点的id，并打开blog或文件夹
    //     if(tree.data.getItem(id).isLeaf){
    //         window.location.href="/blog?id="+id;
    //     }else{
    //         window.location.href="/list?pid="+id;
    //     }
    // });
    //单击开合
    tree.events.on("itemClick", function(id, e){
        if(isDropComplete){
            setTimeout(function () {
                isDropComplete=false;
            },300);//将标志位清空
            return;
        }
        tree.toggle(id);//文件夹的开合
    });
    //拖拽保存层级
    tree.events.on("beforeDrop",function (toId,target,fromId) {
        isDropComplete=true;
        let response = $.ajax({
            type: "post",
            url: "/blog/update",
            async: false,
            data: {id:fromId,pid:toId}
        });

        if(response.status===200){
            layer.msg("移动成功！");
            return true;
        }else{
            layer.msg("移动失败！");
            return false;
        }
    })


    //加载数据
    tree.data.load("/blog/getTreeData").then(function(){
        tree.expandAll();//展开所有节点
    });

}

//==================tag相关======================

function insertTag(data) {
    let tagId=$(data).attr("tagId");
    let value=$(data).attr("value");
    // console.log(tagId,value);
    tokenField.addItems({id:tagId,value:value});
}

function beforeUpdateTagName() {
    let item = tagTree.selection.getItem();
    if(item===undefined){
        layer.msg("请选中要更改的节点");
        return ;
    }
    $('#updateTagNameModal').modal('show');//显示弹窗
    //设置原始的名称
    document.getElementById('updateTagName').value=tagTree.selection.getItem().value;

}

function deleteTag() {
    let id=tagTree.selection.getId();
    if(id===undefined){
        layer.msg("请选择要删除的标签");
        return;
    }
    $.get("/tag/isHasBlog?tagId="+id,function (data, status) {
        if(status==="success"){
            let msg;
            if(data===true){ //标签下有tag
                msg='该标签下有tag,是否删除tag并清除关联关系？';
            }else{
                msg='是否要删除tag?';
            }
            layer.confirm(msg, {
                btn: ['确定','取消'] //按钮
            }, function(){
                // 删除操作
                $.post("/tag/delete",{tagId:id},function (data, status) {
                    layer.msg(data.msg);
                    if(status==="success"){
                        // 去掉tagTree上的tag
                        tagTree.data.remove(id);
                    }
                })
            });
        }else{
            layer.msg(data.msg);
        }
    })

}

function createTag() {
    let name=document.querySelector("#tagName").value;
    if(name==="" || name===undefined){
        layer.msg("标签名称不能为空");
        return;
    }

    $.post("/tag/create", {name: name},
        function (data, status) {
            layer.msg(data.msg);
            if(status==="success"){
                tagTree.data.add({value: name,id:data.data}, -1, tagTree.data.getRoot());//往tree添加tag数据
                clearTagInput(); //清空输入框
            }
        });
}

//获取输入框的值
function getTagInputValue() {
    return  document.querySelector("#tagName").value;
}
//情况输入框的值
function clearTagInput() {
    document.querySelector("#tagName").value="";
}

// 打开添加tag到blog弹窗，并初始化blog对应的tag
function openAddTagToBlogModal(blogId){
    blogIdBuff=blogId;//保存当前正在操作的blogId
    $("#createTagToBlogModal").modal("show");

    //查询id对应的tag
    $.get("tag/getDataByBlogId?blogId="+blogId,function (data,status) {
        if(status==="success"){
            tokenFlag=false; //设置为不触发tag事件
            //把tag设置到输入框中
            tokenField.setItems(JSON.parse(data));
            tokenFlag=true; //恢复标志位
        }
    })
}

//================blog相关================
// 打开目录弹框时默认选中节点
function selectBlog() {
    tree.selection.add("61"); //默认选中
}

// 更新标签
function updateTag() {
    let id = tagTree.selection.getId();
    let name=document.getElementById('updateTagName').value;
    if(name===undefined || name===""){
        layer.msg("名称不能为空");
        return;
    }
    $.post("/tag/update",{tagId:id,name:name},
        function (data, status) {
            layer.msg(data.msg);
            $('#updateTagNameModal').modal('hide');//隐藏弹窗
            if(status==="success" && data.code){ //成功
                tagTree.data.update(id, {value: name});
                clearTagInput();
            }
        })
}

function editBlog() {
    let id = tree.selection.getId();
    if(id===undefined){
        layer.msg("请选中要更改的节点");
        return ;
    }
    window.location.href="/blog/edit?id="+id;
}

function openBlog() {
    let id = tree.selection.getId();
    if(id===undefined){
        layer.msg("请选中要更改的节点");
        return ;
    }

    // 拿到节点的id，并打开blog或文件夹
    if(tree.data.getItem(id).isLeaf){
        window.location.href="/blog?id="+id;
    }else{
        window.location.href="/list?pid="+id;
    }
}

//添加blog
function createBlog() {
    let text=getBlogInputValue()===""?new Date().Format("yyyy-MM-dd hh:mm:ss"):getBlogInputValue();
    let selectedId = tree.selection.getId();
    $.post("/blog/create", {name: text,pid:selectedId},
        function (data, status) {
            layer.msg(data.msg);
            if(status==="success"){
                tree.data.add({value: text,id:data.data}, -1, selectedId);//往tree添加数据
                tree.open(selectedId); //打开父节点
                clearBlogInput(); //清空输入框
                window.location.href="/blog/edit?id="+data.data; //直接打开编辑页
            }
        });
}

//删除blog
function deleteBlog() {
    let id=tree.selection.getId();
    if(id===undefined){
        layer.msg("请选择要删除的节点");
        return;
    }
    $.post("/blog/delete", {id:id},
        function (data, status) {
            layer.msg(data.msg);
            if(status==="success"){
                tree.data.remove(id);
                clearBlogInput();
            }
        });
}

function beforeUpdateName() {
    let item = tree.selection.getItem();
    if(item===undefined){
        layer.msg("请选中要更改的节点");
        return ;
    }
    $('#updateBlogNameModal').modal('show');//显示弹窗
    //设置原始的名称
    document.getElementById('updateBlogName').value=tree.selection.getItem().value;
}

//更新blog名称
function updateBlog() {
    let id = tree.selection.getId();
    let name=document.getElementById('updateBlogName').value;
    if(name===undefined || name===""){
        layer.msg("名称不能为空");
        return;
    }
    $.post("/blog/update",{id:id,name:name},
        function (data, status) {
            layer.msg(data.msg);
            $('#updateBlogNameModal').modal('hide');//隐藏弹窗
            if(status==="success"){
                tree.data.update(id, {value: name});
                clearBlogInput();
            }
        })

}

//搜索blog
function searchBlog() {
    let text = document.querySelector("#blogName").value;
    tree.data.filter();//每次先恢复，再过滤
    tree.data.filter(function(item) {
        return item.value.toLowerCase().indexOf(text) !== -1;
    });
    document.querySelector("#blogName").value="";//清空输入框
}

//获取输入框的值
function getBlogInputValue() {
    return  document.querySelector("#blogName").value;
}
//情况输入框的值
function clearBlogInput() {
    document.querySelector("#blogName").value="";
}

// 日期工具类
Date.prototype.Format = function (fmt) { // author: meizz
    var o = {
        "M+": this.getMonth() + 1, // 月份
        "d+": this.getDate(), // 日
        "h+": this.getHours(), // 小时
        "m+": this.getMinutes(), // 分
        "s+": this.getSeconds(), // 秒
        "q+": Math.floor((this.getMonth() + 3) / 3), // 季度
        "S": this.getMilliseconds() // 毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (let k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};