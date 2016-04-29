package com.bojoy.bjsdk_mainland_new.presenter.account;

import android.content.Context;

/**
 * Created by wutao on 2015/12/24.
 * 账户操作 视图控制器 接口
 */
public interface IAccountPresenter {
    /**
     * 用户登录
     *
     * @param context  上下文
     * @param userName 用户名
     * @param passWord 密码
     */
    void login(Context context, String userName, String passWord);

    /**
     * 游戏试玩
     * @param context 上下文
     */
    void tryPlay(Context context);

    /**
     * 试玩转正
     * @param context
     * @param newPP
     * @param passWord
     */
    void tryChange(Context context, String newPP, String passWord);

    /**
     * 平台注册
     * @param context  上下文
     * @param userName 用户名
     * @param passWord 密码
     * @param email    邮箱
     */
    void platformRegister(Context context, String userName, String passWord, String email);

    /**
     * 自动登陆
     * @param context  上下文
     */
    void autoLogin(Context context);

    /**
     * 加载本地账户列表
     * @param context 上下文
     */
    void loadAccountList(Context context);

    /**
     * 一键注册
     *
     * @param context  上下文
     * @param verifyCode 校验码
     */
    void oneKeyRegister(Context context, String verifyCode);

    /**
     * 获取我方短信平台手机号
     * @param context
     */
    void sendInfo(Context context);

}
