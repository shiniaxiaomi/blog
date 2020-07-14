<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <#include "common/headerCommon.ftl">
    <@header/>

</head>
<body>


<div class="container-xl">
    <div class="row">
        <div id="vditor">${md!""}</div>
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
            "|", "insert-before","insert-after",'headings', 'bold', 'italic', 'strike', 'link', '|',
            'list', 'ordered-list', 'check', 'outdent', 'indent', '|',
            'quote', 'line', 'code', 'inline-code', '|',
            'upload', 'record', 'table', '|',
            'undo', 'redo', '|',
            'edit-mode', "outline",
            {
                name: 'more',
                toolbar: [
                    'fullscreen',
                    'preview',
                    'export',
                ],
            }
        ];
        if (window.innerWidth < 768) {
            toolbar = [
                "insert-before","insert-after",
                'headings', 'bold', 'link', '|',
                'list', 'ordered-list', 'check', '|',
                'quote', 'line', 'code', 'inline-code', '|',
                'undo', 'redo', '|',
                'upload', "outline",
                //自定义保存按钮
                {
                    name: 'save',
                    tip: '保存',
                    icon: '<i class="iconfont icon-baocun"></i>',
                    click: () => {
                        saveBlog();
                    },
                },
                {
                    name: 'more',
                    toolbar: [
                        'table',
                        'edit-mode',
                        'fullscreen',
                        'export',
                        'preview',
                    ],
                }
            ]
        }

        window.vditor = new Vditor('vditor', {
            toolbar, //配置工具栏
            height: window.innerHeight, //设置高度
            width:"100%",
            outline: true, //开启大纲
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
                if(window.location.href.indexOf("#")!=-1){
                    window.location.href=window.location.href;
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

    //保存内容
    function saveBlog() {
        //生成desc
        let desc="";
        let descLength=200;//定义长度
        let arrs=$("#vditor p");
        for(let i=0;i<arrs.length;i++){
            var html=$(arrs[i]).html();
            if(html.indexOf("<img")!=-1){
                continue;
            }
            let text=html;
            if(desc.length+text.length<descLength){
                desc+=text+"。";
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
                mdHtml: vditor.getHTML(),
                desc: desc,
            },
            function (data, status) {
                if(status=="success"){
                    layer.msg("保存成功",{offset: 't',time:750});
                    autoSave=false;//清除自动保存标志位
                }else{
                    layer.msg("保存失败!");
                }
            });
    }
</script>