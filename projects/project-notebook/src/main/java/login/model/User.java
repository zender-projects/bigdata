package login.model;

import common.model.BaseModel;
import lombok.Data;

/**
 *  User model.
 *  @author mac
 *
 create table tnb_user(
 id int not null auto_increment,
 name varchar(255) not null,
 password varchar(255) not null,
 salt varchar(255) not null,
 status tinyint not null default 0,
 create_time timestamp not null default current_timestamp,
 create_userid int not null,
 update_time timestamp not null default current_timestamp on update current_timestamp,
 update_userid int not null
 );
 * */
@Data
public class User extends BaseModel{
    private int id;
    private String username;
    private String password;
    private String salt;
}
