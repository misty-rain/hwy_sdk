package com.bojoy.bjsdk_mainland_new.ui.view.account.findpwd;

import com.bojoy.bjsdk_mainland_new.ui.view.IBaseView;

/**
 * Created by zhouhonghai 2016/01/07
 * 通过邮箱找回密码 视图 接口
 * 视图接口，此处定义视图（activity）中的方法
 */
public interface IPswFindByEmailView extends IBaseView {
	/**
	 * 请求成功的回调
	 * @param email
	 */
    void emailFindSuccess(String email);
}
