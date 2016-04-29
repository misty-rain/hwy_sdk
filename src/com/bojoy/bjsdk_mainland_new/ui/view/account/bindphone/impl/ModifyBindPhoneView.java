package com.bojoy.bjsdk_mainland_new.ui.view.account.bindphone.impl;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.presenter.account.IAccountCenterPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.account.impl.AccountCenterPresenterImpl;
import com.bojoy.bjsdk_mainland_new.support.db.SmsContentObserver;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseActivityPage;
import com.bojoy.bjsdk_mainland_new.ui.view.IBaseView;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.PollingTaskUtil;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.widget.ClearEditText;

import java.util.Map;

/**
 * Created by wutao on 2016/1/18.
 * 修改绑定手机号码
 */
public class ModifyBindPhoneView extends BaseActivityPage implements IBaseView {

    private final String TAG = ModifyBindPhoneView.class.getSimpleName();
    private RelativeLayout mBackLayout = null;
    private Button mGetVerifyCodeButton;
    private ClearEditText mVerifyEdit;
    private TextView mNextTextView,mBindOldNumberTextView,toastTextView;
    private View keyboardView;
    private IAccountCenterPresenter iAccountCenterPresenter;
    //短信监听数据器
    private SmsContentObserver content;
    private String phoneNum = "";

    /**
     * 短信Handler
     */
    protected Handler smsHandler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what){
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
        keyboardView = (View)view.findViewById(ReflectResourceId.getViewId(context,
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
                showProgressDialog();
                iAccountCenterPresenter.getSmsCodeByBindPhone(context,mBindOldNumberTextView.getText().toString().trim());
                initPollingButton(true);
                PollingTaskUtil.getDefault().startPollingTask(SysConstant.SMS_POLLING_PERIOD_TIME,SysConstant.SMS_POLLING_DELAY_TIME, SysConstant.SMS_POLLING_MAX_TIME,TAG);
            }
        });

        //验证按钮
        mNextTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


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
        toastTextView.setText(String.format(str,phoneNum));
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void showSuccess() {

    }

    private void initPollingButton(boolean start) {
        mGetVerifyCodeButton.setEnabled(!start);
        mGetVerifyCodeButton.setBackgroundResource(start ? ReflectResourceId.getDrawableId(
                context, Resource.drawable.bjmgf_sdk_btn_red_not_enable_small) :
                ReflectResourceId.getDrawableId(
                        context, Resource.drawable.bjmgf_sdk_blue_button_small_selector));
    }
}
