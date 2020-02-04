
function buildLoaclDraftHtml(page) {

    console.log("select id,descHtml,tagNames,createTime,updateTime from draft limit "+(page-1)*10+",10")
    _selectDraft("select id,descHtml,tagNames,createTime,updateTime from draft limit "+(page-1)*10+",10",function (data) {
        let localDraft = $("#localDraft");
        for(var i=0;i<data.length;i++){
            var draft=data[i];
            var tagsHtml="";
            let tags = draft.tagNames.split(",");
            for(let j=0;j<tags.length;j++){
                tagsHtml+="<a href='javascript:void(0);' class='badge badge-success'>"+tags[j]+"</a>";
            }

            var html=`
                <div>
                    <!--分界线-->
                    <hr>
                    <!--标题-->
                    <h3 class="blue"><a href="/localDraft?id=`+draft.id+`">`+draft.id+`</a></h3>
        
                    <!--参数-->
                    <p class="text-muted" style="margin-bottom: 0px">
                        <!--博客参数-->
                        <span style="margin-right: 10px">
                            <img class="myIcon" src="/icons/calendar.svg" title="创建日期">
                            `+draft.createTime+`
                        </span>
                        <span style="margin-right: 10px">
                            <img class="myIcon" src="/icons/arrow-repeat.svg" title="更新日期">
                            `+draft.updateTime+`
                        </span>
                        <span style="margin-right: 10px">
                            <img class="myIcon" src="/icons/Eye.svg" title="观看人数">
                            0
                        </span>
                        <!--编辑按钮-->
                        <a type="button" style="margin-top: -2px;" class="btn btn-link btn-xs px-0"
                           href="javascript:void(0);" onclick="deleteFunc('localDraft','`+draft.id+`')">删除草稿</a>
                        <a type="button" style="margin-top: -2px;" class="btn btn-link btn-xs px-0"
                           href="/editLocalDraftDesc?blogId=`+draft.id+`">编辑描述</a>
                        <a type="button" style="margin-top: -2px;" class="btn btn-link btn-xs px-0"
                           href="/editLocalDraft?blogId=`+draft.id+`">编辑草稿</a>
                    </p>
                    <!--标签-->
                    <div style="margin-bottom: 10px">
                        `+tagsHtml+`
                    </div>
        
                    <!--概述,通过markdown的形式进行描述,那么就可以随意的添加图片链接了-->
                    <div class="text-muted">
                        `+draft.descHtml+`
                    </div>
                </div>
            `;
            localDraft.append(html)
        }
    });


}



