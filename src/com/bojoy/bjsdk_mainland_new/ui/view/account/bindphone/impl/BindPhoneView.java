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
import com.bojoy.bjsdk_mainland_new.support.eventbus.EventBus;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseActivityPage;
import com.bojoy.bjsdk_mainland_new.ui.view.IBaseView;
import com.bojoy.bjsdk_mainland_new.ui.view.account.impl.AccountCenterView;
import com.bojoy.bjsdk_mainland_new.utils.*;
import com.bojoy.bjsdk_mainland_new.widget.ClearEditText;

/**
 * Created by wutao on 2016/1/15.
 * 绑定 手机
 */
public class BindPhoneView extends BaseActivityPage implements IBaseView {


    private final String TAG = BindPhoneView.class.getSimpleName();

    private int period = 1000, delay = 0, maxTime = 1000 * 60 * 1, resendTime;
    private RelativeLayout mBackLayout = null;
    private ClearEditText mCodeEditText = null;
    private TextView mCountryNumberTextView, mInputBindNumberTextView, mcompleteTextView;
    private Button mGetVerifyCodeButton = null;
    private View keyboardView;
    private String phoneNum = "";
    private IAccountCenterPresenter iAccountCenterPresenter;

    //短信监听数据器
    private SmsContentObserver content;

    /**
     * 短信Handler
     */
    protected Handler smsHandler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what){
                case 1:
                    LogProxy.d(TAG, (String) message.obj);
                    mCodeEditText.setEditText((String) message.obj);
                    break;
            }
        }
    };


    public BindPhoneView(Context context,
                         PageManager manager, BJMGFActivity activity) {
        super(ReflectResourceId.getLayoutId(context,
                Resource.layout.bjmgf_sdk_dock_account_checkphone), context, manager, activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //注销短信监听
        activity.getContentResolver().unregisterContentObserver(content);
    }


    @Override
    public void setView() {
        iAccountCenterPresenter = new AccountCenterPresenterImpl(context,this);
    }

    public void onCreateView(View view) {
        mBackLayout = (RelativeLayout) view.findViewById(ReflectResourceId.getViewId(context,
                Resource.id.bjmgf_sdk_float_account_manager_checkphone_backLlId));
        mCountryNumberTextView = (TextView) view.findViewById(ReflectResourceId.getViewId(context,
                Resource.id.bjmgf_sdk_float_account_manager_checkphone_coumtryNumber_TvId));
        mInputBindNumberTextView = (TextView) view.findViewById(ReflectResourceId.getViewId(context,
                Resource.id.bjmgf_sdk_float_account_manager_checkphone_inputToBindNumber_TvId));
        mcompleteTextView = (TextView) view.findViewById(ReflectResourceId.getViewId(context,
                Resource.id.bjmgf_sdk_account_manager_checkphone_completeId));
        mGetVerifyCodeButton = (Button) view.findViewById(ReflectResourceId.getViewId(context,
                Resource.id.bjmgf_sdk_check_phone_getVerifyCodeBtnId));
        mCodeEditText = (ClearEditText) view.findViewById(ReflectResourceId.getViewId(context,
                Resource.id.bjmgf_sdk_float_account_manager_checkphone_editTextId));
        keyboardView = (View) view.findViewById(ReflectResourceId.getViewId(context,
                Resource.id.bjmgf_sdk_float_account_manager_checkphone_keyboard));

        phoneNum = (String) getParams().get("phoneNum");
        mInputBindNumberTextView.setText(phoneNum);

        //设置倒计时
        initPollingRsendTv(true);
        //返回操作
        mBackLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                manager.previousPage();
            }
        });

        //重新发送验证码
        mGetVerifyCodeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                PollingTaskUtil.getDefault().startPollingTask(SysConstant.SMS_POLLING_PERIOD_TIME,SysConstant.SMS_POLLING_DELAY_TIME, SysConstant.SMS_POLLING_MAX_TIME,TAG);
                showProgressDialog();
                iAccountCenterPresenter.getSmsCodeByBindPhone(context,phoneNum);

            }
        });

        //绑定手机操作
        mcompleteTextView.setOnClickListener(new View.OnClickListener() {
            // 验证码验证
            @Override
            public void onClick(View arg0) {
                if(StringUtility.isEmpty(mCodeEditText.getEditText())){
                    ToastUtil.showMessage(context,getString(Resource.string.bjmgf_sdk_register_dialog_checkVerifyCodeErrorStr));
                    return;
                }
                if(mCodeEditText.getEditText().trim().length() != 6){
                    ToastUtil.showMessage(context,getString(Resource.string.bjmgf_sdk_register_dialog_checkVerifyCodeErrorStr));
                    return;
                }
                showProgressDialog();
                iAccountCenterPresenter.bindPhone(context,phoneNum,mCodeEditText.getEditText().trim());
            }
        });

        //注册短信监听
        content = new SmsContentObserver(activity, smsHandler);
        activity.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, content);

        //开始轮询时间
        PollingTaskUtil.getDefault().startPollingTask(SysConstant.SMS_POLLING_PERIOD_TIME,SysConstant.SMS_POLLING_DELAY_TIME, SysConstant.SMS_POLLING_MAX_TIME,TAG);
        super.onCreateView(view);
    }


    public void onEventMainThread(PollingTaskUtil.PollingExecuteEvent executeEvent) {
        if (executeEvent.getTaskTag().equals(TAG)) {
            resendTime--;
            initPollingRsendTv(true);
            mGetVerifyCodeButton.setText(resetTimeText());
        }
    }


    public void onEventMainThread(PollingTaskUtil.PollingTimeoutEvent timeoutEvent) {
        resendTime = SysConstant.SMS_LIFE_CYCLE;
        if (timeoutEvent.getTaskTag().equals(TAG)) {
            initPollingRsendTv(false);
            mGetVerifyCodeButton.setText(getString(Resource.string.bjmgf_sdk_floatWindow_accountManager_resendStr));
        }
    }

    /**
     * 重置时间框
     * @return
     */
    private String resetTimeText() {
        return String.format(context.getResources().getString(ReflectResourceId.getStringId(context,
                Resource.string.bjmgf_sdk_floatWindow_accountManager_checkPhone_receiveCodeToastRightStr)),
                resendTime);
    }


    /**
     * 设置倒计时
     * @param start 是否开始
     */
    private void initPollingRsendTv(boolean start) {
        mGetVerifyCodeButton.setEnabled(!start);
        mGetVerifyCodeButton.setBackgroundResource(start ? ReflectResourceId.getDrawableId(
                context, Resource.drawable.bjmgf_sdk_btn_red_not_enable_small) :
                ReflectResourceId.getDrawableId(
                        context, Resource.drawable.bjmgf_sdk_blue_button_small_selector));
    }

    @Override
    public void showError(String message) {
        dismissProgressDialog();
        ToastUtil.showMessage(context,message);
    }

    @Override
    public void showSuccess() {
        dismissProgressDialog();
        AccountCenterView page = new AccountCenterView(context, manager, activity);
        manager.clearTopPage(page);
    }
}
