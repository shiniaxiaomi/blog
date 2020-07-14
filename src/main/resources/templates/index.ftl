<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <link rel="stylesheet" href="/css/blog.css">

    <#include "common/headerCommon.ftl">
    <@header/>

    <style>
        .list-group-item {
            padding: 6px 0px;
            cursor: pointer;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }
    </style>

</head>
<body>

<#--引入顶部导航栏-->
<#include "common/navCommon.ftl">
<@nav/>

<div id="container" class="container" style="margin-top:95px;max-width: 1350px">
    <div id="test" class="row justify-content-center">

        <!--左（简介）-->
        <div class="col-lg-3 d-none d-lg-block" style="max-width: 255px"><!--先设置为全部尺寸隐藏，然后再设置大于lg时显示-->
            <div class="sticky-top whiteBlock shadow-lg p-3 mb-5 bg-white rounded" style="top: 95px">
                <#--引入介绍信息-->
                <#include "common/infoCommon.ftl">
                <@info/>
            </div>
        </div>

        <!--中（blog）-->
        <div class="col-sm-12 col-md-10 col-lg-7"><!--全部尺寸都设置为自动-->
            <div class="whiteBlock shadow-lg p-3 mb-3 bg-white rounded">
                <#include "common/blogItem.ftl" />
                <div class="text-muted d-inline" style="font-size: 20px;">置顶博客</div>
                <hr>
                <div>
                    <!--每篇置顶博客-->
                    <#if stickBlogs??>
                        <#list stickBlogs as blog>
                            <@blogItem blog=blog/>
                        </#list>
                    </#if>
                </div>
            </div>

            <div id="newestBlogContainer" class="whiteBlock shadow-lg p-3 mb-5 bg-white rounded">
                <div class="text-muted d-inline" style="font-size: 20px;">最新博客</div>
                <hr>
                <div id="newestBlogDiv">
                    <!--每篇置顶博客-->
                    <#if newestBlogs??>
                        <#list newestBlogs as blog>
                            <@blogItem blog=blog/>
                        </#list>
                    </#if>
                </div>
            </div>
        </div>

        <!--右（目录）-->
        <div class=" col-md-1 d-none d-md-block" style="max-width: 120px;"><!--先设置为全部尺寸隐藏，然后再设置大于sm时显示-->
            <div class="sticky-top tagPadding shadow-lg mb-5 bg-white rounded" style="top: 95px;">
                <ul id="tags" class="list-group list-group-flush" style="text-align: center"></ul>
            </div>
        </div>

    </div>
</div>


<#include "common/beianCommon.ftl">
<@beian/>


</body>
</html>



<script>

    let page=2; // 当前页数
    let isIncr=false; // 是否新增

    $(function () {
        //开启提示工具
        $('[data-toggle="tooltip"]').tooltip();

        // 定时200毫秒修改一次状态
        setInterval(function () {
            if(isIncr===false){
                isIncr=true;
            }
        },200);

        // 滚动分页实现
        $(document).scroll(function () {
            // 限流
            if(isIncr===false){
                return;
            }

            //滚动与顶部的距离
            var scrollTop = Math.ceil($("html").eq(0).scrollTop());
            //jq转js
            var j = $("html").eq(0)[0];
            //滚动条高度
            var scrollHeight = j.scrollHeight;
            //滚动条高度
            var zj = j.clientHeight;
            // console.log("整体高：" + scrollHeight + " >> 组件高：" + zj + "距离顶部高：" + scrollTop)

            //获取滚动条与底部的距离，如果等于0 证明已下拉到最底部
            if (scrollHeight - scrollTop - zj === 0) {
                isIncr=false; //恢复标志位

                //你自己的分页逻辑
                if(page===-1){
                    return; //如果
                }
                $.get("/blog/page?index="+page,function (data,status) {
                    if(data.code){
                        if(data.data.length===0){
                            page=-1;
                            $("#newestBlogContainer").append("<div class='text-muted text-center' style='padding: 7px 0 2px 2px'>已经到底了</div>");
                            return;
                        }
                        // 将数据动态的插入到尾部
                        let div=$("#newestBlogDiv");
                        for(var i=0;i<data.data.length;i++){
                            var blog=data.data[i];
                            div.append(`
                                <div>
                                    <#--标题-->
                                    <h4 class="overHide" style="padding: 0 60px 0 0">
                                        <a href="/blog?id=`+blog.id+`" style="color:#3b86d8;">`+blog.name+`</a>
                                        <a href="/blog/edit?id=`+blog.id+`" style="font-size: 60%">编辑</a>
                                        <a href="javascript:void(0);" onclick="deleteBlogByHeader(`+blog.id+`)" style="font-size: 60%">删除</a>
                                        <a href="javascript:void(0);" onclick="renameBlog(`+blog.id+`)" style="font-size: 60%">重命名</a>
                                        <a href="javascript:void(0);" data-toggle="modal" data-target="#createTagToBlogModal"
                                            onclick="openAddTagModalOnBlog(`+blog.id+`)" style="font-size: 60%">添加标签</a>
                                    </h4>
                                    <#--标签-->
                                    <div class="d-inline text-muted">
                                        <span style="margin-right: 10px"><i class="iconfont icon-gengxinshijian"></i> `+blog.updateTime.slice(0,10)+`</span>
                                        <span style="margin-right: 10px"><i class="iconfont icon-chakan4"></i> 0</span>
                                        <span style="margin-right: 10px"><i class="iconfont icon-pinglun3"></i> 0</span>

                                        <span class="path" blogId="`+blog.id+`"></span>
                                    </div>
                                    <#--概述-->
                                    <div class="text-muted" style="padding: 7px 0 2px 2px">`+blog.desc+`</div>
                                </div>
                                <hr style="margin: 10px 0">
                        `);
                        }
                        page++;
                    }else{
                        layer.msg(data.msg);
                    }
                })
            }
        });

    })


    function renameBlog(id){
        layer.prompt({title: '输入博客名称'}, function(value, index){
            layer.close(index);
            $.post("/blog/update",{id:id,name:value},function (data,status) {
                if(data.code){
                    window.location.reload();//刷新页面
                }
                layer.msg(data.msg);
            })
        });
    }

    function deleteBlogByHeader(id){
        layer.confirm("确定要删除吗",{
            btn: ['确定','取消'] //按钮
        },function () {
            $.post("/blog/delete",{id:id},function (data,status) {
                if(data.code){
                    window.location.reload();//刷新页面
                }
                layer.msg(data.msg);
            })
        })
    }

</script>