package logmonitor.domain;

import lombok.*;

import java.util.Date;

/**
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
 * */
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int id;
    private String name;
    private String mobile;
    private String email;

    private int isValid;
    private Date createTime;
    private Date updateTime;
    private int createUser;
    private int updateUser;
}
