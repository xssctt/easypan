package com.example.easypan.Util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class JsonUtil {
    private static final Logger logger= LoggerFactory.getLogger(JsonUtil.class);
    public static String converTJson2Obj(Object obj){return JSON.toJSONString(obj); }

    public static <T> T converTJson2Obj(String json,Class<T> classz) {return JSONObject.parseObject(json,classz); }

    public static <T> List<T> converTJsonArray2List(String json, Class<T> classz) {return JSONArray.parseArray(json,classz);
    }



    public static void main(String[] args) {

    }

}
