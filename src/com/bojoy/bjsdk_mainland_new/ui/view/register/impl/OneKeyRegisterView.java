package com.bojoy.bjsdk_mainland_new.ui.view.register.impl;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.presenter.account.IAccountPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.account.impl.AccountPresenterImpl;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseDialogPage;
import com.bojoy.bjsdk_mainland_new.ui.view.register.ISmsView;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.PollingTimeoutTask;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.ToastUtil;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMGFDialog;
import com.bojoy.bjsdk_mainland_new.widget.dialog.ProtocolDialog;

/**
 * Created by wutao on 2015/12/28.
 * 一键注册
 */
public class OneKeyRegisterView extends BaseDialogPage implements ISmsView {

    private final String TAG = OneKeyRegisterView.class.getSimpleName();

    private TextView mProtocolTextView, mSmsWarning;
    private Button mOneKeyRegisterButton;
    IAccountPresenter iAccountPresenter;
    private final String SENT_SMS_ACTION = "sent_sms_action";
    private final String DELIVERED_SMS_ACTION = "delivered_sms_action";
    protected final int Max_Sms_Timeout = 60000;
    protected final int Sms_Timeout_Perid = 1000;
    protected PollingTimeoutTask smsTimeoutTask = new PollingTimeoutTask(
            Sms_Timeout_Perid, 0, Max_Sms_Timeout,
            new PollingTimeoutTask.PollingListener() {

                @Override
                public void onTimeout() {
                    LogProxy.d(TAG, "smsTimeoutTask timeout");
                    dismissProgressDialog();
                    ToastUtil.showMessage(context, getString(Resource.string.bjmgf_sdk_sendMessageFailStr));
                    oneKeyCheckPollingFail();
                }

                @Override
                public void onExecute() {

                }
            });

    public OneKeyRegisterView(Context context, PageManager manager, BJMGFDialog dialog) {
        super(ReflectResourceId.getLayoutId(context, Resource.layout.bjmgf_sdk_one_key_register_page),
                context, manager, dialog);
    }


    @Override
    public void setView() {
        iAccountPresenter = new AccountPresenterImpl(context, this);


        mProtocolTextView = (TextView) pageView.findViewById(ReflectResourceId.getViewId(
                context, Resource.id.bjmgf_sdk_oneKey_register_protocolTextViewId));
        mOneKeyRegisterButton = (Button) pageView.findViewById(ReflectResourceId.getViewId(
                context, Resource.id.bjmgf_sdk_oneKey_register_buttonId));
        mSmsWarning = (TextView) pageView.findViewById(ReflectResourceId.getViewId(context,
                Resource.id.bjmgf_sdk_oneKey_login_smsWarn));
        /** 打开好友玩协议 */
        mProtocolTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ProtocolDialog protocolDialog = new ProtocolDialog(context);
                protocolDialog.show();
            }
        });
        /** 一键注册 */
        mOneKeyRegisterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showProgressDialog();
                iAccountPresenter.sendInfo(context);
            }
        });


    }

    @Override
    public void showError(String message) {
        dismissProgressDialog();
        ToastUtil.showMessage(context, message);
    }

    @Override
    public void showSuccess() {

    }

    public void sendSms() {
        SmsManager smsManager = SmsManager.getDefault();
        Intent sentIntent = new Intent(SENT_SMS_ACTION);
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
                sentIntent, 0);
        Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
        PendingIntent deliverPI = PendingIntent.getBroadcast(context, 0,
                deliverIntent, 0);

        smsTimeoutTask.startPolling();

//        if (oneKeyInfoEvent.getSmsText().length() > 70) {
//            ArrayList<String> msgs = smsManager.divideMessage(oneKeyInfoEvent
//                    .getSmsText());
//            for (String msg : msgs) {
//                smsManager.sendTextMessage(
//                        oneKeyInfoEvent.getDestPhoneNumber(), null, msg,
//                        sentPI, deliverPI);
//            }
//        } else {
//            smsManager.sendTextMessage(oneKeyInfoEvent.getDestPhoneNumber(),
//                    null, oneKeyInfoEvent.getSmsText(), sentPI, deliverPI);
//        }
    }

    private void oneKeyCheckPollingFail() {
        manager.backToTopPage();
    }

    @Override
    public void showGetInfoSuccess(String mobile) {

    }
}
