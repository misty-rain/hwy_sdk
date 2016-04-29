package com.bojoy.bjsdk_mainland_new.presenter.account.findpwd;

import android.content.Context;

/**
 * 
 * 通过手机、邮箱等方式修改密码
 * @author zhouhonghai @time 2016/1/12
 *
 */
public interface IPswFindByEmailPresenter {

	/**
	 * 获取手机验证码
	 * @param context
	 * @param account 账户
	 * @param email
	 */
    void getEmailVerifyCode(Context context, String account, String email);
}
