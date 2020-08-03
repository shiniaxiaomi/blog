<!doctype html>
<html lang="en">
<#include "common/head.ftl">
<@head></@head>
<#include "common/body.ftl">
<@body>
    <#--引入介绍信息-->
    <#include "common/info.ftl">
    <#--主体-->
    <div class="container-xl">
        <div class="row justify-content-center">
            <!--左（简介）-->
            <!--先设置为全部尺寸隐藏，然后再设置大于lg时显示-->
            <div class="col-lg-3 d-none d-lg-block">
                <@info/>
            </div>

            <!--中（blog）-->
            <!--全部尺寸都设置为自动-->
            <div class="col-sm-12 col-md-10 col-lg-9">
                <div>
                    <p class="h5">置顶博客<span class="float-right h6">更多</span></p>
                    <hr/>
                    <p class="h6"><a href="/blog/1">标题名称</a></p>
                    创建时间：xxx  更新时间：xxx 浏览量：xxx
                    标签：xxx
                    描述：xxx
                </div>
                <hr/>
                <div>
                    <p class="h5">最新博客<span class="float-right h6">更多</span></p>
                    <hr/>
                    <p class="h6"><a href="/blog/1">标题名称</a></p>
                    创建时间：xxx  更新时间：xxx 浏览量：xxx
                    标签：xxx
                    描述：xxx
                </div>
            </div>
        </div>

        <#--备案号-->
        <div style="text-align: center;margin: 10px;">
            <a href="http://www.beian.miit.gov.cn" style="color: rgb(118, 118, 118); text-decoration: none;">
                ICP证 : 浙ICP备18021271号
            </a>
        </div>
    </div>
</@body>
</html>

<script>
    $(function () {
        //开启提示工具
        $('[data-toggle="tooltip"]').tooltip();
    })
</script>