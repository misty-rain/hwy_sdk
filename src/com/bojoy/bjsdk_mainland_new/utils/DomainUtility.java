package com.bojoy.bjsdk_mainland_new.utils;

import android.content.Context;

import com.bojoy.bjsdk_mainland_new.api.BaseApi;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.support.http.HttpUtility;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wutao on 2015/12/28. 存储APP CDN 域名 和 URL 信息
 */
public class DomainUtility {

    private final String TAG = DomainUtility.class.getSimpleName();
    String domainJsonStr;
    private String netHead = "http://";

    private static DomainUtility domainUtility;

    private DomainUtility() {
    }

    public static DomainUtility getInstance() {
        if (domainUtility == null) {
            synchronized (HttpUtility.class) {
                if (domainUtility == null) {
                    domainUtility = new DomainUtility();
                }
            }
        }
        return domainUtility;
    }

    /**
     * 读取配置文件 json 对象
     *
     * @param context
     * @return
     */
    private JSONObject getDomainJsonStr(Context context) {
        JSONObject jsonObject = null;
        domainJsonStr = SpUtil.getStringValue(context, SysConstant.CDN_JSON_FILE_NAME, "");
        if (!domainJsonStr.equals("")) {
            try {
                jsonObject = new JSONObject(domainJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
                LogProxy.e(TAG, e.getMessage());
            }
        }
        return jsonObject;
    }

    /**
     * 读取基本API domain 主要用在
     *
     * @param context
     * @return
     */
    public String getServiceSDKDomain(Context context) {
        JSONObject jsonObject = getDomainJsonStr(context);
        if (jsonObject != null) {
            try {
                return netHead + jsonObject.getString("service_sdk_domain");
            } catch (JSONException e) {
                e.printStackTrace();
                LogProxy.e(TAG, e.getMessage());
            }
        }
        return "";
    }

    /**
     * 读取IM API domain 主要用在 未读消息
     *
     * @param context
     * @return
     */
    public String getServiceIMDomain(Context context) {

        JSONObject jsonObject = getDomainJsonStr(context);
        if (jsonObject != null) {
            try {
                return netHead + jsonObject.getString("service_im_domain");
            } catch (JSONException e) {
                e.printStackTrace();
                LogProxy.e(TAG, e.getMessage());
            }
        }
        return "";
    }

    /**
     * 读取客服 API domain 主要用在
     *
     * @param context
     * @return
     */
    public String getCustomeServiceDomain(Context context) {

       /* JSONObject jsonObject = getDomainJsonStr(context);
        if (jsonObject != null) {
            try {
                //return netHead + jsonObject.getString("service_custom_domain");\

            } catch (JSONException e) {
                e.printStackTrace();
                LogProxy.e(TAG, e.getMessage());
            }
        }*/
        return BaseApi.APP_CS_DOMAIN;
    }

    /**
     * 主要用在 读取用户中心相关 API domain
     *
     * @param context
     * @return
     */
    public String getUserCenterDomain(Context context) {

        JSONObject jsonObject = getDomainJsonStr(context);
        if (jsonObject != null) {
            try {
                return netHead + jsonObject.getString("service_ucenter_domain");
            } catch (JSONException e) {
                e.printStackTrace();
                LogProxy.e(TAG, e.getMessage());
            }
        }
        return "";
    }

    /**
     * 主要用在 读取上传请求相关 API domain
     *
     * @param context
     * @return
     */
    public String getUploadDomain(Context context) {

        JSONObject jsonObject = getDomainJsonStr(context);
        if (jsonObject != null) {
            try {
                return netHead + jsonObject.getString("service_upload_domain");
            } catch (JSONException e) {
                e.printStackTrace();
                LogProxy.e(TAG, e.getMessage());
            }
        }
        return "";
    }

    /**
     * 主要用在 读取图片存储请求 API domain
     *
     * @param context
     * @return
     */
    public String getImageResDomain(Context context) {

        JSONObject jsonObject = getDomainJsonStr(context);
        if (jsonObject != null) {
            try {
                return netHead + jsonObject.getString("s_cdn_domain");
            } catch (JSONException e) {
                e.printStackTrace();
                LogProxy.e(TAG, e.getMessage());
            }
        }
        return "";
    }

    /**
     * 主要用在 读取FAQ问题请求 API domain
     *
     * @param context
     * @return
     */
    public String getFAQDomain(Context context) {

        JSONObject jsonObject = getDomainJsonStr(context);
        if (jsonObject != null) {
            try {
                return jsonObject.getString("faq_url");
            } catch (JSONException e) {
                e.printStackTrace();
                LogProxy.e(TAG, e.getMessage());
            }
        }
        return "";
    }

    /**
     * 主要用在 读取FAQ 版本号
     *
     * @param context
     * @return
     */
    public String getFAQVersion(Context context) {

        JSONObject jsonObject = getDomainJsonStr(context);
        if (jsonObject != null) {
            try {
                return jsonObject.getString("faq_ver");
            } catch (JSONException e) {
                e.printStackTrace();
                LogProxy.e(TAG, e.getMessage());
            }
        }
        return "";
    }
    
    /**
     * 获取短信充值notice的参数值
     * 
     * @param context
     * @return
     */
    public String getSmsNotice(Context context){
    	JSONObject jsonObject = getDomainJsonStr(context);
    	if (jsonObject != null) {
            try {
                return jsonObject.getString("sms_notice");
            } catch (JSONException e) {
                e.printStackTrace();
                LogProxy.e(TAG, e.getMessage());
            }
        }
        return "";
    }

    /**
     * 获取意见注册、登录notice的参数值
     *
     * @param context
     * @return
     */
    public String getOneKeyNotice(Context context){
        JSONObject jsonObject = getDomainJsonStr(context);
        if (jsonObject != null) {
            try {
                return jsonObject.getString("onekey_notice");
            } catch (JSONException e) {
                e.printStackTrace();
                LogProxy.e(TAG, e.getMessage());
            }
        }
        return "";
    }
    
    /**
     * 获取短信通道channel
     * @param context
     * @return
     */
    public String getSmsChannel(Context context){
    	JSONObject jsonObject = getDomainJsonStr(context);
    	if (jsonObject != null) {
            try {
                return jsonObject.getString("sms_channel");
            } catch (JSONException e) {
                e.printStackTrace();
                LogProxy.e(TAG, e.getMessage());
            }
        }
        return "";
    }
    /**
     * 主要用在 读取好玩友应用的包名
     *
     * @param context
     * @return
     */
    public String getHwyPackageName(Context context) {

        JSONObject jsonObject = getDomainJsonStr(context);
        if (jsonObject != null) {
            try {
                return jsonObject.getString("platform_check_name");
            } catch (JSONException e) {
                e.printStackTrace();
                LogProxy.e(TAG, e.getMessage());
            }
        }
        return "";
    }

    /**
     * 主要用在 读取好玩友应用下载的地址url
     *
     * @param context
     * @return
     */
    public String getHwyAppDownloadUrl(Context context) {

        JSONObject jsonObject = getDomainJsonStr(context);
        if (jsonObject != null) {
            try {
                return jsonObject.getString("platform_download_url");
            } catch (JSONException e) {
                e.printStackTrace();
                LogProxy.e(TAG, e.getMessage());
            }
        }
        return "";
    }

    /**
     * 获取充值卡URL
     *
     * @param context
     * @return
     */
    public String getRechargCardUrl(Context context) {
        JSONObject jsonObject = getDomainJsonStr(context);
        if (jsonObject != null) {
            try {
                return jsonObject.getString("recharge_card_des_url");
            } catch (JSONException e) {
                e.printStackTrace();
                LogProxy.e(TAG, e.getMessage());
            }
        }
        return "";
    }

    /**
     * 获取充值卡版本号
     *
     * @return
     */
    public String getRechargCardVersion(Context context) {
        JSONObject jsonObject = getDomainJsonStr(context);
        if (jsonObject != null) {
            try {
                return jsonObject.getString("recharge_card_des_ver");
            } catch (JSONException e) {
                e.printStackTrace();
                LogProxy.e(TAG, e.getMessage());
            }
        }
        return "";
    }

    /**
     * 获取充值卡版本号
     *
     * @return
     */
    public String getRealConfirm(Context context) {
        JSONObject jsonObject = getDomainJsonStr(context);
        if (jsonObject != null) {
            try {
                return jsonObject.getString("real_confirm");
            } catch (JSONException e) {
                e.printStackTrace();
                LogProxy.e(TAG, e.getMessage());
            }
        }
        return "";
    }

}
