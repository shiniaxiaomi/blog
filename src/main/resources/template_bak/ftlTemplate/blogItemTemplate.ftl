
<#--列表展示时的每个blogItem-->

<#macro blogItem blog tagColor>
    <#if blog??>
        <div>
            <#--分界线-->
            <hr>
            <#--标题-->
            <h3 class="blue">
                <a href="/blog?id=${blog.id!}">${blog.name!}</a>
            </h3>

            <#--参数-->
            <#include "blogParamsTemplate.ftl">

            <#--标签-->
            <div style="margin-bottom: 10px">
                <#if blog.tags??>
                    <#list blog.tags as tag>
                        <a href="/moreBlogByTag?id=${tag.id!}" class="badge ${tagColor!}">${tag.name!}</a>
                    </#list>
                </#if>
            </div>

            <#--概述,通过markdown的形式进行描述,那么就可以随意的添加图片链接了-->
            <div class="text-muted">
                ${blog.descHtml!}
            </div>
        </div>
    </#if>
</#macro>