package logmonitor.domain;


import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String appId;
    private String line;
    private String ruleId;
    private String keyword;
    private int isEmail;
    private int isPhone;
    private String appName;
}
