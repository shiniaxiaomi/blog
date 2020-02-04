//获取预览的html
function getHtml() {
    //替换header的id
    let query = $(".editormd-preview-container").find(":header");
    if(linkId==undefined || linkId.length==0){
        buildTocHtml();//buildTocHtml必须要在getHtml之前执行
    }
    for(var i=0;i<linkId.length;i++){
        var header=query[i];
        $(header).attr("id",linkId[i]);
    }
    return "<div class=\"markdown-body editormd-preview-container\" previewcontainer=\"true\">"
        +$(".editormd-preview-container").eq(0).html()
        +"</div>"
}

//递归生成tocHtml
var linkId=[];//保存生成的linkId
function buildTocHtml() {
    //如果是移动端,先生成toc目录结构,在构建tocHtml
    if(isMobile){
        editor.watch();
    }
    linkId=[];//清空
    var html={str:""};
    html.str+="<ul class='nav flex-column' id='navbar-example'>";
    var toc=$("#custom-toc-container").find(".markdown-toc-list").eq(0);//拿到ul对象
    if(toc.children().length!=0){
        for(var i=0;i<toc.children().length;i++){
            _buildTocHtml(html,toc.children()[i]);
        }
    }
    html.str+="</ul>";
    return html.str;
}
//去遍历子组件
function _buildTocHtml(html,child) {
    if(child.tagName=="A"){
        linkId.push("h"+child.attributes.level.value+"-"+child.text);
        html.str+="<a class='nav-link' href='#h"+child.attributes.level.value+'-'+child.text+"'>"+child.text+"</a>";
        return;//没有子节点,返回
    }else if(child.tagName=="LI"){
        if(child.length==0){
            return;//没有子节点,返回
        }
        html.str+="<li class='nav-item'>";
        //有子节点,则遍历子节点
        var childs=$(child).children();
        for(var i=0;i<childs.length;i++){
            _buildTocHtml(html,childs[i]);
        }
        html.str+="</li>";
    }else if(child.tagName=="UL"){
        if(child.length==0){
            return;//没有子节点,返回
        }
        html.str+="<ul>";
        //有子节点,则遍历子节点
        var childs=$(child).children();
        for(var i=0;i<childs.length;i++){
            _buildTocHtml(html,childs[i]);
        }
        html.str+="</ul>";
    }
}

Date.prototype.Format = function(fmt) {
    var o = {
        "M+" : this.getMonth()+1,                 //月份
        "d+" : this.getDate(),                    //日
        "h+" : this.getHours(),                   //小时
        "m+" : this.getMinutes(),                 //分
        "s+" : this.getSeconds(),                 //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S"  : this.getMilliseconds()             //毫秒
    };
    //"y+" : 年份
    if(/(y+)/.test(fmt))
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
}

//返回首页
function goHome() {
    setTimeout(function () {
        pop.confirm("是否前往首页?",function () {
            window.location.href="/";
        })
    },500)
}

//返回草稿页
function goDraft() {
    setTimeout(function () {
        pop.confirm("是否前往草稿页?",function () {
            window.location.href="/draftHome";
        })
    },500)
}


//获取tags的信息
function getTags() {
    var tags=tokenfield.getItems();
    var tagNames="";
    for(var i=0;i<tags.length;i++){
        tagNames+=tags[i].name+",";
    }
    tagNames=tagNames.substring(0,tagNames.length-1);
    return tagNames;
}

//重置保存Modal
function resetSaveModal() {
    //隐藏弹窗
    $("#myModal").modal("hide");
    //清除数据
    $("#blogName").val("");
    //清除tag
    tokenfield.emptyItems();
}