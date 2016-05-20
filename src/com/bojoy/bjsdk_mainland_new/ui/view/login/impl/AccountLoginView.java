package com.bojoy.bjsdk_mainland_new.ui.view.login.impl;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.app.tools.BJMGFSDKTools;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BJMGFSdkEvent;
import com.bojoy.bjsdk_mainland_new.presenter.account.IAccountPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.account.impl.AccountPresenterImpl;
import com.bojoy.bjsdk_mainland_new.support.eventbus.EventBus;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseDialogPage;
import com.bojoy.bjsdk_mainland_new.ui.view.register.ISmsView;
import com.bojoy.bjsdk_mainland_new.ui.view.register.impl.AccountRegisterView;
import com.bojoy.bjsdk_mainland_new.ui.view.account.findpwd.impl.FindPwdSplashPage;
import com.bojoy.bjsdk_mainland_new.utils.*;
import com.bojoy.bjsdk_mainland_new.widget.ClearEditText;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMGFDialog;

/**
 * Created by wutao on 2015/12/23.
 * 账户登录视图
 */
public class AccountLoginView extends BaseDialogPage implements ISmsView {

    private EventBus eventBus = EventBus.getDefault();
    private final String TAG = AccountLoginView.class.getSimpleName();
    private RelativeLayout mOneKeyLoginTextView, mNewUserRegisterTextView;
    TextView mForgetPasswordTextView;
    private Button mAccountLoginButton;
    private ClearEditText mAccountEditText, mPwdEditText;
    IAccountPresenter iAccountPresenter;

    /**
     * 试玩按钮
     */
    private RelativeLayout mLoginTryTextView = null;


    protected final int Max_Timeout = 30000;
    protected final int One_Key_Check_Perid_Time = 6000;
    protected PollingTimeoutTask oneKeyCheckPolling = new PollingTimeoutTask(
              One_Key_Check_Perid_Time, One_Key_Check_Perid_Time / 2,
              Max_Timeout, new PollingTimeoutTask.PollingListener() {

        @Override
        public void onTimeout() {
            dismissProgressDialog();
        }

        @Override
        public void onExecute() {
            LogProxy.i(TAG, "one key check polling onexcute");
           iAccountPresenter.oneKeyRegister(context, SpUtil.getStringValue(context, "uuid", ""));
        }
    });

