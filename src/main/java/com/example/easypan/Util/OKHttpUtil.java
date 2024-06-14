package com.example.easypan.Util;

import com.example.easypan.bean.Constants;
import com.example.easypan.enums.ResponseCodeEnum;
import com.example.easypan.exception.BusinessException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OKHttpUtil {

    private static final int TIME_OUT_SECONDS=8;
    private static Logger logger= LoggerFactory.getLogger(OKHttpUtil.class);

    public static OkHttpClient.Builder getClientBuilder() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().followRedirects(false).retryOnConnectionFailure(false);
        clientBuilder.connectTimeout(TIME_OUT_SECONDS,TimeUnit.SECONDS).readTimeout(TIME_OUT_SECONDS,TimeUnit.SECONDS);
        return clientBuilder;
    }

    private static Request.Builder getRequestBuilder(Map<String,String> header){
        Request.Builder requestBuilder=new Request.Builder();

        if (null != requestBuilder){

            for (Map.Entry<String,String> entry : header.entrySet()) {
                String key = entry.getKey();
                String value;
                if (entry.getValue() == null) {
                    value = "";
                }else {
                 value=entry.getValue();
                }
                requestBuilder.addHeader(key,value);

            }

        }

        return requestBuilder;

    }



    public static String getRequest(String url) throws BusinessException{
        ResponseBody responseBody=null;

        try {

            OkHttpClient.Builder clientBuilder=getClientBuilder();
            Request.Builder requestBuilder=getRequestBuilder(null);
            OkHttpClient client = clientBuilder.build();
            Request request= requestBuilder.url(url).build();
            Response response = client.newCall(request).execute();
            responseBody = response.body();
            String responserStr = responseBody.string();
            logger.info("poserRequest请求地址：{}，返回信息：{}",url,responserStr);
            return responserStr;
        }catch (SocketTimeoutException | ConnectException e){
            logger.error("OKHTTP 请求超时,url{}",url,e);
            throw new BusinessException(ResponseCodeEnum.CODE_500);
        }catch (Exception e){
            logger.error("OKHTTP get 请求异常",e);
            return null;
        }finally {
            if (responseBody != null){
                responseBody.close();
            }
        }

    }


}






















