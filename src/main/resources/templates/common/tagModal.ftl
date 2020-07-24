<#macro tagModal>

    <!--tag列表弹框-->
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

                    <div class="dhx_sample-container__widget overflow-auto" <#--id="tagTreeParse"--> style="max-height: 350px"></div>
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

    <!--添加标签弹窗-->
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

</#macro>