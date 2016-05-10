package com.bojoy.bjsdk_mainland_new.model.impl;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import com.bojoy.bjsdk_mainland_new.api.BaseApi;
import com.bojoy.bjsdk_mainland_new.app.tools.BJMGFSDKTools;
import com.bojoy.bjsdk_mainland_new.app.tools.ParamsTools;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseRequestEvent;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseResultCallbackListener;
import com.bojoy.bjsdk_mainland_new.model.IAccountModel;
import com.bojoy.bjsdk_mainland_new.model.entity.BaseAppPassport;
import com.bojoy.bjsdk_mainland_new.support.http.HttpUtility;
import com.bojoy.bjsdk_mainland_new.support.http.callback.StringCallback;
import com.bojoy.bjsdk_mainland_new.support.http.config.FileInput;
import com.bojoy.bjsdk_mainland_new.support.http.config.HttpMethod;
import com.bojoy.bjsdk_mainland_new.utils.AccountSharePUtils;
import com.bojoy.bjsdk_mainland_new.utils.DomainUtility;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.TransferDesEncrypt;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by wutao on 2015/12/24.
 * 账户操作相关业务实现
 */
public class AccountModelImpl implements IAccountModel {

    final String time = String.valueOf(System.currentTimeMillis());
    private final String TAG = AccountModelImpl.class.getSimpleName();

