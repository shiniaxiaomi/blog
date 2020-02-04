<#--blog的相关参数-->

<#--如果是blog-->
<#if blog??>
    <#--博客参数-->
    <span style="margin-right: 10px">
        <img class="myIcon" src="/icons/calendar.svg" title="创建日期">
        ${blog.createTime?string("yyyy-MM-dd")}
    </span>
    <span style="margin-right: 10px">
        <img class="myIcon" src="/icons/arrow-repeat.svg" title="更新日期">
        ${blog.updateTime?string("yyyy-MM-dd")}
    </span>
    <span style="margin-right: 10px">
        <img class="myIcon" src="/icons/Eye.svg" title="观看人数">
        ${blog.hot!}
    </span>

    <#if isLogin?? && isLogin==true>
        <#--编辑按钮-->
        <a type="button" style="margin-top: -2px;" class="btn btn-link btn-xs px-0"
           href="javascript:void(0);" onclick="deleteFunc('blog',${blog.id!})">删除博客</a>
        <a type="button" style="margin-top: -2px;" class="btn btn-link btn-xs px-0"
           href="/editDesc?blogId=${blog.id!}">编辑描述</a>
        <a type="button" style="margin-top: -2px;" class="btn btn-link btn-xs px-0"
           href="/editBlog?blogId=${blog.id!}">编辑博客</a>
    </#if>


<#elseif blogName??>
<#--如果是本地草稿-->
<#--博客参数-->
    <span style="margin-right: 10px">
        <img class="myIcon" src="/icons/calendar.svg" title="创建日期">
        <span id="createTime"></span>
    </span>
    <span style="margin-right: 10px">
        <img class="myIcon" src="/icons/arrow-repeat.svg" title="更新日期">
        <span id="updateTime"></span>
    </span>
    <span style="margin-right: 10px">
        <img class="myIcon" src="/icons/Eye.svg" title="观看人数">
        0
    </span>

    <#--编辑按钮-->
    <a type="button" style="margin-top: -2px;" class="btn btn-link btn-xs px-0"
       href="javascript:void(0);" onclick="deleteFunc('localDraft','${blogName!}')">删除草稿</a>
    <a type="button" style="margin-top: -2px;" class="btn btn-link btn-xs px-0"
       href="/editLocalDraftDesc?blogId=${blogName!}">编辑描述</a>
    <a type="button" style="margin-top: -2px;" class="btn btn-link btn-xs px-0"
       href="/editLocalDraft?blogId=${blogName!}">编辑草稿</a>

</#if>



