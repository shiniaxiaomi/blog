<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="/css/bootstrap.min.css">

    <!-- ⚠️生产环境请指定版本号，如 https://cdn.jsdelivr.net/npm/vditor@x.x.x/dist... -->
    <link rel="stylesheet" href="/vditor/index.css" />
    <script src="/vditor/index.min.js" defer></script>


</head>
<body>

<#--<div class="container-xl">-->
    <div class="row">
        <div class="col-lg-3">
            文件夹目录
        </div>
        <div class="col-lg-9">
            <div id="vditor"></div>
        </div>
    </div>
<#--</div>-->


</body>
</html>

<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="/js/jquery.min.js"></script>
<script src="/js/bootstrap.min.js"></script>

<script>
    $(function () {
        let toolbar=[
            'emoji', 'headings', 'bold', 'italic', 'strike', 'link', '|',
            'list', 'ordered-list', 'check', 'outdent', 'indent', '|',
            'quote', 'line', 'code', 'inline-code', '|',
            'upload', 'record', 'table', '|',
            'undo', 'redo', '|',
            'edit-mode', "outline",
            //自定义保存按钮
            {
                name: 'save',
                tip: '保存',
                icon: '<svg version="1.1" xmlns="http://www.w3.org/2000/svg" width="768" height="768" viewBox="0 0 768 768"><path d="M342 426v-84h426v84h-426zM342 256v-86h426v86h-426zM0 0h768v86h-768v-86zM342 598v-86h426v86h-426zM0 214l170 170-170 170v-340zM0 768v-86h768v86h-768z"></path></svg>',
                click: () => {
                    // alert('custom toolbar')
                    console.log(vditor.getHTML())
                },
            },
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
                'headings', 'bold', 'link', '|',
                'list', 'ordered-list', 'check', '|',
                'quote', 'line', 'code', 'inline-code', '|',
                'undo', 'redo', '|',
                'upload', "outline",
                //自定义保存按钮
                {
                    name: 'save',
                    tip: '保存',
                    icon: '<svg version="1.1" xmlns="http://www.w3.org/2000/svg" width="768" height="768" viewBox="0 0 768 768"><path d="M342 426v-84h426v84h-426zM342 256v-86h426v86h-426zM0 0h768v86h-768v-86zM342 598v-86h426v86h-426zM0 214l170 170-170 170v-340zM0 768v-86h768v86h-768z"></path></svg>',
                    click: () => {
                        alert('custom toolbar')
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
            outline: true, //开启大纲
            value:'',//编辑器初始化值(这里的内容只能存放markdown，不能存放html)
            placeholder: 'Hello, luyingjie', //内容为空时的提示
            // preview: {
            //     hljs:{
            //         // lineNumber:true,//代码开启行号(行号和复制按钮不能共存)
            //     }
            // },
            toolbarConfig: {
                pin: true, //固定工具栏
            },
            counter: {
                enable: true,//启用计数器
                type: 'text',
            },
            cache: {
                enable: true, //默认是true，但是当编辑时，需要先设置为false，之后在修改为true（因为原始内容会被本地缓存覆盖掉）
                "id": "vditorCache", //用于设置缓存id，当开启多个编辑器的时候就有用了
                after: () => {
                    console.log("cache"); //可以在缓存回调中进行自动保存到数据库（但是最好不好，因为稍微修改就会进行回调）
                },
            },
            tab: '    ',//设置tab键为4个空格
            //编辑器渲染完成后的回调（可以在该回调中开启缓存）
            after () {
                vditor.tip('编辑器渲染完成', 2000)
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
</script>