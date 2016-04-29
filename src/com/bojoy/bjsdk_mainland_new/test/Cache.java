package com.bojoy.bjsdk_mainland_new.test;

import java.util.Map;

/**
 * Created by wutao on 2015/12/10.
 */
public interface Cache {
    String getCache();

    int getName(String key);

    long getCacheSize(String key);

    boolean isCache(String key);

    Map<String,String> getParams(String key);





}
