package domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Message {
    private int appId;
    private String appName;
    private String content;
    private int ruleId;
    private String keyword;
    private int isEmail;
    private int isPhone;
}
