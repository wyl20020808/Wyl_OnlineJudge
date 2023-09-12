
create table problem
(
    problem_id  bigint auto_increment
        primary key,
    create_man  bigint   not null,
    create_time DATETIME not null,
    update_man  bigint   not null,
    update_time DATETIME not null
)
    comment '存放problem的大表' charset = utf8;

create table userinfo
(
    user_id       bigint auto_increment primary key,
    register_time DATETIME     null,
    user_name     varchar(32)  not null,
    password      varchar(128) not null,
    user_email    varchar(50)  not null,
    icon          varchar(255) default ''
)
    comment '存放problem的大表' charset = utf8;


-- auto-generated definition1
create table problemcontent
(
    problemid    bigint auto_increment
        primary key,
    title        varchar(50)   default '无'   null,
    description  varchar(2000) default '无'   null,
    background   varchar(2000) default '无'   null,
    hint         varchar(2000) default '无'   null,
    timelimit    int           default 1000   null,
    memorylimit  int           default 1024   null,
    difficulty   varchar(2000) default '无'   null,
    submitcount  int           default 0      null,
    aceptedcount int           default 0      null,
    inputformat  varchar(255)  default '无'   null,
    outputformat varchar(255)  default '无'   null,
    source       varchar(255)  default '原创' null,
    algorithm    varchar(255)  default '无'   null,
    constraint problem_id
        unique (problemid)
)
    comment 'problem具体内容表' charset = utf8;


create table problemsample
(
    id        bigint auto_increment
        primary key,
    input     varchar(50) null,
    output    varchar(50) null,
    problemid int         not null
)
    comment 'problem样例表' charset = utf8;
