package com.bojoy.bjsdk_mainland_new.ui.view.account.findpwd.impl;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.presenter.account.findpwd.ICheckVerifyCodePresenter;
import com.bojoy.bjsdk_mainland_new.presenter.account.findpwd.impl.CheckVerifyCodePresenterImpl;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseDialogPage;
import com.bojoy.bjsdk_mainland_new.ui.view.account.findpwd.ICheckVerifyCodeView;
import com.bojoy.bjsdk_mainland_new.utils.PollingTimeoutTask;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;
import com.bojoy.bjsdk_mainland_new.widget.ClearEditText;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMGFDialog;

/**
 * 
 * @author xuxiaobin CheckPhoneView 校验手机号和验证码页面
 * 
 */
public class CheckPhonePage extends BaseDialogPage implements
		ICheckVerifyCodeView {

	private final String TAG = CheckPhonePage.class.getSimpleName();

	private ClearEditText mCheckCodeEditText;
	private TextView mPhoneNumText;
	private Button mCheckButton;
	protected Button mResetButton;

	private int mResetTime = 60;
	private final int Reset_Max_Timeout = mResetTime * 1000;
	private final int Reset_Period = 1000;
	private PollingTimeoutTask pollingTask;

	private ICheckVerifyCodePresenter mICheckVerifyCodeView;

	public CheckPhonePage(Context context, PageManager manager,
			BJMGFDialog dialog) {
		super(ReflectResourceId.getLayoutId(context,
				Resource.layout.bjmgf_sdk_check_phone_page), context, manager,
				dialog);
	}

	public CheckPhonePage(int layoutId, Context context, PageManager manager,
			BJMGFDialog dialog) {
		super(layoutId, context, manager, dialog);
	}

	@Override
	public void onCreateView(View view) {
		super.onCreateView(view);
	}

	@Override
	public void setView() {
		mPhoneNumText = (TextView) pageView.findViewById(ReflectResourceId
				.getViewId(context,
						Resource.id.bjmgf_sdk_check_phone_nameEditTextId));
		mCheckCodeEditText = (ClearEditText) pageView
				.findViewById(ReflectResourceId.getViewId(context,
						Resource.id.bjmgf_sdk_check_phone_verifyCodeEditTextId));
		mCheckButton = (Button) pageView.findViewById(ReflectResourceId
				.getViewId(context,
						Resource.id.bjmgf_sdk_check_phone_sureButtonId));
		mResetButton = (Button) pageView.findViewById(ReflectResourceId
				.getViewId(context,
						Resource.id.bjmgf_sdk_check_phone_resetButtonId));

		mICheckVerifyCodeView = new CheckVerifyCodePresenterImpl(context, this);

		/** 验证手机号码和验证码按钮 */
		mCheckButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (StringUtility.checkVerifyCodeLength(mCheckCodeEditText
						.getEditText())) {
					// mICheckVerifyCodeView.checkVerifyCode(context, mobileKey,
					// mCheckCodeEditText.getEditText());
				} else {
					// ##showToast(getString(Resource.string.bjmgf_sdk_register_dialog_checkVerifyCodeErrorStr));
				}
				/** test phone register view */
				// PhoneRegisterPage phoneRegisterPage = new
				// PhoneRegisterPage(context, manager, dialog);
				// manager.replacePage(phoneRegisterPage);
			}
		});
		/** 重发验证码按钮 */
		mResetButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// ##showProgressDialog();
				setPollingResetStart();
				// ##communicator.sendRequest(BaseRequestSession.Request_Receive_Check_Code);
			}
		});

		// ##mPhoneNumText.setText(mPhoneNumText.getText().toString() +
		// transformPhoneNumberType(BJMGFCommon.getMobile()));
		setPollingResetStart();

		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
	}

	// @Override
	// public void goBack() {
	// if (pollingTask != null) {
	// pollingTask.suspendPolling();
	// }
	// manager.previousPage();
	// }

	public void setPollingResetStart() {
		mResetTime = 60;
		mResetButton.setEnabled(false);
		mResetButton.setBackgroundResource(ReflectResourceId.getDrawableId(
				context, Resource.drawable.bjmgf_sdk_btn_gray_normal_big));
		mResetButton.setText(resetTimeText());
		pollingTask = new PollingTimeoutTask(Reset_Period, 0,
				Reset_Max_Timeout, new PollingTimeoutTask.PollingListener() {

					@Override
					public void onTimeout() {
						mResetButton
								.setText(getString(Resource.string.bjmgf_sdk_ClickRetryStr));
						mResetButton.setEnabled(true);
						mResetButton.setBackgroundResource(ReflectResourceId
								.getDrawableId(
										context,
										Resource.drawable.bjmgf_sdk_red_button_big_selector));
					}

					@Override
					public void onExecute() {
						mResetButton.setText(resetTimeText());
						mResetTime--;
					}
				});
		pollingTask.startPolling();
	}

	protected String resetTimeText() {
		return String
				.format(context
						.getResources()
						.getString(
								ReflectResourceId
										.getStringId(
												context,
												Resource.string.bjmgf_sdk_register_dialog_checkCodeReSetBtnStr),
								mResetTime));
	}

	// ## public void onEventMainThread(BaseReceiveEvent revEvent) {
	// if (revEvent.getRequestType() ==
	// BaseRequestSession.Request_Receive_Check_Code) {
	// dismissProgressDialog();
	// if (revEvent.isSuccess()) {
	// LogProxy.i(TAG, "Wait verify code");
	// }
	// } else
	// if (revEvent.getRequestType() ==
	// BaseRequestSession.Request_Mobile_Register_Check) {
	// dismissProgressDialog();
	// LogProxy.i(TAG, "Molie Register check " + revEvent.isSuccess());
	// if (revEvent.isSuccess()) {
	// suspendPolling();
	// PhoneRegisterPage phoneRegisterPage = new PhoneRegisterPage(context,
	// manager, dialog);
	// manager.replacePage(phoneRegisterPage);
	// }
	// }
	// }

	private void suspendPolling() {
		if (pollingTask != null) {
			pollingTask.suspendPolling();
		}
	}

	@Override
	public void showError(String message) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void showSuccess() {
		// TODO 自动生成的方法存根

	}

	@Override
	public void checkVerifyCodeSuccess() {
		// TODO 自动生成的方法存根

	}

}
