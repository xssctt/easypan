package com.example.easypan.enums;

public enum UserStatusEnum {
    ENABLE("启用",1),
    UNENABLE("禁用",0);

    UserStatusEnum(String dec, Integer status) {
        this.dec = dec;
        this.status = status;
    }

    private String dec;
    private Integer status;

    public String getDec() {
        return dec;
    }

    public void setDec(String dec) {
        this.dec = dec;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
