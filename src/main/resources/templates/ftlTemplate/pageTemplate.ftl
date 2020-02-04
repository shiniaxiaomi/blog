<#--分页-->
<#macro paging nowPage link type="" blogSize=0 >

<#if nowPage??>
    <#if type="">
        <nav aria-label="Page navigation example">
            <ul class="pagination justify-content-end">
                <a class="page-link" href="${link!}?page=1">首页</a>
                <li class="page-item <#if nowPage==1>disabled</#if>">
                    <a class="page-link" href="${link!}?page=${nowPage-1}" tabindex="0" aria-disabled="true">Previous</a>
                </li>
                <li class="page-item  <#if blogSize lt 10>disabled</#if>">
                    <a class="page-link" href="${link!}?page=${nowPage+1}">Next</a>
                </li>
                <div class="input-group" style="display: contents;">
                    <input id="page" type="text" style="width: 70px;" value="${nowPage!}">
                    <div class="input-group-append">
                        <button class="btn btn-outline-secondary" type="button"
                                onclick="window.location.href='${link!}?page='+$('#page').val()">跳转</button>
                    </div>
                </div>
            </ul>
        </nav>
    <#elseif type="search">
        <nav aria-label="Page navigation example">
            <ul class="pagination justify-content-end">
                <a class="page-link" href="${link!}&page=1">首页</a>
                <li class="page-item <#if nowPage==1>disabled</#if>">
                    <a class="page-link" href="${link!}&page=${nowPage-1}" tabindex="0" aria-disabled="true">Previous</a>
                </li>
                <li class="page-item  <#if blogSize lt 10>disabled</#if>">
                    <a class="page-link" href="${link!}&page=${nowPage+1}">Next</a>
                </li>
                <div class="input-group" style="display: contents;">
                    <input id="page" type="text" style="width: 70px;" value="${nowPage!}">
                    <div class="input-group-append">
                        <button class="btn btn-outline-secondary" type="button"
                                onclick="window.location.href='${link!}&page='+$('#page').val()">跳转</button>
                    </div>
                </div>
            </ul>
        </nav>
    <#elseif type="tag">
        <nav aria-label="Page navigation example">
            <ul class="pagination justify-content-end">
                <a class="page-link" href="${link!}?id=${tag.id!}&page=1">首页</a>
                <li class="page-item <#if nowPage==1>disabled</#if>">
                    <a class="page-link" href="${link!}?id=${tag.id!}&page=${nowPage-1}" tabindex="0" aria-disabled="true">Previous</a>
                </li>
                <li class="page-item  <#if blogSize lt 10>disabled</#if>">
                    <a class="page-link" href="${link!}?id=${tag.id!}&page=${nowPage+1}">Next</a>
                </li>
                <div class="input-group" style="display: contents;">
                    <input id="page" type="text" style="width: 70px;" value="${nowPage!}">
                    <div class="input-group-append">
                        <button class="btn btn-outline-secondary" type="button"
                                onclick="window.location.href='${link!}?id=${tag.id!}&page='+$('#page').val()">跳转</button>
                    </div>
                </div>
            </ul>
        </nav>
    <#elseif type="year">
        <nav aria-label="Page navigation example">
            <ul class="pagination justify-content-end">
                <a class="page-link" href="${link!}?year=${year!}&page=1">首页</a>
                <li class="page-item <#if nowPage==1>disabled</#if>">
                    <a class="page-link" href="${link!}?year=${year!}&page=${nowPage-1}" tabindex="0" aria-disabled="true">Previous</a>
                </li>
                <li class="page-item  <#if blogSize lt 10>disabled</#if>">
                    <a class="page-link" href="${link!}?year=${year!}&page=${nowPage+1}">Next</a>
                </li>
                <div class="input-group" style="display: contents;">
                    <input id="page" type="text" style="width: 70px;" value="${nowPage!}">
                    <div class="input-group-append">
                        <button class="btn btn-outline-secondary" type="button"
                                onclick="window.location.href='${link!}?year=${year!}&page='+$('#page').val()">跳转</button>
                    </div>
                </div>
            </ul>
        </nav>
    </#if>
</#if>

</#macro>