package com.bojoy.bjsdk_mainland_new.presenter.account.findpwd;

import android.content.Context;

/**
 * 
 * 密码找回-修改密码
 * @author zhouhonghai @time 2016/1/12
 *
 */
public interface IFindPswResetPresenter {
    
    /**
     * 修改密码
     * @param context
     * @param VerifyCode
     */
    void findPswReset(Context context, String mobileKey, String VerifyCode, String psw);
}
