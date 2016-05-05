package com.bojoy.bjsdk_mainland_new.model.impl;

import android.content.Context;
import android.util.Log;

import com.bojoy.bjsdk_mainland_new.api.BaseApi;
import com.bojoy.bjsdk_mainland_new.app.tools.BJMGFSDKTools;
import com.bojoy.bjsdk_mainland_new.app.tools.ParamsTools;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseRequestEvent;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseResultCallbackListener;
import com.bojoy.bjsdk_mainland_new.model.IInitSDKModel;
import com.bojoy.bjsdk_mainland_new.model.entity.BaseAppPassport;
import com.bojoy.bjsdk_mainland_new.support.http.HttpUtility;
import com.bojoy.bjsdk_mainland_new.support.http.callback.FileCallback;
import com.bojoy.bjsdk_mainland_new.support.http.callback.StringCallback;
import com.bojoy.bjsdk_mainland_new.support.http.config.HttpMethod;
import com.bojoy.bjsdk_mainland_new.utils.DomainUtility;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by wutao on 2015/12/21.
 * 初始化SDK 模型 实现
 */
public class InitSDKModelImpl implements IInitSDKModel {

    final String time = String.valueOf(System.currentTimeMillis());
    private final String TAG = InitSDKModelImpl.class.getSimpleName();


    /**
     * 初始化SDK
     *
     * @param context  山下文变量
     * @param listener 回调事件，将结果通知给presenter
     */
    @Override
    public void initSDK(Context context, final BaseResultCallbackListener listener) {
        Map<String, String> params = ParamsTools.getInstance().getInitSDKParamsMap(context);
        try {
            HttpUtility.getInstance().execute(HttpMethod.POST, DomainUtility.getInstance().getServiceSDKDomain(context) + BaseApi.INIT, params, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    listener.onError(call, e, BaseRequestEvent.REQUEST_INIT); //将结果通过回调给Presenter
                }

                @Override
                public void onResponse(String response) {
                    listener.onSuccess(response, BaseRequestEvent.REQUEST_INIT);
                } //将结果通过回调给Presenter
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogProxy.e(TAG, e.getMessage());
        }

    }

    /**
     * 检测APP
     *
     * @param context  上下文环境
     * @param listener 回调事件，将结果通知给presenter
     * @paran appKey 应用秘钥
     */
    @Override
    public void appCheck(Context context, final BaseResultCallbackListener listener) {
        Map<String, String> params = ParamsTools.getInstance().getInitSDKParamsMap(context);
        params.put("channel", BaseAppPassport.getChannel());
        try {
            HttpUtility.getInstance().execute(HttpMethod.POST, DomainUtility.getInstance().getServiceSDKDomain(context) + BaseApi.APPCHECK, params, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    listener.onError(call, e, BaseRequestEvent.REQUEST_APP_CHECK_UPDATE);

                }

                @Override
                public void onResponse(String response) {
                    listener.onSuccess(response, BaseRequestEvent.REQUEST_APP_CHECK_UPDATE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogProxy.e(TAG, e.getMessage());
        }

    }


    /**
     * 获得应用基本信息
     *
     * @param context  上下文环境
     * @param listener 回调事件
     */
    @Override
    public void getAppCollocation(Context context, final BaseResultCallbackListener listener) {
        try {
            HttpUtility.getInstance().executeDownloadFile(BaseApi.APP_COLLOCATION_INFO, new FileCallback(context.getFilesDir().toString(), SysConstant.CDN_JSON_FILE_NAME) {
                @Override
                public void inProgress(float progress) {

                }

                @Override
                public void onError(Call call, Exception e) {
                    listener.onError(call, e, BaseRequestEvent.REQUEST_FIRST_DEPLOY);
                }

                @Override
                public void onResponse(File response) {
                    String content = "";
                    try {
                        InputStream instream = new FileInputStream(response);
                        if (instream != null) {
                            InputStreamReader inputreader = new InputStreamReader(instream);
                            BufferedReader buffreader = new BufferedReader(inputreader);
                            String line;
                            //分行读取
                            while ((line = buffreader.readLine()) != null) {
                                content += line + "\n";
                            }
                            instream.close();
                        }
                    } catch (java.io.FileNotFoundException e) {
                        Log.d(TAG, "The File doesn't not exist.");
                    } catch (IOException e) {
                        Log.d(TAG, e.getMessage());
                    }
                    LogProxy.e(TAG, content);
                    listener.onSuccess(content, BaseRequestEvent.REQUEST_FIRST_DEPLOY);
                }
            });
        } catch (Exception e) {
            LogProxy.d(TAG, e.getMessage());
        }
    }

    @Override
    public void getOfflineMsg(Context context, final BaseResultCallbackListener listener) {
        Map<String, String> params = ParamsTools.getInstance().getUserCenterParamsMap(context);
        String t = "0";
        if (BJMGFSDKTools.getInstance().getOfflineTime().trim().length() > 0) {
            t = BJMGFSDKTools.getInstance().getOfflineTime();
        }
        params.put("c", SysConstant.USERCENTER_HEADER_REQUEST_CODE_GET_OFFLINE_MSG + "|" + "1" + "|" + t);
        try {
            HttpUtility.getInstance().execute(HttpMethod.POST, DomainUtility.getInstance().getServiceIMDomain(context) + BaseApi.APP_OFFLINE_MSG, params, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    listener.onError(call, e, BaseRequestEvent.REQUEST_MESSAGE_OFFLINE);
                }

                @Override
                public void onResponse(String response) {
                    listener.onSuccess(response, BaseRequestEvent.REQUEST_MESSAGE_OFFLINE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogProxy.e(TAG, e.getMessage());
        }
    }

}
