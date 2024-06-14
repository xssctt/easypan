package com.example.easypan.Aspect;

import jdk.dynalink.Operation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Optional;

@Aspect
@Component
public class demo {


    @Pointcut("@annotation(com.example.easypan.Annotation.GlobalInterceptor)")
    public void before(){

    }

    @Before("before()")
    public void chulidemo(JoinPoint joinPoint){

    }



    //-----------------------------------
    @After("@annotation(com.example.easypan.Annotation.GlobalInterceptor)")
    public void chulihou(JoinPoint point){
        Object[] args = point.getArgs();
    }

}
