package com.bojoy.bjsdk_mainland_new.ui.view.account.findpwd.impl;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.bojoy.bjsdk_mainland_new.presenter.account.findpwd.IPswFindByEmailPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.account.findpwd.impl.PswFindByEmailPresenterImpl;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseDialogPage;
import com.bojoy.bjsdk_mainland_new.ui.view.account.findpwd.IPswFindByEmailView;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;
import com.bojoy.bjsdk_mainland_new.utils.ToastUtil;
import com.bojoy.bjsdk_mainland_new.widget.ClearEditText;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMGFDialog;

/**
 * @author zhouhonghai FindPwdEmailPage 邮箱找回密码
 */
public class FindPwdEmailPage extends BaseDialogPage implements
		IPswFindByEmailView {

	@SuppressWarnings("unused")
	private final String TAG = FindPwdEmailPage.class.getSimpleName();

	private ClearEditText mEtPassport, mEtEmail;
	private Button mBtnOk;

	IPswFindByEmailPresenter mIPswFindByEmailPresenter;

	private String passport = "";
	private String email = "";

	public FindPwdEmailPage(Context context, PageManager manager,
			BJMGFDialog dialog) {
		super(ReflectResourceId.getLayoutId(context,
				Resource.layout.bjmgf_sdk_find_pwd_email), context, manager,
				dialog);
	}

	@Override
	public void onCreateView(View view) {
		mEtPassport = (ClearEditText) getView(Resource.id.bjmgf_sdk_find_pwd_email_passport);
		mEtEmail = (ClearEditText) getView(Resource.id.bjmgf_sdk_find_pwd_email);
		mBtnOk = (Button) getView(Resource.id.bjmgf_sdk_find_pwd_email_buttonId);

		super.onCreateView(view);
	}

	@Override
	public void setView() {
		mIPswFindByEmailPresenter = new PswFindByEmailPresenterImpl(context,
				this);
		mBtnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				passport = mEtPassport.getEditText();
				email = mEtEmail.getEditText();
				if (StringUtility.checkAccountNameValid(context, passport)
						&& StringUtility.isEmail(email)) {
					// showProgressDialog();
					// communicator.sendRequest(BaseRequestSession.Request_EmailFindPwd,
					// email, passport);
					mIPswFindByEmailPresenter.getEmailVerifyCode(context,
							passport, email);
				}
			}
		});
	}

	@Override
	public void emailFindSuccess(String email) {
		dismissProgressDialog();
		FindPwdEmailConfirmPage page = new FindPwdEmailConfirmPage(context,
				manager, dialog);
		page.setEmail(email);
		manager.addPage(page);
	}

	@Override
	public void goBack() {
		super.goBack();
	}

	@Override
	public boolean canBack() {
		return false;
	}

	@Override
	public void showError(String message) {
		dismissProgressDialog();
		ToastUtil.showMessage(context, message);
	}

	@Override
	public void showSuccess() {
		dismissProgressDialog();
	}

	// public void onEventMainThread(BaseReceiveEvent event) {
	// if (event.getRequestType() == BaseRequestSession.Request_EmailFindPwd) {
	// dismissProgressDialog();
	// if(event.isSuccess()) {
	// FindPwdEmailConfirmPage page = new FindPwdEmailConfirmPage(context,
	// manager, dialog);
	// page.setEmail(email);
	// manager.addPage(page);
	// }
	// }
	// }
}
