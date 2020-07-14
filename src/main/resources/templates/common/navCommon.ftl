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
                    <a class="nav-link" href="javascript:void(0);" data-toggle="modal" data-target="#createBlogModal" onclick="selectBlog()">目录</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="javascript:void(0);" data-toggle="modal" data-target="#createTagModal">标签</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/draft">草稿</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/todo">待办</a>
                </li>
            </ul>

            <#--搜索框-->
            <form class="form-inline my-2 my-lg-0" action="/searchMore">
                <input id="keywordInput" name="blogName" class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search">
                <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
            </form>

        </div>
    </nav>

    <!--更改blog名称弹框-->
    <div class="modal fade" id="updateBlogNameModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true" style="z-index: 1000000">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="createBlogModalLabel">更改名称</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="form-group">
                            <input class="form-control" id="updateBlogName">
                        </div>
                        <button type="submit" onclick="updateBlog();return false;" style="display: none">更改</button>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">取消</button>
                    <button type="submit" class="btn btn-success" onclick="updateBlog()">更改</button>
                </div>
            </div>
        </div>
    </div>

    <!--更改tag名称弹框-->
    <div class="modal fade" id="updateTagNameModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true" style="z-index: 1000000">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="updateTagModalLabel">更改名称</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="form-group">
                            <input class="form-control" id="updateTagName">
                        </div>
                        <button type="submit" onclick="updateTag();return false;" style="display: none">更改</button>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">取消</button>
                    <button type="submit" class="btn btn-success" onclick="updateTag()">更改</button>
                </div>
            </div>
        </div>
    </div>


    <!--创建Blog弹框-->
    <div class="modal fade" id="createBlogModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h6 class="modal-title" id="createBlogModalLabel">目录</h6>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <input class="form-control" id="blogName" autocomplete="off">
                    </div>

                    <div class="dhx_sample-container__widget overflow-auto" id="blogTreeParse" style="max-height: 350px"></div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-sm btn-secondary" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-sm btn-primary" onclick="createBlog()">创建</button>
                    <button type="button" class="btn btn-sm btn-primary" onclick="deleteBlog()">删除</button>
                    <button type="button" class="btn btn-sm btn-primary" onclick="beforeUpdateName()">更改</button>
                    <button type="button" class="btn btn-sm btn-primary" onclick="openBlog()">打开</button>
                    <button type="button" class="btn btn-sm btn-primary" onclick="editBlog()">编辑</button>
                    <button type="button" class="btn btn-sm btn-primary" data-toggle="modal" data-target="#createTagToBlogModal"
                            onclick="openAddTagModal()">添加标签</button>
