package mail;

import jdk.nashorn.internal.objects.annotations.Getter;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Data
public class MailInfo {

    private String mailServerHost = MailCenterConstant.SMTP_SERVER;    //邮件服务器ip
    private String mailServerPort = "25";    // 邮件服务器端口
    private String userName = MailCenterConstant.USERNAME;          //sender用户名
    private String userPassword = MailCenterConstant.PASSWORD;      //密码
    private String fromAddress = MailCenterConstant.USERNAME;       //发送人邮箱
    private String toAddress;         //收件人邮箱
    private String ccAddress;         //抄送人邮箱
    private String formUserName = "日志监控平台";      //邮件发送人Title
    private String mailSubject;       //邮件主题
    private String mailContent;       //邮件内容
    private boolean authValidate = true;     //是否需要身份验证
    private Properties properties;    //邮件会话属性

    public MailInfo() { }

    /**
     * @param title 标题
     * @param content 邮件内容
     * @param receiver 收件人
     * @param ccList 抄送人
     * */
    public MailInfo(String title, String content,
                    List<String> receiver, List<String> ccList) {
        this.mailServerHost = MailCenterConstant.SMTP_SERVER;
        this.userName = MailCenterConstant.USERNAME;
        this.userPassword = MailCenterConstant.PASSWORD;
        this.fromAddress = MailCenterConstant.FROM_ADDRESS;
        this.toAddress = StringUtils.join(receiver, ",");
        this.ccAddress = StringUtils.join(ccList, ",");
        this.mailSubject = title;
        this.mailContent = content;
    }

    @Getter
    public Properties getProperties() {
        Properties p = new Properties();
        p.put("mail.smtp.host", this.mailServerHost);
        p.put("mail.smtp.port", this.mailServerPort);
        p.put("mail.smtp.auth", authValidate ? "true" : "false");
        p.put("mail.smtp.starttls.enable", "true");
        return p;
    }
}
