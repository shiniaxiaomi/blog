let current=1;
let blogId;
let lute=Lute.New();//初始化md解析器

// 在当前页插入一条评论
function insertOneComment(html) {
    // 更新总数
    let count = $("#gt-counts");
    count.text(count.text()+1);

    // 评论内容
    let str=`
            <div class="gt-comment gt-comment-admin" style="transform-origin: center top;">
                <div class="gt-avatar gt-comment-avatar">
                    <img src="/img/GitHub.png" alt="头像">
                </div>
                <div class="gt-comment-content">
                    <div class="gt-comment-header">
                        <a class="gt-comment-username"
                           href="https://github.com/shiniaxiaomi">shiniaxiaomi</a>
                        <span class="gt-comment-text">发表于</span>
                        <span class="gt-comment-date">大约 18 小时前</span>
                        <a class="gt-comment-small-button">点赞</a>
                        <a class="gt-comment-small-button">回复</a>
                    </div>
                    <div class="gt-comment-body markdown-body">
                        `+html+`
                    </div>
                </div>
            </div>
        `;

    // 渲染评论
    $("#commentsDiv").prepend(str);
}

function selectEmailAndUserName() {
    let email = window.localStorage.getItem("email");
    let github_username = window.localStorage.getItem("github_username");
    $("#email").val(email);
    $("#github_username").val(github_username);
}

// 加载评论
function loadComment(id,page) {
    // 每次加载评论时都从缓存先读取信息
    selectEmailAndUserName();

    if(id===undefined){
        throw new Error("博客id不能为空");
    }else{
        blogId=id;
    }
    if(page===undefined){
        page=1;
    }
    $.get("/comment/"+id+"/"+page,function (data,status) {
        if(status==="success" && data.code){
            console.log(data.data);

            // 更新总数
            $("#gt-counts").text(data.data.total);

            // 更新当前页
            current=data.data.current;

            // 评论内容
            let str="";
            for(let i=0;i<data.data.records.length;i++){
                let record = data.data.records[i];
                str+=`
                        <div class="gt-comment gt-comment-admin" style="transform-origin: center top;">
                            <div class="gt-avatar gt-comment-avatar">
                                <img src="/img/GitHub.png" alt="头像">
                            </div>
                            <div class="gt-comment-content">
                                <div class="gt-comment-header">
                                    <a class="gt-comment-username"
                                       href="https://github.com/shiniaxiaomi">shiniaxiaomi</a>
                                    <span class="gt-comment-text">发表于</span>
                                    <span class="gt-comment-date">大约 18 小时前</span>
                                    <a class="gt-comment-small-button">点赞</a>
                                    <a class="gt-comment-small-button">回复</a>
                                </div>
                                <div class="gt-comment-body markdown-body">
                                    `+record.html+`
                                </div>
                            </div>
                        </div>
                    `;
            }

            // 加载更多
            if(data.data.current===data.data.pages){
                $("#loadMoreBtn").hide();
            }else{
                $("#loadMoreBtn").show();
            }

            // 渲染评论
            $("#commentsDiv").append(str);
        }
    });
}

// 提交评论
function commitComment(){
    //校验email
    let email = $("#email").val();
    if(email!=="") {
        let reg = /^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,5}$/;
        if(!reg.test(email)){
            layer.msg("邮箱格式不正确");
            return;
        }
    }

    //校验用户名长度
    let github_username = $("#github_username").val();
    if(github_username!=="" && github_username.length>40) {
        layer.msg("github用户名过长");
        return;
    }

    //校验内容长度
    let comment_content = $("#comment_content").val();
    if(comment_content!=="" && comment_content.length>200) {
        layer.msg("评论过长");
        return;
    }

    // 将输入的评论内容转换为html
    let html = lute.Md2HTML(comment_content);

    // 组装内容（blogId，回复id，email，用户名，html）
    let form = $('#commentForm').serialize();
    form+="&blogId="+blogId;
    form+="&replyId=1";
    form+="&html="+html;

    //提交内容
    $.post("/comment",form,function (data,status) {
        if(status==="success" && data.code){
            layer.msg("评论成功",{time: 1000});
            //将评论内容插入到第一条
            insertOneComment(html);
            // 在评论成功之后，将email缓存在本地
            window.localStorage.setItem("email",email);
            window.localStorage.setItem("github_username",github_username);
            //清空编辑框
            $("#comment_content").val("");
        }else{
            // 如果失败，则提示失败信息
            layer.msg(data.msg,{time: 2000});
        }
    })
}

// 预览评论内容
function preview(){
    let comment_content = $("#comment_content").val();

    //校验内容长度
    if(comment_content!=="" && comment_content.length>200) {
        layer.msg("评论过长");
        return;
    }

    // 将输入的评论内容转换为html
    let html = lute.Md2HTML(comment_content);
    //使用layer直接弹出html的内容（使用lute直接解析md文档为html）
    layer.open({
        type: 1,
        skin: 'layui-layer-lan', //加上边框
        area: ['420px', '240px'], //宽高
        shadeClose:true,
        content: html
    });
}

// 加载更多的评论
function loadMore(){
    loadComment(blogId,current+1)
}

