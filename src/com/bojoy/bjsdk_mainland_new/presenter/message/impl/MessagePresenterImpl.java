package com.bojoy.bjsdk_mainland_new.presenter.message.impl;

import android.content.Context;

import com.bojoy.bjsdk_mainland_new.app.tools.MessageNoticeTools;
import com.bojoy.bjsdk_mainland_new.congfig.ErrorCodeConstants;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseRequestEvent;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseResultCallbackListener;
import com.bojoy.bjsdk_mainland_new.model.IAccountModel;
import com.bojoy.bjsdk_mainland_new.model.IMessageModel;
import com.bojoy.bjsdk_mainland_new.model.entity.BackResultBean;
import com.bojoy.bjsdk_mainland_new.model.entity.MessageNotifyData;
import com.bojoy.bjsdk_mainland_new.model.entity.UserData;
import com.bojoy.bjsdk_mainland_new.model.impl.AccountModelImpl;
import com.bojoy.bjsdk_mainland_new.model.impl.MessageModelImpl;
import com.bojoy.bjsdk_mainland_new.presenter.message.IMessagePresenter;
import com.bojoy.bjsdk_mainland_new.support.fastjson.JSON;
import com.bojoy.bjsdk_mainland_new.ui.view.IBaseView;
import com.bojoy.bjsdk_mainland_new.ui.view.message.IMessageView;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by wutao on 2016/1/22.
 * 消息业务 控制器实现
 */
public class MessagePresenterImpl implements IMessagePresenter, BaseResultCallbackListener {

    private final String TAG = MessagePresenterImpl.class.getSimpleName();
    private IMessageModel iMessageModel;
    private IAccountModel iAccountModel;
    private IBaseView iBaseView;
    Context context;
    //消息集合
    ArrayList<MessageNotifyData> list = new ArrayList<MessageNotifyData>();

    public MessagePresenterImpl(Context context, IBaseView iBaseView) {
        iMessageModel = new MessageModelImpl();
        iAccountModel = new AccountModelImpl();
        this.iBaseView = iBaseView;
        this.context = context;
    }

    @Override
    public void onSuccess(Object response, int requestSessionEvent) {
        try {
            BackResultBean backResultBean = JSON.parseObject((String) response, BackResultBean.class);
            if (backResultBean.getCode() == ErrorCodeConstants.ERROR_CODE_SUCCESS) {
                Map<String, String> paramsMap = null;
                switch (requestSessionEvent) {
                    case BaseRequestEvent.REQUEST_MESSAGE_NOTIFY://获取未读消息
                        list = MessageNoticeTools.getInstance().getUnReadMessageList(backResultBean.getObj());
                        if (list.size() > 0) {
                            //根据消息list得到所有用户信息 此处主要用的 头像
                            getOtherUserInfo(context, MessageNoticeTools.getInstance().getUid(list));
                        } else {
                            ((IMessageView) iBaseView).showNoResultView();
                        }
                        break;
                    case BaseRequestEvent.REQUEST_PF_OTHER_USERS_INFO://根据id 获得 用户 信息
                        Map<Long, UserData> map = MessageNoticeTools.getInstance().parseOtherUsersData(context, backResultBean.getObj());
                        MessageNoticeTools.getInstance().setCurrMsgUserMap(map);
                        ((IMessageView) iBaseView).showMsgListView(list);
                        break;
                }
            } else {
                iBaseView.showError(backResultBean.getMsg());
            }

        } catch (Exception e) {
            LogProxy.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Call call, Exception exception, int requestSessionEvent) {
        iBaseView.showError(exception.getMessage());
    }

    /**
     * 获取未读消息列表
     *
     * @param context 上下文
     */
    @Override
    public void loadUnReadMsgList(Context context) {
        iMessageModel.getUnReadMessageList(context, this);
    }

    /**
     * 获得用户信息
     *
     * @param context 上下文
     * @param uid     用户id
     */
    @Override
    public void getOtherUserInfo(Context context, String uid) {
        iAccountModel.getOtherUserInfo(context, uid, SysConstant.GET_USERINFO_TYPE_BASE, this);

    }
}
