package com.bojoy.bjsdk_mainland_new.presenter.account.impl;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bojoy.bjsdk_mainland_new.app.tools.BJMGFSDKTools;
import com.bojoy.bjsdk_mainland_new.congfig.ErrorCodeConstants;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BJMGFSdkEvent;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseRequestEvent;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseResultCallbackListener;
import com.bojoy.bjsdk_mainland_new.model.IAccountModel;
import com.bojoy.bjsdk_mainland_new.model.ISmsModel;
import com.bojoy.bjsdk_mainland_new.model.entity.BackResultBean;
import com.bojoy.bjsdk_mainland_new.model.entity.PassPort;
import com.bojoy.bjsdk_mainland_new.model.entity.UserData;
import com.bojoy.bjsdk_mainland_new.model.impl.AccountModelImpl;
import com.bojoy.bjsdk_mainland_new.model.impl.SmsModelImpl;
import com.bojoy.bjsdk_mainland_new.presenter.account.IAccountCenterPresenter;
import com.bojoy.bjsdk_mainland_new.support.eventbus.EventBus;
import com.bojoy.bjsdk_mainland_new.support.fastjson.JSON;
import com.bojoy.bjsdk_mainland_new.ui.view.IBaseView;
import com.bojoy.bjsdk_mainland_new.ui.view.account.IAccountCenterView;
import com.bojoy.bjsdk_mainland_new.ui.view.account.IUserInfoView;
import com.bojoy.bjsdk_mainland_new.ui.view.cs.IQuestionView;
import com.bojoy.bjsdk_mainland_new.ui.view.cs.impl.SendQuestionView;
import com.bojoy.bjsdk_mainland_new.utils.AccountUtil;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;

import java.io.File;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by wutao on 2016/1/11.
 * 账户中心 控制器实现
 */
public class AccountCenterPresenterImpl implements IAccountCenterPresenter, BaseResultCallbackListener {


    private IAccountModel iAccountModel;
    private ISmsModel iSmsModel;
    private IBaseView iBaseView;
    Context context;
    private String nickName,birth;

    private final String TAG = AccountCenterPresenterImpl.class.getSimpleName();


    public AccountCenterPresenterImpl(Context context, IBaseView iBaseView) {
        iAccountModel = new AccountModelImpl();

        this.iBaseView = iBaseView;
        this.context = context;
    }

    /**
     * 获得账户信息
     *
     * @param context 上下文
     */
    @Override
    public void getAccountInfo(Context context) {
        iAccountModel.getAccountInfo(context, this);

    }

    /**
     * 获得账户会员信息
     *
     * @param context
     */
    @Override
    public void getAccountVipInfo(Context context) {
        iAccountModel.getUserVipInfo(context, this);

    }

    /**
     * 获得自己的账户信息
     *
     * @param context
     */
    @Override
    public void getUserInfoForSelf(Context context) {
        iAccountModel.getUserInfoForSelf(context, 1, this);
    }

    /**
     * 修改个人信息
     *
     * @param context  上下文
     * @param nickName 昵称
     * @param birth    生日
     */
    @Override
    public void editUserInfo(Context context, String nickName, String birth) {
        this.nickName = nickName;
        this.birth = birth;
        iAccountModel.editUserInfo(context, nickName, birth, this);


    }

    /**
     * 上传用户头像
     *
     * @param context   上下文
     * @param imageView 显示头像view
     * @param filePath  文件路径
     */
    @Override
    public void uploadUserFace(Context context, ImageView imageView, String filePath) {
        File file = new File(filePath);
        iAccountModel.uploadUserFace(context, file, this);

    }

    @Override
    public void modifyPassword(Context context, String oldPwd, String newPwd) {
        iAccountModel.modifyPassword(context, oldPwd, newPwd, this);
    }

    /**
     * 好玩友平台注册 ，区别于SDK 注册
     *
     * @param context  上下文
     * @param nickName 昵称
     * @param sex      性别 1：男，0：女
     * @param birth    生日
     */
    @Override
    public void hwyPlatformRegister(Context context, String nickName, String sex, String birth) {
        iAccountModel.hwyPlatformRegister(context, nickName, sex.equals(ReflectResourceId.getStringId(context, Resource.string.bjmgf_sdk_sex_male)) ? SysConstant.SEX_MALE : SysConstant.SEX_FEMALE, birth, this);

    }

