<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <title>写博客</title>

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="/css/bootstrap.min.css">

    <#-- editormd -->
    <link rel="stylesheet" href="/editor.md/css/editormd.min.css" />
    <#--blog-->
    <link rel="stylesheet" href="/css/blog.css">
    <#--tagsinput-->
    <link href="tagsinput/tokenfield.css" rel="stylesheet" type="text/css">

    <style>
        /*编辑页面下的toc样式*/
        #custom-toc-container .markdown-toc-list{
            padding-left: 20px;
        }
        #custom-toc-container .markdown-toc-list ul{
            display: flex;
            flex-wrap: wrap;
            padding-left: 20px;
            margin-bottom: 0;
            list-style: none;
            flex-direction: column!important;
        }
        #custom-toc-container .markdown-toc-list li{
            display: block;
        }
        /*小屏幕*/
        @media (max-width: 768px) {
            .whiteBlock{
                padding: 0px;
            }
            .vh-65{
                height: 100px;
            }
        }
    </style>

</head>
<body>

<#--引入顶部导航栏-->
<#include "ftlTemplate/navTemplate.ftl">
<@header/>

<div class="row mx-2">
    <#--左侧(目录)-->
    <div class="col-md-3" style="max-width: 350px;">
        <#--toc目录-->
        <div class="whiteBlock px-0 ">

            <#--如果是编辑页面,则出现按钮组-->
            <#if editFlag ??>
                <div class="center mb-2">
                    <#--保存按钮组-->
                    <div class="btn-group btn-group-sm mx-1" role="group">
                        <button id="btnGroupDrop1" type="button" class="btn btn-secondary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            保存
                        </button>
                        <div class="dropdown-menu" aria-labelledby="btnGroupDrop1">
                            <#if editFlag=="edit" || editFlag=="editBlog">
                                <a class="dropdown-item" href="javascript:void(0);" data-toggle="tooltip" data-placement="top"
                                   onclick="openModalFunc('saveBlog')"
                                   <#if isLogin==true>title="将博客保存到线上"<#else>title="请先登入"</#if> >保存博客</a>
                            </#if>
                            <#if editFlag=="editDesc">
                                <a class="dropdown-item" href="javascript:void(0);" data-toggle="tooltip" data-placement="top"
                                   onclick="saveDesc()"
                               <#if isLogin==true>title="将博客描述保存到线上"<#else >title="请先登入"</#if> >保存博客描述</a>
                            </#if>
                            <#if editFlag=="edit" || editFlag=="editLocalDraft">
                                <a class="dropdown-item" href="javascript:void(0);" data-toggle="tooltip" data-placement="top"
                                   onclick="openModalFunc('saveLocalDraft')"
                                   title="无需登入,直接保存在本地">保存到本地草稿</a>
                            </#if>
                            <#if editFlag=="editLocalDraftDesc">
                                <a class="dropdown-item" href="javascript:void(0);" data-toggle="tooltip" data-placement="top"
                                   onclick="saveLocalDesc()"
                                   title="无需登入,直接保存在本地">保存到本地草稿描述</a>
                            </#if>
                        </div>
                    </div>
                    <#--其他按钮组-->
                    <div class="btn-group btn-group-sm mx-1" role="group">
                        <button id="btnGroupDrop2" type="button" class="btn btn-secondary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            其他
                        </button>
                        <div class="dropdown-menu" aria-labelledby="btnGroupDrop2">
                            <a class="dropdown-item" href="javascript:void(0);" id="refreshTocBtn"
                               data-toggle="tooltip" data-placement="top" title="打开右侧的预览视图后目录会自动刷新">刷新目录</a>
                        </div>
                    </div>
                    <hr>
                </div>
            </#if>

            <p class="ml-3">目录</p>
            <div class="overflow-auto vh-65">
                <div id="custom-toc-container"></div>
            </div>
        </div>
    </div>

    <#--右侧(编辑器)-->
    <div class="col-md-9">
        <div class="whiteBlock vh-85">
            <#--编辑器-->
            <div id="test-editor">
                <textarea style="display:none;"
                ><#if editFlag=="editBlog">${blog.md!}</#if><#if editFlag=="editDesc">${blog.desc!}</#if></textarea>
            </div>
        </div>
    </div>
