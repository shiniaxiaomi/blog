let current=1;
let blogId;
let replyId;
let lute=Lute.New();//初始化md解析器

// 在当前页插入一条评论
function insertOneComment(commentId,content,createTime,username,githubUsername) {
    // 更新总数
    let count = $("#gt-counts");
    count.text(parseInt(count.text())+1);

    if(username===undefined){
        username=$("#username").val();
    }
    if(githubUsername===undefined){
        githubUsername = $("#githubUsername").val();
    }
    // 评论内容
    let str=`
            <div class="gt-comment gt-comment-admin" style="transform-origin: center top;">
                <div class="gt-avatar gt-comment-avatar">
                    <img src="/img/GitHub.png" alt="头像">
                </div>
                <div class="gt-comment-content">
                    <div class="gt-comment-header">
                        <a class="gt-comment-username"
                           href="https://github.com/`+githubUsername+`">`+username+`</a>
                        <span class="gt-comment-text">发表于</span>
                        <span class="gt-comment-date">大约 `+getUpdateTime(createTime)+`</span>
                        <a class="gt-like-small-button" href="javascript:void(0)" onclick="likeClick(this,`+commentId+`)">点赞<span>0</span></a>
                        <a class="gt-reply-small-button" href="javascript:void(0)" onclick="reply('`+username+`',`+commentId+`,'`+githubUsername+`')">回复</a>
                    </div>
                    <div class="gt-comment-body markdown-body">
                        `+lute.Md2HTML(content)+`
                    </div>
                </div>
            </div>
        `;

    // 渲染评论
    $("#commentsDiv").prepend(str);

    // 渲染查看内容链接
    renderLink();
}

function selectEmailAndUserName() {
    let username = window.localStorage.getItem("username");
    let email = window.localStorage.getItem("email");
    let githubUsername = window.localStorage.getItem("githubUsername");
    $("#username").val(username===""?null:username);
    $("#email").val(email===""?null:email);
    $("#githubUsername").val(githubUsername===""?null:githubUsername);
}