    /**
     * 用户登录
     *
     * @param context          上下文
     * @param userName         用户名
     * @param passWord         密码
     * @param callbackListener 回调事件
     */
    @Override
    public void login(Context context, String userName, String passWord, final BaseResultCallbackListener callbackListener) {
        Map<String, String> params = ParamsTools.getInstance().getBaseParamsMap(context, true);
        params.put("pp", userName.trim());
        params.put("pwd", passWord);
        try {
            HttpUtility.getInstance().execute(HttpMethod.POST, DomainUtility.getInstance().getServiceSDKDomain(context) + BaseApi.APP_PLATFORM_LOGIN, params, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    callbackListener.onError(call, e, BaseRequestEvent.REQUEST_LOGIN);
                }

                @Override
                public void onResponse(String response) {
                    callbackListener.onSuccess(response, BaseRequestEvent.REQUEST_LOGIN);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogProxy.e(TAG, e.getMessage());
        }


    }

    /**
     * 平台注册
     *
     * @param context          上下文
     * @param userName         用户名
     * @param passWord         密码
     * @param email            邮箱
     * @param callbackListener 回调事件
     */
    @Override
    public void platformRegister(Context context, String userName, String passWord, String email, final BaseResultCallbackListener callbackListener) {
        Map<String, String> params = ParamsTools.getInstance().getBaseParamsMap(context, true);
        params.put("pp", userName.trim());
        params.put("pwd", passWord);
        params.put("email", email);
        params.put("channel", BaseAppPassport.getChannel());
        try {
            HttpUtility.getInstance().execute(HttpMethod.POST, DomainUtility.getInstance().getServiceSDKDomain(context) + BaseApi.APP_PLATFORM_REGISTER, params, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    callbackListener.onError(call, e, BaseRequestEvent.REQUEST_REGISTER);
                }

                @Override
                public void onResponse(String response) {
                    callbackListener.onSuccess(response, BaseRequestEvent.REQUEST_REGISTER);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogProxy.e(TAG, e.getMessage());
        }

    }

    /**
     * 试玩游戏
     *
     * @param context  上下文
     * @param tryKey   手机的唯一标识 ，先取 mac ，后取 imei
     * @param listener 回调事件 ，将结果通知 presenter
     */
    @Override
    public void tryLogin(Context context, String tryKey, final BaseResultCallbackListener listener) {
        Map<String, String> params = ParamsTools.getInstance().getBaseParamsMap(context, true);
        params.put("trykey", tryKey);
        params.put("channel", BaseAppPassport.getChannel());
        try {
            HttpUtility.getInstance().execute(HttpMethod.POST, DomainUtility.getInstance().getServiceSDKDomain(context) + BaseApi.APP_TRY_LOGIN, params, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    listener.onError(call, e, BaseRequestEvent.REQUEST_TRY_LOGIN);
                }

                @Override
                public void onResponse(String response) {
                    listener.onSuccess(response, BaseRequestEvent.REQUEST_TRY_LOGIN);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogProxy.e(TAG, e.getMessage());
        }
    }

    @Override
    public void tryChange(Context context, String userName, String passWord, BaseResultCallbackListener listener) {
        Map<String, String> params = ParamsTools.getInstance().getBaseParamsMap(context, true);
        params.put("newPp", userName);
        params.put("newPwd", passWord);
        try {
            HttpUtility.getInstance().execute(HttpMethod.POST, DomainUtility.getInstance().getServiceSDKDomain(context) + BaseApi.APP_TRY_CHANGE, params, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    listener.onError(call, e, BaseRequestEvent.REQUEST_TRY_CHANGE);
                }

                @Override
                public void onResponse(String response) {
                    listener.onSuccess(response, BaseRequestEvent.REQUEST_TRY_CHANGE);
                }
            });
        } catch (Exception e) {

        }
    }

    /**
     * 一键注册and 定时检测
     *
     * @param context    上下文
     * @param verifyCode 校验码
     * @param listener   回调事件 ，将结果通知presenter
     */
    @Override
    public void oneKeyRegister(Context context, String verifyCode, final BaseResultCallbackListener listener) {
        Map<String, String> params = ParamsTools.getInstance().getBaseParamsMap(context, true);
        params.put("verifyCode", verifyCode);
        try {
            HttpUtility.getInstance().execute(HttpMethod.POST, DomainUtility.getInstance().getServiceSDKDomain(context) + BaseApi.APP_ONEKEY_CHECK, params, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    listener.onError(call, e, BaseRequestEvent.REQUEST_ONE_KEY_CHECK);
                }

                @Override
                public void onResponse(String response) {
                    listener.onSuccess(response, BaseRequestEvent.REQUEST_ONE_KEY_CHECK);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogProxy.e(TAG, e.getMessage());
        }
    }

    /**
     * 向手机发送验证码
     *
     * @param context  上下文
     * @param mobile   手机号码
     * @param listener 回调事件 将结果通知presenter
     */
    @Override
    public void sendPhoneCode(Context context, String mobile, final BaseResultCallbackListener listener) {
        Map<String, String> params = ParamsTools.getInstance().getBaseParamsMap(context, true);
        params.put("cmd", "mobileReg");
        params.put("mobile", mobile);
        try {
            HttpUtility.getInstance().execute(HttpMethod.POST, DomainUtility.getInstance().getServiceSDKDomain(context) + BaseApi.APP_SEND_PHONE_CODE, params, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    listener.onError(call, e, BaseRequestEvent.REQUEST_SENDSMS_CODE);
                }

                @Override
                public void onResponse(String response) {
                    listener.onSuccess(response, BaseRequestEvent.REQUEST_SENDSMS_CODE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogProxy.e(TAG, e.getMessage());
        }
    }

    @Override
    public void mobileRegCheck(Context context, String mobile, String verifyCode, String pwd, int status, BaseResultCallbackListener listener) {

        Map<String, String> params = ParamsTools.getInstance().getBaseParamsMap(context, true);
        params.put("mobile", mobile);
        params.put("verifyCode", verifyCode);
        if (status == 2) {
            params.put("pwd", pwd);
        }
        ;
        try {
            HttpUtility.getInstance().execute(HttpMethod.POST, DomainUtility.getInstance().getServiceSDKDomain(context) + BaseApi.APP_MOBILE_REG_CHECK, params, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    listener.onError(call, e, BaseRequestEvent.REQUEST_MOBILE_REGISTER_CHECK);
                }

                @Override
                public void onResponse(String response) {
                    listener.onSuccess(response, BaseRequestEvent.REQUEST_MOBILE_REGISTER_CHECK);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogProxy.e(TAG, e.getMessage());
        }
    }


    /**
     * 自动登陆
     *
     * @param context  上下文
     * @param listener 回调事件  将结果通知presenter
     */
    @Override
    public void autoLogin(Context context, final BaseResultCallbackListener listener) {
        Map<String, String> params = ParamsTools.getInstance().getBaseParamsMap(context, false);
        if (BJMGFSDKTools.getInstance().getCurrentPassPort() != null)
            params.put("token", BJMGFSDKTools.getInstance().getCurrentPassPort().getToken());
        else
            params.put("token", AccountSharePUtils.getLocalAccountList(context).get(0).getToken());
        try {
            HttpUtility.getInstance().execute(HttpMethod.POST, DomainUtility.getInstance().getServiceSDKDomain(context) + BaseApi.APP_AUTO_LOGIN, params, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    listener.onError(call, e, BaseRequestEvent.REQUEST_AUTO_LOGIN);
                }

                @Override
                public void onResponse(String response) {
                    listener.onSuccess(response, BaseRequestEvent.REQUEST_AUTO_LOGIN);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogProxy.e(TAG, e.getMessage());
        }
    }

    /**
     * 手机找回密码
     *
     * @param context
     * @param listener 回调事件  将结果通知presenter
     */
    @Override
    public void mobileFindPsw(Context context, String account, final BaseResultCallbackListener listener) {
        Map<String, String> params = ParamsTools.getInstance().getBaseParamsMap(context, false);
        params.put("pp", account);
        try {
            HttpUtility.getInstance().execute(HttpMethod.POST, DomainUtility.getInstance().getServiceSDKDomain(context) + BaseApi.APP_MOBILE_FIND_PSW, params, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    listener.onError(call, e, BaseRequestEvent.REQUEST_FIND_PASSWORD_VERIFY_CODE);
                }

                @Override
                public void onResponse(String response) {
                    listener.onSuccess(response, BaseRequestEvent.REQUEST_FIND_PASSWORD_VERIFY_CODE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogProxy.e(TAG, e.getMessage());
        }

    }

    /**
     * 找回密码-重置密码
     *
     * @param context
     * @param mobileKey
     * @param verifyCode
     * @param psw
     * @param listener   回调事件  将结果通知presenter
     */
    @Override
    public void resetPswForFindPsw(Context context, String mobileKey, String verifyCode, String psw, final BaseResultCallbackListener listener) {
        Map<String, String> params = ParamsTools.getInstance().getBaseParamsMap(context, false);
        params.put("mobileKey", mobileKey);
        params.put("verifyCode", verifyCode);
        params.put("pwd", psw);
        try {
            HttpUtility.getInstance().execute(HttpMethod.POST, DomainUtility.getInstance().getServiceSDKDomain(context) + BaseApi.APP_MOBILE_RESET_PSW, params, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    listener.onError(call, e, BaseRequestEvent.REQUEST_RESET_PASSWORD);
                }

                @Override
                public void onResponse(String response) {
                    listener.onSuccess(response, BaseRequestEvent.REQUEST_RESET_PASSWORD);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogProxy.e(TAG, e.getMessage());
        }
    }


    /**
     * 邮箱找回密码
     *
     * @param account
     * @param email
     */
    @Override
    public void emailFindPsw(Context context, String account, String email,
                             final BaseResultCallbackListener listener) {
        Map<String, String> params = ParamsTools.getInstance().getBaseParamsMap(context, false);
        params.put("pp", account);
        params.put("email", email);
        try {
            HttpUtility.getInstance().execute(HttpMethod.POST, DomainUtility.getInstance().getServiceSDKDomain(context) + BaseApi.APP_EMAIL_FIND_PSW, params, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    listener.onError(call, e, BaseRequestEvent.REQUEST_EMAILFINDPWD);
                }

                @Override
                public void onResponse(String response) {
                    listener.onSuccess(response, BaseRequestEvent.REQUEST_EMAILFINDPWD);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogProxy.e(TAG, e.getMessage());
        }
    }

    /**
     * 获得账户信息
     *
     * @param context  上下文
     * @param listener 回调事件 将结果通知presenter
     */
    @Override
    public void getAccountInfo(Context context, final BaseResultCallbackListener listener) {
        Map<String, String> params = ParamsTools.getInstance().getBaseParamsMap(context, false);
        try {
            HttpUtility.getInstance().execute(HttpMethod.POST, DomainUtility.getInstance().getServiceSDKDomain(context) + BaseApi.APP_GET_ACCOUNT_INFO, params, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    listener.onError(call, e, BaseRequestEvent.REQUEST_GET_ACCOUNT_INFO);
                }

                @Override
                public void onResponse(String response) {
                    listener.onSuccess(response, BaseRequestEvent.REQUEST_GET_ACCOUNT_INFO);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogProxy.e(TAG, e.getMessage());
        }
    }

    /**
     * 获的用户 会员信息
     *
     * @param context  上下文
     * @param listener 回调事件 将结果通知presenter
     */
    @Override
    public void getUserVipInfo(Context context, final BaseResultCallbackListener listener) {
        Map<String, String> params = ParamsTools.getInstance().getUserCenterParamsMap(context);
        try {
            HttpUtility.getInstance().execute(HttpMethod.POST, DomainUtility.getInstance().getServiceSDKDomain(context) + BaseApi.APP_GET_USERVIP_INFO, params, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    listener.onError(call, e, BaseRequestEvent.REQUEST_VIP_LEVEL);
                }

                @Override
                public void onResponse(String response) {
                    listener.onSuccess(response, BaseRequestEvent.REQUEST_VIP_LEVEL);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogProxy.e(TAG, e.getMessage());
        }

    }


    /**
     * 查询自己的个人信息
     *
     * @param context  上下文
     * @param type     更新类型1：基本信息，2：详细信息，3：全部信息；如果为3，不验证最后更新时间，最后更新时间可以为空
     * @param listener 回调事件 将结果通知presenter
     */
    @Override
    public void getUserInfoForSelf(Context context, int type, final BaseResultCallbackListener listener) {
        Map<String, String> params = ParamsTools.getInstance().getUserCenterParamsMap(context);
        /*String code = TransferDesEncrypt.encryptDef(SysConstant.USERCENTER_HEADER_REQUEST_CODE_GET_USERINFO_FOR_SELF + "|" + "0" + "|" + type);
        String c = null;
        try {
            c = URLEncoder.encode(code, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        params.put("c", SysConstant.USERCENTER_HEADER_REQUEST_CODE_GET_USERINFO_FOR_SELF + "|" + "0" + "|" + type);
        try {

            HttpUtility.getInstance().execute(HttpMethod.POST, DomainUtility.getInstance().getUserCenterDomain(context) + BaseApi.APP_USERCERTER_REQUEST, params, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    listener.onError(call, e, BaseRequestEvent.REQUEST_PF_USER_INFO);
                }

                @Override

                public void onResponse(String response) {
                    listener.onSuccess(response, BaseRequestEvent.REQUEST_PF_USER_INFO);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogProxy.e(TAG, e.getMessage());
        }
    }

    /**
     * 修改个人信息
     *
     * @param context  上下文
     * @param nickName 昵称
     * @param birth    生日
     * @param listener 回调事件 将结果通知presenter
     */
    @Override
    public void editUserInfo(Context context, String nickName, String birth, final BaseResultCallbackListener listener) {
        Map<String, String> params = ParamsTools.getInstance().getUserCenterParamsMap(context);
       /* String nickNameUTF = "";
        try {
            nickNameUTF = URLEncoder.encode(nickName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
          String code = TransferDesEncrypt.encryptDef( SysConstant.USERCENTER_HEADER_REQUEST_CODE_EDIT_USERINFO_FOR_SELF + "|" + nickName + "||" + birth + "|||||||||||");
        String c = null;
        try {
            c = URLEncoder.encode(code, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        */
        params.put("c", SysConstant.USERCENTER_HEADER_REQUEST_CODE_EDIT_USERINFO_FOR_SELF + "|" + nickName + "||" + birth + "|||||||||||");
        //params.put("c",c);
        try {
            HttpUtility.getInstance().execute(HttpMethod.POST, DomainUtility.getInstance().getUserCenterDomain(context) + BaseApi.APP_USERCERTER_REQUEST, params, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    listener.onError(call, e, BaseRequestEvent.REQUEST_PF_MODIFY_USER_INFO);
                }

                @Override
                public void onResponse(String response) {
                    listener.onSuccess(response, BaseRequestEvent.REQUEST_PF_MODIFY_USER_INFO);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogProxy.e(TAG, e.getMessage());
        }
    }

    /**
     * 上传用户头像
     *
     * @param context  上下文
     * @param file     照片file 流
     * @param listener 回调事件 将结果通知presenter
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    @Override
    public void uploadUserFace(Context context, File file, final BaseResultCallbackListener listener) {
        Map<String, String> params = ParamsTools.getInstance().getUserCenterParamsMap(context);
        params.put("stepMd5F", "1");
        List<FileInput> list = new ArrayList<FileInput>();
        if (file.isFile()) {
            FileInput fileInput = new FileInput("file", file.getPath(), file);
            list.add(fileInput);
        }
        try {
            HttpUtility.getInstance().executeUpLoadFile(DomainUtility.getInstance().getUploadDomain(context) + BaseApi.APP_UPLOAD_USER_FACE, params, list.isEmpty() ? null : list, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    listener.onError(call, e, BaseRequestEvent.REQUEST_PF_UPLOAD_FACE_IMAGE);
                }

                @Override
                public void onResponse(String response) {
                    listener.onSuccess(response, BaseRequestEvent.REQUEST_PF_UPLOAD_FACE_IMAGE);
                }
            });
        } catch (Exception e) {

        }

    }

    /**
     * 修改密码
     *
     * @param context  上下文
     * @param oldPwd   老密码
     * @param newPwd   新密码
     * @param listener 回调事件 将结果通知presenter
     */
    @Override
    public void modifyPassword(Context context, String oldPwd, String newPwd, final BaseResultCallbackListener listener) {
        Map<String, String> params = ParamsTools.getInstance().getBaseParamsMap(context, false);
        params.put("oldPwd", oldPwd);
        params.put("newPwd", newPwd);

        try {
            HttpUtility.getInstance().execute(HttpMethod.POST, DomainUtility.getInstance().getServiceSDKDomain(context) + BaseApi.APP_MODIFY_PWD, params, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    listener.onError(call, e, BaseRequestEvent.REQUEST_PASSWORD_MODIFY);
                }

                @Override
                public void onResponse(String response) {
                    listener.onSuccess(response, BaseRequestEvent.REQUEST_PASSWORD_MODIFY);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogProxy.e(TAG, e.getMessage());
        }
    }

    /**
     * 好玩友平台注册
     *
     * @param context  上下文
     * @param nickName 昵称
     * @param sex      性别
     * @param birth    生日
     * @param listener 回调事件 将结果通知presenter
     */
    @Override
    public void hwyPlatformRegister(Context context, String nickName, String sex, String birth, final BaseResultCallbackListener listener) {
        Map<String, String> params = ParamsTools.getInstance().getUserCenterParamsMap(context);
        params.put("c", TransferDesEncrypt.encryptDef(SysConstant.USERCENTER_HEADER_REQUEST_CODE_HWYPLATFORM_REGISTER + "|" + nickName + "|" + sex + "|" + birth));
        try {
            HttpUtility.getInstance().execute(HttpMethod.POST, DomainUtility.getInstance().getUserCenterDomain(context) + BaseApi.APP_USERCERTER_REQUEST, params, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    listener.onError(call, e, BaseRequestEvent.REQUEST_PF_REGISTER);
                }

                @Override
                public void onResponse(String response) {
                    listener.onSuccess(response, BaseRequestEvent.REQUEST_PF_REGISTER);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogProxy.e(TAG, e.getMessage());
        }
    }

    /**
     * 绑定邮箱
     *
     * @param context  上下文
     * @param email    邮箱地址
     * @param listener 回调事件  将结果通知presenter
     */
    @Override
    public void bindEmail(Context context, String email, final BaseResultCallbackListener listener) {
        Map<String, String> params = ParamsTools.getInstance().getBaseParamsMap(context, false);
        params.put("email", email);
        try {
            HttpUtility.getInstance().execute(HttpMethod.POST, DomainUtility.getInstance().getServiceSDKDomain(context) + BaseApi.APP_BIND_EMAIL, params, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    listener.onError(call, e, BaseRequestEvent.REQUEST_BINDEMAIL);
                }

                @Override
                public void onResponse(String response) {
                    listener.onSuccess(response, BaseRequestEvent.REQUEST_BINDEMAIL);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogProxy.e(TAG, e.getMessage());
        }

    }

    /**
     * 绑定手机号
     *
     * @param context    上下文
     * @param phoneNum   需要绑定的手机号
     * @param verifyCode 验证码
     * @param listener   回调事件 将结果通知presenter
     */
    @Override
    public void bindPhone(Context context, String phoneNum, String verifyCode, final BaseResultCallbackListener listener) {
        Map<String, String> params = ParamsTools.getInstance().getBaseParamsMap(context, false);
        params.put("mobile", phoneNum);
        params.put("verifyCode", verifyCode);
        try {
            HttpUtility.getInstance().execute(HttpMethod.POST, DomainUtility.getInstance().getServiceSDKDomain(context) + BaseApi.APP_BIND_PHONE, params, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    listener.onError(call, e, BaseRequestEvent.REQUEST_BINDPHONE);
                }

                @Override
                public void onResponse(String response) {
                    listener.onSuccess(response, BaseRequestEvent.REQUEST_BINDPHONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogProxy.e(TAG, e.getMessage());
        }
    }

    /**
     * 验证解除绑定手机号 时获得验证码
     *
     * @param context    上下文
     * @param verifyCode 验证码
     * @param listener   回调事件 将结果通知presenter
     */
    @Override
    public void validateCodeForUnBindPhone(Context context, String verifyCode, BaseResultCallbackListener listener) {
        Map<String, String> params = ParamsTools.getInstance().getBaseParamsMap(context, false);
        params.put("verifyCode", verifyCode);
        try {
            HttpUtility.getInstance().execute(HttpMethod.POST, DomainUtility.getInstance().getServiceSDKDomain(context) + BaseApi.APP_UNBIND_PHONE, params, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    listener.onError(call, e, BaseRequestEvent.GETREQUEST_UNBINDPHONE_VERIFY_CODE_CHECK);
                }

                @Override
                public void onResponse(String response) {
                    listener.onSuccess(response, BaseRequestEvent.GETREQUEST_UNBINDPHONE_VERIFY_CODE_CHECK);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogProxy.e(TAG, e.getMessage());
        }
    }

    @Override
    public void sendInfo(Context context, final BaseResultCallbackListener listener) {
        try {
            HttpUtility.getInstance().execute(HttpMethod.POST, DomainUtility.getInstance().getServiceSDKDomain(context) + BaseApi.APP_SEND_INFO, ParamsTools.getInstance().getBaseParamsMap(context, false), new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    listener.onError(call, e, BaseRequestEvent.REQUEST_ONE_KEY_INFO);
                }

                @Override
                public void onResponse(String response) {
                    listener.onSuccess(response, BaseRequestEvent.REQUEST_ONE_KEY_INFO);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogProxy.e(TAG, e.getMessage());
        }
    }

    /**
     * 获得其他用户信息
     *
     * @param context  上下文
     * @param uid      用户uid
     * @param type     信息类型 1：基本信息，2：详细信息，3：全部信息
     * @param listener 回调事件 将结果通知presenter
     */
    @Override
    public void getOtherUserInfo(Context context, String uid, int type, final BaseResultCallbackListener listener) {
        Map<String, String> params = ParamsTools.getInstance().getUserCenterParamsMap(context);
        String[] uidArrays = uid.split(",");
        StringBuilder cStr = new StringBuilder(SysConstant.USERCENTER_HEADER_REQUEST_CODE_GET_OTHER_USERINFO + "|" + type);
        for (int i = 0; i < uidArrays.length; i++) {
            cStr.append("|" + uidArrays[i] + "," + "0");
        }
        /*String code = TransferDesEncrypt.encryptDef(cStr.toString());
        String c = null;
        try {
            c = URLEncoder.encode(code, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        params.put("c", cStr.toString());
        try {

            HttpUtility.getInstance().execute(HttpMethod.POST, DomainUtility.getInstance().getUserCenterDomain(context) + BaseApi.APP_USERCERTER_REQUEST, params, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    listener.onError(call, e, BaseRequestEvent.REQUEST_PF_OTHER_USERS_INFO);
                }

                @Override

                public void onResponse(String response) {
                    listener.onSuccess(response, BaseRequestEvent.REQUEST_PF_OTHER_USERS_INFO);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogProxy.e(TAG, e.getMessage());
        }

    }

    /**
     * 登出
     *
     * @param context  上下文
     * @param listener 回调事件 将结果通知presenter
     */
    @Override
    public void logout(Context context, final BaseResultCallbackListener listener) {
        Map<String, String> params = ParamsTools.getInstance().getBaseParamsMap(context, false);
        try {
            HttpUtility.getInstance().execute(HttpMethod.POST, DomainUtility.getInstance().getServiceSDKDomain(context) + BaseApi.APP_PLATFORM_LOGOUT, params, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    listener.onError(call, e, BaseRequestEvent.REQUEST_LOGOUT);

                }

                @Override
                public void onResponse(String response) {
                    listener.onSuccess(response, BaseRequestEvent.REQUEST_LOGOUT);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogProxy.e(TAG, e.getMessage());
        }
    }
}
