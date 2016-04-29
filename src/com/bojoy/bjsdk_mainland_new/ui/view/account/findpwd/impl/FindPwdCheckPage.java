package com.bojoy.bjsdk_mainland_new.ui.view.account.findpwd.impl;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.presenter.account.findpwd.ICheckVerifyCodePresenter;
import com.bojoy.bjsdk_mainland_new.presenter.account.findpwd.IPswFindPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.account.findpwd.impl.CheckVerifyCodePresenterImpl;
import com.bojoy.bjsdk_mainland_new.presenter.account.findpwd.impl.PswFindPresenterImpl;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.view.account.findpwd.ICheckVerifyCodeView;
import com.bojoy.bjsdk_mainland_new.ui.view.account.findpwd.IPswFindView;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.PollingTimeoutTask;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;
import com.bojoy.bjsdk_mainland_new.utils.ToastUtil;
import com.bojoy.bjsdk_mainland_new.widget.ClearEditText;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMGFDialog;

/**
 * 
 * @author xuxiaobin FindPwdCheckPage 找回密码校验手机和验证码页面
 * 
 */
public class FindPwdCheckPage extends CheckPhonePage implements
		ICheckVerifyCodeView, IPswFindView {

	private final String TAG = FindPwdCheckPage.class.getSimpleName();

	private TextView mBindPhoneNumTextView;
	private Button mNextStepButton;
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

	public FindPwdCheckPage(Context context, PageManager manager,
			BJMGFDialog dialog) {
		super(ReflectResourceId.getLayoutId(context,
				Resource.layout.bjmgf_sdk_find_password_check_page), context,
				manager, dialog);
	}

	@Override
	public void setView() {

		mBindPhoneNumTextView = (TextView) pageView
				.findViewById(ReflectResourceId.getViewId(context,
						Resource.id.bjmgf_sdk_checkCode_sendPhoneTextViewId));
		mNextStepButton = (Button) pageView.findViewById(ReflectResourceId
				.getViewId(context,
						Resource.id.bjmgf_sdk_checkCode_nextStepBtnId));
		mResetButton = (Button) pageView.findViewById(ReflectResourceId
				.getViewId(context,
						Resource.id.bjmgf_sdk_checkCode_resetButtonId));
		mCheckCodeEditText = (ClearEditText) pageView
				.findViewById(ReflectResourceId
						.getViewId(
								context,
								Resource.id.bjmgf_sdk_check_verifyCode_contentEditTextId));

		mICheckVerifyCodePresenter = new CheckVerifyCodePresenterImpl(context,
				this);
		mIPswFindPresenter = new PswFindPresenterImpl(context, this);

		currentAccount = (String) getParams().get("account");

		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (pollingTask != null) {
					pollingTask.suspendPolling();
				}
				manager.backToTopPage();
			}
		});

		mNextStepButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mCheckCodeEditText.getEditText().length() == 6) {
					// showProgressDialog();
					mICheckVerifyCodePresenter.checkVerifyCode(context,
							(String) getParams().get("mobileKey"),
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
				mIPswFindPresenter.getVerifyCode(context, currentAccount);
			}
		});
		mBindPhoneNumTextView.setText(mBindPhoneNumTextView.getText()
				+ StringUtility.getMoblieKey(
						(String) getParams().get("mobileKey"), 1));
		setPollingResetStart();
	}

	@Override
	public void checkVerifyCodeSuccess() {
		dismissProgressDialog();
		if (pollingTask != null) {
			pollingTask.suspendPolling();
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mobileKey", (String) getParams().get("mobileKey"));
		params.put("checkCode", mCheckCodeEditText.getEditText());
		FindPwdResetPage pwdResetPage = new FindPwdResetPage(context, manager,
				dialog);
		pwdResetPage.putParams(params);
		manager.replacePage(pwdResetPage);
	}

	@Override
	public void mobileFindSuccess(String mobileKey) {
		dismissProgressDialog();
		LogProxy.i(TAG, "Wait verify code");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mobileKey", mobileKey);
		params.put("accout", currentAccount);
		putParams(params);
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
