package com.bojoy.bjsdk_mainland_new.model;

import android.content.Context;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseResultCallbackListener;

/**
 * Created by wutao on 2016/1/15.
 * 短信类相关业务接口 ，包括 各类短信验证码的发送、等
 */
public interface ISmsModel {
    /**
     * 获得绑定 手机号的验证码
     * @param context 上下文
     * @param phoneNum 需要绑定的手机号
     * @param listener 回调事件 将结果通知 给presenter
     */
    void getBindPhoneCode(Context context, String phoneNum, final BaseResultCallbackListener listener);
}
