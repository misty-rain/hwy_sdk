package com.bojoy.bjsdk_mainland_new.ui.view.account.bindphone;

import com.bojoy.bjsdk_mainland_new.ui.view.IBaseView;

/**
 * Created by wutao on 2016/1/15.
 *  绑定手机号  视图接口
 */
public interface IModifyBindPhoneView extends IBaseView {

    /**
     * 换绑时验证验证码成功
     * @param message
     */
    void unbindPhoneCheckVerifyCodeSuccess(String message);
}
