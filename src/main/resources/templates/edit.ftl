<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <#include "common/headerCommon.ftl">
    <@header/>

    <style>
        .vditor-toolbar .vditor-toolbar--pin{
            padding-left:0px;
        }
        .vditor-outline{
            width: 170px;
        }
    </style>

</head>
<body>

<#--引入顶部导航栏-->
<#include "common/navCommon.ftl">
<@nav/>

<div class="container-fluid" style="margin-top:85px;">
    <div class="row">
        <div class="col-md-2 d-none d-md-block">
            <div id="blogTreeParse"></div>
        </div>
        <div class="col col-md-10">
            <div id="vditor" ></div>
        </div>
    </div>
</div>


</body>
</html>


<script>

    //关闭和刷新页面时自动保存
    window.onbeforeunload = function (e) {
        window.localStorage.setItem("needReload","true");//设置为需要刷新页面
        saveBlog();//自动保存
    };

    $(function () {

        let toolbar=[
            {
                name: '返回',
                tip: '返回',
                icon: '<i class="iconfont icon-fanhui1"></i>',
                click: () => {
                    window.localStorage.setItem("needReload","true");//设置为需要刷新页面
                    window.history.back();
                },
            },
            {
                name: '编辑',
                tip: '编辑',
                icon: '<i class="iconfont icon-ai-edit"></i>',
                click: () => {

                },
            },
            {
                name: 'save',
                tip: '保存',
                icon: '<i class="iconfont icon-baocun"></i>',
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
                    'edit-mode',
                    'fullscreen',
                    'preview',
                    'export',
                    'bold',
                    'italic',
                    'strike',
                    'record'
                ],
            }
        ];

        $.get("/blog/getMDByBlogId?id=${blogId!0}",function (data,status) {
            window.vditor = new Vditor('vditor', {
                toolbar, //配置工具栏
                height: window.innerHeight*0.87, //设置高度
                width:"100%",
                outline: true, //开启大纲
                value:data,//编辑器初始化值(这里的内容只能存放markdown，不能存放html)
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
                    id: ${blogId!"vditor"}, //用于设置缓存id，当开启多个编辑器的时候就有用了
                    after: () => {
                        autoSave=true;
                        console.log("cache"); //可以在缓存回调中进行自动保存到数据库（但是最好不好，因为稍微修改就会进行回调）
                    },
                },
                tab: '    ',//设置tab键为4个空格
                //编辑器渲染完成后的回调（可以在该回调中开启缓存）
                after () {
                    //每3分钟自动保存
                    setInterval(function () {
                        saveBlog();
                    },1000*60*3);

                    // 开启缓存
                    setTimeout(function () {
                        vditor.enableCache();//在渲染完成3秒之后启动缓存
                    },3000);

                    // 调转到指定的编辑位置
                    if(window.location.href.indexOf("#")!==-1){
                        console.log(decodeURIComponent(window.location.href));
                        window.location.href=decodeURIComponent(window.location.href);
                    }

                },
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

    })

    //保存内容
    function saveBlog() {
        //生成desc
        let desc="";
        let descLength=200;//定义长度
        let arrs=$("#vditor p");
        debugger
        for(let i=0;i<arrs.length;i++){
            var html=$(arrs[i]).html();
            if(html.indexOf("<img")!==-1){
                continue;
            }
            let text=$(arrs[i]).text();
            if(desc.length+text.length<descLength){
                desc+=text+"... ; ";
            }else{
                desc+=text.slice(0,descLength-desc.length)+"...";
                break;
            }
        }
        //保存
        $.post("/blog/update",
            {
                id: ${blogId!},
                md: vditor.getValue(),
                mdHtml: document.querySelector(".vditor-reset").innerHTML
                    .replace(/<pre class="vditor-wysiwyg__pre">/g,"<pre class=\"vditor-wysiwyg__pre\" style=\"display: none;\">")
                    .replace(/this\.previousElementSibling\.select\(\);/g,
                        "this.previousElementSibling.value=this.parentElement.parentElement.previousSibling.innerText;this.previousElementSibling.select();"),
                desc: desc,
            },
            function (data, status) {
                if(status==="success"){
                    layer.msg("保存成功",{offset: 't',time:750});
                    autoSave=false;//清除自动保存标志位
                }else{
                    layer.msg("保存失败!");
                }
            });
    }
</script>