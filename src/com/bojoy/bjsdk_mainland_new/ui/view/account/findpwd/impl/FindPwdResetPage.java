package com.bojoy.bjsdk_mainland_new.ui.view.account.findpwd.impl;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.presenter.account.findpwd.IFindPswResetPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.account.findpwd.impl.FindPswResetPresenterImpl;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseDialogPage;
import com.bojoy.bjsdk_mainland_new.ui.view.IBaseView;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;
import com.bojoy.bjsdk_mainland_new.utils.ToastUtil;
import com.bojoy.bjsdk_mainland_new.widget.ClearEditText;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMGFDialog;

/**
 * 
 * @author xuxiaobin FindPwdResetPage 找回密码重置页面
 * 
 */
public class FindPwdResetPage extends BaseDialogPage implements IBaseView {

	private final String TAG = FindPwdResetPage.class.getSimpleName();

	IFindPswResetPresenter mFindPswResetPresenter;

	private Button mResetPwdButton;
	private TextView mPhoneNumEditText;
	private ClearEditText mPwdEditText;

	public FindPwdResetPage(Context context, PageManager manager,
			BJMGFDialog dialog) {
		super(ReflectResourceId.getLayoutId(context,
				Resource.layout.bjmgf_sdk_find_password_reset_page), context,
				manager, dialog);
	}

	@Override
	public void onCreateView(View view) {
		mResetPwdButton = (Button) view.findViewById(ReflectResourceId
				.getViewId(context, Resource.id.bjmgf_sdk_findPwd_buttonId));
		mPhoneNumEditText = (TextView) view.findViewById(ReflectResourceId
				.getViewId(context,
						Resource.id.bjmgf_sdk_findPwd_nameEditTextId));
		mPwdEditText = (ClearEditText) view.findViewById(ReflectResourceId
				.getViewId(context,
						Resource.id.bjmgf_sdk_findPwd_passwordEditTextId));
		/** 重置密码 */
		mResetPwdButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!StringUtility.checkPasswordValid(context,
						mPwdEditText.getEditText())) {
					return;
				}
				// if (checkAccountEqualPassword(BJMGFCommon.getPassport(),
				// mPwdEditText.getEditText())) {
				// return;
				// } // by zhouhonghai
				showProgressDialog();
				mFindPswResetPresenter.findPswReset(context,
						(String) getParams().get("mobileKey"),
						(String) getParams().get("checkCode"),
						mPwdEditText.getEditText());
			}
		});

		super.onCreateView(view);
	}

	@Override
	public void setView() {
		String hint = mPhoneNumEditText.getText().toString();

		mFindPswResetPresenter = new FindPswResetPresenterImpl(context, this);
		mPhoneNumEditText.setText(String.format(hint, StringUtility
				.getMoblieKey((String) getParams().get("mobileKey"), 1)));

		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				manager.backToTopPage();
			}
		});
	}

	@Override
	public void showError(String message) {
		dismissProgressDialog();
		LogProxy.i(TAG, message);
	}

	@Override
	public void showSuccess() {
		dismissProgressDialog();
		ToastUtil
				.showMessage(
						context,
						getString(Resource.string.bjmgf_sdk_find_password_resetSuccessedTextViewStr));
		manager.clean();
		dialog.dismiss();
	}

	// public void onEventMainThread(BaseReceiveEvent resetPwdEvent) {
	// if (resetPwdEvent.getRequestType() ==
	// BaseRequestSession.Request_Reset_Password) {
	// LogProxy.i(TAG, "password reset " + resetPwdEvent.isSuccess());
	// dismissProgressDialog();
	// if (resetPwdEvent.isSuccess()) {
	// showToast(getString(Resource.string.bjmgf_sdk_find_password_resetSuccessedTextViewStr));
	// if (!bjmgfData.isNeedExit) {
	// manager.backToTopPage();
	// } else {
	// bjmgfData.isNeedExit = false;
	// quit();
	// manager.clean();
	// BJMGFSdk.getDefault().logoutOnlySDK();
	// }
	// }
	// }
	// }
}
