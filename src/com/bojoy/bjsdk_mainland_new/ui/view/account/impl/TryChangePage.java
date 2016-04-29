package com.bojoy.bjsdk_mainland_new.ui.view.account.impl;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bojoy.bjsdk_mainland_new.app.tools.BJMGFSDKTools;
import com.bojoy.bjsdk_mainland_new.presenter.account.IAccountPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.account.impl.AccountPresenterImpl;
import com.bojoy.bjsdk_mainland_new.support.eventbus.EventBus;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
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
 * @author zhouhonghai
 * TryChangePage 试玩账号新账号密码设置界面
 */
public class TryChangePage extends BaseDialogPage implements IBaseView {
	
	@SuppressWarnings("unused")
	private final String TAG = TryChangePage.class.getSimpleName();
	
	private EventBus eventBus = EventBus.getDefault();
	
//	/**
//	 * 提示文字
//	 */
//	private TextView mPromptTextView = null;
	private Button mSubmitBtn;
	private ClearEditText mAccountEditText, mPwdEditText;

	private TextView changeAccountTextView;
	IAccountPresenter iAccountPresenter;
	
	public TryChangePage(Context context, PageManager manager, BJMGFDialog dialog) {
		super(ReflectResourceId.getLayoutId(context, Resource.layout.bjmgf_sdk_account_try_user_modifypp_page),
				context, manager, dialog);
	}

	@Override
	public void onCreateView(View view) {
//		mPromptTextView = (TextView)view.findViewById(ReflectResourceId.getViewId(context, 
//				Resource.id.bjmgf_sdk_account_try_user_modifypp_promptId));
		mSubmitBtn = (Button)view.findViewById(ReflectResourceId.getViewId(context, 
				Resource.id.bjmgf_sdk_account_try_user_modifypp_buttonId));
		mAccountEditText = (ClearEditText)view.findViewById(ReflectResourceId.getViewId(context, 
				Resource.id.bjmgf_sdk_account_try_user_modifypp_nameEditTextId));
		mPwdEditText = (ClearEditText)view.findViewById(ReflectResourceId.getViewId(context, 
				Resource.id.bjmgf_sdk_account_try_user_modifypp_passwordEditTextId));
		changeAccountTextView = (TextView)view.findViewById(ReflectResourceId.getViewId(context, 
				Resource.id.bjmgf_sdk_account_try_user_modifypp_changeAccontId));
		
		mSubmitBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				/** 手机号也可以登录 **/	
				if (!(StringUtility.checkAccountNameValid(context, mAccountEditText.getEditText())&&StringUtility.checkPasswordValid(context,
						mPwdEditText.getEditText()))) {
					return;
				}
				showProgressDialog();
				LogProxy.d(TAG, "mAccountEditText.getEditText()=" + mAccountEditText.getEditText() + "\n" + "mPwdEditText.getEditText()=" + mPwdEditText.getEditText());
//				bjmgfData.setTempPassport(mAccountEditText.getEditText());
//				bjmgfData.setTempPwd(mPwdEditText.getEditText());
				iAccountPresenter.tryChange(context,mAccountEditText.getEditText(),mPwdEditText.getEditText());
//				communicator.sendRequest(BaseRequestSession.Request_Try_change, mAccountEditText.getEditText(),
//						mPwdEditText.getEditText());
			}
		});
		//切换账号
		changeAccountTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				BJMGFSDKTools.getInstance().setCurrUserStatusOnLine(false);
				quit();
				Toast.makeText(context, context.getResources().getString(
						ReflectResourceId.getStringId(context, Resource.string.bjmgf_sdk_switch_account_success)), 
						Toast.LENGTH_SHORT).show();
			}
		});
		
//		pageView.setOnTouchListener(new OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				if (v.equals(pageView) && event.getAction() == MotionEvent.ACTION_DOWN) {
//					LogProxy.d(TAG, "onTouch pageView");
//					quit();
//				}
//				return false;
//			}
//		});
		super.onCreateView(view);
	}

	@Override
	public void setView() {

		hideBack();
		iAccountPresenter = new AccountPresenterImpl(context, this);
	}
	
	@Override
	public boolean canBack() {
		return true;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
/*	public void onEventMainThread(BaseReceiveEvent event) {
		if (event.getRequestType() == BaseRequestSession.Request_Try_change) {
			dismissProgressDialog();
			LogProxy.d(TAG, "xxxx msg = " + event.getRespMsg());
			if(event.isSuccess()) {
//				eventBus.post(new BJMGFSdkEvent(BJMGFSdkEvent.APP_POSITIVE_USER));//通知代理 更换游戏中的Token
//				BJMGFSdk.getDefault().getDockManagerBeta().openDock();
//				showToastCenter(getString(Resource.string.bjmgf_sdk_try_change_Success_MsgStr));
				AccountUtil.saveAccount(context);
				showToastCenter(getString(Resource.string.bjmgf_sdk_trychange_restart));
				quit();
				ColUtil.collectSDKLogin(BJMGFGlobalData.COL_CHANGETRY);
				bjmgfData.isNeedSetAccount = false;
//				openDockAccountPage();
				//增加清除本地试玩提醒信息
				WarnUtil.clearTryWarn(context);
				eventBus.post(new BJMGFSdkEvent(BJMGFSdkEvent.APP_RESTART));
			}
		}
	}*/
	
	/**
	 *  打开账户管理界面
	 */
	private void openDockAccountPage() {
		//AccountUtil.saveAccount(context);
		BJMGFActivity.canLandscape = true;
		Intent intent = new Intent(dialog.getActivity(), BJMGFActivity.class);
		intent.putExtra(BJMGFActivity.Page_Class_Name_Key, AccountCenterView.class.getCanonicalName());
		dialog.getActivity().startActivity(intent);
	}


	@Override
	public void showError(String message) {
		dismissProgressDialog();
		ToastUtil.showMessage(context, message);
	}

	@Override
	public void showSuccess() {
		dismissProgressDialog();
		quit();
		ToastUtil.showMessage(context,getString(Resource.string.bjmgf_sdk_trychange_restart));
	}
}
