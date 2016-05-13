package com.bojoy.bjsdk_mainland_new.ui.view.register.impl;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.app.tools.BJMGFSDKTools;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BJMGFSdkEvent;
import com.bojoy.bjsdk_mainland_new.presenter.account.IAccountPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.account.impl.AccountPresenterImpl;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseDialogPage;
import com.bojoy.bjsdk_mainland_new.ui.view.IBaseView;
import com.bojoy.bjsdk_mainland_new.ui.view.login.impl.OneKeyLoginView;
import com.bojoy.bjsdk_mainland_new.ui.view.register.IRegisterView;
import com.bojoy.bjsdk_mainland_new.ui.view.register.ISmsView;
import com.bojoy.bjsdk_mainland_new.utils.*;
import com.bojoy.bjsdk_mainland_new.widget.ClearEditText;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMGFDialog;
import com.bojoy.bjsdk_mainland_new.widget.dialog.ProtocolDialog;

/**
 * Created by wutao on 2015/12/28.
 * 账户注册视图
 */

public class AccountRegisterView extends BaseDialogPage implements IRegisterView, ISmsView {


    private TextView mProtocolTextView;
    private ClearEditText mAccountEditText, mPwdEditText;
    private Button mRegisterButton;
    private RelativeLayout mTryPlay, mPhoneRgister;
    private IAccountPresenter accountPresenter;
    private final String TAG = AccountRegisterView.class.getSimpleName();


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
            accountPresenter.oneKeyRegister(context, SpUtil.getStringValue(context, "uuid", ""));
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

    public AccountRegisterView(Context context, PageManager manager, BJMGFDialog dialog) {
        super(ReflectResourceId.getLayoutId(context, Resource.layout.bjmgf_sdk_account_register_page),
                  context, manager, dialog);
    }

    @Override
    public void onCreateView(View view) {
        mProtocolTextView = (TextView) view
                  .findViewById(ReflectResourceId
                            .getViewId(
                                      context,
                                      Resource.id.bjmgf_sdk_account_register_protocolTextViewId));
        mAccountEditText = (ClearEditText) view.findViewById(ReflectResourceId
                  .getViewId(context,
                            Resource.id.bjmgf_sdk_account_register_nameEditTextId));
        mPwdEditText = (ClearEditText) view
                  .findViewById(ReflectResourceId
                            .getViewId(
                                      context,
                                      Resource.id.bjmgf_sdk_account_register_passwordEditTextId));
        mRegisterButton = (Button) view.findViewById(ReflectResourceId
                  .getViewId(context,
                            Resource.id.bjmgf_sdk_account_register_buttonId));
        mTryPlay = (RelativeLayout) getView(Resource.id.bjmgf_sdk_account_register_tryTextViewId);
        mPhoneRgister = (RelativeLayout) getView(Resource.id.bjmgf_sdk_account_register_byPhoneId);
        /** 打开协议按钮 */
        mProtocolTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ProtocolDialog protocolDialog = new ProtocolDialog(context);
                protocolDialog.show();

            }
        });
        /** 注册按钮 */
        mRegisterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //判断注册 信息
                if (StringUtility.isEmpty(mAccountEditText.getEditText())) {
                    showError(getString(Resource.string.bjmgf_sdk_login_dialog_accountInputHintStr));
                    return;
                }
                if (StringUtility.isEmpty(mPwdEditText.getEditText())) {
                    showError(getString(Resource.string.bjmgf_sdk_login_dialog_passwordInputHintStr));
                    return;
                }
                showProgressDialog();
                accountPresenter.platformRegister(context, mAccountEditText.getEditText().toString().trim(), mPwdEditText.getEditText().toString().trim(), "");

            }
        });

        if (BJMGFSDKTools.getInstance().mPlatform == SysConstant.PLATFORM_GAME_ID) {
            mTryPlay.setVisibility(View.GONE);
        } else {
            mTryPlay.setVisibility(View.VISIBLE);
        }

        mTryPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showProgressDialog();
                accountPresenter.tryPlay(context);

            }
        });

        mPhoneRgister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                BJMGFSDKTools.getInstance().isByRegister = true;
                // by sunhaoyang
                if (Utility.isHaveSimCard(context)) {
                    showProgressDialog();
                    accountPresenter.sendInfo(context);
                } else {
                    AskVerifyCodeView verifyCodePage = new AskVerifyCodeView(
                              context, manager, dialog);
                    manager.addPage(verifyCodePage);
                }
            }
        });

        BJMGFSDKTools.getInstance().baseDialogPage = this;

        super.onCreateView(view);
    }

    @Override
    public void onResume() {
        context.registerReceiver(sendMessage, new IntentFilter(SysConstant.SENT_SMS_ACTION));
        context.registerReceiver(receiverMessage, new IntentFilter(
                  SysConstant.DELIVERED_SMS_ACTION));
        super.onResume();
    }

    @Override
    public void onPause() {
        context.unregisterReceiver(sendMessage);
        context.unregisterReceiver(receiverMessage);
        super.onPause();
    }

    @Override
    public void setView() {
        accountPresenter = new AccountPresenterImpl(context, this);
    }

    @Override
    public void showError(String message) {
        dismissProgressDialog();
        ToastUtil.showMessage(context, message);
    }

    /**
     * 注册成功后 ，view 的处理
     */
    @Override
    public void showSuccess() {
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


    @Override
    public void showRegisterSuccess() {
        eventBus.post(new BJMGFSdkEvent(BJMGFSdkEvent.APP_REGISTER_SUCCESS));
        if (oneKeyCheckPolling != null) {
            LogProxy.i(TAG, "oneKeyCheckPolling suspend");
            oneKeyCheckPolling.suspendPolling();
            smsTimeoutTask.suspendPolling();
        }
        dismissProgressDialog();
        openWelcomePage();
    }
}
