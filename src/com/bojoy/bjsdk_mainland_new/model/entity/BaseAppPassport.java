package com.bojoy.bjsdk_mainland_new.model.entity;

/**
 * Created by wutao on 2015/12/23.
 * 应用基本配置消息类
 * 主要存储APPId、Appkey、channel platformId、version、Secret等信息
 */
public class BaseAppPassport {
    public static final String APP_SECRET="3d^7u*U72-h+fRhjB$1y";//应用秘钥

    public static String appId; //应用Id
    public static String appKey;//应用key
    public static String channel;//应用渠道
    public static String platformId;//应用平台
    public static String version;//版本号


    public static String getPlatformId() {
        return platformId;
    }

    public static void setPlatformId(String platformId) {
        BaseAppPassport.platformId = platformId;
    }

    public static String getVersion() {
        return version;
    }

    public static void setVersion(String version) {
        BaseAppPassport.version = version;
    }

    public static String getAppId() {

        return appId;
    }

    public static void setAppId(String appId) {
        BaseAppPassport.appId = appId;
    }

    public static String getAppKey() {
        return appKey;
    }

    public static void setAppKey(String appKey) {
        BaseAppPassport.appKey = appKey;
    }

    public static String getChannel() {
        return channel;
    }

    public static void setChannel(String channel) {
        BaseAppPassport.channel = channel;
    }

}
