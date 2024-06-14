package com.example.easypan.Annotation;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;


@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Mapping
@Documented
public @interface GlobalInterceptor {

    /**
     *
     * @return
     */

    boolean chackparams() default false;


    /**
     *
     * @return
     */
    boolean chackLogin() default true;


    /**
     *
     * @return
     */
    boolean chackAdmin() default false;


}
