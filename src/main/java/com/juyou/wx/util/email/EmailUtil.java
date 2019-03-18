package com.juyou.wx.util.email;

import com.juyou.wx.util.logger.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 邮件工具
 *
 * @author zeopean
 * @since 2018-07-21
 */
@Component
public class EmailUtil {

    @Autowired
    private JavaMailSender mailSender;


    public void sendMail(EmailBean email) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(email.getFromEmail());
            mailMessage.setSubject(email.getSubject());
            mailMessage.setSentDate(new Date());
            mailMessage.setText(email.getContent());

            String[] toAddress = email.getToEmails().split(";");
            for (int i = 0; i < toAddress.length; i++) {
                mailMessage.setTo(toAddress[i]);
                // 发送邮件
                this.mailSender.send(mailMessage);
                // 打日志
            }
            LogUtil.info("发送邮件成功！msg:" + email.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