    protected BroadcastReceiver sendMessage = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            LogProxy.i(TAG, "result=" + getResultCode());
            smsTimeoutTask.suspendPolling();
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    oneKeyCheckPolling.startPolling();
                    ToastUtil.showMessage(context, getString(Resource.string.bjmgf_sdk_sendMessageSuccessedStr));
                    break;
                default:
                    dismissProgressDialog();
                    ToastUtil.showMessage(context, getString(Resource.string.bjmgf_sdk_sendMessageFailStr));
                    if (getResultCode() == Activity.RESULT_CANCELED) {
                        // send sms cancel

                    } else {
                        // send sms fail
                    }
                    break;
            }
        }
    };

    private BroadcastReceiver receiverMessage = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            ToastUtil.showMessage(context, getString(Resource.string.bjmgf_sdk_receiveMessageSuccessedStr));
        }
    };

    protected final int Max_Sms_Timeout = 60000;
    protected final int Sms_Timeout_Perid = 1000;
    protected PollingTimeoutTask smsTimeoutTask = new PollingTimeoutTask(
              Sms_Timeout_Perid, 0, Max_Sms_Timeout,
              new PollingTimeoutTask.PollingListener() {

                  @Override
                  public void onTimeout() {
                      LogProxy.d(TAG, "smsTimeoutTask timeout");
                      dismissProgressDialog();
                      ToastUtil.showMessage(context, getString(Resource.string.bjmgf_sdk_sendMessageFailStr));
                  }

                  @Override
                  public void onExecute() {
                      LogProxy.d(TAG, "smsTimeoutTask timeout");
                  }
              });


    public AccountLoginView(Context context, PageManager manager, BJMGFDialog dialog) {
        super(ReflectResourceId.getLayoutId(context, Resource.layout.bjmgf_sdk_account_login_page),
                  context, manager, dialog);
    }

    @Override
    public void onCreateView(View view) {
        mOneKeyLoginTextView = (RelativeLayout) view.findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_account_login_byPhoneId));
        mNewUserRegisterTextView = (RelativeLayout) view.findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_account_login_newUserRegisterTextViewId));
        mForgetPasswordTextView = (TextView) view.findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_account_login_forgetPasswordTextViewId));
        mLoginTryTextView = (RelativeLayout) view.findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_account_login_tryTextViewId));
        mAccountLoginButton = (Button) view.findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_account_login_buttonId));
        mAccountEditText = (ClearEditText) view.findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_account_login_nameEditTextId));
        mPwdEditText = (ClearEditText) view.findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_account_login_passwordEditTextId));

        /** 手机登录 */
        mOneKeyLoginTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (Utility.isHaveSimCard(context)) {
                    showProgressDialog();
                    iAccountPresenter.sendInfo(context);

                } else {
                    showError(getString(Resource.string.bjmgf_sdk_cannot_get_phone));
                    manager.backToTopPage();
                }

            }
        });

        /**
         * //显示注册视图
         */
        mNewUserRegisterTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                AccountRegisterView registerPage = new AccountRegisterView(context, manager, dialog);
                manager.addPage(registerPage);
            }
        });

        mForgetPasswordTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //忘记密码
                FindPwdSplashPage findPwdPage = new FindPwdSplashPage(context, manager, dialog);
                manager.addPage(findPwdPage);
            }
        });
        mAccountLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                /** 手机号也可以登录 **/
                //登陆
                if (StringUtility.isEmpty(mAccountEditText.getEditText())) {
                    showError(getString(Resource.string.bjmgf_sdk_login_dialog_accountInputHintStr));
                    return;
                }
                if (StringUtility.isEmpty(mPwdEditText.getEditText())) {
                    showError(getString(Resource.string.bjmgf_sdk_login_dialog_passwordInputHintStr));
                    return;
                }

                showProgressDialog();
                iAccountPresenter.login(context, mAccountEditText.getEditText(), mPwdEditText.getEditText());
            }
        });

        //试玩动作
        mLoginTryTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showProgressDialog();
                iAccountPresenter.tryPlay(context);


            }
        });

        mAccountEditText.getEdit().setFilters(new InputFilter[]{new BJMInputFilter()});
       // if (BJMGFSDKTools.getInstance().isShowUserName)
        isDisplayBackIcon();


  /*      Iterator iterator = AccountSharePUtils.getAll(context).keySet().iterator();
        String json = "";
        while (iterator.hasNext()) {
            json = (String) iterator.next();
            break;
        }

        try {
            JSONObject jsonObject = new JSONObject(json);
            mAccountEditText.getEdit().setText(jsonObject.getString("pp"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogProxy.i(TAG, "Current  passport() = " + json);*/
     /*   if(!BJMGFCommon.getPassport().contains("@")) {
            mAccountEditText.getEdit().setText(BJMGFCommon.getPassport());
        }*/
        super.onCreateView(view);
    }


    /**
     * 是否显示back 按钮
     */
    private void isDisplayBackIcon() {
        backView = (LinearLayout) getView(Resource.id.bjmgf_sdk_back);
        if (AccountSharePUtils.getAll(context).size() > 0) {
            if (backView != null) {
                backView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // BJMGFSDKTools.getInstance().bjmgfDialog = new BJMGFDialog(BJMGFSdk.getDefault().rootActivity, BJMGFSdk.getDefault().rootActivity, BJMGFDialog.Page_AccountLogin);
                        // BJMGFSDKTools.getInstance().bjmgfDialog.show();
                        BaseDialogPage loginPage = new AccountLoginListView(context, manager, dialog);
                        manager.clearTopPage(loginPage);
                    }
                });
            }
            showBack();

        } else {
            hideBack();
        }

        if (SpUtil.getIntValue(context, SysConstant.ISMODIFYPWDFLAGFORDIALOG, 0) == 1) {
            hideBack();
            SpUtil.setIntValue(context, SysConstant.ISMODIFYPWDFLAGFORDIALOG, 0);
        }

    }

    @Override
    public void setView() {
        iAccountPresenter = new AccountPresenterImpl(context, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        dialog.setCancelable(true);
        context.registerReceiver(sendMessage, new IntentFilter(SysConstant.SENT_SMS_ACTION));
        context.registerReceiver(receiverMessage, new IntentFilter(
                  SysConstant.DELIVERED_SMS_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        context.unregisterReceiver(sendMessage);
        context.unregisterReceiver(receiverMessage);
    }

    @Override
    public void showError(String message) {
        dismissProgressDialog();
        ToastUtil.showMessage(context, message);
    }

    @Override
    public void showSuccess() {
        //eventBus.post(new BJMGFSdkEvent(BJMGFSdkEvent.APP_REGISTER_SUCCESS));
        if (oneKeyCheckPolling != null) {
            LogProxy.i(TAG, "oneKeyCheckPolling suspend");
            oneKeyCheckPolling.suspendPolling();
            smsTimeoutTask.suspendPolling();
        }
        dismissProgressDialog();
        openWelcomePage();
    }

    @Override
    public void showGetInfoSuccess(String mobile) {
        if (!mobile.equals(""))
            BJMGFSDKTools.getInstance().sendSms(context, mobile, smsTimeoutTask);
    }

}
