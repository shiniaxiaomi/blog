<#--定义首页的blog列表-->

<#macro blogList blogs tagColor >
    <div>
        <#--标题和更多-->
        <#nested>
    </div>
    <!--每篇置顶博客-->
    <#if blogs??>
        <#list blogs as blog>
            <#include "blogItemTemplate.ftl">
            <@blogItem blog=blog tagColor=tagColor/>
        </#list>
    </#if>
</#macro>