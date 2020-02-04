<#--定义顶部的header导航栏 默认将fixedTop参数设置为true,表示固定在顶部-->

<#macro header fixedTop=true >
    <#--导航栏-->
    <nav class="navbar navbar-expand-lg navbar-light bg-white px-3 shadow <#if fixedTop==true>fixed-top</#if>">
    <#--图标-->
    <nav class="navbar navbar-light">
        <span class="navbar-brand" >
            <a class="text-decoration-none" href="#">
                <img src="/img/myself111.jpg" width="30" height="30"
                     class="d-inline-block align-top rounded-circle" alt="">
            </a>
            <a class="font-weight-bold d-inline text-decoration-none" href="javascript:void(0);" id="openLoginBtn">是你啊小米</a>
        </span>
    </nav>
    <#--用于小屏幕-->
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <#--菜单按钮-->
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link" href="/">首页 <span class="sr-only">(current)</span></a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/aboutMe">关于</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/draftHome">草稿</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/edit">写博客</a>
            </li>
        </ul>

        <#--搜索框-->
        <form class="form-inline my-2 my-lg-0" action="/searchMore">
            <input id="keywordInput" name="blogName" class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search">
            <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
        </form>

    </div>
</nav>
</#macro>