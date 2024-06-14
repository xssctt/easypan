package com.example.easypan.enums;

public enum FileCategoryEnum {

    VIDEO(1,"video","视频"),
    MUSIC(2,"music","音频"),
    IMAGE(3,"image","图片"),
    DOC(4,"doc","文档"),
    OTHER(5,"other","其他");


    private Integer category;
    private String code;
    private String dosc;

    FileCategoryEnum(Integer category, String code, String dosc) {
        this.category = category;
        this.code = code;
        this.dosc = dosc;
    }

    public static FileCategoryEnum getByCode(String code){
        for (FileCategoryEnum f: FileCategoryEnum.values()) {
            if (f.getCode().equals(code)){
                return f;
            }
        }
        return null;
    }


    public Integer getCategory() {
        return category;
    }

    public String getCode() {
        return code;
    }

    public String getDosc() {
        return dosc;
    }
}
