package com.bojoy.bjsdk_mainland_new.ui.view.account.findpwd;

import com.bojoy.bjsdk_mainland_new.ui.view.IBaseView;

/**
 * Created by zhouhonghai 2016/01/07
 * 通过手机找回密码 视图 接口
 * 视图接口，此处定义视图（activity）中的方法
 */
public interface ICheckVerifyCodeView extends IBaseView {
	/**
	 * 验证验证码成功的回调
	 */
	void checkVerifyCodeSuccess();
}
