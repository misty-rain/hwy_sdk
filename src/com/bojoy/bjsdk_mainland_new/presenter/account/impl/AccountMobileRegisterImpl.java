package com.bojoy.bjsdk_mainland_new.presenter.account.impl;

import android.content.Context;

import com.bojoy.bjsdk_mainland_new.congfig.ErrorCodeConstants;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseRequestEvent;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseResultCallbackListener;
import com.bojoy.bjsdk_mainland_new.model.IAccountModel;
import com.bojoy.bjsdk_mainland_new.model.entity.BackResultBean;
import com.bojoy.bjsdk_mainland_new.model.impl.AccountModelImpl;
import com.bojoy.bjsdk_mainland_new.presenter.account.IAccountMobileRegristerPresenter;
import com.bojoy.bjsdk_mainland_new.support.fastjson.JSON;
import com.bojoy.bjsdk_mainland_new.ui.view.register.IAskMobileVerifyCodeView;

import okhttp3.Call;

/**
 * Created by wutao on 2015/12/29.
 * 一键注册 控制器实现
 */
public class AccountMobileRegisterImpl implements IAccountMobileRegristerPresenter, BaseResultCallbackListener {

    private final String TAG = AccountMobileRegisterImpl.class.getSimpleName();
    private int MOBILE_REG_STATUS = 0;

    private IAccountModel iAccountModel;
    Context context;

    IAskMobileVerifyCodeView iBaseView;

    public AccountMobileRegisterImpl(Context context, IAskMobileVerifyCodeView iBaseView) {
        iAccountModel = new AccountModelImpl();
        this.iBaseView = iBaseView;
        this.context = context;
    }


    @Override
    public void onSuccess(Object response, int requestSessionEvent) {
        try {
            BackResultBean backResultBean = JSON.parseObject((String) response, BackResultBean.class);
            if(requestSessionEvent == BaseRequestEvent.REQUEST_ONE_KEY_CHECK) {
                if (backResultBean.getCode() == ErrorCodeConstants.ERROR_CODE_SUCCESS) {
                    iBaseView.showSuccess();
                }else{
                    iBaseView.showError(backResultBean.getMsg());
                }
            }else if(requestSessionEvent == BaseRequestEvent.REQUEST_SENDSMS_CODE) {
                if (backResultBean.getCode() == ErrorCodeConstants.ERROR_CODE_SUCCESS) {
                    iBaseView.showSuccess();
                }else{
                    iBaseView.showError(backResultBean.getMsg());
                }
            }else if(requestSessionEvent == BaseRequestEvent.REQUEST_MOBILE_REGISTER_CHECK){
                if (backResultBean.getCode() == ErrorCodeConstants.ERROR_CODE_SUCCESS) {
                    if(MOBILE_REG_STATUS == 1){
                        iBaseView.MobileVerifyCodeSuccess(backResultBean.getObj());
                    }else if(MOBILE_REG_STATUS == 2){
                        iBaseView.MobileVerifyCodeSuccess(backResultBean.getObj());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Call call, Exception exception, int requestSessionEvent) {

    }

    @Override
    public void sendPhoneCode(Context context, String mobile) {
        iAccountModel.sendPhoneCode(context, mobile, this);
    }

    @Override
    public void checkVerifyCode(Context context, String mobile, String verifyCode, String pwd, int status) {
        MOBILE_REG_STATUS = status;
        iAccountModel.mobileRegCheck(context, mobile, verifyCode, pwd, status, this);
    }

}
