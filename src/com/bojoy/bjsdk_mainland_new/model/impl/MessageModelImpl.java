package com.bojoy.bjsdk_mainland_new.model.impl;

import android.content.Context;

import com.bojoy.bjsdk_mainland_new.api.BaseApi;
import com.bojoy.bjsdk_mainland_new.app.tools.ParamsTools;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseRequestEvent;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseResultCallbackListener;
import com.bojoy.bjsdk_mainland_new.model.IMessageModel;
import com.bojoy.bjsdk_mainland_new.support.http.HttpUtility;
import com.bojoy.bjsdk_mainland_new.support.http.callback.StringCallback;
import com.bojoy.bjsdk_mainland_new.support.http.config.HttpMethod;
import com.bojoy.bjsdk_mainland_new.utils.DomainUtility;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by wutao on 2016/1/22.
 * 消息相关业务实现
 */
public class MessageModelImpl implements IMessageModel {

    private final String TAG = MessageModelImpl.class.getSimpleName();

    /**
     * 获得未读消息
     *
     * @param context          山下文
     * @param callbackListener 回调事件 将结果通知 给presenter
     */
    @Override
    public void getUnReadMessageList(Context context, final BaseResultCallbackListener callbackListener) {

        Map<String, String> params = ParamsTools.getInstance().getUserCenterParamsMap(context);
      /*  String code = TransferDesEncrypt.encryptDef(String.valueOf(SysConstant.USERCENTER_HEADER_REQUEST_CODE_UNREAD_MESSAGE) + "|" + System.currentTimeMillis());
        String c = null;
        try {
            c = URLEncoder.encode(code, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        params.put("stepMd5", "1");
        params.put("stepWeb", "1");
        params.put("c", String.valueOf(SysConstant.USERCENTER_HEADER_REQUEST_CODE_UNREAD_MESSAGE) + "|" + System.currentTimeMillis());

        try {
            HttpUtility.getInstance().execute(HttpMethod.POST, DomainUtility.getInstance().getServiceIMDomain(context) + BaseApi.APP_IM_REQUEST, params, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    callbackListener.onError(call, e, BaseRequestEvent.Request_Message_Notify);
                }

                @Override
                public void onResponse(String response) {
                    callbackListener.onSuccess(response, BaseRequestEvent.Request_Message_Notify);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogProxy.e(TAG, e.getMessage());
        }


    }
}
