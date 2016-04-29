package com.bojoy.bjsdk_mainland_new.utils;

import com.bojoy.bjsdk_mainland_new.app.tools.BJMGFSDKTools;

import java.util.Comparator;

/**
 * Created by wutao on 2016/1/8.
 * 比较器 工具类，目前主要用于 账户列表的排序
 */
public class CompartorUtil implements Comparator{

    private static CompartorUtil compartorUtil;


    private CompartorUtil() {
    }

    public static CompartorUtil getInstance() {
        if (compartorUtil == null) {
            synchronized (CompartorUtil.class) {
                if (compartorUtil == null) {
                    compartorUtil = new CompartorUtil();

                }
            }
        }
        return compartorUtil;
    }

    @Override
    public int compare(Object o, Object t1) {
       String str1= (String) o;
       String str2= (String) t1;
        String [] params1=str1.split(",");
        String [] params2=str2.split(",");
        return params1[params1.length-1].compareTo(params2[params2.length-1]);
    }


}
