package com.bojoy.bjsdk_mainland_new.presenter.account.impl;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
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
import com.bojoy.bjsdk_mainland_new.support.fastjson.JSONObject;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BasePage;
import com.bojoy.bjsdk_mainland_new.ui.view.IBaseView;
import com.bojoy.bjsdk_mainland_new.ui.view.account.IAccountCenterView;
import com.bojoy.bjsdk_mainland_new.ui.view.account.IUserInfoView;
import com.bojoy.bjsdk_mainland_new.ui.view.account.bindphone.IModifyBindPhoneView;
import com.bojoy.bjsdk_mainland_new.ui.view.cs.IQuestionView;
import com.bojoy.bjsdk_mainland_new.ui.view.cs.impl.SendQuestionView;
import com.bojoy.bjsdk_mainland_new.utils.AccountUtil;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.SpUtil;
import com.bojoy.bjsdk_mainland_new.utils.Utility;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMGFDialog;

import java.io.File;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by wutao on 2016/1/11.
 * 账户中心 控制器实现
 */
public class AccountCenterPresenterImpl implements IAccountCenterPresenter, BaseResultCallbackListener {


    private IAccountModel iAccountModel;
    private IBaseView iBaseView;
    Context context;
    private String nickName, birth;
    private String bindPhoneNum;

    private final String TAG = AccountCenterPresenterImpl.class.getSimpleName();
    private EventBus eventBus = EventBus.getDefault();
    private ISmsModel iSmsModel = new SmsModelImpl();


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
        iSmsModel.getBindPhoneCode(context, phoneNum, this);
    }

    /**
     * 获得解除绑定手机 验证码
     *
     * @param context 上下文
     */
    @Override
    public void getSmsCodeByUnBindPhone(Context context) {
        iSmsModel.getUnBindPhoneCode(context, this);
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
        this.bindPhoneNum = phoneNum;
        iAccountModel.bindPhone(context, phoneNum, verifyCode, this);

    }

    /**
     * 验证解除绑定手机号 时获得验证码
     *
     * @param context    上下文
     * @param verifyCode 验证码
     */
    @Override
    public void validateCodeForUnBindPhone(Context context, String verifyCode) {
        iAccountModel.validateCodeForUnBindPhone(context, verifyCode, this);
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
                    case BaseRequestEvent.REQUEST_VIP_LEVEL://获得会员等级信息
                        paramsMap = JSON.parseObject(backResultBean.getObj(), Map.class);
                        if (iBaseView instanceof SendQuestionView) {
                            ((IQuestionView) iBaseView).showUserVipInfo(paramsMap);
                        } else {
                            ((IAccountCenterView) iBaseView).showUserVipInfo(paramsMap);
                        }
                        break;
                    case BaseRequestEvent.REQUEST_PF_MODIFY_USER_INFO://修改个人信息
                        ((IUserInfoView) iBaseView).showEditUserInfoSuccess();
                        UserData userData = BJMGFSDKTools.getInstance().getCurrUserData();
                        userData.setBirth(birth);
                        userData.setNick(nickName);
                        BJMGFSDKTools.getInstance().setCurrUserData(userData);

                        break;
                    case BaseRequestEvent.REQUEST_GET_ACCOUNT_INFO://获取账号信息事件
                        SpUtil.setStringValue(context, BJMGFSDKTools.getInstance().currentPassPort.getUid(), backResultBean.getObj());
                        ((IAccountCenterView) iBaseView).showAccountInfo();
                        break;
                    case BaseRequestEvent.REQUEST_PF_UPLOAD_FACE_IMAGE://上传用户头像
                        String faceUrl = BJMGFSDKTools.getInstance().getPFFaceUrl(context, Long.valueOf(BJMGFSDKTools.getInstance().getCurrentPassPort().getUid()), JSON.parseObject(backResultBean.getObj()).getString("path"));
                        Log.d(TAG, faceUrl);
                        BJMGFSDKTools.getInstance().getCurrUserData().setFaceUrl(faceUrl);
                        iBaseView.showSuccess();
                        break;
                    case BaseRequestEvent.REQUEST_PASSWORD_MODIFY://修改个人密码
                        PassPort passPort = JSON.parseObject(backResultBean.getObj(), PassPort.class);
                        BJMGFSDKTools.getInstance().setCurrentPassPort(passPort);
                        AccountUtil.remove(context, passPort.getUid());
                        AccountUtil.saveAccount(context, passPort.getUid(), backResultBean.getObj().toString());
                        iBaseView.showSuccess();
                        break;
                    case BaseRequestEvent.REQUEST_PF_REGISTER://好玩友平台注册
                        ((IUserInfoView) iBaseView).showRegisterHwySuccess();
                        break;
                    case BaseRequestEvent.REQUEST_BINDPHONE_CHECK_CODE:// 获取绑定手机验证码
                        iBaseView.showSuccess();
                        break;
                    case BaseRequestEvent.REQUEST_UNBINDPHONE_VERIFY_CODE://获得接触绑定手机验证码
                        iBaseView.showSuccess();
                        break;
                    case BaseRequestEvent.REQUEST_BINDPHONE://绑定手机
                        JSONObject jsonObject = JSON.parseObject(SpUtil.getStringValue(context, BJMGFSDKTools.getInstance().getCurrentPassPort().getUid(), ""));
                        jsonObject.put("bindMobile",bindPhoneNum);
                        SpUtil.setStringValue(context,BJMGFSDKTools.getInstance().getCurrentPassPort().getUid(), jsonObject.toJSONString());
                        iBaseView.showSuccess();
                        break;
                    case BaseRequestEvent.GETREQUEST_UNBINDPHONE_VERIFY_CODE_CHECK://解除绑定手机时获得验证码 的验证
                        ((IModifyBindPhoneView) iBaseView).unbindPhoneCheckVerifyCodeSuccess(backResultBean.getMsg());
                        break;
                    case BaseRequestEvent.REQUEST_BINDEMAIL://绑定邮箱
                        SpUtil.setIntValue(context,SysConstant.EMAIL_BIND_STATUS,1);
                        iBaseView.showSuccess();
                        break;
                    case BaseRequestEvent.REQUEST_LOGOUT://登出
                        BJMGFSDKTools.getInstance().setCurrUserStatusOnLine(false);
                        BJMGFSDKTools.getInstance().setCurrUserData(null);
                        eventBus.post(new BJMGFSdkEvent(BJMGFSdkEvent.App_Logout));

                        break;
                }
            } else {
                if (backResultBean.getMsg().equals(context.getString(ReflectResourceId.getStringId(context, Resource.string.bjmgf_sdk_auth_id_expired)))) {
                    BJMGFSDKTools.getInstance().switchLoginOrLoginListView(context);
                } else {
                    iBaseView.showError(backResultBean.getMsg());
                }
            }

        } catch (Exception e) {
            LogProxy.e(TAG, e.getMessage());
        }

    }

    @Override
    public void onError(Call call, Exception exception, int requestSessionEvent) {

    }
}
