<#macro page>
    <nav aria-label="Page navigation">
        <ul class="pagination" style="flex-flow: row wrap;">
            <li class="page-item">
                <a class="page-link" aria-label="Previous"
                   href="${url!}<#if currentPage??><#if currentPage-1 lt 1>${pages!}<#else>${currentPage-1}</#if></#if>" >
                    <span aria-hidden="true">&laquo;</span>
                    <span class="sr-only">Previous</span>
                </a>
            </li>
            <#list 1..pages as page>
                <li class="page-item <#if currentPage??><#if page==currentPage>active</#if></#if>">
                    <a class="page-link" href="${url!}${page!}">${page!}</a>
                </li>
            </#list>
            <li class="page-item">
                <a class="page-link" aria-label="Next"
                   href="${url!}<#if currentPage??><#if currentPage+1 gt pages>1<#else>${currentPage+1}</#if></#if>" >
                    <span aria-hidden="true">&raquo;</span>
                    <span class="sr-only">Next</span>
                </a>
            </li>
            <li class="page-item disabled">
                <a class="page-link" href="#" aria-label="Next">
                    总计：<span aria-hidden="true" id="totalSpan">${total!}</span>
                </a>
            </li>
        </ul>
    </nav>
</#macro>