package com.bojoy.bjsdk_mainland_new.presenter.account.findpwd.impl;

import android.content.Context;

import com.bojoy.bjsdk_mainland_new.congfig.ErrorCodeConstants;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseRequestEvent;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseResultCallbackListener;
import com.bojoy.bjsdk_mainland_new.model.IAccountModel;
import com.bojoy.bjsdk_mainland_new.model.entity.BackResultBean;
import com.bojoy.bjsdk_mainland_new.model.impl.AccountModelImpl;
import com.bojoy.bjsdk_mainland_new.presenter.account.findpwd.IFindPswResetPresenter;
import com.bojoy.bjsdk_mainland_new.support.eventbus.EventBus;
import com.bojoy.bjsdk_mainland_new.support.fastjson.JSON;
import com.bojoy.bjsdk_mainland_new.ui.view.IBaseView;

import okhttp3.Call;


/**
 * 密码找回-修改密码  控制器实现
 *
 * @author zhouhonghai @time 2016/1/12
 */
public class FindPswResetPresenterImpl implements IFindPswResetPresenter, BaseResultCallbackListener {

    IBaseView mIBaseView;
    private IAccountModel mIAccountModel;
    EventBus eventBus = EventBus.getDefault();
    Context context;
    private final String TAG = FindPswResetPresenterImpl.class.getSimpleName();


    public FindPswResetPresenterImpl(Context context, IBaseView iBaseView) {
        mIAccountModel = new AccountModelImpl();
        this.mIBaseView = iBaseView;
        this.context = context;
    }


    @Override
    public void onSuccess(Object response, int requestSessionEvent) {
        BackResultBean backResultBean = JSON.parseObject((String) response, BackResultBean.class);
        switch (requestSessionEvent) {
            case BaseRequestEvent.Request_Reset_Password://通过验证码找回密码事件回调
                if (backResultBean.getCode() == ErrorCodeConstants.ERROR_CODE_SUCCESS) {
                    mIBaseView.showSuccess();

                } else {
                    mIBaseView.showError(backResultBean.getMsg());
                }
                break;
        }
    }

    @Override
    public void onError(Call call, Exception exception, int requestSessionEvent) {
        mIBaseView.showError(exception.getMessage());
    }


    @Override
    public void findPswReset(Context context, String mobileKey,
                             String VerifyCode, String psw) {
        mIAccountModel.resetPswForFindPsw(context, mobileKey, VerifyCode, psw,
                  this);

    }
}
