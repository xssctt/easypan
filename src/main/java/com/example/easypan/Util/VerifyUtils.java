package com.example.easypan.Util;

import com.example.easypan.enums.VerifyRefexEnum;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerifyUtils {

    /**
     *
     * @param regex
     * @param value
     * @return
     */
    public static boolean verify(String regex, String value) {
        if (StringTools.isEmpty(value)){
            return false;
        }
        //regex 正则-->pattern
        Pattern pattern = Pattern.compile(regex);
        //value 正则匹配value--->matcher
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
        //matcher.matches()：尝试将整个输入字符串与正则表达式匹配。
        //matcher.find()：尝试在输入字符串中找到与正则表达式匹配的子字符串。
        //matcher.lookingAt()：尝试从输入字符串的起始位置开始匹配正则表达式。
        //matcher.group()  获取与上一次匹配操作相匹配的子字符串。
    }

    /**
     *
     * @param regex
     * @param value
     * @return
     */
    public static boolean verify(VerifyRefexEnum regex, String value){
        return verify(regex.getRegex(),value);
    }
}



















