package com.bojoy.bjsdk_mainland_new.presenter.account.findpwd.impl;

import android.content.Context;

import com.bojoy.bjsdk_mainland_new.congfig.ErrorCodeConstants;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseRequestEvent;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseResultCallbackListener;
import com.bojoy.bjsdk_mainland_new.model.IAccountModel;
import com.bojoy.bjsdk_mainland_new.model.entity.BackResultBean;
import com.bojoy.bjsdk_mainland_new.model.impl.AccountModelImpl;
import com.bojoy.bjsdk_mainland_new.presenter.account.findpwd.ICheckVerifyCodePresenter;
import com.bojoy.bjsdk_mainland_new.support.eventbus.EventBus;
import com.bojoy.bjsdk_mainland_new.support.fastjson.JSON;
import com.bojoy.bjsdk_mainland_new.ui.view.account.findpwd.ICheckVerifyCodeView;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;

import okhttp3.Call;


/**
 * 密码找回-验证验证码  控制器实现
 *
 * @author zhouhonghai @time 2016/1/12
 */
public class CheckVerifyCodePresenterImpl implements ICheckVerifyCodePresenter, BaseResultCallbackListener {

    ICheckVerifyCodeView mICheckVerifyCodeView;
    private IAccountModel mIAccountModel;
    EventBus eventBus = EventBus.getDefault();
    Context context;
    private final String TAG = CheckVerifyCodePresenterImpl.class.getSimpleName();


    public CheckVerifyCodePresenterImpl(Context context, ICheckVerifyCodeView iCheckVerifyCodeView) {
        mIAccountModel = new AccountModelImpl();
        this.mICheckVerifyCodeView = iCheckVerifyCodeView;
        this.context = context;
    }


    @Override
    public void onSuccess(Object response, int requestSessionEvent) {
        BackResultBean backResultBean = JSON.parseObject((String) response, BackResultBean.class);
        switch (requestSessionEvent) {
            case BaseRequestEvent.REQUEST_RESET_PASSWORD://通过验证码找回密码事件回调
                if (backResultBean.getCode() == ErrorCodeConstants.ERROR_CODE_SUCCESS) {
                    mICheckVerifyCodeView.checkVerifyCodeSuccess();

                } else {
                    mICheckVerifyCodeView.showError(backResultBean.getMsg());
                }
                break;
        }
    }

    @Override
    public void onError(Call call, Exception exception, int requestSessionEvent) {

    }

    @Override
    public void checkVerifyCode(Context context, String mobileKey, String VerifyCode) {
        LogProxy.i(TAG, "checkVerifyCode:" + VerifyCode);
        mIAccountModel.resetPswForFindPsw(context, mobileKey, VerifyCode, "", this);
    }


}
