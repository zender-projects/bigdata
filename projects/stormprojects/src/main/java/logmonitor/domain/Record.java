package logmonitor.domain;

import lombok.*;

import java.util.Date;

/**
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
 * */
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Record {

    private int id;
    private int appId;
    private int ruleId;
    private int isEmail;
    private int isPhone;
    private int isClose;
    private String noticeInfo;

    private int isValid;
    private Date createTime;
    private Date updateTime;
    private int createUser;
    private int updateUser;
}