<#--                    <button  onclick="addTagToBlog()"></button>-->
                </div>
            </div>
        </div>
    </div>

    <!--创建tag弹框-->
    <div class="modal fade" id="createTagModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h6 class="modal-title" id="createTagModalLabel">标签</h6>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <input class="form-control" id="tagName" autocomplete="off">
                    </div>

                    <div class="dhx_sample-container__widget overflow-auto" id="tagTreeParse" style="max-height: 350px"></div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-sm  btn-secondary" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-sm  btn-primary" onclick="createTag()">创建</button>
                    <button type="button" class="btn btn-sm  btn-primary" onclick="deleteTag()">删除</button>
                    <button type="button" class="btn btn-sm  btn-primary" onclick="beforeUpdateTagName()">更改</button>
                </div>
            </div>
        </div>
    </div>

    <!--创建tag弹框-->
    <div class="modal fade" id="createTagToBlogModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h6 class="modal-title" id="createTagModalLabel">标签</h6>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label for="blogTags" class="col col-form-label">标签</label>
                        <div class="col" >
                            <input id="tagInput" />
                        </div>
                    </div>

                    <div class="form-group" id="tagsDiv">
                        <#--创建所有标签，供点击选择-->
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-sm  btn-secondary" data-dismiss="modal">取消</button>
                </div>
            </div>
        </div>
    </div>


    <script>

        function openAddTagModal() {
            //获取选中blog的id
            blogIdBuff = tree.selection.getItem().id;

            //查询id对应的tag
            $.get("tag/getDataByBlogId?blogId="+blogIdBuff,function (data,status) {
                if(status==="success"){
                    tokenFlag=false; //设置为不触发tag事件
                    //把tag设置到输入框中
                    tokenfield.setItems(JSON.parse(data));
                    tokenFlag=true; //恢复标志位
                }
            })
        }

        function openAddTagModalOnBlog(blogId){
            blogIdBuff=blogId;
            //查询id对应的tag
            $.get("tag/getDataByBlogId?blogId="+blogIdBuff,function (data,status) {
                if(status==="success"){
                    tokenFlag=false; //设置为不触发tag事件
                    //把tag设置到输入框中
                    tokenfield.setItems(JSON.parse(data));
                    tokenFlag=true; //恢复标志位
                }
            })
        }


        let tree;
        let isDropComplete=false;
        let tokenfield;
        let tokenFlag=true; //如果是初始化，则不触发tag事件
        let blogIdBuff=undefined;

        $(function () {
            initTagTree();
            initBlogTree();
            $('#blogName').bind('keypress',function(event){
                if(event.keyCode == "13") {
                    searchBlog();
                }
            });

            $.get("/tag/getData",function (data,status) {
                data = JSON.parse(data);
                //开启标签
                tokenfield = new Tokenfield({
                    el: document.querySelector('#tagInput'),
                    items: data,
                    setItems: [],
                    itemData:'value',
                    itemLabel:'value',
                });
                // 删除前的回调
                tokenfield.on("removeToken",function (event,data) {
                    console.log(event,data)
                    let response = $.ajax({
                        type: "post",
                        url: "/tag/deleteRelation",
                        async: false,
                        data: {
                            blogId: blogIdBuff, //博客id
                            tagId:data.id, //标签id
                        }
                    });

                    layer.msg(response.responseJSON.msg);
                    if(response.responseJSON.code){
                        return true;
                    }else{
                        return false;
                    }
                });
                // 添加前的回调
                tokenfield.on("addToken",function (event,data) {
                    if(!tokenFlag){
                        return true;
                    }

                    let response = $.ajax({
                        type: "post",
                        url: "/tag/addRelation",
                        async: false,
                        data: {
                            blogId: blogIdBuff, //博客id
                            tagName:data.value, //标签名称
                            isNew:data.isNew==undefined?false:true, //标签是否新增
                            tagId:data.id, //标签id
                        }
                    });

                    layer.msg(response.responseJSON.msg);
                    if(response.responseJSON.code){
                        return true;
                    }else{
                        return false;
                    }
                });

                // 创建可以点击添加标签的功能
                for(let i=0;i<data.length;i++){
                    $("#tagsDiv").append(`
                        <a href="javascript:void(0)" class="badge badge-danger"  value='`+data[i].value+`' tagId='`+data[i].id+`'  onclick="insertTag(this)">`+data[i].value+`</a>
                    `);
                }
            })

        })

        function insertTag(data) {
            var tagId=$(data).attr("tagId");
            var value=$(data).attr("value");
            console.log(tagId,value)
            tokenfield.addItems({id:tagId,value:value});
        }

        // 打开目录弹框时默认选中节点
        function selectBlog() {
            tree.selection.add(61); //默认选中
        }


        function updateTag() {
            let id = tagTree.selection.getId();
            let name=document.getElementById('updateTagName').value;
            if(name===undefined || name===""){
                layer.msg("名称不能为空");
                return;
            }
            $.post("/tag/update",{tagId:id,name:name},
                function (data, status) {
                    $('#updateTagNameModal').modal('hide');//隐藏弹窗
                    if(status==="success" && data.code){ //成功
                        tagTree.data.update(id, {value: name});
                        clearTagInput();
                        layer.msg("更新成功！");
                    }else{
                        layer.msg("更新失败！");
                    }
                })
        }

        let tagTree;
        function initTagTree() {
            tagTree = new dhx.Tree("tagTreeParse");

            //加载数据
            tagTree.data.load("/tag/getData").then(function(){
                // tree.expandAll();//展开所有节点

                //创建右侧导航栏
                let buf="";
                tagTree.data.map(item=>{
                    if(item.isLeaf==undefined){
                        buf+="<a class='list-group-item' style='font-size: 80%' href='/tag?pid="+item.id+"'>"+item.value+"</a>";
                    }
                })
                $("#tags").html(buf);

            });

            // tree.selection.add("Books"); //默认选中
        }

        function beforeUpdateTagName() {
            let item = tagTree.selection.getItem();
            if(item==undefined){
                layer.msg("请选中要更改的节点");
                return ;
            }
            $('#updateTagNameModal').modal('show');//显示弹窗
            //设置原始的名称
            document.getElementById('updateTagName').value=tagTree.selection.getItem().value;

        }

        function deleteTag() {
            let id=tagTree.selection.getId();
            if(id==undefined){
                layer.msg("请选择要删除的标签");
                return;
            }
            $.get("/tag/isHasBlog?tagId="+id,function (data, status) {
                if(status==="success"){
                    let msg;
                    if(data==true){ //标签下有tag
                        msg='该标签下有tag,是否删除tag并清除关联关系？';
                    }else{
                        msg='是否要删除tag?';
                    }
                    layer.confirm(msg, {
                        btn: ['确定','取消'] //按钮
                    }, function(){
                        // 删除操作
                        $.post("/tag/delete",{tagId:id},function (data, status) {
                            if(status==="success"){
                                layer.msg("删除成功");
                                // 去掉tagTree上的tag
                                tagTree.data.remove(id);
                            }else{
                                layer.msg("删除失败");
                            }
                        })
                    });
                }else{
                    layer.msg("系统错误");
                }
            })

        }

        function createTag() {

            let name=document.querySelector("#tagName").value;
            if(name==="" || name===undefined){
                layer.msg("标签名称不能为空");
                return;
            }

            $.post("/tag/create", {name: name},
                function (data, status) {
                    if(status==="success"){
                        tagTree.data.add({value: name,id:data.data}, -1, tagTree.data.getRoot());//往tree添加tag数据
                        clearTagInput(); //清空输入框
                        layer.msg("创建成功！");
                    }else{
                        layer.msg("创建失败！");
                    }
                });
        }


        function editBlog() {
            let id = tree.selection.getId();
            if(id==undefined){
                layer.msg("请选中要更改的节点");
                return ;
            }

            window.location.href="/blog/edit?id="+id;

        }


        function openBlog() {
            let id = tree.selection.getId();
            if(id==undefined){
                layer.msg("请选中要更改的节点");
                return ;
            }

            // 拿到节点的id，并打开blog或文件夹
            if(tree.data.getItem(id).isLeaf){
                window.location.href="/blog?id="+id;
            }else{
                window.location.href="/list?pid="+id;
            }
        }




        //（在blog表中添加一个pid字段，记录父节点的id）
        //todo 之后blog的关系就保存在json中，以tree的方式进行维护（及blog既是博客，又是文件夹），当tree发生修改时，保存一份到服务器
        //如果是添加blog，则在blog表中添加
        //如果修改的blog名称，则在blog表中更新名称
        //如果blog的层级发生变化，则修改对应blog的pid
        //在模态框关闭的时候保存当前的层级结构（之前修改的内容）
        function initBlogTree() {

            tree = new dhx.Tree("blogTreeParse",{
                dragMode:"both"
            });

            //双击跳转
            // tree.events.on("ItemDblClick", function(id, e){
            //     // 拿到节点的id，并打开blog或文件夹
            //     if(tree.data.getItem(id).isLeaf){
            //         window.location.href="/blog?id="+id;
            //     }else{
            //         window.location.href="/list?pid="+id;
            //     }
            // });
            //单击开合
            tree.events.on("itemClick", function(id, e){
                if(isDropComplete){
                    setTimeout(function () {
                        isDropComplete=false;
                    },300);//将标志位清空
                    return;
                }
                tree.toggle(id);//文件夹的开合
            });
            //拖拽保存层级
            tree.events.on("beforeDrop",function (toId,target,fromId) {
                isDropComplete=true;
                let response = $.ajax({
                    type: "post",
                    url: "/blog/update",
                    async: false,
                    data: {id:fromId,pid:toId}
                });

                if(response.status===200){
                    layer.msg("移动成功！");
                    return true;
                }else{
                    layer.msg("移动失败！");
                    return false;
                }
            })


            //加载数据
            tree.data.load("/blog/getTreeData").then(function(){
                tree.expandAll();//展开所有节点
            });


        }

        //遍历树结构的工具
        // function forEachItem(arr,func) {
        //     console.log(arr)
        //     arr.map(item => {
        //         func(item);
        //         if(item.items!=undefined && item.items.length>0){
        //             forEachItem(item.items,func);
        //         }
        //     })
        // }

        function getPath(id,obj) {
            let item=tree.data.getItem(id);
            if(item.parent==undefined){
                return;
            }

            let pItem=tree.data.getItem(item.parent);
            if(pItem==undefined){
                return;
            }
            obj.push("/"+"<a href='/list?pid="+item.parent+"'>"+pItem.value+"</a>");

            getPath(item.parent,obj);
        }

        //构建一个树
        function buildTree(list) {
            return fn(list,undefined);
        }

        //将数据转化成树形结构数组
        function fn(data,pid) {
            var result = [], temp;
            for (var i = 0; i < data.length; i++) {
                if (data[i].pid == pid) {
                    var obj = {
                        "id": data[i].id,
                        "value": data[i].name,
                        "pid": data[i].pid
                    };
                    temp = fn(data, data[i].id);
                    if (temp.length > 0) {
                        obj.items = temp;
                    }else{
                        obj.isLeaf=true; //标记为叶子
                    }
                    result.push(obj);
                }
            }
            return result;
        }

        Date.prototype.Format = function (fmt) { // author: meizz
            var o = {
                "M+": this.getMonth() + 1, // 月份
                "d+": this.getDate(), // 日
                "h+": this.getHours(), // 小时
                "m+": this.getMinutes(), // 分
                "s+": this.getSeconds(), // 秒
                "q+": Math.floor((this.getMonth() + 3) / 3), // 季度
                "S": this.getMilliseconds() // 毫秒
            };
            if (/(y+)/.test(fmt))
                fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
        }

        //添加blog
        function createBlog() {
            let text=getBlogInputValue()==""?new Date().Format("yyyy-MM-dd hh:mm:ss"):getBlogInputValue();
            let selectedId = tree.selection.getId();
            $.post("/blog/create", {name: text,pid:selectedId},
                function (data, status) {
                    if(status==="success"){
                        tree.data.add({value: text,id:data.data}, -1, selectedId);//往tree添加数据
                        tree.open(selectedId); //打开父节点
                        clearBlogInput(); //清空输入框
                        layer.msg("创建成功！");
                        window.location.href="/blog/edit?id="+data.data; //直接打开编辑页
                    }else{
                        layer.msg("创建失败！");
                    }
                });
        }

        //删除blog
        function deleteBlog() {
            let id=tree.selection.getId();
            if(id==undefined){
                layer.msg("请选择要删除的节点");
                return;
            }
            $.post("/blog/delete", {id:id},
                function (data, status) {
                    if(status==="success"){
                        tree.data.remove(id);
                        clearBlogInput();
                        layer.msg("删除成功！");
                    }else{
                        layer.msg("删除失败！");
                    }
                });
        }

        function beforeUpdateName() {
            let item = tree.selection.getItem();
            if(item==undefined){
                layer.msg("请选中要更改的节点");
                return ;
            }
            $('#updateBlogNameModal').modal('show');//显示弹窗
            //设置原始的名称
            document.getElementById('updateBlogName').value=tree.selection.getItem().value;
        }

        //更新blog名称
        function updateBlog() {
            let id = tree.selection.getId();
            let name=document.getElementById('updateBlogName').value;
            if(name===undefined || name===""){
                layer.msg("名称不能为空");
                return;
            }
            $.post("/blog/update",{id:id,name:name},
                function (data, status) {
                    $('#updateBlogNameModal').modal('hide');//隐藏弹窗
                    if(status==="success"){
                        tree.data.update(id, {value: name});
                        clearBlogInput();
                        layer.msg("更新成功！");
                    }else{
                        layer.msg("更新失败！");
                    }
            })

        }

        //获取输入框的值
        function getBlogInputValue() {
            return  document.querySelector("#blogName").value;
        }
        //情况输入框的值
        function clearBlogInput() {
            document.querySelector("#blogName").value="";
        }
        //获取输入框的值
        function getTagInputValue() {
            return  document.querySelector("#tagName").value;
        }
        //情况输入框的值
        function clearTagInput() {
            document.querySelector("#tagName").value="";
        }

        //搜索blog
        function searchBlog() {
            let text = document.querySelector("#blogName").value;
            tree.data.filter();//每次先恢复，再过滤
            tree.data.filter(function(item) {
                return item.value.toLowerCase().indexOf(text) !== -1;
            });
            document.querySelector("#blogName").value="";//清空输入框
        }
    </script>
</#macro>