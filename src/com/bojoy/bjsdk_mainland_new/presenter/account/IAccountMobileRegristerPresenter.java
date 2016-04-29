package com.bojoy.bjsdk_mainland_new.presenter.account;

import android.content.Context;

/**
 * Created by wutao on 2015/12/21.
 * 一键注册 视图控制器 接口
 */
public interface IAccountMobileRegristerPresenter {


    /**
     * 输入手机号获取验证码
     * @param context
     * @param mobile
     */
    void sendPhoneCode(Context context, String mobile);


    /**
     * 输入验证码注册
     * @param context
     * @param mobile
     * @param verifyCode
     * @param pwd
     * @param status 手机号注册状态 1：验证验证码    2：手机号注册（含密码）
     */
    void checkVerifyCode(Context context, String mobile, String verifyCode, String pwd, int status);
}
