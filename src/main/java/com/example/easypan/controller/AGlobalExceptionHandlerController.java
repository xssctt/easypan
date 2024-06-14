package com.example.easypan.controller;



import com.example.easypan.entity.vo.ResponseVo;

import com.example.easypan.enums.ResponseCodeEnum;

import com.example.easypan.exception.BusinessException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import javax.servlet.http.HttpServletRequest;
import java.net.BindException;

@RestControllerAdvice
public class AGlobalExceptionHandlerController extends ABaseController{

    private static final Logger logger= LoggerFactory.getLogger(AGlobalExceptionHandlerController.class);

    @ExceptionHandler(value = Exception.class)
    Object handleException(Exception e, HttpServletRequest request){
        logger.error("",request.getRequestURI(),e);
        ResponseVo ajaxResponse=new ResponseVo();

        if (e instanceof NoHandlerFoundException){

            ajaxResponse.setInfo(ResponseCodeEnum.CODE_404.getMsg());
            ajaxResponse.setStatus(STATUS_ERROR);
            ajaxResponse.setCode(ResponseCodeEnum.CODE_404.getCode());
        }else if (e instanceof BusinessException){
            BusinessException businessException=(BusinessException) e;
            ajaxResponse.setInfo(businessException.getMessage());
            ajaxResponse.setStatus(STATUS_ERROR);
            ajaxResponse.setCode(businessException.getCode());
        }else if (e instanceof BindException){
            ajaxResponse.setInfo(ResponseCodeEnum.CODE_600.getMsg());
            ajaxResponse.setStatus(STATUS_ERROR);
            ajaxResponse.setCode(ResponseCodeEnum.CODE_600.getCode());
        }else if(e instanceof DuplicateKeyException){
            ajaxResponse.setInfo(ResponseCodeEnum.CODE_601.getMsg());
            ajaxResponse.setStatus(STATUS_ERROR);
            ajaxResponse.setCode(ResponseCodeEnum.CODE_601.getCode());
        }else {
            ajaxResponse.setInfo(ResponseCodeEnum.CODE_500.getMsg());
            ajaxResponse.setStatus(STATUS_ERROR);
            ajaxResponse.setCode(ResponseCodeEnum.CODE_500.getCode());
        }

        return ajaxResponse;
    }

}

