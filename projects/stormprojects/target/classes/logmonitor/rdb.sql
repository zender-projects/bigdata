/**
    用户表.
*/
drop table if exists `t_users`;
create table `t_users` (
    id bigint(20) primary key auto_increment not null comment '主键ID',
    name varchar(20) default null comment '用户名称',
    mobile varchar(11) default null comment '用户手机号',
    email varchar(50) default null comment '用户邮箱',
    is_valid int(1) default 0 comment '是否有效 0 无效 1 有效',
    create_time timestamp default current_timestamp comment '创建时间',
    update_time timestamp default current_timestamp on update current_timestamp comment '更新时间',
    create_user bigint not null comment '创建人',
    update_user bigint not null comment '更新人'
);
insert  into `t_users`(`id`,`name`,`mobile`,`email`,`is_valid`,`create_time`,`update_time`,`create_user`,`update_user`) values
(1,'张冬','15101535064','15101535064@163.com',1,'2015-11-12 14:59:37','2015-11-11 16:59:13',1,1),
(2,'张冬','15101535064','15101535064@163.com',1,'2015-11-12 15:00:14','2015-11-12 15:00:16',1,1),
(3,'张冬','15101535064','15101535064@163.com',1,'2015-11-12 15:01:11','2015-11-12 15:01:13',1,1);

/**
    应用类型表.
*/
drop table if exists `t_app_type`;
create table `t_app_type`(
    id bigint(20) primary key auto_increment not null comment '主键ID',
    name varchar(100) default null comment '应用名称, eg:linux, kafka, storm, web',
    is_valid int(1) default 0 comment '是否有效 0 无效 1 有效',
    create_time timestamp default current_timestamp comment '创建时间',
    update_time timestamp default current_timestamp on update current_timestamp comment '更新时间',
    create_user bigint not null comment '创建人',
    update_user bigint not null comment '更新人'
);
insert into `t_app_type`(id, name, is_valid, create_time, update_time, create_user, update_user) values
(1,'storm', 1, '2015-11-11 16:58:40','2015-11-11 16:58:42',1,1);

/**
    应用表.
*/
drop table if exists `t_apps`;
create table `t_apps` (
    id bigint(20) primary key auto_increment not null comment '主键ID',
    name varchar(100) not null comment '应用名称',
    type_id bigint(20) not null comment '应用类型',
    business varchar(50) not null comment '所属业务',
    is_online int(1) default 0 comment '是否上线  0 未上线  1 上线',
    description varchar(500) default null comment '描述',
    is_valid int(1) default 0 comment '是否有效 0 无效 1 有效',
    create_time timestamp default current_timestamp comment '创建时间',
    update_time timestamp default current_timestamp on update current_timestamp comment '更新时间',
    create_user bigint not null comment '创建人',
    update_user bigint not null comment '更新人'
);
insert  into `t_apps`(`id`,`name`,`type_id`,`business`,`is_online`,`description`,`is_valid`,`create_time`,`update_time`,`create_user`, `update_user`) values
(1,'storm集群',1,'支付',1,'测试',1,'2015-11-12 18:15:23','2015-11-11 16:58:21',1,1),
(2,'java应用',,1,'物流',1,'测试',1,'2015-11-12 18:15:28','2015-11-12 09:55:45',1,1);

/**
  规则表.
*/
drop table if exists `t_rules`;
create table `t_rules`(
    id bigint primary key auto_increment not null comment '主键ID',
    name varchar(100) default null comment '规则名称',
    keyword varchar(100) default null comment '规则关键字',
    app_id bigint(20) not null comment '规则所属应用',
    description varchar(500) default null comment '描述',
    is_valid int(1) default 0 comment '是否有效 0 无效 1 有效',
    create_time timestamp default current_timestamp comment '创建时间',
    update_time timestamp default current_timestamp on update current_timestamp comment '更新时间',
    create_user bigint not null comment '创建人',
    update_user bigint not null comment '更新人'
);
insert  into `t_rules`(`id`,`name`,`keyword`,`app_id`,`description`,`is_valid`,`create_time`,`update_time`,`create_user`,`update_user`) values
(1,'exe','Exception',2,'Exception',1,'2015-11-11 17:02:05','2015-11-11 16:57:25',1,1),
(2,'sys','测试数据',2,'测试数据',1,'2015-11-12 10:02:13','2015-11-12 10:02:13',1,1),
(3,'error','错误信息',2,'错误信息',1,'2015-11-12 16:00:56','2015-11-12 16:00:58',1,1);

/**
  规则触发记录表.
*/
drop table if exists `t_trigger_record`;
create table `t_trigger_record`(
    id bigint primary key auto_increment not null comment '主键ID',
    app_id bigint(20) not null comment '应用ID',
    rule_id bigint(20) not null comment '规则ID',
    is_email int(1) not null comment '是否邮件告知，0: 未告知  1：告知',
    is_phone int(1) not null comment '是否短信告知，0：未告知   1：告知',
    is_close int(1) not null comment '是否处理完毕, 0:未告知  1：告知',
    notice_info varchar(500) default null comment '告警消息明细',
    is_valid int(1) default 0 comment '是否有效 0 无效 1 有效',
    create_time timestamp default current_timestamp comment '创建时间',
    update_time timestamp default current_timestamp on update current_timestamp comment '更新时间',
    create_user bigint not null comment '创建人',
    update_user bigint not null comment '更新人'
);

