package com.bojoy.bjsdk_mainland_new.ui.view.account.findpwd.impl;



import com.bojoy.bjsdk_mainland_new.app.BJMGFSdk;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseDialogPage;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMGFDialog;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author zhouhonghai
 * FindPwdSplashPage 找回密码首页
 */
public class FindPwdSplashPage extends BaseDialogPage {
	
	@SuppressWarnings("unused")
	private final String TAG = FindPwdSplashPage.class.getSimpleName();
	
	private Button mBtnPhone, mBtnEmail = null;
	
	private TextView mTvFindPersonal = null;
	
	public FindPwdSplashPage(Context context, PageManager manager, BJMGFDialog dialog) {
		super(ReflectResourceId.getLayoutId(context, Resource.layout.bjmgf_sdk_account_forget_pwd_splash), 
				context, manager, dialog);
	}

	@Override
	public void onCreateView(View view) {
		mBtnPhone = (Button) getView(Resource.id.bjmgf_sdk_find_password_splash_phone);
		mBtnEmail = (Button)getView(Resource.id.bjmgf_sdk_find_password_splash_email);
		mTvFindPersonal = (TextView)getView(Resource.id.bjmgf_sdk_find_password_bottom_personal);
		
		super.onCreateView(view);
	}

	@Override
	public void setView() {
		SpannableStringBuilder builder = new SpannableStringBuilder(mTvFindPersonal.getText().toString());  
		ForegroundColorSpan redSpan = new ForegroundColorSpan(context.getResources().getColor(ReflectResourceId.getColorId(context, Resource.color.bjmgf_sdk_find_pwd_personal_blue)));
		builder.setSpan(redSpan, 13, mTvFindPersonal.getText().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mTvFindPersonal.setText(builder); 
		
		mBtnPhone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FindPasswordPage page = new FindPasswordPage(context, manager, dialog);
				manager.addPage(page);
			}
		});
		
		mBtnEmail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FindPwdEmailPage page = new FindPwdEmailPage(context, manager, dialog);
				manager.addPage(page);
			}
		});
	}
	
	@Override
	public void goBack() {
//		if(bjmgfData.isNeedQuit) {
//			bjmgfData.isNeedQuit = false;
//			quit();
//		} else {
//			manager.previousPage();
//		}
	}
}
