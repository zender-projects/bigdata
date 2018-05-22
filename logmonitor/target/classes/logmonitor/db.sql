/* 用户表 */
drop table if exists `tb_log_monitor_user`;
create table `tb_log_monitor_user`(
    `id` bigint(20) not null auto_increment comment '用户编号，自增',
    `name` varchar(20) default null comment '用户名称',
    `mobile` varchar(11) default null comment '用户手机号',
    `email` varchar(50) default null comment '用户邮箱',
    `isValid` int(1) default 0 comment '是否有效 0：有效 1：无效',
    `create_time` timestamp not null default current_timestamp comment '创建时间',
    `update_time` timestamp not null default current_timestamp on update current_timestamp comment '更新时间',
    `create_user` varchar(100) default null comment '创建用户',
    `update_user` varchar(100) default null comment '修改用户'
)default charset=utf8;

insert  into `log_monitor_user`(`id`,`name`,`mobile`,`email`,`isValid`,`createDate`,`updateDate`,`createUser`,`updateUser`) values
(1,'张冬','15101535064','15101535064@163.com',1,'2015-11-12 14:59:37','2015-11-11 16:59:13','maoxiangyi','maoxiangyi'),
(2,'张冬','15101535064','15101535064@163.com',1,'2015-11-12 15:00:14','2015-11-12 15:00:16','maoxiangyi','maoxiangyi'),
(3,'张冬','15101535064','15101535064@163.com',1,'2015-11-12 15:01:11','2015-11-12 15:01:13','maoxiangyi','maoxiangyi');

/* 应用类型表 */
drop table if exists `tb_log_monitor_app_type`;
create table `tb_log_monitor_app_type`(
    `id` int(11) not null auto_increment comment '应用类型编号',
    `name` varchar(100) default null comment '应用类型名称',
    `isValid` int(1) default 0 comment '是否有效 0：有效 1：无效',
    `create_time` timestamp not null default current_timestamp comment '创建时间',
    `update_time` timestamp not null default current_timestamp on update current_timestamp comment '更新时间',
    `create_user` varchar(100) default null comment '创建用户',
    `update_user` varchar(100) default null comment '修改用户'
)default charset=utf8;
insert  into `log_monitor_app_type`(`id`,`name`,`createDate`,`updataDate`) values
(1,'storm','2015-11-11 16:58:40','2015-11-11 16:58:42');

/* 应用表 */
drop table if exists `tb_log_monitor_app`;
create table `tb_log_monitor_app`(
     `id` int(11) not null auto_increment comment '应用编号',
     `name` varchar(100) default null comment '应用名称',
     `desc` varchar(250) default null comment '应用描述',
     `isOnline` int(1) default 1 comment '应用是否在线 0：在线 1：不在线',
     `typeId` int(11) default null comment '应用类型id',
     `userIds` varchar(100) default null comment '应用所属用户id，逗号隔开',
     `isValid` int(1) default 0 comment '是否有效 0：有效 1：无效',
     `create_time` timestamp not null default current_timestamp comment '创建时间',
     `update_time` timestamp not null default current_timestamp on update current_timestamp comment '更新时间',
     `create_user` varchar(100) default null comment '创建用户',
     `update_user` varchar(100) default null comment '修改用户'

)default charset=utf8;;

insert  into `log_monitor_app`(`id`,`name`,`desc`,`isOnline`,`typeId`,`createDate`,`updateaDate`,`createUser`,`updateUser`,`userId`) values
(1,'storm集群','storm集群',1,1,'2015-11-12 18:15:23','2015-11-11 16:58:21','maoxiangyi','maoxiangyi','1'),
(2,'java应用','java引用',1,1,'2015-11-12 18:15:28','2015-11-12 09:55:45','maoxiangyi','maoxiangyi','1');

/* 规则表 */
drop table if exists `tb_log_monitor_rule`;
create table `tb_log_monitor_rule`(
    `id` int(11) not null auto_increment comment '规则编号',
    `name` varchar(100) default null comment '规则名称',
    `desc` varchar(250) default null comment '规则描述',
    `keyword` varchar(100) default null comment '规则关键字',
    `appId` int(11) default null comment '规则所属应用ID',
    `isValid` int(1) default 0 comment '是否有效 0：有效 1：无效',
    `create_time` timestamp not null default current_timestamp comment '创建时间',
    `update_time` timestamp not null default current_timestamp on update current_timestamp comment '更新时间',
    `create_user` varchar(100) default null comment '创建用户',
    `update_user` varchar(100) default null comment '修改用户'
)default charset=utf8;

insert  into `log_monitor_rule`(`id`,`name`,`desc`,`keyword`,`isValid`,`appId`,`createDate`,`updateDate`,`createUser`,`updateUser`) values
(1,'exe','Exception','Exception',1,1,'2015-11-11 17:02:05','2015-11-11 16:57:25','maoxiangyi','maoxiangyi'),
(2,'sys','测试数据','sys',1,2,'2015-11-12 10:02:13','0000-00-00 00:00:00','maoxiangyi','maoxiangyi'),
(3,'error','错误信息','error',1,3,'2015-11-12 16:00:56','2015-11-12 16:00:58','maoxiangyi','maoxiangyi');

/* 规则记录表 */
drop table if exists `tb_log_monitor_notify_record`;
create table `tb_log_monitor_notify_record`(
    `id` int(11) not null auto_increment comment '告警信息编号',
    `appId` int(11) default null comment '告警信息所属应用',
    `ruleId` int(11) default null comment '告警信息所属规则编号',
    `isEmail` int(1) default 0 comment '是否邮件通知 0:未告知 1：已告知',
    `isPhone` int(1) default 0 comment '是否短信通知 0：未告知 1：已告知',
    `isClosed` int(1) default 0 comment '是否处理完毕 0：未处理 1：已处理',
    `noticeInfo` varchar(500) default null comment '告警信息明细',
    `create_time` timestamp not null default current_timestamp comment '创建时间',
    `update_time` timestamp not null default current_timestamp on update current_timestamp comment '更新时间'
);
