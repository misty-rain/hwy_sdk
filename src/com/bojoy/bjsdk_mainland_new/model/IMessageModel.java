package com.bojoy.bjsdk_mainland_new.model;

import android.content.Context;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseResultCallbackListener;

/**
 * Created by wutao on 2016/1/22.
 * 消息相关业务接口，各类消息的拉取、发送等
 */
public interface IMessageModel {

    /**
     *  获得未读消息
     * @param context 山下文
     * @param callbackListener 回调事件 将结果通知 给presenter
     */
    void getUnReadMessageList(Context context, final BaseResultCallbackListener callbackListener);
}
