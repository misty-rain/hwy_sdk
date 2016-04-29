package com.bojoy.bjsdk_mainland_new.utils;

import android.content.Context;

import com.bojoy.bjsdk_mainland_new.presenter.init.IInitPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.init.impl.InitSDKPresenterImpl;
import com.bojoy.bjsdk_mainland_new.support.eventbus.EventBus;

/**
 * 消息通知5分钟轮询查询新的离线消息
 * Created by sunhaoyang on 2016/2/3.
 */
public class MessagePollingTool implements PollingTimeoutTask.PollingListener {

    private final String TAG = MessagePollingTool.class.getSimpleName();

    private PollingTimeoutTask task;
    private EventBus eventBus = EventBus.getDefault();

    private Object block = new Object();

    private Context mContext = null;

    public MessagePollingTool() {}

    public MessagePollingTool (Context context) {
        mContext = context;
    }

    public void start() {
        // TODO: 2016/2/3 判断类型如果不是消息类型，就return 
//        if (!bjmgfData.isOpenPollMsg()) {
//            return;
//        }
        task = new PollingTimeoutTask(5 * 60 * 1000, 5000, 0, this, false);
        // for test
//		task = new PollingTimeoutTask(8000, 5000, 0, this, false);
        task.startPolling();
    }

    public void stop() {
        // TODO: 2016/2/3 判断类型如果不是消息类型，就return 
//        if (!bjmgfData.isOpenPollMsg()) {
//            return;
//        }
        if (task == null || !task.isStart()) {
            return;
        }
        task.suspendPolling();
        task = null;
    }

    @Override
    public void onExecute() {
        sendMsgOfflineRequest();
    }

    @Override
    public void onTimeout() {

    }

    private void sendMsgOfflineRequest() {
        // TODO: 2016/2/3 判断是否平台用户
//        if (bjmgfData.isPlatformUser()) {
//
//        }
        IInitPresenter initP = new InitSDKPresenterImpl(mContext, null);
        initP.getOfflineMsg(mContext);
    }
}