function getCommentById(commentId,blog_id) {
    blogId=blog_id;
    $.get("/comment/"+commentId,function (data,status) {
        if(status==="success" && data.code){
            let record = data.data;
            insertOneComment(commentId,record.content,new Date(record.createTime),record.username,record.githubUsername);
        }
    })
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
                                       href="https://github.com/`+record.githubName+`">`+record.username+`</a>
                                    <span class="gt-comment-text">发表于</span>
                                    <span class="gt-comment-date">大约 `+getUpdateTime(new Date(record.createTime))+`</span>
                                    <a class="gt-like-small-button" href="javascript:void(0)" onclick="likeClick(this,`+record.id+`)">点赞<span>`+record.likeCount+`</span></a>
                                    <a class="gt-reply-small-button" href="javascript:void(0)" onclick="reply('`+record.username+`',`+record.id+`,'`+record.githubName+`')">回复</a>
                                </div>
                                <div class="gt-comment-body markdown-body">
                                    `+record.content+`
                                </div>
                            </div>
                        </div>
                    `;
            }

            // 加载更多
            if(data.data.records.length===0 || data.data.current===data.data.pages){
                $("#loadMoreBtn").hide();
            }else{
                $("#loadMoreBtn").show();
            }

            // 渲染评论
            $("#commentsDiv").append(str);

            // 渲染查看内容链接
            renderLink();
        }
    });
}

// 处理查看内容的链接点击事件
function renderLink() {
    // 给查看内容添加点击时间
    $(".gt-comment-body a[href*='/comment']").each((index,item)=>{
        let href = $(item).attr("href");
        $(item).attr("href","javascript:void(0)"); //让链接失效
        $(item).click(function () {
            $.get(href,function (data,status) {
                if(status==="success" && data.code){
                    let record=data.data;
                    let str=`
                        <div class="gt-comment gt-comment-admin" style="transform-origin: center top;">
                            <div class="gt-comment-content">
                                <div class="gt-comment-header">
                                    <a class="gt-comment-username"
                                       href="https://github.com/`+record.githubName+`">`+record.username+`</a>
                                    <span class="gt-comment-text">发表于</span>
                                    <span class="gt-comment-date">大约 `+getUpdateTime(new Date(record.createTime))+`</span>
                                    <a class="gt-like-small-button" href="javascript:void(0)" onclick="likeClick(this,`+record.id+`)">点赞<span>`+record.likeCount+`</span></a>
                                    <a class="gt-reply-small-button" href="javascript:void(0)" onclick="reply('`+record.username+`',`+record.id+`,'`+record.githubName+`')">回复</a>
                                </div>
                                <div class="gt-comment-body markdown-body">
                                    `+record.content+`
                                </div>
                            </div>
                        </div>
                    `;

                    //使用layer直接弹出html的内容（使用lute直接解析md文档为html）
                    layer.open({
                        type: 1,
                        skin: 'layui-layer-lan', //加上边框
                        area: ['420px', '240px'], //宽高
                        shadeClose:true,
                        scrollbar: false,
                        content: "<div class='vditor-reset'>"+str+"</div>",
                    });
                    // 如果存在查看内容，继续渲染
                    renderLink();
                }
            });
        })
    })
}

function likeClick(dom,commentId) {
    if($(dom).attr("disabled") !==undefined){
        return;
    }
    $.post("/comment/like/incr",{id:commentId},function (data,status) {
        if(status==="success" && data.code){
            let likeCountSpan = $(dom).find("span").eq(0);
            likeCountSpan.text(Number.parseInt(likeCountSpan.text())+1);
            $(dom).css("color","#CCC");
            $(dom).attr("disabled",true);
        }
    });
}

// 回复评论
function reply(username,commentId,githubUsername) {
    // 更新评论的id
    replyId=commentId;
    $("#cancelCommitBtn").show();
    $("#replyBtn").show();
    $("#commitCommentBtn").hide();
    // 将要回复的原始内容设置到编辑框中
    let textarea = $("#commentContent");
    textarea.val("> [@"+username+"](https://github.com/"+githubUsername+") [查看内容](/comment/"+commentId+")\n\n");

    // 将页面跳转到评论区域
    let target = textarea;
    if(target.length===1){
        let top = target.offset().top-100;
        if(top > 0){
            $('html,body').animate({scrollTop:top}, 400);
        }
    }
    // 聚焦编辑框
    textarea.focus();
}

//取消回复
function cancelCommit() {
    $("#cancelCommitBtn").hide();
    $("#replyBtn").hide();
    $("#commitCommentBtn").show();
    replyId=undefined;
    //清空输入框
    $("#commentContent").val("");
}


// 提交评论
function commitComment(replyFlag){
    //校验用户名长度
    let username = $("#username").val().trim();
    if(username==="") {
        layer.msg("用户名不能为空");
        return;
    }
    if(username!=="" && username.length>30) {
        layer.msg("用户名过长");
        return;
    }

    //校验email
    let email = $("#email").val().trim();
    if(email!=="" && username.length>30) {
        layer.msg("邮箱过长");
        return;
    }else if(email!==""){
        let reg = /^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,5}$/;
        if(!reg.test(email)){
            layer.msg("邮箱格式不正确");
            return;
        }
    }

    //校验github用户名长度
    let githubUsername = $("#githubUsername").val().trim();
    if(githubUsername!=="" && githubUsername.length>30) {
        layer.msg("github用户名过长");
        return;
    }

    //校验内容长度
    let commentContent = $("#commentContent").val().trim();
    if(commentContent==="") {
        layer.msg("评论不能为空");
        return;
    }
    // 将输入的评论内容转换为html
    commentContent = lute.Md2HTML(commentContent);
    if(commentContent!=="" && commentContent.length>1000) {
        layer.msg("评论过长");
        return;
    }

    // 组装内容（blogId，回复id，email，用户名，html）
    let form = $('#commentForm').serialize();
    form+="&blogId="+blogId;
    if(replyId!==undefined){
        form+="&replyId="+replyId;
    }
    form+="&commentContent="+commentContent;

    //提交内容
    $.post("/comment",form,function (data,status) {
        if(status==="success" && data.code){
            let textarea = $("#commentContent");
            layer.msg("评论成功",{time: 1000});
            //将评论内容插入到第一条
            insertOneComment(data.data,textarea.val(),new Date());
            // 在评论成功之后，将email缓存在本地
            if(username!==undefined && username!=="")
                window.localStorage.setItem("username",username);
            if(email!==undefined && email!=="")
                window.localStorage.setItem("email",email);
            if(githubUsername!==undefined && githubUsername!=="")
                window.localStorage.setItem("githubUsername",githubUsername);
            //清空编辑框
            textarea.val("");
            //如果是回复，则点击取消回复
            if(replyFlag!==undefined){
                cancelCommit();
            }
        }else{
            // 如果失败，则提示失败信息
            layer.msg(data.msg,{time: 2000});
        }
    })
}

// 预览评论内容
function preview(){
    let commentContent = $("#commentContent").val();

    //校验内容长度
    if(commentContent!=="" && commentContent.length>200) {
        layer.msg("评论过长");
        return;
    }

    // 将输入的评论内容转换为html
    let html = lute.Md2HTML(commentContent);
    //使用layer直接弹出html的内容（使用lute直接解析md文档为html）
    layer.open({
        type: 1,
        skin: 'layui-layer-lan', //加上边框
        area: ['420px', '240px'], //宽高
        shadeClose:true,
        scrollbar: false,
        content: "<div class='vditor-reset'>"+html+"</div>",
    });
}

// 加载更多的评论
function loadMore(){
    loadComment(blogId,current+1)
}

// 时间转化工具
function getUpdateTime(updateTime) {
    if (updateTime === null) {
        return ''
    }
    let now = new Date().getTime();
    let second = Math.floor((now - updateTime) / (1000));
    let minute = Math.floor(second / 60);
    let hour = Math.floor(minute / 60);
    let day = Math.floor(hour / 24);
    let month = Math.floor(day / 31);
    let year = Math.floor(month / 12);
    if (year > 0) {
        return year + '年前'
    } else if (month > 0) {
        return month + '月前'
    } else if (day > 0) {
        let ret = day + '天前';
        if (day >= 7 && day < 14) {
            ret = '1周前'
        } else if (day >= 14 && day < 21) {
            ret = '2周前'
        } else if (day >= 21 && day < 28) {
            ret = '3周前'
        } else if (day >= 28 && day < 31) {
            ret = '4周前'
        }
        return ret
    } else if (hour > 0) {
        return hour + ' 小时前'
    } else if (minute > 0) {
        return minute + ' 分钟前'
    } else if (second > 0) {
        return second + ' 秒前'
    } else {
        return ' 刚刚'
    }
}