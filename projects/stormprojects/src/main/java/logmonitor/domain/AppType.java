package logmonitor.domain;

import lombok.*;

import java.util.Date;

/**
 create table `t_app_type`(
 id bigint(20) primary key auto_increment not null comment '主键ID',
 name varchar(100) default null comment '应用名称, eg:linux, kafka, storm, web',
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
public class AppType {
    private int id;
    private String name;

    private int idValid;
    private Date createTime;
    private Date updateTime;
    private int createUser;
    private int updateUser;
}
