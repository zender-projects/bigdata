package mail;

import lombok.extern.java.Log;
import org.apache.commons.lang.StringUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Objects;
import java.util.Properties;

/**
 * 邮件发送器.
 * @author mac
 * */
@Log
public class MailSender {


    /**
     * 发送邮件，邮件内容为文本格式
     * @param mailInfo
     * @return boolan
     * */
    public static boolean sendMail(MailInfo mailInfo) {
        try{
            Message mailMessage = generateBaseInfo(mailInfo);
            mailMessage.setText(mailInfo.getMailContent());
            Transport.send(mailMessage);
            log.info("[TEXT 邮件发送完毕，成功时间：+ " + System.currentTimeMillis() +"]");
            return true;
        }catch (MessagingException ex) {
            ex.printStackTrace();
            log.info("[邮件发送异常]");
        }catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            log.info("[邮件发送异常]");
        }
        return false;
    }


    /**
     * 发送邮件，邮件内容
     * @param mailInfo
     * @return boolean
     * */
    public static boolean sendHtmlMail(MailInfo mailInfo) {
        try{
            Message mailMessage = generateBaseInfo(mailInfo);
            Multipart mainPart = new MimeMultipart();
            BodyPart html = new MimeBodyPart();
            html.setContent(mailInfo.getMailContent(), "text/html;charset=utf-8");
            mainPart.addBodyPart(html);
            mailMessage.setContent(mainPart);
            Transport.send(mailMessage);
            log.info("[HTML 邮件发送完毕，成功时间：" + System.currentTimeMillis() +"]");
            return true;
        }catch (MessagingException ex) {
            ex.printStackTrace();
            log.info("[邮件发送异常]");
        }catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            log.info("[邮件发送异常]");
        }
        return false;
    }


    public static Message generateBaseInfo(MailInfo mailInfo)
            throws UnsupportedEncodingException, MessagingException {

        //判断是否需要身份验证
        MailAuthenticator authenticator = null;
        Properties properties = mailInfo.getProperties();
        if(mailInfo.isAuthValidate()) {
            authenticator = new MailAuthenticator(mailInfo.getUserName(),
                    mailInfo.getUserPassword());
        }

        //根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session.getDefaultInstance(properties, authenticator);
        Message mailMessage = null;
        //根据session 创建一个邮件消息
        mailMessage = new MimeMessage(sendMailSession);
        //创建邮件发送者地址
        Address from = new InternetAddress(mailInfo.getFromAddress(), mailInfo.getUserName());
        mailMessage.setFrom(from);
        //多接收人
        if(!Objects.isNull(mailInfo.getToAddress()) && mailInfo.getToAddress().contains(",")) {
            mailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailInfo.getToAddress()));
        }else {
            //单接收人
            Address to = new InternetAddress(mailInfo.getToAddress());
            mailMessage.setRecipient(Message.RecipientType.TO, to);
        }
        if(StringUtils.isNotBlank(mailInfo.getCcAddress())) {
            //多抄送人
            if(mailInfo.getCcAddress().contains(",")) {
                mailMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(mailInfo.getCcAddress()));
            }else{
                //单抄送人
                mailMessage.setRecipient(Message.RecipientType.CC, new InternetAddress(mailInfo.getCcAddress()));
            }
        }
        mailMessage.setSubject(mailInfo.getMailSubject());
        mailMessage.setSentDate(new Date());
        return mailMessage;
    }

    public static void main(String[] args) {

        MailInfo info = new MailInfo();
        info.setToAddress("758751094@qq.com");
        info.setMailContent("aaaaa");

        MailSender.sendMail(info);

    }
}
