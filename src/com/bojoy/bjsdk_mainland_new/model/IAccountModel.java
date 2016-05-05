package com.bojoy.bjsdk_mainland_new.model;

import android.content.Context;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseResultCallbackListener;

import java.io.File;

/**
 * Created by wutao on 2015/12/24.
 * 账户操作相关业务接口，包括登陆、注册、手机一键注册
 */
public interface IAccountModel{
    /**
     *  用户登录
     * @param  context 上下文
     * @param userName 用户名
     * @param passWord 密码
     * @param callbackListener 回调事件
     */
    void login(Context context,String userName, String passWord, final BaseResultCallbackListener callbackListener);

    /**
     * 平台注册
     * @param  context 上下文
     * @param userName 用户名
     * @param passWord 密码
     * @param email  邮箱
     * @param callbackListener 回调事件
     */
    void platformRegister(Context context,String userName,String passWord,String email,final BaseResultCallbackListener callbackListener);

    /**
     *试玩游戏
     * @param context 上下文
     * @param tryKey 手机的唯一标识 ，先取 mac ，后取 imei
     * @param listener  回调事件 ，将结果通知 presenter
     */
    void tryLogin(Context context,String tryKey,final BaseResultCallbackListener listener);

    /**
     * 修改试玩账号
     * @param context   上下文
     * @param newPP  用户名
     * @param passWord  新密码
     * @param callbackListener  回调事件
     */
    void tryChange(Context context,String newPP, String passWord, final BaseResultCallbackListener callbackListener);

    /**
     * 一键注册and 定时检测
     * @param context 上下文
     * @param verifyCode 校验码
     * @param listener 回调事件 ，将结果通知presenter
     */
    void oneKeyRegister(Context context,String verifyCode,final BaseResultCallbackListener listener);

    /**
     * 向手机发送验证码
     * @param context 上下文
     * @param mobile  手机号码
     * @param listener  回调事件 将结果通知presenter
     */
    void sendPhoneCode(Context context,String mobile,final BaseResultCallbackListener listener);


    /**
     * 输入验证码注册
     * @param context
     * @param mobile
     * @param verifyCode
     * @param listener
     */
    void mobileRegCheck(Context context, String mobile, String verifyCode, String pwd, int status, final BaseResultCallbackListener listener);

    /**
     * 自动登陆
     * @param context 上下文
     * @param listener 回调事件  将结果通知presenter
     */
    void autoLogin(Context context,final BaseResultCallbackListener listener);
    
    /**
     * 手机找回密码
     * @param context 上下文
     * @param listener 回调事件  将结果通知presenter
     */
    void mobileFindPsw(Context context, String account, final BaseResultCallbackListener listener);
    
    /**
     * 密码找回-重置密码
     * @param context
     * @param mobileKey
     * @param verifyCode
     * @param pwd
     * @param listener
     */
	void resetPswForFindPsw(Context context, String mobileKey, String verifyCode, String pwd, final BaseResultCallbackListener listener);
	
	/**
	 * 邮箱找回密码
	 * @param context
	 * @param account
	 * @param email
	 * @param listener
	 */
	void emailFindPsw(Context context, String account, String email, final BaseResultCallbackListener listener);

    /**
     * 获得账户信息
     * @param context 上下文
     * @param listener 回调事件 将结果通知presenter
     */
    void getAccountInfo(Context context,final BaseResultCallbackListener listener);

    /**
     * 获得用户 会员信息
     * @param context 上下文
     * @param listener  回调事件 将结果通知presenter
     */
    void getUserVipInfo(Context context,final BaseResultCallbackListener listener);

    /**
     * 查询自己个人信息
     * @param context 上下文
     * @param type 更新类型1：基本信息，2：详细信息，3：全部信息；如果为3，不验证最后更新时间，最后更新时间可以为空
     * @param listener 回调事件 将结果通知presenter
     */
    void getUserInfoForSelf(Context context,int type,final BaseResultCallbackListener listener);

    /**
     * 修改自己的个人信息
     *
     * @param context 上下文
     * @param nickName 昵称
     * @param birth 生日
     * @param listener 回调事件 将结果通知presenter
     */
    void editUserInfo(Context context,String nickName,String birth ,final BaseResultCallbackListener listener);

    /**
     *  上传用户头像
     * @param context 上下文
     * @param file  照片file 流
     * @param listener 回调事件 将结果通知presenter
     */
    void uploadUserFace(Context context, File file,final BaseResultCallbackListener listener);

    /**
     * 修改密码
     * @param context 上下文
     * @param oldPwd 老密码
     * @param newPwd 新密码
     * @param listener 回调事件 将结果通知presenter
     */
    void modifyPassword(Context context,String oldPwd,String newPwd,final BaseResultCallbackListener listener);

    /**
     * 好玩友平台注册，区别于SDK 注册
     * @param context 上下文
     * @param nickName 昵称
     * @param sex 性别
     * @param birth 生日
     * @param listener 回调事件 将结果通知presenter
     */
    void hwyPlatformRegister(Context context,String nickName,String sex,String birth,final BaseResultCallbackListener listener);


    /**
     * 绑定邮箱
     * @param context 上下文
     * @param email 邮箱地址
     * @param listener 回调事件  将结果通知presenter
     */
    void bindEmail(Context context,String email,final BaseResultCallbackListener listener);

    /**
     * 绑定手机号
     * @param context 上下文
     * @param phoneNum 绑定的手机号
     * @param verifyCode 验证码
     * @param listener 回调事件 将结果通知presenter
     */
    void bindPhone(Context context,String phoneNum,String verifyCode,final BaseResultCallbackListener listener);

    /**
     * 验证解除绑定手机号 时获得验证码
     * @param context
     * @param verifyCode
     * @param listener
     */
    void validateCodeForUnBindPhone(Context context,String verifyCode,final BaseResultCallbackListener listener);

    /**
     * 获取我方短信平台手机号
     * @param context
     * @param listener
     */
    void sendInfo(Context context,final BaseResultCallbackListener listener);
    /**
     * 获得其他用户信息
     * @param context 上下文
     * @param uid 用户uid
     * @param type 信息类型 1：基本信息，2：详细信息，3：全部信息
     * @param listener 回调事件 将结果通知presenter
     */
    void getOtherUserInfo(Context context,String uid,int type,final BaseResultCallbackListener listener);

    /**
     * 退出
     * @param context 上下文
     * @param listener 回调事件 将结果通知presenter
     */
    void logout(Context context,final BaseResultCallbackListener listener);
}