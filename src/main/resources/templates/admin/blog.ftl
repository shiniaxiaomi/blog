<!doctype html>
<html lang="en">
<#include "../common/head.ftl">
<#include "../common/body.ftl">
<#include "../common/left.ftl">
<#include "../common/right.ftl">
<#include "../common/sidebar.ftl">
<@head>
    <!-- vditor -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/vditor@3.4.1/dist/index.css" />
    <script src="https://cdn.jsdelivr.net/npm/vditor@3.4.1/dist/index.min.js" defer></script>
    <!--icons-->
    <link rel="stylesheet" href="http://at.alicdn.com/t/font_1990451_36hvvfymceu.css">
    <!--ztree-->
    <link rel="stylesheet" href="/ztree/zTreeStyle.css" />

    <!--相关js-->
    <script src="/layer/layer.js"></script>
    <script src="/ztree/jquery.ztree.all.min.js"></script>

    <script src="/js/catalog.js"></script>
    <script src="/js/tag.js"></script>

    <style>
        <#--右键菜单-->
        div#rMenu {position:absolute; visibility:hidden; top:0; background-color: #555;text-align: left;padding: 2px;}
        div#rMenu ul{margin: 0;padding: 0}
        div#rMenu ul li{margin: 1px 0;padding: 0 5px;cursor: pointer;list-style: none outside none;background-color: #DFDFDF;}
        div#rMenu ul li:hover{background-color: #dfc4a0;}
        <#--根节点-->
        .ztree li span.button.switch.level0 {visibility:hidden; width:1px;}
        .ztree li ul.level0 {padding:0; background:none;}
        <#--字体大小-->
        .ztree *{
            font-size: 13px;
        }
        <#--搜索高亮样式-->
        .highlight_red {color:#A60000;}

        @media (max-width: 768px){
            #tocDiv{
                order: 2;
            }
        }
    </style>
</@head>
<#include "../common/body.ftl">
<@body class="container-fluid">
    <@left class="col-md-1 col-lg-2" style="max-width: 150px">
        <@sidebar></@sidebar>
    </@left>
    <@right class="col-md-11 col-lg-10">
        <div class="row">
            <div id="tocDiv" class="col-12 col-md-3 order-1">
                <div>
                    <input class="mr-2" id="searchInput" autocomplete="off">
                </div>
                <div class="my-1">
                    <button type="button" class="btn btn-secondary btn-sm mx-1" onclick="searchNode()">搜索</button>
                    <div class="btn-group btn-group-sm" role="group">
                        <button id="btnGroupDrop1" type="button" class="btn btn-secondary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            操作
                        </button>
                        <div class="dropdown-menu" aria-labelledby="btnGroupDrop1">
                            <a id="createFolderBtn" class="dropdown-item btn-sm" href="javascript:void(0);" onclick="addNode('file');">增加节点</a>
                            <a id="editNodeBtn" class="dropdown-item btn-sm" href="javascript:void(0);" onclick="editTreeNode();">编辑节点</a>
                            <a id="deleteNodeBtn" class="dropdown-item btn-sm" href="javascript:void(0);" onclick="removeTreeNode();">删除节点</a>
                            <a id="createNodeBtn" class="dropdown-item btn-sm" href="javascript:void(0);" onclick="addNode('folder');">创建文件夹</a>
                            <a id="updateFolderStatusBtn" class="dropdown-item btn-sm" href="javascript:void(0);" onclick="openFolderStatusModel();">设置共享状态</a>
                        </div>
                    </div>
                    <button type="button" class="btn btn-secondary btn-sm mx-1" onclick="quickNewFile()">快速新建</button>

                </div>
                <div class="overflow-auto" style="height: 500px">
                    <ul id="treeDemo" class="ztree"></ul>
                </div>
            </div>

            <div class="col-12 col-md-9 order-1">
                <div id="vditor"></div>
            </div>
        </div>
    </@right>
</@body>

