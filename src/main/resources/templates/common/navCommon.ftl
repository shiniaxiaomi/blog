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
                    <a class="nav-link" href="javascript:void(0);" data-toggle="modal" data-target="#createTagModal">目录</a>
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
                    <h5 class="modal-title" id="createTagModalLabel">更改名称</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="form-group">
                            <input class="form-control" id="blogName">
                        </div>
                        <button type="submit" onclick="updateBlog()" style="display: none">更改</button>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">取消</button>
                    <button type="submit" class="btn btn-success" onclick="updateBlog()">更改</button>
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
                    <button type="button" class="btn btn-primary" onclick="createBlog()">创建</button>
                    <button type="button" class="btn btn-primary" onclick="deleteBlog()">删除</button>
                    <button type="button" class="btn btn-primary" onclick="beforeUpdateName()">更改</button>
                </div>
            </div>
        </div>
    </div>

    <script>



        let tree;
        let isDropComplete=false;

        $(function () {
            initTag();
            $('#tagName').bind('keypress',function(event){
                if(event.keyCode == "13") {
                   searchBlog();
                }
            });
        })

        //（在blog表中添加一个pid字段，记录父节点的id）
        //todo 之后blog的关系就保存在json中，以tree的方式进行维护（及blog既是博客，又是文件夹），当tree发生修改时，保存一份到服务器
        //如果是添加blog，则在blog表中添加
        //如果修改的blog名称，则在blog表中更新名称
        //如果blog的层级发生变化，则修改对应blog的pid
        //在模态框关闭的时候保存当前的层级结构（之前修改的内容）
        function initTag() {

            tree = new dhx.Tree("treeParse",{
                dragMode:"both"
            });

            //双击跳转
            tree.events.on("ItemDblClick", function(id, e){
                // 拿到节点的id，并打开blog或文件夹
                if(tree.data.getItem(id).isLeaf){
                    window.location.href="/blog?id="+id;
                }else{
                    window.location.href="/blogs?pid="+id;
                }
            });
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
                $.post("/blog/update",{id:fromId,pid:toId},function () {
                    if(status==="success"){
                        layer.msg("移动成功！");
                        return true;
                    }else{
                        layer.msg("移动失败！");
                        return false;
                    }
                })
            })


            //加载数据
            $.get("/user/getData",function (data, status) {
                if(status==="success"){
                    let treeData=buildTree(data);
                    tree.data.parse(treeData); //加载数据
                    tree.expandAll();//展开所有节点
                }else{
                    layer.msg("数据加载失败！");
                }
            })

            // tree.selection.add("Books"); //默认选中

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

        //添加tag
        function createBlog() {
            let text=getInputValue()==""?new Date().Format("yyyy-MM-dd hh:mm:ss"):getInputValue();
            let selectedId = tree.selection.getId();
            $.post("/blog/create", {name: text,pid:selectedId},
                function (data, status) {
                    if(status==="success"){
                        tree.data.add({value: text,id:data.data}, -1, selectedId);//往tree添加数据
                        tree.open(selectedId); //打开父节点
                        clearInput(); //清空输入框
                        layer.msg("创建成功！");
                        // window.location.href="/blog/edit?id="+data.data; //直接打开编辑页
                    }else{
                        layer.msg("创建失败！");
                    }
                });
        }

        //删除tag
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
                        clearInput();
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
            document.getElementById('blogName').value=tree.selection.getItem().value;
        }

        //更新tag名称
        function updateBlog() {
            let id = tree.selection.getId();
            let name=document.getElementById('blogName').value;
            if(name===undefined || name===""){
                layer.msg("名称不能为空");
                return;
            }
            $.post("/blog/update",{id:id,name:name},
                function (data, status) {
                    $('#updateBlogNameModal').modal('hide');//隐藏弹窗
                    if(status==="success"){
                        tree.data.update(id, {value: name});
                        clearInput();
                        layer.msg("更新成功！");
                    }else{
                        layer.msg("更新失败！");
                    }
            })

        }

        //获取输入框的值
        function getInputValue() {
            return  document.querySelector("#tagName").value;
        }
        //情况输入框的值
        function clearInput() {
            document.querySelector("#tagName").value="";
        }

        //搜索tag
        function searchBlog() {
            let text = document.querySelector("#tagName").value;
            tree.data.filter();//每次先恢复，再过滤
            tree.data.filter(function(item) {
                return item.value.toLowerCase().indexOf(text) !== -1;
            });
            document.querySelector("#tagName").value="";//清空输入框
        }
    </script>
</#macro>