package com.bojoy.bjsdk_mainland_new.presenter.account.findpwd;

import android.content.Context;

/**
 * 
 * 密码找回-验证验证码
 * @author zhouhonghai @time 2016/1/12
 *
 */
public interface ICheckVerifyCodePresenter {
    
    /**
     * 验证验证码
     * @param context
     * @param VerifyCode
     */
    void checkVerifyCode(Context context, String mobileKey, String VerifyCode);
    
    
}