<div class="modal fade" id="configModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">配置博客信息</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="configForm" onsubmit="return false;">
                    <div class="form-group row">
                        <label for="recipient-name" class="col-2 col-form-label">状态</label>
                        <div class="ml-3 form-check-inline col">
                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio" name="isStick" id="radio3" value="1">
                                <label class="form-check-label" for="radio3">置顶</label>
                            </div>
                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio" name="isStick" id="radio4" value="0">
                                <label class="form-check-label" for="radio4">不置顶</label>
                            </div>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label for="recipient-name" class="col-2 col-form-label">共享</label>
                        <div class="ml-3 form-check-inline col">
                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio" name="isPrivate" id="radio1" value="0">
                                <label class="form-check-label" for="radio1">公有</label>
                            </div>
                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio" name="isPrivate" id="radio2" value="1">
                                <label class="form-check-label" for="radio2">私有</label>
                            </div>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label for="message-text" class="col-2 col-form-label">标签</label>
                        <div class="col">
                            <div class="form-inline">
                                <input id="searchTagInput" autocomplete="off">
                                <button type="button" class="btn btn-secondary btn-sm m-1" onclick="searchTag(originalTagData)">搜索</button>
                            </div>
                            <div id="tags"></div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" onclick="saveConfig()">保存</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="configFolderModal" tabindex="-1" role="dialog" aria-labelledby="folderModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="folderModalLabel">更新文件夹的共享状态</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="configFolderForm" onsubmit="return false;">
                    <div class="form-group row">
                        <label for="recipient-name" class="col-2 col-form-label">共享</label>
                        <div class="ml-3 form-check-inline col">
                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio" name="isPrivate" id="f_radio1" value="0">
                                <label class="form-check-label" for="f_radio1">公有</label>
                            </div>
                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio" name="isPrivate" id="f_radio2" value="1">
                                <label class="form-check-label" for="f_radio2">私有</label>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" onclick="updateFolderConfig()">保存</button>
            </div>
        </div>
    </div>
</div>
<#--<div id="rMenu">-->
<#--    <ul>-->
<#--        <li id="m_add_blog" onclick="addNode('file');">增加节点</li>-->
<#--        <li id="m_add_folder" onclick="addNode('folder');">创建文件夹</li>-->
<#--        <li id="m_edit" onclick="editTreeNode();">编辑节点</li>-->
<#--        <li id="m_remove" onclick="removeTreeNode();">删除节点</li>-->
<#--    </ul>-->
<#--</div>-->
</html>


