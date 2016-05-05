package com.bojoy.bjsdk_mainland_new.ui.view.account.bindphone.impl;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.net.RequestUtils;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.presenter.account.IAccountCenterPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.account.impl.AccountCenterPresenterImpl;
import com.bojoy.bjsdk_mainland_new.support.db.SmsContentObserver;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseActivityPage;
import com.bojoy.bjsdk_mainland_new.ui.view.IBaseView;
import com.bojoy.bjsdk_mainland_new.ui.view.account.bindphone.IModifyBindPhoneView;
import com.bojoy.bjsdk_mainland_new.ui.view.account.impl.AccountCenterView;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.PollingTaskUtil;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;
import com.bojoy.bjsdk_mainland_new.utils.ToastUtil;
import com.bojoy.bjsdk_mainland_new.widget.ClearEditText;

import java.util.Map;

/**
 * Created by wutao on 2016/1/18.
 * 修改绑定手机号码
 */
public class ModifyBindPhoneView extends BaseActivityPage implements IModifyBindPhoneView {

    private final String TAG = ModifyBindPhoneView.class.getSimpleName();
    private RelativeLayout mBackLayout = null;
    private Button mGetVerifyCodeButton;
    private ClearEditText mVerifyEdit;
    private TextView mNextTextView, mBindOldNumberTextView, toastTextView;
    private View keyboardView;
    private IAccountCenterPresenter iAccountCenterPresenter;
    //短信监听数据器
    private SmsContentObserver content;
    private String phoneNum = "";
    private int resendTime = SysConstant.SMS_LIFE_CYCLE;

    /**
     * 短信Handler
     */
    protected Handler smsHandler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    LogProxy.d(TAG, (String) message.obj);
                    mVerifyEdit.setEditText((String) message.obj);
                    break;
            }
        }
    };


    public ModifyBindPhoneView(Context context, PageManager manager,
                               BJMGFActivity activity) {
        super(ReflectResourceId.getLayoutId(context,
                  Resource.layout.bjmgf_sdk_dock_account_modifyphone), context,
                  manager, activity);
    }

    public void onCreateView(View view) {
        mVerifyEdit = (ClearEditText) view.findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_float_account_manager_modifyphone_VerifyEditTextId));
        mBackLayout = (RelativeLayout) view.findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_float_account_manager_modifyphone_backLlId));
        mGetVerifyCodeButton = (Button) view.findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_send_verifyCodeBtnId));
        toastTextView = (TextView) view.findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_account_manager_Tips_To_Old_Number_Id));
        mNextTextView = (TextView) view.findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_account_manager_modifyphone_verifyCode_nextId));
        mBindOldNumberTextView = (TextView) view.findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_account_manager_Bind_Mobile_Old_Number_Id));
        keyboardView = (View) view.findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_float_account_manager_modifyphone_keyboard));

        mBackLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                initPollingButton(false);
                PollingTaskUtil.getDefault().suspendGlobalPolling();
                manager.previousPage();
            }
        });

        mGetVerifyCodeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                resendTime = 60 * 1;
                showProgressDialog();
                iAccountCenterPresenter.getSmsCodeByUnBindPhone(context);
                PollingTaskUtil.getDefault().startPollingTask(SysConstant.SMS_POLLING_PERIOD_TIME, SysConstant.SMS_POLLING_DELAY_TIME, SysConstant.SMS_POLLING_MAX_TIME, TAG);
                initPollingButton(true);
            }
        });

        //验证按钮
        mNextTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (validateText()) {
                    showProgressDialog();
                    iAccountCenterPresenter.validateCodeForUnBindPhone(context, mVerifyEdit.getEditText());
                }


            }
        });

        //注册短信监听
        content = new SmsContentObserver(activity, smsHandler);
        activity.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, content);

        super.onCreateView(view);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //注销短信监听
        activity.getContentResolver().unregisterContentObserver(content);
    }

    @Override
    public void setView() {
        iAccountCenterPresenter = new AccountCenterPresenterImpl(context, this);
        Map<String, Object> maps = getParams();
        phoneNum = maps.get("phone").toString();
        mBindOldNumberTextView.setText(phoneNum);
        String str = toastTextView.getText().toString();
        toastTextView.setText(String.format(str, phoneNum));
        initPollingButton(PollingTaskUtil.getDefault().isStart(TAG));

    }

    /**
     * 验证edittext
     *
     * @return
     */
    private boolean validateText() {
        if (StringUtility.isEmpty(mVerifyEdit.getEditText())) {
            showError(getString(Resource.string.bjmgf_sdk_register_dialog_checkVerifyCode_null_ErrorStr));
            return false;

        } else {
            if (mVerifyEdit.getEditText().length() == 6) {
                return true;
            } else {
                showError(getString(Resource.string.bjmgf_sdk_find_password_inputCheckCodeContentEditTextStr));
                return false;
            }
        }
    }

    public void onEventMainThread(PollingTaskUtil.PollingExecuteEvent executeEvent) {
        if (executeEvent.getTaskTag().equals(TAG)) {
            resendTime--;
            mGetVerifyCodeButton.setText(resetTimeText());
        }
    }

    private String resetTimeText() {
        return String.format(context.getResources().getString(ReflectResourceId.getStringId(context,
                  Resource.string.bjmgf_sdk_floatWindow_accountManager_checkPhone_receiveCodeToastRightStr)), resendTime);
    }

    public void onEventMainThread(PollingTaskUtil.PollingTimeoutEvent timeoutEvent) {
        if (timeoutEvent.getTaskTag().equals(TAG)) {
            mGetVerifyCodeButton.setText(getString(Resource.string.bjmgf_sdk_floatWindow_accountManager_resendStr));
            initPollingButton(false);
        }
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

    private void initPollingButton(boolean start) {
        mGetVerifyCodeButton.setEnabled(!start);
        mGetVerifyCodeButton.setBackgroundResource(start ? ReflectResourceId.getDrawableId(
                  context, Resource.drawable.bjmgf_sdk_btn_red_not_enable_small) :
                  ReflectResourceId.getDrawableId(
                            context, Resource.drawable.bjmgf_sdk_blue_button_small_selector));
    }

    @Override
    public void unbindPhoneCheckVerifyCodeSuccess(String message) {
        dismissProgressDialog();
        PollingTaskUtil.getDefault().suspendGlobalPolling();
        GetBindPhoneSmsCodeView bindPhoneView = new GetBindPhoneSmsCodeView(context, manager, activity);
        manager.replacePage(bindPhoneView);
    }
}
