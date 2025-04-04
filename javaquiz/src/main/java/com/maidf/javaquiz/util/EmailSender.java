package com.maidf.javaquiz.util;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Data
@ConfigurationProperties("email")
@Primary
public class EmailSender {

    private String host;
    private String from;
    private String username;
    private String password;

    @Autowired
    private EncryUtil encryUtil;

    public void sendEmail(String to, String msg) throws EmailException {

        String pass = null;
        try {
            pass = encryUtil.decrypt(password);
        } catch (Exception e) {
            log.error("邮箱密码解密失败！");
        }

        SimpleEmail email = new SimpleEmail();
        email.setHostName(host);
        email.setSmtpPort(587); // 使用TLS
        email.setAuthentication(username, pass);
        email.setStartTLSRequired(true);
        email.setFrom(from);

        email.addTo(to);
        email.setSubject("验证码");
        email.setMsg(msg);
        email.send();
        log.info("Email successfully sent!");

    }

}