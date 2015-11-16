package com.renren.ugc.comment.storm.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;



/**
 * 发送邮件的接口
 * 
 * @author fu.li@opi-corp.com
 * @version 2010-3-22 下午03:55:29
 */
public class EmailUtils {

    private static EmailUtils instance = new EmailUtils();

    public static EmailUtils getInstance() {
        return instance;
    }

    private EmailUtils(){
    }

    /**
     * 发送邮件的接口
     * 
     * @param receiverListStr 接收者列表，如fu.li@opi-corp.com,xiaojie.bai@opi-corp.com
     * @param ccListStr 抄送列表，如fu.li@opi-corp.com,xiaojie.bai@opi-corp.com
     * @param messageBody 邮件主题内容
     * @param title 邮件标题
     */
    public void sendEmail(String title, String messageBody,
        String receiverListStr, String ccListStr) {
        try {
            Properties props = new Properties();
            Session sendMailSession;
            // Store store;
            Transport transport;
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.host", Constants.ALARM_SMTP_SERVER); // smtp主机名。
            props.put("mail.smtp.user", Constants.ALARM_SMTP_USERNAME); // 发送方邮件地址。
            props.put("mail.smtp.password", Constants.ALARM_SMTP_PASSWORD); // 邮件密码。
            PopupAuthenticator popA = new PopupAuthenticator();// 邮件安全认证。
            PasswordAuthentication pop =
                    popA.performCheck(Constants.ALARM_SMTP_USERNAME,
                    		Constants.ALARM_SMTP_PASSWORD); // 填写用户名及密码
            sendMailSession = Session.getInstance(props, popA);
            Message newMessage = new MimeMessage(sendMailSession);
            newMessage.setFrom(new InternetAddress(Constants.ALARM_SMTP_USERNAME));
            InternetAddress[] addressList =
                    InternetAddress.parse(receiverListStr);
            newMessage.setRecipients(Message.RecipientType.TO, addressList); // 接收方邮件地址
            Calendar ca = Calendar.getInstance();
            ca.setTime(new java.util.Date());
            newMessage.setSubject(title);
            newMessage.setSentDate(new Date());

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(messageBody, "text/html;charset=gbk");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            newMessage.setContent(multipart);
            // 添加抄送
            if (ccListStr != null && ccListStr.trim().length() > 0) {
                String[] ccAddressList = ccListStr.split(",");
                InternetAddress[] ccList =
                        new InternetAddress[ccAddressList.length];
                for (int i = 0; i < ccAddressList.length; i++) {
                    ccList[i] = new InternetAddress(ccAddressList[i]);
                }
                newMessage.setRecipients(Message.RecipientType.CC, ccList);
            }
            transport = sendMailSession.getTransport("smtp");
            transport.send(newMessage);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }

    public class PopupAuthenticator extends Authenticator {

        String username = null;
        String password = null;

        public PopupAuthenticator(){
        }

        public PasswordAuthentication performCheck(String user, String pass) {
            username = user;
            password = pass;
            return getPasswordAuthentication();
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }

    public static void main(String a[]) {
        String title = "title";
        String messageBody = "messageBody";
        String receiverListStr = "lei.xu1@renren-inc.com";
        EmailUtils.getInstance().sendEmail(title, messageBody, receiverListStr,
            null);
    }
}
