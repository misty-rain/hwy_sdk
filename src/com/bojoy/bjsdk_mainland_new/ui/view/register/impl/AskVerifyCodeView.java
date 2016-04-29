package com.bojoy.bjsdk_mainland_new.ui.view.register.impl;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.bojoy.bjsdk_mainland_new.presenter.account.IAccountMobileRegristerPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.account.impl.AccountMobileRegisterImpl;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseDialogPage;
import com.bojoy.bjsdk_mainland_new.ui.view.register.IAskMobileVerifyCodeView;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;
import com.bojoy.bjsdk_mainland_new.utils.ToastUtil;
import com.bojoy.bjsdk_mainland_new.widget.ClearEditText;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMGFDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author xuxiaobin
 * AskVerifyCodeView 请求验证码页面
 * 
 */
public class AskVerifyCodeView extends BaseDialogPage implements IAskMobileVerifyCodeView{
	
	@SuppressWarnings("unused")
	private final String TAG = AskVerifyCodeView.class.getSimpleName();

	private Button mReceiveCheckCodeButton;
	private ClearEditText mPhoneNumEditText;

	IAccountMobileRegristerPresenter mIAccountMobileRegristerPresenter = null;
	/**
	 * 是否是从一键登录界面进入
	 */
	private boolean isFromOneKeyLogin = false;
	
	public boolean isFromOneKeyLogin() {
		return isFromOneKeyLogin;
	}

	public void setFromOneKeyLogin(boolean isFromOneKeyLogin) {
		this.isFromOneKeyLogin = isFromOneKeyLogin;
	}

	public AskVerifyCodeView(Context context, PageManager manager, BJMGFDialog dialog) {
		super(ReflectResourceId.getLayoutId(context, Resource.layout.bjmgf_sdk_ask_verify_code_page),
				context, manager, dialog);
		isFromOneKeyLogin = false;

	}
	
	@Override
	public void onCreateView(View view) {
		mReceiveCheckCodeButton = (Button)view.findViewById(ReflectResourceId.getViewId(
				context, Resource.id.bjmgf_sdk_checkCode_register_buttonId));
		mPhoneNumEditText = (ClearEditText)view.findViewById(ReflectResourceId.getViewId(
				context, Resource.id.bjmgf_sdk_checkCode_register_nameEditTextId));
		/** 获取验证码 */
		mReceiveCheckCodeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (!StringUtility.checkPhoneNumberFormat(context, mPhoneNumEditText.getEditText())) {
					return;
				}
				showProgressDialog();
				mIAccountMobileRegristerPresenter.sendPhoneCode(context, mPhoneNumEditText.getEditText().toString());

				/** test check phone view */
//				CheckPhoneView checkPhonePage = new CheckPhoneView(context, manager, dialog);
//				manager.addPage(checkPhonePage);
			}
		});
		LogProxy.d(TAG, "isFromOneKeyLogin" + isFromOneKeyLogin);
		super.onCreateView(view);
	}

	@Override
	public void setView() {
		mIAccountMobileRegristerPresenter = new AccountMobileRegisterImpl(context,this);
	}
	
	@Override
	public boolean canBack() {
		isFromOneKeyLogin = false;
		return super.canBack();
	}

	@Override
	public void showError(String message) {
		dismissProgressDialog();
		ToastUtil.showMessage(context, message);
	}

	@Override
	public void showSuccess() {
		dismissProgressDialog();
		CheckPhoneView checkPhonePage = new CheckPhoneView(context, manager, dialog);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mobile", mPhoneNumEditText.getEdit().toString());
		checkPhonePage.putParams(params);
		manager.addPage(checkPhonePage);
	}

	@Override
	public void MobileVerifyCodeSuccess(String obj) {

	}
}
