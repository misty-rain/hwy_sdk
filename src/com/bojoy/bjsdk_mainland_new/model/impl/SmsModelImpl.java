package com.bojoy.bjsdk_mainland_new.model.impl;

import android.content.Context;

import com.bojoy.bjsdk_mainland_new.api.BaseApi;
import com.bojoy.bjsdk_mainland_new.app.tools.ParamsTools;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseRequestEvent;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseResultCallbackListener;
import com.bojoy.bjsdk_mainland_new.model.ISmsModel;
import com.bojoy.bjsdk_mainland_new.support.http.HttpUtility;
import com.bojoy.bjsdk_mainland_new.support.http.callback.StringCallback;
import com.bojoy.bjsdk_mainland_new.support.http.config.HttpMethod;
import com.bojoy.bjsdk_mainland_new.utils.DomainUtility;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by wutao on 2016/1/15.
 * 短信类 相关业务 数据请求类
 */
public class SmsModelImpl implements ISmsModel {

        private final String TAG = SmsModelImpl.class.getSimpleName();

        /**
         * 获取绑定手机验证码
         *
         * @param context  上下文
         * @param phoneNum 需要绑定的手机号
         * @param listener 回调事件 将结果通知 给presenter
         */
    @Override
    public void getBindPhoneCode(Context context, String phoneNum, final BaseResultCallbackListener listener) {
        Map<String, String> params = ParamsTools.getInstance().getBaseParamsMap(context, false);
        params.put("mobile", phoneNum);
        try {
            HttpUtility.getInstance().execute(HttpMethod.POST, DomainUtility.getInstance().getServiceSDKDomain(context) + BaseApi.APP_GET_SMSCODE_BY_BIND_PHONE, params, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    listener.onError(call, e, BaseRequestEvent.Request_BindPhone_Check_Code);

                }

                @Override
                public void onResponse(String response) {
                    listener.onSuccess(response, BaseRequestEvent.Request_BindPhone_Check_Code);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogProxy.e(TAG, e.getMessage());
        }


    }
}
