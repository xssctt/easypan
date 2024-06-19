package com.example.easypan.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SysSetting implements Serializable {

    private String redisterMailTitle="邮箱验证码";

    private String redisterMailContent="你好，你的邮箱验证码是: %s,15分钟内有效";

    private Integer userInitUserSpace=500;

    public String getRedisterMailTitle() {
        return redisterMailTitle;
    }

    public void setRedisterMailTitle(String redisterMailTitle) {
        this.redisterMailTitle = redisterMailTitle;
    }

    public String getRedisterMailContent() {
        return redisterMailContent;
    }

    public void setRedisterMailContent(String redisterMailContent) {
        this.redisterMailContent = redisterMailContent;
    }

    public Integer getUserInitUserSpace() {
        return userInitUserSpace;
    }

    public void setUserInitUserSpace(Integer userInitUserSpace) {
        this.userInitUserSpace = userInitUserSpace;
    }
}
