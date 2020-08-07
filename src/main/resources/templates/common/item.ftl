<#macro item blogList>
    <#if blogList??>
        <#list blogList as blog>
            <p class="h4"><a href="/blog/${blog.id!}">${blog.name!}</a></p>
            🕑&nbsp;${blog.createTime!?string("yyyy-MM")}&nbsp;&nbsp;
            ⏳&nbsp;${blog.updateTime!?string("yyyy-MM-dd")}&nbsp;&nbsp;
            👀&nbsp;${blog.visitCount!0}&nbsp;&nbsp;
            🔖&nbsp;
            <#list blog.tags as tag>
                <!--默认访问第一页-->
                <a class="tagLink" href="/tag/${tag.id!}/1">${tag.name!}</a>&nbsp;
            </#list>
            <br>
            <div class="vditor-reset text-muted">
                ${blog.desc!}
            </div>
            <hr>
        </#list>
    </#if>
</#macro>