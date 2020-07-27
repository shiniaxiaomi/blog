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

    <!--tree-->
    <link rel="stylesheet" href="/tree/tree.min.css">
    <script type="text/javascript" src="/tree/tree.min.js"></script>

    <!--icons-->
    <link rel="stylesheet" href="http://at.alicdn.com/t/font_1907545_woxoxos7lxc.css">

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
<div class="container" style="max-width: 1300px">
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
                    <div id="catalog"></div>
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

<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="/js/jquery.min.js"></script>
<script src="/js/popper.min.js"></script>
<script src="/js/bootstrap.min.js"></script>

<script>

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
            name: '编辑', tip: '编辑', icon: '<i class="iconfont icon-ai-edit"></i>',
            click: () => {},
        },
        {
            name: 'save', tip: '保存', icon: '<i class="iconfont icon-baocun"></i>',
            click: () => {
                saveBlog();
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

    $(function () {
        // 初始化目录
        let tree = new dhx.Tree("catalog",{
            dragMode:"both"
        });
        //加载数据
        tree.data.load("/blog/getTreeData").then(function(){
            tree.expandAll();//展开所有节点
        });
        //单击编辑文件
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