    /**
     * 获取绑定手机 验证码
     *
     * @param context  上下文
     * @param phoneNum 手机号
     */
    @Override
    public void getSmsCodeByBindPhone(Context context, String phoneNum) {
        iSmsModel = new SmsModelImpl();
        iSmsModel.getBindPhoneCode(context, phoneNum, this);
    }

    /**
     * 绑定手机
     *
     * @param context    上下文
     * @param phoneNum   手机号
     * @param verifyCode 验证码
     */
    @Override
    public void bindPhone(Context context, String phoneNum, String verifyCode) {
        iAccountModel.bindPhone(context, phoneNum, verifyCode, this);

    }

    /**
     * 绑定 邮箱
     *
     * @param context 上下文
     * @param email   邮箱
     */
    @Override
    public void bindEmail(Context context, String email) {
        iAccountModel.bindEmail(context, email, this);

    }

    /**
     * 登出
     *
     * @param context 上下文
     */
    @Override
    public void logout(Context context) {
        iAccountModel.logout(context, this);
    }

    @Override
    public void onSuccess(Object response, int requestSessionEvent) {

        try {
            BackResultBean backResultBean = JSON.parseObject((String) response, BackResultBean.class);
            if (backResultBean.getCode() == ErrorCodeConstants.ERROR_CODE_SUCCESS) {
                Map<String, String> paramsMap = null;
                switch (requestSessionEvent) {
                    case BaseRequestEvent.Request_VIP_Level://获得会员等级信息
                        paramsMap = JSON.parseObject(backResultBean.getObj(), Map.class);
                        if (iBaseView instanceof SendQuestionView) {
                            ((IQuestionView) iBaseView).showUserVipInfo(paramsMap);
                        } else {
                            ((IAccountCenterView) iBaseView).showUserVipInfo(paramsMap);
                        }
                        break;
                    case BaseRequestEvent.Request_PF_Modify_User_Info://修改个人信息
                        ((IUserInfoView) iBaseView).showEditUserInfoSuccess();
                        UserData userData = BJMGFSDKTools.getInstance().getCurrUserData();
                        userData.setBirth(birth);
                        userData.setNick(nickName);
                        BJMGFSDKTools.getInstance().setCurrUserData(userData);
                        break;
                    case BaseRequestEvent.Request_PF_Upload_Face_Image://上传用户头像
                        String faceUrl = BJMGFSDKTools.getInstance().getPFFaceUrl(context,Long.valueOf(BJMGFSDKTools.getInstance().getCurrentPassPort().getUid()),JSON.parseObject(backResultBean.getObj()).getString("path"));
                        Log.d(TAG,faceUrl);
                        BJMGFSDKTools.getInstance().getCurrUserData().setFaceUrl(faceUrl);
                        iBaseView.showSuccess();
                        break;
                    case BaseRequestEvent.Request_Password_Modify://修改个人密码
                        PassPort passPort = JSON.parseObject(backResultBean.getObj(), PassPort.class);
                        BJMGFSDKTools.getInstance().setCurrentPassPort(passPort);
                        AccountUtil.remove(context, passPort.getUid());
                        AccountUtil.saveAccount(context, passPort.getUid(), backResultBean.getObj().toString());
                        iBaseView.showSuccess();
                        break;
                    case BaseRequestEvent.Request_PF_Register://好玩友平台注册
                        ((IUserInfoView) iBaseView).showRegisterHwySuccess();
                        break;
                    case BaseRequestEvent.Request_BindPhone_Check_Code:// 获取绑定手机验证码
                        iBaseView.showSuccess();
                        break;
                    case BaseRequestEvent.Request_BindPhone://绑定手机
                        iBaseView.showSuccess();
                        break;
                    case BaseRequestEvent.Request_BindEmail://绑定邮箱
                        iBaseView.showSuccess();
                        break;
                    case BaseRequestEvent.Request_Logout://登出
                        BJMGFSDKTools.getInstance().setCurrUserStatusOnLine(false);
                        BJMGFSDKTools.getInstance().setCurrUserData(null);
                        EventBus.getDefault().post(new BJMGFSdkEvent(BJMGFSdkEvent.App_Logout));
                        break;
                }
            } else {
                iBaseView.showError(backResultBean.getMsg());
            }

        } catch (Exception e) {
            LogProxy.e(TAG, e.getMessage());
        }

    }

    @Override
    public void onError(Call call, Exception exception, int requestSessionEvent) {

    }
}
