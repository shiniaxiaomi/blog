<#macro body class="container-xl">
    <body>
        <#--引入顶部导航栏-->
        <#include "../common/nav.ftl">
        <@nav/>

        <div class=${class}>
            <div class="row justify-content-center">
                <#nested >
            </div>
        </div>
    </body>
</#macro>