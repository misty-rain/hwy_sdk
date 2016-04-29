package com.bojoy.bjsdk_mainland_new.ui.view.account;

import com.bojoy.bjsdk_mainland_new.model.entity.UserData;
import com.bojoy.bjsdk_mainland_new.ui.view.IBaseView;

import java.util.Map;

/**
 * Created by wutao on 2016/1/11.
 * 账户中心视图 接口
 * 视图接口，此处定义视图（activity）中的方法
 */
public interface IAccountCenterView extends IBaseView {

    /**
     * 显示账户信息
     */
    void showAccountInfo();

    /**
     * 切换账户
     */
    void switchAccount();

    /**
     * 显示用户会员信息
     * @param map
     */
    void showUserVipInfo(Map<String,String> map);

    /**
     * 显示用户信息 包括昵称、性别、头像、生日
     */
    void showUserInfo();



}
