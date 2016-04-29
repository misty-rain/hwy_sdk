package com.bojoy.bjsdk_mainland_new.ui.view.register.impl;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.bojoy.bjsdk_mainland_new.presenter.account.IAccountMobileRegristerPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.account.impl.AccountMobileRegisterImpl;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseDialogPage;
import com.bojoy.bjsdk_mainland_new.ui.view.register.IAskMobileVerifyCodeView;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;
import com.bojoy.bjsdk_mainland_new.widget.ClearEditText;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMGFDialog;

/**
 * 
 * @author xuxiaobin PhoneRegisterView 手机注册页面
 * 
 */
public class PhoneRegisterView extends BaseDialogPage implements IAskMobileVerifyCodeView {

	private final String TAG = PhoneRegisterView.class.getSimpleName();

	private static int MOBILE_REG_STATUS = 2;

	IAccountMobileRegristerPresenter mIAccountMobileRegristerPresenter = null;

	private Button mPhoneRegisterButton;
	private EditText mPhoneNumEditText;
	private ClearEditText mPwdEditText;

	public PhoneRegisterView(Context context, PageManager manager,
							 BJMGFDialog dialog) {
		super(ReflectResourceId.getLayoutId(context,
				Resource.layout.bjmgf_sdk_phone_register_page), context,
				manager, dialog);
	}

	@Override
	public void onCreateView(View view) {
		mPhoneRegisterButton = (Button) view.findViewById(ReflectResourceId
				.getViewId(context,
						Resource.id.bjmgf_sdk_phone_register_buttonId));
		mPhoneNumEditText = (EditText) view.findViewById(ReflectResourceId
				.getViewId(context,
						Resource.id.bjmgf_sdk_phone_register_nameEditTextId));
		mPwdEditText = (ClearEditText) view
				.findViewById(ReflectResourceId
						.getViewId(
								context,
								Resource.id.bjmgf_sdk_phone_register_passwordEditTextId));
		/** 手机注册按钮 */
		mPhoneRegisterButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!StringUtility.checkPasswordValid(context, mPwdEditText.getEditText())) {
					return;
				}
				if (getParams().get("mobile").toString().equals(mPwdEditText.getEditText())) {
					return;
				}
				showProgressDialog();
				mIAccountMobileRegristerPresenter.checkVerifyCode(context, getParams().get("mobile").toString(), getParams().get("verifyCode").toString(), mPwdEditText.getEdit().toString(), MOBILE_REG_STATUS);
/*				communicator.sendRequest(
						BaseRequestSession.Request_Mobile_Register,
						mPhoneNumEditText.getText().toString(),
						mPwdEditText.getEditText());*/
			}
		});
		super.onCreateView(view);
	}

	@Override
	public void setView() {

		mIAccountMobileRegristerPresenter = new AccountMobileRegisterImpl(context, this);
		mPhoneNumEditText.setText(StringUtility.transformPhoneNumberType(getParams().get("mobile").toString()));

		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				if (bjmgfData.isByRegister) {
//					manager.backToPage(bjmgfData.baseDialogPage);
//				} else {
//					manager.backToTopPage();
//				}
				manager.backToTopPage();
			}
		});
	}

	@Override
	public void MobileVerifyCodeSuccess(String obj) {
		LogProxy.i(TAG, "MobileVerifyCodeSuccess : " + obj);
	}

	@Override
	public void showError(String message) {

	}

	@Override
	public void showSuccess() {

	}

	// @Override
	// public void goBack() {
	// manager.previousPage();
	// }

/*	public void onEventMainThread(LoginRevEvent mobileRegisterEvent) {
		if (mobileRegisterEvent.getRequestType() == BaseRequestSession.Request_Mobile_Register) {
			LogProxy.i(TAG,
					"mobileRegisterEvent " + mobileRegisterEvent.isSuccess());
			dismissProgressDialog();
			if (mobileRegisterEvent.isSuccess()) {
				ColUtil.collectSDKLogin(BJMGFGlobalData.COL_PHONELOGIN);
				openWelcomePage();
			}
		}
	}*/
}
