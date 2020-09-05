let zTree, zNodes,nodeList=[];
let setting = {
    edit:{
        enable: true,
        editNameSelectAll:true,
        showRemoveBtn: false,
        showRenameBtn: false,
        // 拖动规则
        drag:{
            isCopy:false,
            isMove : true,
            prev:false,
            inner:true,
            next:false
        }
    },
    view: {
        dblClickExpand: true,
        fontCss:getFontCss
    },
    data: {
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "pid",
            rootPId: null
        }
    },
    callback: {
        // onRightClick: OnRightClick,
        beforeDrop: beforeDrop,
        beforeClick:beforeClick,//点击之前，如果点击了不是已经选中的节点，则自动保存右侧对应的内容
        beforeRename:beforeRename
    }
};

// 搜索的样式使用
function getFontCss(treeId, treeNode) {
    return  (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
}
function hideBtn(treeNode) {
    let $createFolderBtn = $("#createFolderBtn");
    let $createNodeBtn = $("#createNodeBtn");
    let $editNodeBtn = $("#editNodeBtn");
    let $deleteNodeBtn = $("#deleteNodeBtn");
    // let $updateFolderStatusBtn = $("#updateFolderStatusBtn");

    $createFolderBtn.show();
    $createNodeBtn.show();
    $editNodeBtn.show();
    $deleteNodeBtn.show();
    // $updateFolderStatusBtn.show();

    if(treeNode.name==="公有" || treeNode.name==="私有"){
        $editNodeBtn.hide();
        $deleteNodeBtn.hide();
        // $updateFolderStatusBtn.hide();
    }else if(!treeNode.isFolder){ //如果是文件
        $createFolderBtn.hide();
        $createNodeBtn.hide();
        // $updateFolderStatusBtn.hide();
    }
}

// 点击节点回调
function beforeClick(treeId, treeNode, clickFlag) {
    // 控制每个节点的按钮权限
    hideBtn(treeNode);

    // 首次加载
    if(zTree.getSelectedNodes().length===0){
        loadMD(treeNode);
    }
    // 改变了选择节点
    else if(treeNode.id!==zTree.getSelectedNodes()[0].id){
        if(isEdit){
            updateBlog(true); //自动保存
        }
        loadMD(treeNode); //加载
    }
    isEdit=false;//将编辑状态置为false
    return true;
}
// 修改名称保存之前
function beforeRename(treeId, treeNode, newName, isCancel) {
    // 没有修改
    if(treeNode.name===newName){
        return true;
    }
    let response =$.ajax({
        type: "POST",//方法类型
        url: "/catalog/updateName" ,//url
        async:false,
        data: {id:treeNode.id,name:newName,isFolder:treeNode.isFolder,blogId:treeNode.blogId},
    });
    if(response.status===200 && response.responseJSON.code){
        layer.msg("更新成功");
        setTimeout(function () {
            zTree.cancelEditName(newName);
        },100);
        return true;
    }else{
        layer.msg("更新失败");
        setTimeout(function () {
            zTree.cancelEditName();
        },100);
        return false;
    }
}

// 拖入事件之前（只有文件夹才能拖入子节点）
function beforeDrop(treeId, treeNodes, targetNode, moveType) {
    // 如果是文件夹
    if(targetNode && targetNode.isFolder !== undefined && targetNode.isFolder===true){
        console.log(treeId, treeNodes, targetNode)
        if (window.confirm("确定要移动该文件吗？")) {
            let response =$.ajax({
                type: "POST",//方法类型
                url: "/catalog/move" ,//url
                async:false,
                data: {id:treeNodes[0].id,pid:targetNode.id,isPrivate:getIsPrivate(targetNode),isFolder:treeNodes[0].isFolder},// 被移动的是否是文件夹
            });
            if(response.status===200 && response.responseJSON.code){
                layer.msg("移动成功");
                return true;
            }else{
                layer.msg(response.responseJSON===undefined?"移动失败":response.responseJSON.msg);
            }
        }
    }else{
        layer.msg("不能拖入到文件中");
    }
    return false;
}

// 加载对应id的blog内容到编辑器
function loadMD(treeNode) {
    // 在编辑器上显示对应的blog内容
    // 如果点击的是文件夹，则将右侧编辑器设置为不编辑
    if(treeNode.isFolder){
        window.vditor.setValue("文件夹不能进行编辑",false);
        window.vditor.disabled();
    }
    // 如果点击的是文件，则在右侧编辑器中显示文件内容
    else{
        console.log("加载内容");
        window.blogId=treeNode.blogId;
        window.vditor.enable();
        $.get("/blog/md?id="+treeNode.blogId,function (data,status) {
            if(status==="success" && data.code){
                window.vditor.setValue(data.data, false);
            }
        })
    }
}

// 初始化刚添加的节点的工作
function initNewNode(newNode) {
    // 筛选刚添加的节点
    console.log(newNode.id);
    let node = zTree.getNodesByParam("id", newNode.id, null)[0];
    // 设置选中节点
    zTree.selectNode(node);
    // 加载当前选中节点的md
    loadMD(node);
    // 默认重命名节点
    editTreeNode(false);//不询问是否编辑
    // 设置按钮权限
    hideBtn(node);
}

// 添加待整理的文件
function quickNewFile() {
    // 添加文件（默认添加以日期+时间为名称的文件）
    let name=new Date().Format("yyyy-MM-dd hh:mm:ss");
    let pNode = zTree.getNodesByParam("name", "私有", null)[0];
    let item={name: name,pid:pNode.id,isFolder:false,icon:"/ztree/img/file.png",checked:true,isPrivate:true};
    $.post("/catalog/insert",item,function (data,status) {
        if(status==="success" && data.code){
            item.blogId=data.data.blogId; //添加返回的blogId值
            item.id=data.data.id; //添加返回的目录itemId值

            let newNode = item;
            zTree.addNodes(pNode, newNode);// 默认添加到待整理文件夹
            initNewNode(newNode);
        }else{
            layer.msg("接口调用失败");
        }
    })
}

// 添加file或文件夹
function addNode(type) { //[file,folder]
    let selectedNode = getSelectedNode();

    // 添加文件（默认添加以日期+时间为名称的文件）
    let name=new Date().Format("yyyy-MM-dd hh:mm:ss");
    let item={name: name,pid:selectedNode.id};
    item.isPrivate = getIsPrivate(selectedNode);
    if(type==="file"){
        item.isFolder=false;
        item.icon="/ztree/img/file.png";
    }else if(type==="folder"){
        item.isFolder=true;
        item.icon="/ztree/img/folder.png";
    }

    $.post("/catalog/insert",item,function (data,status) {
        if(status==="success" && data.code){
            item.blogId=data.data.blogId; //添加返回的blogId值
            item.id=data.data.id; //添加返回的目录itemId值
            let newNode = item;
            // 如果有选中节点
            if (selectedNode) {
                newNode.checked = selectedNode.checked;
                zTree.addNodes(selectedNode, newNode);
            } else {
                zTree.addNodes(null, newNode);// 默认添加
            }
            initNewNode(newNode);
        }else{
            layer.msg("接口调用失败");
        }
    })
}

// 删除节点
function removeTreeNode() {
    let selectedNode = getSelectedNode();
    if (selectedNode.children && selectedNode.children.length > 0) {
        layer.msg("文件夹下还有文件，不能删除");
    } else {
        layerConfirm("确定删除该文件吗？",function () {
            $.post("/catalog/delete",{id:selectedNode.id,isFolder:selectedNode.isFolder,blogId:selectedNode.blogId},function (data,status) {
                if(status==="success" && data.code){
                    zTree.removeNode(selectedNode);
                    window.vditor.setValue("", true);//清空编辑器数据
                    window.vditor.disabled();//	禁用编辑器
                    layer.msg("删除成功")
                }else{
                    layer.msg("删除失败");
                }
            })
        })
    }
}
// 编辑节点
function editTreeNode(isConfirm) {
    let selectedNode = getSelectedNode();
    if(isConfirm){
        layerConfirm("确定编辑名称吗？",function () {
            zTree.editName(selectedNode);
        })
    }else{
        zTree.editName(selectedNode);
    }
}

// 加载目录数据
function initTree(isLogin){
    // 权限校验
    if(isLogin===false){
        setting = {
            edit:{
                enable: false,
            },
            view: {
                dblClickExpand: true,
                fontCss:getFontCss
            },
            data: {
                simpleData: {
                    enable: true,
                    idKey: "id",
                    pIdKey: "pid",
                    rootPId: null
                }
            },
            callback: {
                beforeClick:function (treeId, treeNode, clickFlag) {
                    if(treeNode.blogId===null){
                        return;
                    }
                    window.location="/blog/"+treeNode.blogId;
                }
            }
        };
    }

    // 绑定搜索输入框的回车事件
    $("#searchInput").keypress(function(e) {
        if (e.keyCode === 13) {
            // highlightSearch();
            filterSearch();
        }
    });

    // 加载目录数据
    $.get("/catalog",function (data,status) {
        // 指定icon图标并保存目录节点
        zNodes = data.data.map(item=>{
            if(item.isFolder!==undefined && item.isFolder===true){
                item.icon="/ztree/img/folder.png";
                item.open=true;
            }else{
                item.icon="/ztree/img/file.png";
            }
            return item;
        });
        // 初始化树（加载数据和配置）
        $.fn.zTree.init($("#treeDemo"), setting, zNodes);
        zTree = $.fn.zTree.getZTreeObj("treeDemo");
    });
}

// 返回选中的blogId
function getSelectedBlogId() {
    // 判断选中的不是文件夹或者是否没有选择
    if(zTree.getSelectedNodes()===undefined || zTree.getSelectedNodes()[0]===undefined || zTree.getSelectedNodes()[0].isFolder){
        layer.msg('请选择对应的blog');
        throw new Error( '请选择对应的blog' );
    }
    return zTree.getSelectedNodes()[0].blogId;
}

// 询问框工具方法
function layerConfirm(msg,func) {
    let index = layer.confirm(msg, {
        btn: ['确定','取消'] //按钮
    }, function(){
        func();
        layer.close(index); //如果设定了yes回调，需进行手工关闭
    }, function(){
        layer.close(index); //如果设定了yes回调，需进行手工关闭
    });
}

// 过滤搜索节点
function filterSearch() {
    // 节点全部展开
    zTree.expandAll(true);
    let $searchInput = $("#searchInput");
    let value=$searchInput.val().toLocaleLowerCase();
    if (value === "") {
        cancelFilter();
        return;
    }
    nodeList = zTree.getNodesByParamFuzzy("name", value);

    let $treeDemo = $("#treeDemo li a");

    // 隐藏所有节点
    let filter = $treeDemo.filter((index,item)=>{
        $(item).parent().hide();
        return $(item).text().toLocaleLowerCase().indexOf(value)!==-1;
    });

    // 显示匹配节点
    $("#treeDemo .level0").eq(0).show();
    for(let i=0;i<filter.length;i++){
        $(filter[i]).show();//自己显示
        let buff=$(filter[i]).parent(); //父节点显示
        while(buff.attr("class").indexOf("level0")!==0){
            buff.show();// 父节点显示
            buff=buff.parent();
        }
    }
}

// 取消过滤
function cancelFilter() {
    let $treeDemo = $("#treeDemo li a");
    $treeDemo.filter((index,item)=>{
        $(item).parent().show();
        return false;
    });
}

// 高亮搜索节点
function highlightSearch() {
    // 节点全部展开
    zTree.expandAll(true);
    let $searchInput = $("#searchInput");
    let value=$searchInput.val();
    updateNodes(false,nodeList);
    if (value === "") return;
    nodeList = zTree.getNodesByParamFuzzy("name", value);
    updateNodes(true,nodeList);
    // 清空搜索内容
    // $searchInput.val('');
}

// 取消高亮
function cancelHighlight() {
    updateNodes(false,nodeList);
}

function updateNodes(highlight,nodeList){
    for( let i=0, l=nodeList.length; i<l; i++ ) {
        nodeList[i].highlight = highlight;
        nodeList[i].hiddenNodes = false;
        zTree.updateNode(nodeList[i]);
    }
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

// 返回选中的节点
function getSelectedNode() {
    let selectedNodes = zTree.getSelectedNodes();
    if(selectedNodes.length===0){
        layer.msg("请选中对应的节点");
        throw new Error("请选中对应的节点");
    }

    return selectedNodes[0];
}

// 获取当前创建的文件是否为私有（传入要判断的节点，即选中的文件夹）
function getIsPrivate(node) {
    let isPrivate = true;
    let nodes = node.getPath();
    if(nodes.length>0 && nodes[0].name==="公有"){
        isPrivate=false;
    }
    return isPrivate;
}
