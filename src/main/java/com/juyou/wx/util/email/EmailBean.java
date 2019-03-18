package com.juyou.wx.util.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 邮件bean
 *
 * @author zeopean
 * @since 2018-07-21
 */

@Component
public class EmailBean {

    /**
     * 发件人邮箱服务器
     */
    @Value("${spring.mail.host}")
    private String emailHost;

    /**
     * 发件人用户名
     */
    @Value("${spring.mail.username}")
    private String emailUserName;

    /**
     * 发件人邮件密码
     */
    @Value("${spring.mail.password}")
    private String emailPassword;

    /**
     * 发件人邮箱
     */
    @Value("${mail.from.addr}")
    private String fromEmail;

    /**
     * 收件人邮箱，多个邮箱以“;”分隔
     */
    @Value("${mail.to.addrs}")
    private String toEmails;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件内容
     */
    private String content;


    public EmailBean() {
    }

    public String getEmailHost() {
        return emailHost;
    }

    public void setEmailHost(String emailHost) {
        this.emailHost = emailHost;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getEmailUserName() {
        return emailUserName;
    }

    public void setEmailUserName(String emailUserName) {
        this.emailUserName = emailUserName;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }

    public String getToEmails() {
        return toEmails;
    }

    public void setToEmails(String toEmails) {
        this.toEmails = toEmails;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



    @Override
    public String toString() {
        return "EmailBean{" +
                "emailHost='" + emailHost + '\'' +
                ", emailUserName='" + emailUserName + '\'' +
                ", emailPassword='" + emailPassword + '\'' +
                ", fromEmail='" + fromEmail + '\'' +
                ", toEmails='" + toEmails + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
