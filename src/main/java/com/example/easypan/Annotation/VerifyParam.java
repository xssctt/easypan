package com.example.easypan.Annotation;


import com.example.easypan.enums.VerifyRefexEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface VerifyParam {
    int min() default -1;

    int max() default -1;

    boolean required() default false;

    VerifyRefexEnum regex() default VerifyRefexEnum.NO;








}
