package com.example.easypan.enums;

import org.apache.commons.lang3.ArrayUtils;

/**
 * 文件详细分类 枚举
 */
public enum FileTypeEnum {
    VIDEO(FileCategoryEnum.VIDEO,1,new String[]{".mp4",".avi",".rmvb",".mkv",".mov"},"视频文件"),
    MUSIC(FileCategoryEnum.MUSIC,2,new String[]{".mp3",".wav",".wma",".mp2",".flac",".midi",".ra",".ape",".aac",".cda"},"音频文件"),
    IMAGE(FileCategoryEnum.IMAGE,3,new String[]{".jpeg",".jpg",".png","gif",".bmp",".dds",".psd",".pdt",".webp",".xmp",".svg",".tiff"},"图片"),
    PDF(FileCategoryEnum.DOC,4,new String[]{".pdf"},"pdf文件"),
    WORD(FileCategoryEnum.DOC,5,new String[]{".docx"},"word文件"),
    EXCEL(FileCategoryEnum.DOC,6,new String[]{".xlsx"},"excel文件"),
    TXT(FileCategoryEnum.DOC,7,new String[]{".txt"},"txt文件"),
    PROGRAME(FileCategoryEnum.OTHER,8,new String[]{".h",".c",".hpp",".hxx",".cpp",".cc",".c++",".cxx",".m",".o",".s",".dll",".cs","java",".class",".js",".ts",".css",".scss",".vue",".jsx",".sql",".md",".json",".html",".xml",""},"code"),
    ZIP(FileCategoryEnum.OTHER,8,new String[]{".rar",".zip",".7z",".cab",".arj",".lzh",".tar",".gz",".ace",".ace",".uue",".bz",".jar",".iso","mpq"},"压缩包zip"),
    OTHER(FileCategoryEnum.OTHER,10,new String[]{},"其他");

    private  FileCategoryEnum fileCategoryEnum;
    private Integer type;
    private String[] suffix;
    private String desc;

    FileTypeEnum(FileCategoryEnum fileCategoryEnum, Integer type, String[] suffix, String desc) {
        this.fileCategoryEnum = fileCategoryEnum;
        this.type = type;
        this.suffix = suffix;
        this.desc = desc;
    }
    public static FileTypeEnum getFileTypeBySuffix(String suffix){
        for (FileTypeEnum type: FileTypeEnum.values()) {
            if (ArrayUtils.contains(type.getSuffix(),suffix)){
                return type;
            }
        }
        return FileTypeEnum.OTHER;
    }


    public static FileTypeEnum getFileTypeByType(Integer type){
        for (FileTypeEnum typeEnum: FileTypeEnum.values()) {
            if ( typeEnum.getType().equals(type) ){
                return typeEnum;
            }
        }
        return null;
    }





    public FileCategoryEnum getFileCategoryEnum() {
        return fileCategoryEnum;
    }

    public Integer getType() {
        return type;
    }

    public String[] getSuffix() {
        return suffix;
    }

    public String getDesc() {
        return desc;
    }
}
