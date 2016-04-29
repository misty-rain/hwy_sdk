package com.bojoy.bjsdk_mainland_new.ui.view.account;

import com.bojoy.bjsdk_mainland_new.ui.view.IBaseView;

import java.util.Map;

/**
 * Created by wutao on 2016/1/11.
 * 用户信息视图接口
 */
public interface  IUserInfoView extends IBaseView {
    /**
     * 显示用户信息
     * @param
     */
    void showUserInfo();

    /**
     * 显示编辑用户信息成功
     */
    void showEditUserInfoSuccess();

    /**
     * 显示注册好玩友成功
     */
    void showRegisterHwySuccess();

    /**
     * 显示上传头像成功
     */
    void showUploadHeadIconSuccess();

}
