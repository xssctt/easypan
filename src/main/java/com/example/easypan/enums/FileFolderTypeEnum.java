package com.example.easypan.enums;

public enum FileFolderTypeEnum {
    FILE(0,"文件"),
    FOLDER(1,"目录");

    private Integer folderType;
    private String  desc;

    FileFolderTypeEnum(Integer folderType, String desc) {
        this.folderType = folderType;
        this.desc = desc;
    }

    public Integer getFolderType() {
        return folderType;
    }

    public String getDesc() {
        return desc;
    }
}
