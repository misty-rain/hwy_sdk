package com.bojoy.bjsdk_mainland_new.presenter.init;

import android.content.Context;

/**
 * Created by wutao on 2015/12/21.
 * 初始化SDK 视图控制器 接口
 */
public interface IInitPresenter {

    /**
     * 初始化SDK
     *
     * @param context    山下文变量
     */
    void initSDK(Context context);

    /**
     * 检测APP
     *
     * @param context    上下文环境
     * @paran appKey 应用秘钥
     */
    void appCheck(Context context);

    /**
     * 获取应用基本配置
     * @param context
     */
    void  appCollocation(Context context);

    /**
     * 获取离线消息
     * @param context
     */
    void getOfflineMsg(Context context);
}
