package com.example.easypan.Util;

import com.example.easypan.bean.Constants;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;



public class StringTools {

    /**
     * 生成随机数
     *
     * @param count
     * @return
     * RandomStringUtils.random(count,false,true);
     * 各位 是否有字母 是否有数字
     */
    public static final String getRandomNumber(Integer count){
        return RandomStringUtils.random(count,false,true);
    }
    public static final String getRandomString(Integer count){
        return RandomStringUtils.random(count,true,true);
    }


    public static boolean isEmpty(String str) {

        if (null == str || "".equals(str) || "null".equals(str) || "\u0000".equals(str)){
            return true;
        }else if ("".equals(str.trim())){
            return true;
        }

        return false;
    }
    public static String encodingByMd5(String originString){
        return isEmpty(originString) ? null : DigestUtils.md5Hex(originString);
    }

    public static boolean pathIsOk(String filepath) {
        if (StringTools.isEmpty(filepath)) {
            return true;
        }
        if (filepath.contains("../") || filepath.contains("..\\")) {
            return false;
        }

        return true;
    }


    public static String rename(String fileName) {
        String fileNameRel=getFileNameNoSuffix(fileName);
        String suffix = getFileNameSuffix(fileNameRel);
        return fileNameRel+"_"+getRandomString(Constants.LENGTH_5)+suffix;
    }

    public static String getFileNameNoSuffix(String fileName) {
        Integer index = fileName.lastIndexOf(".");
        if (index == -1) {
            return fileName;
        }
        fileName=fileName.substring(0,index);
        return fileName;
    }

    public static String getFileNameSuffix(String fileName) {
        Integer index = fileName.lastIndexOf(".");
        if (index == -1) {
            return "";
        }
       String suffix=fileName.substring(index);
        return suffix;
    }


    public static void main(String[] args) {
        String a="ssss.aa.cc.vv";

        System.out.println(getFileNameSuffix(a));
        System.out.println(getFileNameNoSuffix(a));
        System.out.println(rename(a));
    }















}
