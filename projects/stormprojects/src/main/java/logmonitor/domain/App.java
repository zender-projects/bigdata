package logmonitor.domain;

import lombok.*;

import java.util.Date;

/**
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
 * */
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class App {
    private int id;
    private String name;
    private int typeId;
    private String business;
    private int isOnline;
    private String userIds;
    private String description;

    private int isValid;
    private Date createTime;
    private Date updateTime;
    private int createUser;
    private int updateUser;
}
