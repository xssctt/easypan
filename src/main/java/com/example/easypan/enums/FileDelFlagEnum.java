package com.example.easypan.enums;

public enum FileDelFlagEnum {
    DEL(0,"删除"),
    RECYCLE(1,"回收"),
    USING(2,"使用");
    private Integer flag;
    private String desc;

    FileDelFlagEnum(Integer flag, String desc) {
        this.desc = desc;
        this.flag = flag;
    }


    public Integer getFlag() {
        return flag;
    }

    public String getDesc() {
        return desc;
    }
}
