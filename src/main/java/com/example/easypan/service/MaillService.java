package com.example.easypan.service;

import com.example.easypan.entity.po.Maill;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class MaillService {
    @Resource
    JavaMailSender sender;

    public void SendMail(Maill maill){
        //SimpleMailMessage是一个比较简易的邮件封装，支持设置一些比较简单内容
        SimpleMailMessage message = new SimpleMailMessage();
        //设置邮件标题
        message.setSubject(maill.getHeader());
        //设置邮件内容
        message.setText(maill.getContent());
        //设置邮件发送给谁，可以多个，这里就发给你的QQ邮箱
        message.setTo(maill.getMaill());
        //邮件发送者，这里要与配置文件中的保持一致
        message.setFrom("xss13523773853@163.com");
        //OK，万事俱备只欠发送
        sender.send(message);
    }


    public void SendMailop(Maill maill){
        MimeMessage message = sender.createMimeMessage();
        //使用MimeMessageHelper来帮我们修改MimeMessage中的信息
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setSubject(maill.getHeader());
            helper.setText(maill.getContent());
            helper.setTo(maill.getMaill());
            helper.setFrom("xss13523773853@163.com");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        //发送修改好的MimeMessage
        sender.send(message);
    }

}
