<#--定义顶部的header导航栏 默认将fixedTop参数设置为true,表示固定在顶部-->

<#macro nav active="首页">

<#--导航栏-->
    <nav class="navbar navbar-expand-lg navbar-light bg-white shadow sticky-top mb-3">
        <#--图标-->
        <nav class="navbar navbar-light">
        <span class="navbar-brand" >
            <a style="color: #3b86d8" class="font-weight-bold d-inline text-decoration-none" href="/user/login/form">是你啊小米</a>
            <img style="width: 11%;margin-bottom: 3px;margin-left: 5px;" src="/img/online.png" <#if !isLogin>hidden</#if>>
        </span>
        </nav>
        <#--用于小屏幕-->
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <#--菜单按钮-->
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <div class="navbar-nav mr-auto">
                <a class="<#if active=="首页">active</#if> nav-link" href="/">首页</a>
                <a class="<#if active=="目录">active</#if> nav-link" href="/index/toc">目录</a>
                <a class="<#if active=="标签">active</#if> nav-link" href="/index/tag">标签</a>
                <div class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="ToolMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        工具
                    </a>
                    <div class="dropdown-menu" aria-labelledby="toolDropdownMenuLink">
                        <a class="dropdown-item" href="/tool/regular">正则表达式</a>
                        <a class="dropdown-item" href="/word">单词</a>
                    </div>
                </div>
                <div class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="moreDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        更多
                    </a>
                    <div class="dropdown-menu" aria-labelledby="moreDropdownMenuLink">
<#--                        <a class="dropdown-item" href="/index/draft">草稿</a>-->
                        <a class="dropdown-item" href="/index/todo">待办</a>
                        <a class="dropdown-item" href="/index/about">关于</a>
                        <a class="dropdown-item" href="/admin">后台</a>
                    </div>
                </div>
                <a class="<#if active=="反馈">active</#if> nav-link" href="/index/feedback">反馈</a>
            </div>

            <#--搜索框-->
            <form class="form-inline my-2 my-lg-0" method="get" action="/blog/search/index">
                <input id="keywordInput" name="keyword" autocomplete="off" class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search">
                <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
            </form>
        </div>
    </nav>

</#macro>