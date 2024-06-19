package com.example.easypan;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.lang.Console;
import com.example.easypan.Util.StringTools;
import com.example.easypan.bean.Constants;
import com.example.easypan.config.AppConfig;
import com.example.easypan.config.EmailConfig;
import com.example.easypan.enums.FileTypeEnum;
import com.example.easypan.redis.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@SpringBootTest
class EasypanApplicationTests {
    @Resource
    EmailConfig config;

    @Resource
    RedisUtil redisUtil;


    @Test
    void contextLoads() {

//        redisUtil.setCacheObject("xssx","sss");
//        redisUtil.redisTemplate.opsForValue().set("xss","aaaaaaaaaaaaaa");
//        redisUtil.expire("xss",10);

        //定义图形验证码的长和宽
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);

//图形验证码写出，可以写出到文件，也可以写出到流
        lineCaptcha.write("d:/line.png");
//输出code
        Console.log(lineCaptcha.getCode());
//验证图形验证码的有效性，返回boolean值
        lineCaptcha.verify("1234");

//重新生成验证码
        lineCaptcha.createCode();
        lineCaptcha.write("d:/line.png");
//新的验证码
        Console.log(lineCaptcha.getCode());
//验证图形验证码的有效性，返回boolean值
        lineCaptcha.verify("1234");


    }

    @Resource
    AppConfig appConfig;

    @Test
    void context() {

      String a="aaaa/cvcccfff/bbb.vv";

        System.out.println(a.substring(0,a.indexOf("/")));


//
//        String avatarFloderName=Constants.FILE_FOLDER_FILE+Constants.FILE_FOLDER_AVATAR_NAME;
//        String avatarFloderPath=appConfig.getPath()+avatarFloderName;
//        System.out.println(new File(avatarFloderPath).getPath());
//        System.out.println(avatarFloderPath);
//        if (!new File(avatarFloderPath+ Constants.AVATAR_DEFULT).exists()){
//
//            System.out.println("21222222");
//        }else {
//            System.out.println("success");
//        }





    }


}
