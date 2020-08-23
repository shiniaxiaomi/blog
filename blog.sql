create table blog
(
    id          int auto_increment
        primary key,
    name        varchar(100)                         null comment '博客名称',
    `desc`      varchar(1000)                        null comment '博客描述',
    md          longtext                             null comment '博客md原文',
    md_html     longtext                             null comment '博客html',
    is_stick    tinyint(1) default 0                 null comment '是否置顶，0（false不置顶），1（true置顶）',
    is_private  tinyint(1) default 0                 null comment '是否私有（默认是公有）',
    visit_count int        default 0                 null comment '浏览次数',
    create_time timestamp  default CURRENT_TIMESTAMP null comment '创建时间',
    update_time timestamp  default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint blog_name_uindex
        unique (name)
);

create index blog_is_stick_is_private_update_time_create_time_index
    on blog (is_stick asc, is_private asc, update_time desc, create_time desc);

create table blog_file_relation
(
    id      int auto_increment
        primary key,
    blog_id int null comment '博客id',
    file_id int null comment '文件id'
)
    comment '博客所引用文件的关联表';

create table blog_tag_relation
(
    id      int auto_increment
        primary key,
    blog_id int null comment '博客id',
    tag_id  int null comment '标签id',
    constraint blog_tag_relation_blog_id_tag_id_uindex
        unique (blog_id, tag_id)
)
    comment 'tag和blog的中间表';

create table catalog
(
    id         int auto_increment
        primary key,
    name       varchar(20)          null comment '名称（如果是blog，需要同步去blog表更新名称）',
    pid        int                  null comment '父级的id',
    is_folder  tinyint(1) default 0 null comment '是否为文件夹',
    blog_id    int                  null comment '记录blog的id，如果是文件夹，则为空（这个要带给前端，点击的时候可以打开对应的blog）',
    is_private tinyint(1) default 0 null comment '是否为私有的，默认为公有的'
)
    comment '目录关系表';

create index catalog_blog_id_index
    on catalog (blog_id);

create index catalog_is_private_is_folder_name_index
    on catalog (is_private asc, is_folder desc, name asc);

create table comment
(
    id              int auto_increment comment '评论id'
        primary key,
    blog_id         int                                 null comment '博客id',
    comment_user_id int                                 null comment '评论用户id',
    reply_id        int                                 null comment '回复的评论id',
    content         varchar(1000)                       null comment '评论内容',
    like_count      int       default 0                 null comment '点赞数',
    create_time     timestamp default CURRENT_TIMESTAMP null comment '创建时间'
)
    comment '评论表';

create index comment_create_time_index
    on comment (create_time desc);

create table comment_user
(
    id          int auto_increment comment '评论用户id'
        primary key,
    username    varchar(30) null comment '用户名',
    email       varchar(30) null comment '邮箱',
    github_name varchar(30) null comment 'github用户名（也可以随便输入，也可以为空）',
    avatar      varchar(40) null comment '头像链接'
)
    comment '评论用户表';

create table config
(
    id             int auto_increment
        primary key,
    secret_id      varchar(40) null comment '腾讯翻译secretId',
    secret_key     varchar(40) null comment '腾讯翻译secretKey',
    email          varchar(40) null comment 'blog发送邮箱',
    email_password varchar(40) null
)
    comment '配置信息';

create table file
(
    id    int auto_increment
        primary key,
    name  varchar(40)   null comment '文件名称',
    type  int default 0 null comment '标记文件类型，0：图片，1是文件',
    count int default 1 null comment '被博客的引用数',
    constraint file_name_uindex
        unique (name)
)
    comment '文件列表';

create table tag
(
    id    int auto_increment
        primary key,
    name  varchar(20) null comment '标签名称',
    count int         null comment '标签下的blog数量',
    constraint tag_name_uindex
        unique (name)
);

create table user
(
    id        int auto_increment
        primary key,
    user_name varchar(20) null,
    password  varchar(20) null,
    tree      longtext    null comment '文件目录信息',
    constraint user_userName_uindex
        unique (user_name)
);

create table word
(
    id      int auto_increment
        primary key,
    name    varchar(20) null comment '名称',
    context longtext    null comment '分词后的单词数组（包含了单词，翻译和出现次数）',
    `index` longtext    null comment '已经背过的index'
)
    comment '保存单词文件';

