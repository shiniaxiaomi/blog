<!doctype html>
<html lang="en">
<#include "../common/head.ftl">
<#include "../common/body.ftl">
<#include "../common/left.ftl">
<#include "../common/right.ftl">
<#include "../common/comment.ftl">
<@head>
    <script src="https://cdn.jsdelivr.net/npm/vditor@3.4.1/dist/js/lute/lute.min.js"></script>
    <!-- comment -->
    <link rel="stylesheet" href="/css/comment.css" />
    <script src="/js/comment.js"></script>
    <!-- layer -->
    <script src="https://cdn.jsdelivr.net/npm/layer-v3.0.3@1.0.1/layer.min.js"></script>
    <style>
        .gt-avatar >img{
            border-radius: 50%!important;
            width: 40px !important;
            height: auto !important;
            margin: 5px 0 0 5px;
        }
        body .layui-layer-lan .layui-layer-title{background:rgb(67 101 162); color:#fff; border: none;}
        body .layui-layer-page .layui-layer-content{padding: 10px}
        #comment-container .vditor-reset blockquote{
            border-left:.25em solid #d1d5da;
        }
    </style>
    <script>
        // 当从编辑页面返回时，刷新页面
        if(window.localStorage.getItem("needReload") === "true"){
            window.localStorage.setItem("needReload","false");
            setTimeout(function () {
                window.location.reload();
            },100);
        }
    </script>
</@head>
<@body style="max-width: 1300px" active="博客">
    <@left class="col-lg-4 d-none d-lg-block" style="max-width: 300px">
        <!--公共导航栏-->
        <div class="sticky-top" style="top: 100px;z-index: 1000">
            <div class=" input-group-sm">
                <input id="header-search" class="form-control w-100 mb-1" type="text">
            </div>
            <div id="toc" class="overflow-auto" style="height: 545px;font-size: 14px"></div>
        </div>
    </@left>
    <@right class="col-md-12 col-lg-8" style="border-left: 2px solid #e9ecef">
        <!--面包屑导航-->
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item">
                    <a href="/">首页</a>|<a href="/blog/${blogId!}">返回博客</a>
                </li>
                <li class="breadcrumb-item active" aria-current="page">${blogName!}</li>
            </ol>
        </nav>

        <!--评论-->
        <div id="comment-container">
            <div class="gt-container vditor-reset">
                <div class="gt-meta">
                    <span id="gt-counts" class="gt-counts">0</span><span>条评论</span>
                </div>
                <div class="gt-header">
                    <div class="gt-avatar gt-comment-avatar">
                        <img src="/img/GitHub.png" alt="头像">
                    </div>
                    <div class="gt-header-comment">
                        <form id="commentForm" onsubmit="return false;">
                            <textarea id="comment_content" class="gt-header-textarea " placeholder="说点什么"
                                      style="overflow-wrap: break-word; resize: none; height: 100px;"></textarea>
                            <div class="gt-header-controls">
                                <input id="username" name="username" class="comment-input" placeholder="Name : 必填" autocomplete="off">
                                <input id="email" name="email" class="comment-input" placeholder="Email : 接收回复(选填)" autocomplete="off">
                                <input id="github_username" name="github_username" class="comment-input" placeholder="GithubName : 选填" autocomplete="off">
                                <div style="float: right">
                                    <button id="cancelCommitBtn" class="gt-btn gt-btn-public" style="display: none" onclick="cancelCommit()"><span class="gt-btn-text">取消回复</span></button>
                                    <button id="replyBtn" class="gt-btn gt-btn-public" style="display: none" onclick="commitComment('reply')"><span class="gt-btn-text">回复</span></button>
                                    <button id="commitCommentBtn" class="gt-btn gt-btn-public" onclick="commitComment()"><span class="gt-btn-text">评论</span></button>
                                    <button class="gt-btn gt-btn-preview" onclick="preview()"><span class="gt-btn-text">预览</span></button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
                <div class="gt-comments">
                    <!--评论内容-->
                    <div id="commentsDiv" style="position: relative;"></div>
                </div>
            </div>
        </div>

    </@right>
</@body>
</html>

<script>
    $(function () {
        // 每次加载评论时都从缓存先读取信息
        selectEmailAndUserName();

        getCommentById(${commentId!},${blogId!});

        // 自动点击回复
        setTimeout(function () {
            $(".gt-reply-small-button").eq(0).click();
        },500)
    })
</script>