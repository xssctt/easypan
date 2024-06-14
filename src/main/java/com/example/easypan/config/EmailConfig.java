package com.example.easypan.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("emailConfig")
public class EmailConfig {

    @Value("${spring.mail.username}")
    private String sendUserName;

    public String getSendUserName() {
        return sendUserName;
    }
}
