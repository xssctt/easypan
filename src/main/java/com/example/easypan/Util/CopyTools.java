package com.example.easypan.Util;

import cn.hutool.core.bean.BeanUtil;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class CopyTools {

    public static <T,S> List<T> copyList(List<S> sList,Class<T> classz){
        List<T> list=new ArrayList<>();
        for (S s: sList) {
            T t=null;
            try {
                t = classz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            BeanUtils.copyProperties(s,t);
            list.add(t);
        }
        return list;


    }

    public static <T,S> T copy(S s,Class<T> classz){

        T t=null;

        try {
            t = classz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        BeanUtils.copyProperties(s,t);
        return t;
    }

}