</div>


<#--modal-->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="headerName">headerName</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form>
                    <div class="form-group">
                        <label for="blogName" class="col col-form-label">名称</label>
                        <div class="col">
                            <input class="form-control" id="blogName" placeholder=""
                                   value="<#if editFlag=="editBlog">${blog.name!}<#elseif editFlag=="editLocalDraft">${blogName!}</#if>">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="blogTags" class="col col-form-label">标签</label>
                        <div class="col" >
                            <input id="tagInput" />
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" id="saveBtn" flag="">saveBtnName</button>
            </div>
        </div>
    </div>
</div>



<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="/js/jquery.min.js"></script>
<script src="/js/popper.min.js"></script>
<script src="/js/bootstrap.min.js"></script>
<!-- editormd -->
<script src="/editor.md/editormd.min.js"></script>
<!-- 弹窗 -->
<script src="/js/pop.js"></script>
<!-- tagsinput -->
<script src="tagsinput/tokenfield.web.js"></script>

<#--引入登入模板(该模板需要刚在jquery加载之后的body标签内)-->
<#include  "ftlTemplate/loginTemplate.ftl">

<script src="/js/websql.js"></script>
<script src="/js/edit.js"></script>


<script type="text/javascript">

    let isMobile = /Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent);
    var isLogin=${isLogin?string('true', 'false')};
    var blogId=<#if blog??>${blog.id}<#else>undefined</#if>;
    var tokenfield =undefined;
    var editor=undefined;

    //如果编辑本地草稿,则先查询出对应的内容
    var localDraft=undefined;
    $(function() {

        //如果编辑本地草稿,则先读取
        <#if editFlag=="editLocalDraft" || editFlag=="editLocalDraftDesc">
            selectDraftByName("${blogName!}",function (data) {
                localDraft=data[0];
            })
        </#if>

        //开启提示工具
        $('[data-toggle="tooltip"]').tooltip();
        //开启标签
        tokenfield = new Tokenfield({
            el: document.querySelector('#tagInput'),
            items: ${tipTags!"[]"},
            setItems: <#if blog??>${blog.tagNames!"[]"}<#else>[]</#if>,
        });
        //编辑器
        editor = editormd("test-editor", {
            width               : "100%",
            height              : 600,
            path                : "/editor.md/lib/",
            markdown            : "",
            saveHTMLToTextarea  : true,//开启获取html代码
            codeFold            : true,//代码折叠
            atLink              : false,//关闭@link的解析
            emailLink           : false,//关闭@email的解析
            todoList            : true,//开启todolist
            //开启图片上传
            imageUpload         : true,
            imageFormats        : ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
            imageUploadURL      : "/uploadImg",//上传图片的请求链接,
            tocStartLevel       : 1 ,//toc目录开始节点
            //加载成功后的回调
            onload : function() {
                //开启toc目录
                editor.config({
                    tocContainer : "#custom-toc-container",
                    tocDropdown   : false
                });
                //判断如果是移动端,则关闭右侧预览
                if(isMobile){
                    editor.unwatch();
                }

                //如果编辑本地草稿或本地草稿描述
                <#if editFlag=="editLocalDraft">
                    this.setMarkdown(localDraft.md);
                <#elseif editFlag=="editLocalDraftDesc">
                    this.setMarkdown(localDraft.desc);
                </#if>

                <#--
                this.fullscreen();
                this.unwatch();
                this.watch().fullscreen();
                this.setMarkdown("");
                this.width("100%");
                this.height(480);
                this.resize("100%", 640);
                buildTocHtml();
                -->
            }
        });
        <#--
        testEditor.show();//显示编辑器
        testEditor.hide();//隐藏编辑器
        editor.appendMarkdown("dsfsdfsdf");//追加markdown的内容
        editor.getMarkdown();//获取markdown
        editor.getHTML();//获取html
        editor.watch();//开启预览
        editor.unwatch();//关闭预览
        editor.previewing();//全屏预览
        editor.fullscreen();//全屏编辑
        editor.showToolbar();//显示工具栏
        editor.hideToolbar();//隐藏工具栏
        editor.insertValue("????");testEditormd.focus();//在光标所在处插入文本
        -->
    });

    //刷新目录
    $("#refreshTocBtn").click(function () {
        editor.watch();
    })

    //不同modal的按钮点击所触发的事件(控制不同的modal的标题和按钮名称)
    function openModalFunc(flag) {
        //控制不同的标题
        switch (flag) {
            case "saveBlog":
                //如果未登入
                if(!isLogin){
                    pop.prompt("请先登入", 1500);
                    return;
                }
                $("#headerName").text("保存博客");
                $("#saveBtn").text("保存博客");
                $("#saveBtn").attr("flag","saveBlog");
                break;
            case "saveLocalDraft":
                $("#headerName").text("保存本地草稿");
                $("#saveBtn").text("保存本地草稿");
                $("#saveBtn").attr("flag","saveLocalDraft");
                tokenfield.setItems({id:"本地草稿",name:"本地草稿"});
                break;
        }
        //显示modal
        $("#myModal").modal();
    }

    //modal框的点击事件(事件的分发)
    $("#saveBtn").click(function () {
        var flag= $("#saveBtn").attr("flag");
        switch (flag) {
            case "saveBlog":
                saveBlog();
                break;
            case "saveLocalDraft":
                saveLocalDraft();
                break;
        }
    })

    //保存blog的函数
    function saveBlog() {
        $.post("/saveBlog",{
            id:blogId,
            name:$("#blogName").val(),
            tagNames:getTags(),
            md:editor.getMarkdown(),
            tocHtml:buildTocHtml(),//生成toc内容
            mdHtml: getHtml(),
        },function (data,status) {
            //成功提示
            pop.prompt(data.data, 1500);
            resetSaveModal();
            goHome();
        })
    }

    //保存本地草稿
    function saveLocalDraft() {
        var date=new Date().Format("yyyy-MM-dd");
        if(localDraft==undefined){
            //不存在
            insertDraft({
                id: $("#blogName").val(),
                desc: "",
                descHtml: "",
                md: editor.getMarkdown(),
                mdHtml: getHtml(),
                tocHtml: buildTocHtml(),
                createTime: date,
                updateTime: date,
                tagNames:getTags()
            })
        }else{
            //先删除,修改后插入数据
            deleteDraftByName(localDraft.id);
            localDraft.md=editor.getMarkdown();
            localDraft.mdHtml=getHtml();
            localDraft.tocHtml=buildTocHtml();
            localDraft.tagNames=getTags();
            localDraft.updateTime=date;
            insertDraft(localDraft);
        }

        resetSaveModal();
        pop.prompt("保存成功");
        goDraft();
    }

    //保存本地草稿描述
    function saveLocalDesc() {
        pop.confirm("确定保存本地草稿描述吗?",function () {
            var date=new Date().Format("yyyy-MM-dd");

            //先删除,修改后插入数据
            deleteDraftByName(localDraft.id);
            localDraft.desc=editor.getMarkdown();
            localDraft.descHtml=getHtml();
            localDraft.updateTime=date;
            insertDraft(localDraft);

            resetSaveModal();
            pop.prompt("保存成功");
            goDraft();
        })

    }

    //保存博客描述
    function saveDesc() {
        pop.confirm("确定保存博客描述吗?",function () {
            $.post("/saveDesc",{
                id:blogId,
                desc:editor.getMarkdown(),
                descHtml:getHtml()
            },function (data,status) {
                //成功提示
                pop.prompt(data.data,1000);
                goHome();
            })
        })
    }


</script>

</body>
</html>