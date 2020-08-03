<#macro body>
    <body>
        <#--引入顶部导航栏-->
        <#include "../common/nav.ftl">
        <@nav/>

        <#nested >

        <#--备案号-->
        <div style="text-align: center;margin: 10px;">
            <a href="http://www.beian.miit.gov.cn" style="color: rgb(118, 118, 118); text-decoration: none;">
                ICP证 : 浙ICP备18021271号
            </a>
        </div>
    </body>
</#macro>