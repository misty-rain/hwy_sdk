package com.bojoy.bjsdk_mainland_new.ui.view.account.findpwdfullscreen;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.presenter.account.findpwd.ICheckVerifyCodePresenter;
import com.bojoy.bjsdk_mainland_new.presenter.account.findpwd.IPswFindPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.account.findpwd.impl.CheckVerifyCodePresenterImpl;
import com.bojoy.bjsdk_mainland_new.presenter.account.findpwd.impl.PswFindPresenterImpl;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseActivityPage;
import com.bojoy.bjsdk_mainland_new.ui.view.account.findpwd.ICheckVerifyCodeView;
import com.bojoy.bjsdk_mainland_new.ui.view.account.findpwd.IPswFindView;
import com.bojoy.bjsdk_mainland_new.ui.view.account.findpwd.impl.CheckPhonePage;
import com.bojoy.bjsdk_mainland_new.ui.view.account.findpwd.impl.FindPwdResetPage;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.PollingTimeoutTask;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;
import com.bojoy.bjsdk_mainland_new.utils.ToastUtil;
import com.bojoy.bjsdk_mainland_new.widget.ClearEditText;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMGFDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wutao on 2016/5/24.
 */
public class FindPwdCheckView extends CheckPhoneView implements ICheckVerifyCodeView, IPswFindView {

    private final String TAG = FindPwdCheckView.class.getSimpleName();

    private TextView mBindPhoneNumTextView;
    private ClearEditText mCheckCodeEditText;
    private ICheckVerifyCodePresenter mICheckVerifyCodePresenter;
    private IPswFindPresenter mIPswFindPresenter;
    private String currentAccount;

    private int mResetTime = 60;
    @SuppressWarnings("unused")
    private final int Reset_Max_Timeout = mResetTime * 1000;
    @SuppressWarnings("unused")
    private final int Reset_Period = 1000;
    private PollingTimeoutTask pollingTask;

    public FindPwdCheckView(Context context,
                            PageManager manager, BJMGFActivity activity) {
        super(ReflectResourceId.getLayoutId(context,
                  Resource.layout.bjmgf_sdk_find_password_check_page_fullscreen), context, manager, activity);
    }

    @Override
    public void onCreateView(View view) {
        super.onCreateView(view);
    }

    @Override
    public void setView() {
        setTopicStr(getString(Resource.string.bjmgf_sdk_floatWindow_findpwd_title));
        setRightButtonStr(getString(Resource.string.bjmgf_sdk_find_password_nextStepBtnStr));

        mBindPhoneNumTextView = (TextView) getView(Resource.id.bjmgf_sdk_checkCode_sendPhoneTextViewId);
        mResetButton = (Button) getView(Resource.id.bjmgf_sdk_checkCode_resetButtonId);
        mCheckCodeEditText = (ClearEditText) getView(Resource.id.bjmgf_sdk_check_verifyCode_contentEditTextId);

        mICheckVerifyCodePresenter = new CheckVerifyCodePresenterImpl(context,
                  this);
        mIPswFindPresenter = new PswFindPresenterImpl(context, this);

        currentAccount = getBundle().getString("account");

        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pollingTask != null) {
                    pollingTask.suspendPolling();
                }
                manager.backToTopPage();
            }
        });

        setRightButtonClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCheckCodeEditText.getEditText().length() == 6) {
                    // showProgressDialog();
                    mICheckVerifyCodePresenter.checkVerifyCode(context,
                              getBundle().getString("mobileKey"),
                              mCheckCodeEditText.getEditText());
                } else {
                    ToastUtil
                              .showMessage(
                                        context,
                                        getString(Resource.string.bjmgf_sdk_register_dialog_checkVerifyCodeErrorStr));
                }

            }
        });

        mResetButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showProgressDialog();
                setPollingResetStart();
                if (currentAccount != null)
                    if (!currentAccount.equals(""))
                        mIPswFindPresenter.getVerifyCode(context, currentAccount);
            }
        });
        mBindPhoneNumTextView.setText(mBindPhoneNumTextView.getText()
                  + StringUtility.getMoblieKey(
                  getBundle().getString("mobileKey"), 1));
        setPollingResetStart();

    }

    @Override
    public void checkVerifyCodeSuccess() {
        dismissProgressDialog();
        if (pollingTask != null) {
            pollingTask.suspendPolling();
        }
        Bundle bundle = new Bundle();
        bundle.putString("mobileKey",getBundle().getString("mobileKey"));
        bundle.putString("checkCode",mCheckCodeEditText.getEditText());
        FindPwdResetView pwdResetPage = new FindPwdResetView(context, manager,
                  activity);
        pwdResetPage.setBundle(bundle);
        manager.replacePage(pwdResetPage);
    }

    @Override
    public void mobileFindSuccess(String mobileKey) {
        dismissProgressDialog();
        LogProxy.i(TAG, "Wait verify code");
        Bundle bundle = new Bundle();
        bundle.putString("mobileKey",mobileKey);
        bundle.putString("accout",currentAccount);
        setBundle(bundle);
    }

    @Override
    public void showError(String message) {
        dismissProgressDialog();
        super.showError(message);
    }

    @Override
    public void showSuccess() {
        dismissProgressDialog();
        super.showSuccess();
    }
}
