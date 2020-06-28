<#--定义顶部的header导航栏 默认将fixedTop参数设置为true,表示固定在顶部-->

<#macro nav>
<#--导航栏-->
<nav class="navbar navbar-expand-lg navbar-light bg-white px-3 shadow fixed-top">
        <#--图标-->
        <nav class="navbar navbar-light">
        <span class="navbar-brand" >
            <a class="text-decoration-none" href="#">
<#--                <img src="/img/myself.jpg" width="30" height="30"-->
<#--                     class="d-inline-block align-top rounded-circle" alt="">-->
            </a>
            <a style="color: #3b86d8" class="font-weight-bold d-inline text-decoration-none" href="javascript:void(0);" id="openLoginBtn">是你啊小米</a>
        </span>
        </nav>
        <#--用于小屏幕-->
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <#--菜单按钮-->
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item">
                    <a class="nav-link" href="/">首页 <span class="sr-only">(current)</span></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/aboutMe">关于</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/draft">草稿</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="javascript:void(0);" data-toggle="modal" data-target="#createBlogModal" <#--onclick="createBlog()"-->>写博客</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="javascript:void(0);" data-toggle="modal" data-target="#createTagModal">标签</a>
                </li>
            </ul>

            <#--搜索框-->
            <form class="form-inline my-2 my-lg-0" action="/searchMore">
                <input id="keywordInput" name="blogName" class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search">
                <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
            </form>

        </div>
    </nav>

    <!--创建blog弹框-->
    <div class="modal fade" id="createBlogModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="createTagModalLabel">创建Blog</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="form-group">
                            <input class="form-control" id="blogName">
                        </div>
                        <button type="submit" onclick="createBlog()" style="display: none">创建</button>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">取消</button>
                    <button type="submit" class="btn btn-primary" onclick="createBlog()">创建</button>
                </div>
            </div>
        </div>
    </div>


    <!--创建tag弹框-->
    <div class="modal fade" id="createTagModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="createTagModalLabel">创建tag</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <input class="form-control" id="tagName" autocomplete="off">
                    </div>

                    <div class="dhx_sample-container__widget overflow-auto" id="treeParse" style="max-height: 350px"></div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-primary" onclick="createTag()">创建</button>
                    <button type="button" class="btn btn-primary" onclick="deleteTag()">删除</button>

                </div>
            </div>
        </div>
    </div>

    <script>

        let tree;

        $(function () {
            initTag();
            $('#tagName').bind('keypress',function(event){
                if(event.keyCode == "13") {
                   searchTag();
                }
            });
        })

        //创建一个blog
        function createBlog() {
            var name=document.getElementById("blogName").value;
            $.post("/blog/create", {name: name},
                function (data, status) {
                    if(status=="success"){
                        layer.msg("创建成功！");
                        window.location.href="/blog/edit?id="+data.data;
                    }else{
                        layer.msg("创建失败！");
                    }
                    layer.close(index);
                });
        }

        //（在blog表中添加一个pid字段，记录父节点的id）
        //todo 之后blog的关系就保存在json中，以tree的方式进行维护（及blog既是博客，又是文件夹），当tree发生修改时，保存一份到服务器
        //如果是添加blog，则在blog表中添加
        //如果修改的blog名称，则在blog表中更新名称
        //如果blog的层级发生变化，则修改对应blog的pid
        function initTag() {

            tree = new dhx.Tree("treeParse",
                {
                    dragMode: "both", //可拖拽
                    // editable: true, //可编辑
                }
            );

            //在添加之前
            tree.data.events.on("Change", function(id,status,updatedItem){
                console.log("An item is updated",status,updatedItem);
            });
            tree.data.events.on("BeforeAdd", function(newItem){
                console.log("A new item will be added",newItem);
                //通过ajax请求添加节点，如果添加成功，拿到数据库生成的id设置到newItem中，返回true；如果添加失败，则返回false；
                return true;
            });
            tree.data.events.on("BeforeRemove", function(removedItem){
                console.log("An item will be removed",removedItem);
                //通过ajax请求添加节点，如果删除成功，则返回true，否则返回false
                return true;
            });

            tree.data.load("/getData");


            console.log(tree.data)

            tree.selection.add("Books");//默认选中
        }

        //添加tag
        function createTag() {
            var input = document.querySelector("#tagName");
            var value = input.value;
            var selected = tree.selection.getId();
            tree.data.add({value: value}, -1, selected);
            tree.open(selected);
            input.value = "";
        }

        //删除tag
        function deleteTag() {
            var id = tree.selection.getId();
            tree.data.remove(id);
        }

        //搜索tag
        function searchTag() {
            var text = document.querySelector("#tagName").value;
            tree.data.filter();//每次先恢复，再过滤
            tree.data.filter(function(item) {
                return item.value.toLowerCase().indexOf(text) !== -1;
            });
            document.querySelector("#tagName").value="";//清空输入框

        }
    </script>
</#macro>