<#macro body class="container-xl" style="" active="">
    <body>
        <#--引入顶部导航栏-->
        <#include "../common/nav.ftl">
        <@nav active/>

        <div class="${class!}" style="${style!}">
            <div class="row justify-content-center">
                <#nested >
            </div>
        </div>
    </body>
</#macro>