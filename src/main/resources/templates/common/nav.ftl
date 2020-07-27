<#--定义顶部的header导航栏 默认将fixedTop参数设置为true,表示固定在顶部-->

<#macro nav>

<#--导航栏-->
    <nav class="navbar navbar-expand-lg navbar-light bg-white shadow sticky-top mb-3">
        <#--图标-->
        <nav class="navbar navbar-light">
        <span class="navbar-brand" >
            <a style="color: #3b86d8" class="font-weight-bold d-inline text-decoration-none" href="#" id="openLoginBtn">是你啊小米</a>
        </span>
        </nav>
        <#--用于小屏幕-->
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <#--菜单按钮-->
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <div class="navbar-nav mr-auto">
                <a class="nav-link active" href="/">首页 <span class="sr-only">(current)</span></a>
                <a class="nav-link" href="/aboutMe">关于</a>
                <a class="nav-link" href="#" data-toggle="modal" data-target="#createBlogModal" onclick="selectBlog()">目录</a>
                <a class="nav-link" href="#" data-toggle="modal" data-target="#createTagModal">标签</a>

                <div class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        更多
                    </a>
                    <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                        <a class="dropdown-item" href="/draft">草稿</a>
                        <a class="dropdown-item" href="/todo">待办</a>
                        <a class="dropdown-item" href="/word">单词</a>
                        <a class="dropdown-item" href="/admin">后台</a>
                    </div>
                </div>
            </div>

            <#--搜索框-->
            <form class="form-inline my-2 my-lg-0" action="/searchMore">
                <input id="keywordInput" name="blogName" class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search">
                <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
            </form>
        </div>
    </nav>

</#macro>