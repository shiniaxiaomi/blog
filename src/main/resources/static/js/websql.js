let isLog=true;
let db=undefined;

// 插入一条数据(如果id存在,则再插入相同的id数据无效)
function insertDraft(draft){
    db.transaction(function (tx) {
        tx.executeSql("INSERT INTO draft (id, desc,descHtml,md,mdHtml,tocHtml,createTime,updateTime,tagNames) VALUES (?,?,?,?,?,?,?,?,?)",
            [draft.id,draft.desc,draft.descHtml,draft.md,draft.mdHtml,draft.tocHtml,draft.createTime,draft.updateTime,draft.tagNames]);
    });
}

//使用sql查询数据
function _selectDraft(sql,dataFunc){
    db.transaction(function (tx) {
        tx.executeSql(sql, [], function (tx, results) {
            dataFunc(results.rows);
        }, null);
    });
}
function selectDraftByName(blogName,dataFunc){
    var sql="select * from draft where id=?";
    if(isLog) console.log(sql);
    db.transaction(function (tx) {
        tx.executeSql(sql, [blogName], function (tx, results) {
            dataFunc(results.rows);
        }, null);
    });
}

//使用sql更新数据
function _updateDraft(sql){
    db.transaction(function(tx) {
        tx.executeSql(sql);
    });
}
function updateDraftByName(sql,params,blogName){
    var sql=sql+" where id='"+blogName+"'";
    if(isLog) console.log(sql,params);
    db.transaction(function(tx) {
        tx.executeSql(sql,params);
    });
}

//删除数据
function _deleteDraft(sql){
    db.transaction(function(tx) {
        tx.executeSql(sql);
    });
}
function deleteDraftByName(blogName){
    var sql="delete from draft where id=?";
    if(isLog) console.log(sql,blogName);

    db.transaction(function(tx) {
        tx.executeSql(sql,[blogName]);
    });
}


//示例:
//新增
// insertDraft({
//     id:"ceshi312",
//     desc:"",
//     descHtml:"dsfsd",
//     md:"",
//     mdHtml:"",
//     tocHtml:"",
//     createTime:"",
//     updateTime:"",
//     tagNames:""
// })

//更改
// updateDraftByName("update draft set md='1'","ceshi");

//查询
// selectDraft("select * from draft",function(data){
//     console.log(data);
// });
// selectDraftByName("ceshi",function(data){
//     console.log(data);
// })

//删除
// deleteDraftByName("ceshi")


$(function(){

    //检查是否支持webSQL
    try {
        db = openDatabase('localDraft', '1.0', 'localDraft', 2 * 1024 * 1024);
        db.transaction(function (tx) {
            tx.executeSql('CREATE TABLE IF NOT EXISTS draft (id unique , desc,descHtml,md,mdHtml,tocHtml,createTime,updateTime,tagNames)');
        });
    } catch (error) {
        console.log(error);
        pop.prompt("您的浏览器不支持本地缓存草稿,建议升级浏览器或更换成谷歌浏览器!");
        return;//结束,不在继续操作
    }

})


