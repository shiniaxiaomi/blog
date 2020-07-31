<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="/css/bootstrap.min.css">

    <link rel="stylesheet" href="/css/blog.css">

    <!-- vditor -->
    <link rel="stylesheet" href="/vditor/index.css" />
    <script src="/vditor/index.min.js" defer></script>

    <!--icons-->
    <link rel="stylesheet" href="http://at.alicdn.com/t/font_1907545_woxoxos7lxc.css">

    <link rel="stylesheet" href="/ztree/zTreeStyle.css" />



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
    </style>


</head>
<body>

<#--引入顶部导航栏-->
<#include "../common/nav.ftl">
<#include "../common/record.ftl">
<@nav/>

<#--index页面默认就是展示blog的页面
左右结构
左边是简单的目录，可以增删查改（类似于简单的文件夹操作，新建后，右边就是空白的，可以直接进行编辑）
右边是博客的编辑区域把
-->

<#--主体-->
<div class="container-fluid" >
    <div class="row justify-content-center">
        <!--左（公共导航栏）-->
        <div class="col-2" style="max-width: 150px">
            <a class="d-block mb-2" href="/admin/blog">博客目录</a>
            <a class="d-block mb-2" href="/admin/tag">标签管理</a>
        </div>

        <!--中（blog）-->
        <div class="col-10">
            <div class="row">
                <div class="col-3">
                    <div class="form-inline">
                        <input class="mr-2" id="searchInput" autocomplete="off">
                        <button type="button" class="btn btn-secondary btn-sm" onclick="searchNode()">搜索</button>
                    </div>
                    <div class="overflow-auto" style="height: 500px">
                        <ul id="treeDemo" class="ztree"></ul>
                    </div>
                </div>

                <div class="col-9">
                    <div id="vditor"></div>
                </div>
            </div>
        </div>

        <#--备案信息-->
        <@record/>
    </div>
</div>

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

<div id="rMenu">
    <ul>
        <li id="m_add_blog" onclick="addNode('file');">增加节点</li>
        <li id="m_add_folder" onclick="addNode('folder');">创建文件夹</li>
        <li id="m_edit" onclick="editTreeNode();">编辑节点</li>
        <li id="m_remove" onclick="removeTreeNode();">删除节点</li>
    </ul>
</div>


<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="/js/jquery.min.js"></script>
<script src="/js/popper.min.js"></script>
<script src="/js/bootstrap.min.js"></script>

<script src="/layer/layer.js"></script>
<script src="/ztree/jquery.ztree.all.min.js"></script>

<script src="/js/catalog.js"></script>
<script src="/js/tag.js"></script>


<script>

    function saveConfig() {
        $.post("/blog/config",$("#configForm").serialize()+"&id="+getSelectedBlogId(),function (data,status) {
            if(status==="success" && data.code){
                layer.msg("保存成功");
                $("#configModal").modal("hide");
            }
        })
    }

    function updateBlog(){
        console.log("保存内容");
        console.log(zTree.getSelectedNodes()[0]!==undefined && zTree.getSelectedNodes()[0].isFolder)
        if(zTree.getSelectedNodes()[0]===undefined || zTree.getSelectedNodes()[0].isFolder){
            return;
        }
        $.post("/blog/update",{
            id: zTree.getSelectedNodes()[0].blogId,
            md: window.vditor.getValue(),
        },function (data,status) {
            if(status==="success" && data.code){
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
        updateBlog();//自动保存
    };

    $(function () {

        initTree(); // 初始化目录
        initTags(); // 初始化标签

        // 工具栏
        let toolbar=[
            {
                name: '返回', tip: '返回', icon: '<i class="iconfont icon-fanhui1"></i>',
                click: () => {
                    window.localStorage.setItem("needReload","true");//设置为需要刷新页面
                    window.history.back();
                },
            },
            {
                name: '配置信息', tip: '编辑', icon: '<i class="iconfont icon-ai-edit"></i>',
                click: () => {
                    loadConfig();// 加载对应的blog配置
                    resetTags();// 清除掉搜索高亮
                    $('#configModal').modal("show");
                },
            },
            {
                name: '保存', tip: '保存', icon: '<i class="iconfont icon-baocun"></i>',
                click: () => {
                    updateBlog();
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
            outline: false, //开启大纲
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
                accept: 'image/*,.mp3, .wav, .rar',
                token: 'test',
                url: '/api/upload/editor',
                linkToImgUrl: '/api/upload/fetch',
                filename (name) {
                    return name.replace(/[^(a-zA-Z0-9\u4e00-\u9fa5\.)]/g, '').
                    replace(/[\?\\/:|<>\*\[\]\(\)\$%\{\}@~]/g, '').
                    replace('/\\s/g', '')
                },
            },
        })
    })

</script>


</body>
</html>
