package com.example.easypan.Aspect;

import com.example.easypan.Annotation.GlobalInterceptor;
import com.example.easypan.Annotation.VerifyParam;
import com.example.easypan.Util.StringTools;
import com.example.easypan.Util.VerifyUtils;
import com.example.easypan.bean.Constants;
import com.example.easypan.config.AppConfig;
import com.example.easypan.entity.dto.SessionWebUserDto;
import com.example.easypan.entity.po.UserInfo;
import com.example.easypan.enums.ResponseCodeEnum;
import com.example.easypan.exception.BusinessException;
import com.example.easypan.service.UserInfoService;
import com.jayway.jsonpath.internal.function.text.Concatenate;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Aspect
@Component
public class GlobalInterceptorAspect {

    private static final Logger logger= LoggerFactory.getLogger(GlobalInterceptorAspect.class);
    private static final String TYPE_STRING="java.lang.String";
    private static final String TYPE_LONG="java.lang.Long";
    private static final String TYPE_INTEGER="java.lang.Integer";
    @Resource
    private AppConfig appConfig;
    @Resource
    private UserInfoService userInfoService;

    @Pointcut("@annotation(com.example.easypan.Annotation.GlobalInterceptor)")
    private void requestInterceptor(){

    }


    @Before("requestInterceptor()")
    public void interceptorDo(JoinPoint joinPoint) throws BusinessException {
        try {
            Object target = joinPoint.getTarget();//被代理对象
            Object[] args = joinPoint.getArgs();// 拦截方法传入参数实际的值
            Object aThis = joinPoint.getThis();// 代理对象
            String methodName = joinPoint.getSignature().getName();// 拦截方法的名称
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();//转化MethodSignature  方法签名  joinPoint.getSignature()子类

            Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();//获取被代理对象方法  然后获取参数 类型 数组//String.class  int.class
            Method method = target.getClass().getMethod(methodName, parameterTypes); // 通过反射 （方法名称 参数类型）获取这个方法对象

            GlobalInterceptor annotation = method.getAnnotation(GlobalInterceptor.class);//获取注解
            if (null == annotation){
                return;
            }

            /**
             * 校验登录
             */
            if (annotation.chackLogin() || annotation.chackAdmin()){
                checkLogin(annotation.chackAdmin());
            }

            if (annotation.chackparams()){
                validateParam(method,args);
            }

        }catch (BusinessException e){
            logger.error("全局拦截器异常",e);
            throw e;
        }catch (Exception e){
            logger.error("全局拦截器异常",e);
            e.printStackTrace();
        }catch (Throwable e){
            logger.error("全局拦截器异常",e);
            e.printStackTrace();
        }
    }

    /**
     *
     * @param m 方法对象
     * @param arg 方法参数
     * @throws BusinessException
     */
    private void validateParam(Method m,Object[] arg)throws BusinessException{

        Parameter[] parameters = m.getParameters();//Method 参数对象 列表
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i]; //参数对象
            VerifyParam annotation = parameter.getAnnotation(VerifyParam.class);//获取参数的注解
            Object value = arg[i];// 参数值
            if (annotation == null){
                continue;
            }
            /**
             *
             */
            if (TYPE_STRING.equals(parameter.getParameterizedType().getTypeName())  || TYPE_LONG.equals(parameter.getParameterizedType().getTypeName())) {
                checkValue(value,annotation);// 校验参数值
            }else {
                checkObjValue(parameter,value);// 校验对象参数值
            }
        }

    }


    /**
     *
     */
    private void checkLogin(boolean checkAdmin){
        ServletRequestAttributes requestAttributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpSession session = request.getSession();
        SessionWebUserDto sessionWebUserDto =(SessionWebUserDto) session.getAttribute(Constants.SESSION_KEY);

        if (sessionWebUserDto == null){
            //没有登录
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }
        if (checkAdmin || !sessionWebUserDto.isAdmin()) {
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }
    }


    /**
     *
     * @param parameter 方法的参数对象，包含该参数的类型信息、注解等。
     * @param value  方法的参数值，传入的是某个具体方法参数的实际值。
     */
    private void checkObjValue(Parameter parameter, Object value) {

        try {
            String typeName = parameter.getParameterizedType().getTypeName();//参数对象 -》 获取参数对象类型（Interger.class） -》 类型的名称（java.lang.Interger）
            Class<?> aClass = Class.forName(typeName);//通过类名加载类(加载 java.lang.Interger)
            Field[] fields = aClass.getDeclaredFields();//获取类所有参数
            for (Field f:fields) {
                VerifyParam annotation = f.getAnnotation(VerifyParam.class);//参数的注解
                if (annotation == null){
                    continue;
                }
                f.setAccessible(true);//更改访问权限
                Object o = f.get(value); //获取字段值

                checkValue(o,annotation);//有注解 校验
            }

        }catch (BusinessException e){
            logger.error("校验参数失败",e);
            throw e;
        }catch (Exception e){
            logger.error("校验参数失败",e);
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

    }

    /**
     *
     * @param value  方法的参数值，传入的是某个具体方法参数的实际值。
     * @param annotation 注解
     */
    private void checkValue(Object value, VerifyParam annotation) {
        Boolean isEmpty = value == null || StringTools.isEmpty(value.toString());
        Integer length = value == null ? 0 :value.toString().length();

        if (isEmpty && annotation.required()){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (!isEmpty && (annotation.max() != -1 && annotation.max() < length || annotation.min() !=-1 && annotation.min() > length)){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (!isEmpty && !StringTools.isEmpty(annotation.regex().getRegex()) && !VerifyUtils.verify(annotation.regex(),String.valueOf(value))){
            throw new BusinessException(ResponseCodeEnum.CODE_600);

        }

    }

//    @Before("@annotation(com.example.easypan.Annotation.GlobalInterceptor)")
//    public void interceptorDo(){
//
//    }























}
