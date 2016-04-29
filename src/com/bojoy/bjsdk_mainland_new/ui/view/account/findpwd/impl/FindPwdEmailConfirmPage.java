package com.bojoy.bjsdk_mainland_new.ui.view.account.findpwd.impl;

import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseDialogPage;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMGFDialog;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author zhouhonghai
 * FindPwdEmailConfirmPage 邮箱找回密码确认页面
 */
public class FindPwdEmailConfirmPage extends BaseDialogPage {
	
	@SuppressWarnings("unused")
	private final String TAG = FindPwdEmailConfirmPage.class.getSimpleName();
	
	private String mEmail = "";
	
	private TextView mTvContent;
	private Button mBtnOk;
	
	public FindPwdEmailConfirmPage(Context context, PageManager manager, BJMGFDialog dialog) {
		super(ReflectResourceId.getLayoutId(context, Resource.layout.bjmgf_sdk_find_pwd_email_confirm), 
				context, manager, dialog);
	}

	@Override
	public void onCreateView(View view) {
		mTvContent = (TextView) getView(Resource.id.bjmgf_sdk_find_pwd_email_confirm_text);
		mBtnOk = (Button) getView(Resource.id.bjmgf_sdk_find_pwd_email_confirm_buttonId);
		
		super.onCreateView(view);
	}

	@Override
	public void setView() {
		hideBack();
		
		mTvContent.setText(StringUtility.convertStringFormat(mEmail, getString(Resource.string.bjmgf_sdk_floatWindow_accountManager_bindEmail_resend_str)));
		
		mBtnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				manager.backToTopPage();
			}
		});
	}
	
	@Override
	public void goBack() {
		super.goBack();
	}
	
	@Override
	public boolean canBack() {
		return false;
	}
	
	public void setEmail(String email) {
		mEmail = email;
	}
}
