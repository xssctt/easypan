package com.example.easypan.enums;

public enum FileStatusEnum {
    TRANSFER(0,"转码中"),
    TRANSFER_SUCCES(1,"转码成功"),
    TRANSFER_FAIL(2,"转码失败"),
    USING(3,"使用中");


    private Integer status;
    private String desc;

    FileStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
