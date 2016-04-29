package com.bojoy.bjsdk_mainland_new.presenter.message;

import android.content.Context;

/**
 * Created by wutao on 2016/1/22.
 * 消息操作相关 视图控制器 接口
 */
public interface IMessagePresenter {
    /**
     * 获取未读消息列表
     * @param context 上下文
     */
    void loadUnReadMsgList(Context context);

    /**
     * 根据uid 获得用户信息
     * @param context 上下文
     * @param uid 用户id
     */
    void getOtherUserInfo(Context context,String uid);

}
