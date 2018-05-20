package domain;

import lombok.Data;
import lombok.ToString;

/**
 * 发送消息后将发送记录写入数据库
 * @author mac
 * */
@Data
@ToString
public class Record {
    private int id;
    private int appId;
    private int ruleId;
    private int isEmail;
    private int isPhone;
    private int isClosed;
    private String context;
}
