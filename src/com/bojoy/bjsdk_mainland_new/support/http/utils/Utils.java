package com.bojoy.bjsdk_mainland_new.support.http.utils;

import java.util.Map;

/**
 * Created by wutao on 2016/2/2.
 */
public class Utils {

    /**
     * 将请求的结果 ，组装成url 串
     * @param url
     * @param params
     * @return
     */
    public static String parseParamsStr(String url,Map<String,String> params){
        StringBuilder stringBuilder = new StringBuilder(url + "?");
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                stringBuilder.append(key + "=" + params.get(key) + "&");

            }
            stringBuilder = stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }
}
