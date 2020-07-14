<#macro blogItem blog>
    <#if blog??>
        <div>
            <#--标题-->
            <h4 class="overHide" style="padding: 0 60px 0 0">
                <a href="/blog?id=${blog.id!}" style="color:#3b86d8;">${blog.name!}</a>
                <a href="/blog/edit?id=${blog.id!}" style="font-size: 60%">编辑</a>
                <a href="javascript:void(0);" onclick="deleteBlogByHeader(${blog.id!})" style="font-size: 60%">删除</a>
                <a href="javascript:void(0);" onclick="renameBlog(${blog.id!})" style="font-size: 60%">重命名</a>
                <a href="javascript:void(0);" onclick="openAddTagModalOnBlog(${blog.id!})"
                   data-toggle="modal" data-target="#createTagToBlogModal"
                    style="font-size: 60%">添加标签</a>
            </h4>
            <#--标签-->
            <div class="d-inline text-muted">
                <span style="margin-right: 10px"><i class="iconfont icon-gengxinshijian"></i> ${blog.updateTime?string("yyyy-MM-dd")}</span>
                <span style="margin-right: 10px"><i class="iconfont icon-chakan4"></i> 0</span>
                <span style="margin-right: 10px"><i class="iconfont icon-pinglun3"></i> 0</span>

                <span class="path" blogId="${blog.id!}"></span>
            </div>
            <#--概述-->
            <div class="text-muted" style="padding: 7px 0 2px 2px">${blog.desc!}</div>
        </div>
        <hr style="margin: 10px 0">
    </#if>
</#macro>