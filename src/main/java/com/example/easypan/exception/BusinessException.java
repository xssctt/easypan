package com.example.easypan.exception;



import com.example.easypan.enums.ResponseCodeEnum;

public class BusinessException extends RuntimeException{

    private ResponseCodeEnum codeEnum;

    private Integer code;

    private String message;

    //Exception(String:message,Throwable)  message
    public BusinessException(String message,Throwable e){
        super(message,e);
        this.message=message;
    }
    //Exception(String:message)   message
    public BusinessException(String message){
        super(message);
        this.message=message;
    }
////Exception(Throwable)
    public BusinessException(Throwable e){
        super(e);
    }
////Exception(codeEnum:ResponseCodeEnum)  codeEnum code  message
    public BusinessException(ResponseCodeEnum codeEnum){
        super(codeEnum.getMsg());
        this.codeEnum=codeEnum;
        this.code=codeEnum.getCode();
        this.message=codeEnum.getMsg();
    }
//Exception(String:message)   message code
    public BusinessException(Integer code,String message){
        super(message);
        this.code=code;
        this.message=message;
    }


    public ResponseCodeEnum getCodeEnum() {
        return codeEnum;
    }

    public void setCodeEnum(ResponseCodeEnum codeEnum) {
        this.codeEnum = codeEnum;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}