<script>

    function saveConfig() {
        $.post("/blog/config",$("#configForm").serialize()+"&id="+getSelectedBlogId(),function (data,status) {
            if(status==="success" && data.code){
                layer.msg("保存成功");
                $("#configModal").modal("hide");
            }
        })
    }

    // 更新blog
    function updateBlog(tip){
        console.log("保存内容");
        let layerMsg = layer.msg("正在保存...",{time:0});
        // console.log(zTree.getSelectedNodes()[0]!==undefined && zTree.getSelectedNodes()[0].isFolder);
        if(zTree.getSelectedNodes()[0]===undefined || zTree.getSelectedNodes()[0].isFolder){
            layer.msg("请选择对应的博客");
            return;
        }
        let mdValue = window.vditor.getValue();

        $.post("/blog/update",{
            id: zTree.getSelectedNodes()[0].blogId,
            md: mdValue,
            desc: lute.Md2HTML((mdValue.substr(0,200)+"...").replace(/#.*/g,""))
        },function (data,status) {
            layer.close(layerMsg);
            if(status==="success" && data.code){
                if(tip!==undefined){
                    layer.msg("保存成功",{time:1000});
                }
                // 保存完后，将修改状态设置为false
                isEdit=false;
            }else{
                layer.msg("保存失败");
            }
        })
    }

    // 加载blog相关的配置
    function loadConfig() {
        // 清空选中
        $("#radio1").prop('checked', false);
        $("#radio2").prop('checked', false);
        $("#radio3").prop('checked', false);
        $("#radio4").prop('checked', false);
        for(let i=0;i<originalTagData.length;i++){
            $("#"+originalTagData[i].id).prop('checked', false);
        }

        // 根据blogId请求blog信息，获取是否私有和其关联的tag，并自动进行勾选操作
        $.get("/blog/config?id="+getSelectedBlogId(),function (data,status) {
            if(status==="success" && data.code) {
                // 设置私有选中
                if(data.data.isPrivate){
                    $("#radio2").prop('checked', true);
                }else{ //设置公有选中
                    $("#radio1").prop('checked', true);
                }
                // 设置置顶状态
                if(data.data.isStick){
                    $("#radio3").prop('checked', true);
                }else{
                    $("#radio4").prop('checked', true);
                }
                // 设置tag选中
                let tagIds = data.data.checkedTagIds;
                for(let i=0;i<tagIds.length;i++){
                    $("#"+tagIds[i]).prop('checked', true);
                }
            }
        })
    }

    //关闭和刷新页面时自动保存
    window.onbeforeunload = function (e) {
        window.localStorage.setItem("needReload","true");//设置为需要刷新页面
        if(isEdit){
            updateBlog();//自动保存
        }
    };

    let isEdit=false;
    window.blogId=${blogId!0};
    let lute;
    let isClick= true; //防多次点击

    // 打开或关闭大纲
    function toggleToc(){
        $("#vditor .vditor-toolbar__item button[aria-label=大纲]").eq(0).click();
    }

    $(function () {

        initTree(); // 初始化目录
        initTags(); // 初始化标签

        // 绑定编辑框的编辑事件，修改是否编辑的状态
        $("#vditor").keypress(function(e) {
            isEdit=true;
        });
        // 当输入 删除，回车，tab键时，如果焦点在编辑框，则修改是否编辑的状态
        $("#vditor").keyup(function(e) {
            if(e.keyCode===8 || e.keyCode===9 || e.keyCode===13){
                isEdit=true;
            }
        });

        // 工具栏
        let toolbar=[
            {
                name: '返回', tip: '返回', icon: '<i class="iconfont icon-fanhui1"></i>',tipPosition: 's',
                click: () => {
                    window.localStorage.setItem("needReload","true");//设置为需要刷新页面
                    if(isEdit){
                        updateBlog();//自动保存
                    }
                    setTimeout(function () {
                        window.history.back();
                    },250);
                },
            },
            {
                name: '配置信息', tip: '编辑', icon: '<i class="iconfont icon-ai-edit"></i>',tipPosition: 's',
                click: () => {
                    loadConfig();// 加载对应的blog配置
                    resetTags();// 清除掉搜索高亮
                    $('#configModal').modal("show");
                },
            },
            {
                name: '保存', tip: '保存', icon: '<i class="iconfont icon-baocun"></i>',tipPosition: 's',
                click: () => {
                    // 如果点击保存按钮，则为强制保存（需要防止多次点击）
                    if(isClick){
                        isClick= false; //只能点击一次
                        updateBlog("tip");
                        setTimeout(function(){
                            isClick = true; //1000ms后回复点击
                        }, 1000);
                    }
                },
            },
            {
                name: '删除图片', tip: '删除图片(只支持快捷键)', icon: '<i class="iconfont icon-shanchu"></i>',
                tipPosition: 's', hotkey: '⌘-⇧-S',
                click: () => {
                    let selection = vditor.getSelection();
                    if(selection==="") {
                        window.vditor.tip("请选择对应的图片", 1000);
                        return;
                    }
                    if(window.blogId===0) {
                        window.vditor.tip("请选择对应的博客", 1000);
                        return;
                    }
                    // 当点击删除图片按钮时，将选中的图片链接内容删除，并且删除掉博客对该图片的引用
                    $.post("/file/deleteRelation",{name: selection.substr(6),blogId:window.blogId},function (data,status) {
                        if(status==="success" && data.code){
                            window.vditor.tip(data.msg, 1000);
                            window.vditor.deleteValue();//删除选中的url
                        }else{
                            window.vditor.tip("删除失败", 2000);
                        }
                    })
                },
            },
            {
                name: '查看附件', tip: '查看附件', icon: '<i class="iconfont icon-fujian"></i>',tipPosition: 's',
                click: () => {
                    // 当点击查看附件按钮时，跳转到文件管理页面，并显示该blog所对应的分页附件
                    if(zTree.getSelectedNodes()[0]===undefined || zTree.getSelectedNodes()[0].isFolder){
                        layer.msg("请选择对应的博客");
                        return;
                    }
                    window.location="/admin/file/"+blogId+"/1";
                },
            },
            "|","outline", "insert-before","insert-after",'headings', 'link', '|',
            'list', 'ordered-list', 'check', 'outdent', 'indent', '|',
            'quote', 'line', 'code', 'inline-code', '|',
            'upload', 'table', '|',
            'undo', 'redo', '|',
            {
                name: 'more',
                toolbar: [
                    'edit-mode', 'fullscreen', 'preview',
                    'export', 'bold', 'italic', 'strike', 'record'
                ],
            }
        ];
        // 初始化编辑器
        window.vditor = new Vditor('vditor', {
            toolbar, //配置工具栏
            height: window.innerHeight*0.8, //设置高度
            width:"100%",
            outline: "${blogId!"null"}"==="null"?false:true, //开启大纲
            value:'',//编辑器初始化值(这里的内容只能存放markdown，不能存放html)
            placeholder: '', //内容为空时的提示
            toolbarConfig: {
                pin: true, //固定工具栏
            },
            counter: {
                enable: true,//启用计数器
                type: 'text',
            },
            cache: {
                enable: false, //默认是true，但是当编辑时，需要先设置为false，之后在修改为true（因为原始内容会被本地缓存覆盖掉）
            },
            tab: '    ',//设置tab键为4个空格
            //图片上传
            upload: {
                url:'/blog/upload',
                // 自定义上传
                handler(files){
                    let data = new FormData();
                    data.append('file', files[0]);
                    debugger
                    $.ajax({
                        type: 'POST',
                        url: "/blog/upload/"+window.blogId,
                        data: data,
                        cache: false,
                        processData: false,
                        contentType: false,
                        success: function (data) {
                            window.vditor.tip(data.msg, 1000);
                            if(data.data.isImg){
                                // 插入图片链接
                                vditor.insertValue("!["+data.data.fileName+"](/file/"+data.data.fileName+")\n");
                            }else{
                                // 插入超链接
                                vditor.insertValue("["+data.data.fileName+"](/file/"+data.data.fileName+")");
                            }
                        },
                        error: function (data) {
                            window.vditor.tip(data, 3000);
                        }
                    });
                },
            },
            after(){
                // 初始化后修改工具栏的提示位置
                $(".vditor-toolbar__item").each(function () {
                    let item = $(this).children().eq(0);
                    if(item.hasClass("vditor-tooltipped__nw")){
                        item.removeClass("vditor-tooltipped__nw");
                        item.addClass("vditor-tooltipped__sw");
                    }else if(item.hasClass("vditor-tooltipped__n")){
                        item.removeClass("vditor-tooltipped__n");
                        item.addClass("vditor-tooltipped__s");
                    }else if(item.hasClass("vditor-tooltipped__ne")){
                        item.removeClass("vditor-tooltipped__ne");
                        item.addClass("vditor-tooltipped__se");
                    }
                });

                lute=Lute.New();//初始化md解析器

                //==========编辑时锚点的跳转==============
                // 编辑对应的blogId
                let blogId=${blogId!"null"};
                if(blogId!=="null"){
                    setTimeout(function () {
                        // 让左侧的菜单选中
                        let node = zTree.getNodeByParam("blogId", blogId, null);
                        zTree.selectNode(node);
                        // 加载md内容
                        loadMD(node);
                        // 跳转到对应的锚点
                        setTimeout(function () {
                            if(location.hash!==""){
                                let hash = "ir-"+decodeURI(location.hash).substr(1).replace(/[.*|+=\-()\s]/g,"-");
                                $("#vditor .vditor-outline__content div[data-id^="+hash+"]").click();//刷新页面锚点
                                toggleToc();//关闭大纲
                            }
                        },150);
                    },100)
                }
            },
        })

    })

</script>
