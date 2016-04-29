package com.bojoy.bjsdk_mainland_new.presenter.account;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by wutao on 2016/1/11.
 * 账户中心视图控制器 接口
 */
public interface IAccountCenterPresenter {
    /**
     * 获得账户信息
     * @param context 上下文
     *
     */
    void getAccountInfo(Context context);

    /**
     * 获的账户会员信息
     * @param context
     */
    void getAccountVipInfo(Context context);

    /**
     * 获得自己个人信息
     * @param context
     */
    void getUserInfoForSelf(Context context);

    /**
     * 修改个人信息
     * @param context 上下文
     * @param nickName 昵称
     * @param birth 生日
     */
    void editUserInfo(Context context,String nickName,String birth);

    /**
     * 上传个人头像
     * @param context 上下文
     * @param imageView 显示头像view
     * @param filePath 文件路径
     */
    void uploadUserFace(Context context, ImageView imageView,String filePath);

    /**
     * 修改密码
     * @param context 上下文
     * @param oldPwd 老密码
     * @param newPwd 新密码
     */
    void modifyPassword(Context context,String oldPwd,String newPwd);

    /**
     * 好玩友平台注册  ，区别于SDK 注册
     * @param context 上下文
     * @param nickName  昵称
     * @param sex 性别 1：男，0：女
     * @param birth 生日
     */
    void hwyPlatformRegister(Context context,String nickName,String sex,String birth);

    /**
     * 获取绑定 手机 验证码
     * @param context
     * @param phoneNum
     */
    void getSmsCodeByBindPhone(Context context,String phoneNum);

    /**
     * 绑定手机
     * @param context 上下文
     * @param phoneNum 手机号
     * @param verifyCode 验证码
     */
    void bindPhone(Context context,String phoneNum,String verifyCode);

    /**
     * 绑定邮箱
     * @param context 上下文
     * @param email 邮箱
     */
    void bindEmail(Context context,String  email);

    /**
     * 退出
     * @param context  上下文
     */
    void logout(Context context);

}

