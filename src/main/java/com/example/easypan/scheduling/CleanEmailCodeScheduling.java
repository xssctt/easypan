package com.example.easypan.scheduling;

import com.example.easypan.entity.po.EmailCode;
import com.example.easypan.entity.query.EmailCodeQuery;
import com.example.easypan.mappers.EmailCodeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

//@Slf4j
//@Component
//public class CleanEmailCodeScheduling {
//
//
//    @Resource
//    private EmailCodeMapper<EmailCode, EmailCodeQuery> emailCodeMapper;
//
//    //0 0 0 1/1 * ? *
//    @Scheduled(cron = "0 0 0 1/1 * ? *")
//    public void cleancode(){
//        log.info("清除 email code 失效的数据");
//        emailCodeMapper.deleteAllStatusIsOne();
//    }
//
//}
