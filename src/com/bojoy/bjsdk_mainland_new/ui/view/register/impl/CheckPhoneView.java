package com.bojoy.bjsdk_mainland_new.ui.view.register.impl;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.presenter.account.IAccountMobileRegristerPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.account.impl.AccountMobileRegisterImpl;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseDialogPage;
import com.bojoy.bjsdk_mainland_new.ui.view.register.IAskMobileVerifyCodeView;
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
 * 
 * @author xuxiaobin CheckPhoneView 校验手机号和验证码页面
 * 
 */
public class CheckPhoneView extends BaseDialogPage implements IAskMobileVerifyCodeView {

	private final String TAG = CheckPhoneView.class.getSimpleName();

	private static int MOBILE_REG_STATUS = 1;

	private ClearEditText mCheckCodeEditText;
	private TextView mPhoneNumText;
	private Button mCheckButton;
	protected Button mResetButton;

	private int mResetTime = 60;
	private final int Reset_Max_Timeout = mResetTime * 1000;
	private final int Reset_Period = 1000;
	private PollingTimeoutTask pollingTask;

	IAccountMobileRegristerPresenter mIAccountMobileRegristerPresenter = null;

	public CheckPhoneView(Context context, PageManager manager,
						  BJMGFDialog dialog) {
		super(ReflectResourceId.getLayoutId(context,
				Resource.layout.bjmgf_sdk_check_phone_page), context, manager,
				dialog);
	}

	public CheckPhoneView(int layoutId, Context context, PageManager manager,
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

		mIAccountMobileRegristerPresenter = new AccountMobileRegisterImpl(context,this);

		/** 验证手机号码和验证码按钮 */
		mCheckButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (StringUtility.checkVerifyCodeLength(mCheckCodeEditText
						.getEditText())) {
					mIAccountMobileRegristerPresenter.checkVerifyCode(context, getParams().get("mobile").toString(),
					 mCheckCodeEditText.getEditText(),"", MOBILE_REG_STATUS);
				} else {
					ToastUtil.showMessage(context,getString(Resource.string.bjmgf_sdk_register_dialog_checkVerifyCodeErrorStr));
				}
				/** test phone register view */
				// PhoneRegisterView phoneRegisterPage = new
				// PhoneRegisterView(context, manager, dialog);
				// manager.replacePage(phoneRegisterPage);
			}
		});
		/** 重发验证码按钮 */
		mResetButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// ##showProgressDialog();
				setPollingResetStart();
				mIAccountMobileRegristerPresenter.sendPhoneCode(context, getParams().get("mobile").toString());
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
	// PhoneRegisterView phoneRegisterPage = new PhoneRegisterView(context,
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
		ToastUtil.showMessage(context, message);
	}

	@Override
	public void showSuccess() {

	}

	@Override
	public void MobileVerifyCodeSuccess(String obj) {
		suspendPolling();
		PhoneRegisterView phoneRegisterPage = new PhoneRegisterView(context, manager, dialog);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mobile", getParams().get("mobile").toString());
		params.put("verifyCode", mCheckCodeEditText.getEditText());
		phoneRegisterPage.putParams(params);
		manager.replacePage(phoneRegisterPage);
	}
}
