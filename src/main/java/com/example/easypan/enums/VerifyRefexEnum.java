package com.example.easypan.enums;

public enum VerifyRefexEnum {
    NO("","不校验"),
    IP("((?:(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d)\\.){3}(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d))","IP地址"),
    POSITIVE_INTEGER("^[0-9]*[1-9][0-9]*$","正整数"),
    NUMBER_LETTER_UNDER_LINE("^[A-Za-z0-9_]+$","数字_英文字母_下划线组成的字符串"),
    EMAIL("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$","邮箱"),
    PHONE("^(13[0-9]|14[5|7]|15[0-3]|15[5-9]|18[0-9]|17[0-9])\\d{8}$","手机号码"),
    COMMON("^[\\u4E00-\\u9FA5A-Za-z0-9_]+$","数字_字母_中文_下划线"),
    PASSWORD("^[a-zA-Z0-9_!@#$%^&*?]{8,12}$","只能_数字_字母_特殊字符_8-12位"),
    ACCOUNT("^[a-zA-Z][a-zA-Z0-9_]{0,}$","字母开头_数字_字母_下划线组成"),
    USERNAME("[A-Za-z0-9_\\-\\u4e00-\\u9fa5]+","");
    private String regex;
    private String desc;

    VerifyRefexEnum(String regex, String desc) {
        this.regex = regex;
        this.desc = desc;
    }

    public String getRegex() {
        return regex;
    }


    public String getDesc() {
        return desc;
    }


}
