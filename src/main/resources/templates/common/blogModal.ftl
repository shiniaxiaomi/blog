<#macro blogModal>

    <!--Blog列表弹框-->
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

                    <div class="dhx_sample-container__widget overflow-auto" <#--id="blogTreeParse"--> style="max-height: 350px"></div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-sm btn-secondary" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-sm btn-primary" onclick="createBlog()">创建</button>
                    <button type="button" class="btn btn-sm btn-primary" onclick="deleteBlog()">删除</button>
                    <button type="button" class="btn btn-sm btn-primary" onclick="beforeUpdateName()">更改</button>
                    <button type="button" class="btn btn-sm btn-primary" onclick="openBlog()">打开</button>
                    <button type="button" class="btn btn-sm btn-primary" onclick="editBlog()">编辑</button>
                    <button type="button" class="btn btn-sm btn-primary"
                            onclick="openAddTagToBlogModal(tree.selection.getItem().id);">添加标签</button>
                </div>
            </div>
        </div>
    </div>

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

</#macro>