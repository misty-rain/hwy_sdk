package com.bojoy.bjsdk_mainland_new.model;

import android.content.Context;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseResultCallbackListener;

/**
 * Created by wutao on 2015/12/21.
 * 初始化 SDK 模型 接口
 */
public interface IInitSDKModel {
    /**
     * 初始化SDK
     * @param context 山下文变量
     * @param listener  回调事件，将结果通知给presenter
     */
    void initSDK(Context context,final BaseResultCallbackListener listener);

    /**
     *  检测APP
     * @param context 上下文环境
     * @param listener 回调事件，将结果通知给presenter
     */
    void appCheck(Context context,final BaseResultCallbackListener listener);

    /**
     * 获取App 基本配置信息
     * @param context 上下文环境
     * @param listener 回调事件
     */
    void getAppCollocation(Context context,final  BaseResultCallbackListener listener);

    /**
     * 获取离线消息
     * @param context
     */
    void getOfflineMsg(Context context, final BaseResultCallbackListener listener);
}
