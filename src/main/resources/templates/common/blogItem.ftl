<#macro blogItem blog>
    <#if blog??>
        <div>
            <#--标题-->
            <h4 class="overHide" style="padding: 0 60px 0 0">
                <a href="/blog?id=${blog.id!}" style="color:#3b86d8;">${blog.name!}</a>
            </h4>
            <#--标签-->
            <div class="d-inline text-muted">
                <span style="margin-right: 10px"><i class="iconfont icon-gengxinshijian"></i> ${blog.updateTime?string("yyyy-MM-dd")}</span>
                <span style="margin-right: 10px"><i class="iconfont icon-chakan4"></i> 0</span>
                <span style="margin-right: 10px"><i class="iconfont icon-pinglun3"></i> 0</span>

<#--                <a href="/moreBlogByTag?id=1212" class="badge badge-secondary">java</a>-->
<#--                <a href="/moreBlogByTag?id=1212" class="badge badge-secondary">mybatis</a>-->
                这里不显示徽章了，而是显示blog所在层级路径，并且每个路径都可以直接点击
            </div>
            <#--概述-->
            <div class="text-muted" style="padding: 7px 0 2px 2px">${blog.desc!}</div>
        </div>
        <hr style="margin: 10px 0">
    </#if>
</#macro>