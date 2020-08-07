<#macro body class="container-xl" style="">
    <body>
        <#--引入顶部导航栏-->
        <#include "../common/nav.ftl">
        <@nav/>

        <div class="${class!}" style="${style!}">
            <div class="row justify-content-center">
                <#nested >
            </div>
        </div>
    </body>
</#macro>