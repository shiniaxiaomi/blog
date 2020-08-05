<!doctype html>
<html lang="en">
<#include "common/head.ftl">
<#include "common/body.ftl">
<#include "common/left.ftl">
<#include "common/right.ftl">
<#include "common/info.ftl">
<@head></@head>
<@body>
    <@left class="col-lg-3 d-none d-lg-block">
        <@info/>
    </@left>

    <@right class="col-sm-12 col-md-10 col-lg-9">
        <div>
            <p class="h5">置顶博客<span class="float-right h6">更多</span></p>
            <hr/>
            <#if stickBlogList??>
                <#list stickBlogList as blog>
                    <p class="h6"><a href="/blog/${blog.id!}">${blog.name!}</a></p>
                    创建时间：${blog.createTime!}  更新时间：${blog.updateTime!} 浏览量：xxx
                    标签：xxx
                    描述：${blog.desc!}
                </#list>
            </#if>
        </div>
        <hr/>
        <div>
            <p class="h5">最新博客<span class="float-right h6">更多</span></p>
            <hr/>
            <#if newestBlogList??>
                <#list newestBlogList as blog>
                    <p class="h6"><a href="/blog/${blog.id!}">${blog.name!}</a></p>
                    创建时间：${blog.createTime?string("yyyy-MM-dd")}  更新时间：${blog.updateTime?string("yyyy-MM-dd")} 浏览量：xxx
                    标签：xxx
                    描述：${blog.desc!}
                </#list>
            </#if>
        </div>
    </@right>
</@body>
</html>

<script>
    $(function () {
        //开启提示工具
        $('[data-toggle="tooltip"]').tooltip();
    })
</script>