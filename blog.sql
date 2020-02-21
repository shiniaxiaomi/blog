create table blog
(
    id         int auto_increment
        primary key,
    name       varchar(1000) null comment '博客名称',
    `desc`     varchar(2000) null comment '博客的描述',
    descHtml   longtext      null comment '博客描述的html',
    md         longtext      null comment '笔记源码',
    mdHtml     longtext      null comment '博客内容的html',
    tocHtml    longtext      null comment 'toc目录html',
    createTime datetime      null,
    updateTime datetime      null,
    hot        int           null comment '观看人数',
    tagNames   varchar(500)  null comment '缓存一个blog对应的所有tag名称'
);

create table blogandtag
(
    blogId int not null comment '博客Id',
    tagId  int not null comment 'tagId',
    constraint blogId
        unique (blogId, tagId)
);

create table tag
(
    id    int auto_increment
        primary key,
    name  varchar(20) null,
    count int         null comment '属于该tag的博客数量',
    constraint tag_name_uindex
        unique (name)
);

create table user
(
    id         int auto_increment
        primary key,
    userName   varchar(20) null,
    password   varchar(20) null,
    visitCount int         null comment '博客的总访问次数',
    constraint user_userName_uindex
        unique (userName)
);