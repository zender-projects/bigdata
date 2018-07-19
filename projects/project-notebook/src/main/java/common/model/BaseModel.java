package common.model;

import common.constant.Flag;
import lombok.Data;

import java.util.Date;

@Data
public abstract class BaseModel {
    protected Flag flag;
    protected Date createTime;
    private int createUser;
    protected Date updateTime;
    protected int updateUser;
}
