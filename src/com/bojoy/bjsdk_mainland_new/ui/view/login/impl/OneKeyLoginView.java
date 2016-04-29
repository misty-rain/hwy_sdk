package com.bojoy.bjsdk_mainland_new.ui.view.login.impl;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import com.bojoy.bjsdk_mainland_new.presenter.account.IAccountPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.account.impl.AccountPresenterImpl;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseDialogPage;
import com.bojoy.bjsdk_mainland_new.ui.view.IBaseView;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.PollingTimeoutTask;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.SpUtil;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;
import com.bojoy.bjsdk_mainland_new.utils.ToastUtil;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMGFDialog;

import java.util.ArrayList;

/**
 * Created by zhouhonghai on 2016/2/27.
 */
public abstract class OneKeyLoginView extends BaseDialogPage implements IBaseView {

    protected static final String TAG = OneKeyLoginView.class.getSimpleName();

    public OneKeyLoginView(int layoutId, Context context, PageManager manager, BJMGFDialog dialog) {
        super(layoutId, context, manager, dialog);
    }


    protected IAccountPresenter iAccountPresenter;

    protected final String SENT_SMS_ACTION = "sent_sms_action";
    protected final String DELIVERED_SMS_ACTION = "delivered_sms_action";
    protected final int Max_Sms_Timeout = 60000;
    protected final int Sms_Timeout_Perid = 1000;

    protected final int Max_Timeout = 300000;
    protected final int One_Key_Check_Perid_Time = 6000;
    protected PollingTimeoutTask oneKeyCheckPolling = new PollingTimeoutTask(
            One_Key_Check_Perid_Time, One_Key_Check_Perid_Time / 2,
            Max_Timeout, new PollingTimeoutTask.PollingListener() {

        @Override
        public void onTimeout() {
            dismissProgressDialog();
            oneKeyCheckPollingFail();
        }

        @Override
        public void onExecute() {
            LogProxy.i(TAG, "one key check polling onexcute");
            iAccountPresenter.oneKeyRegister(context, SpUtil.getStringValue(context, "uuid", ""));
        }
    });
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
                    LogProxy.d(TAG, "smsTimeoutTask onExecute");
                }
            });

    protected BroadcastReceiver sendMessage = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            LogProxy.i(TAG, "result=" + getResultCode());
            smsTimeoutTask.suspendPolling();
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    oneKeyCheckPolling.startPolling();
                    ToastUtil.showMessage(context, getString(Resource.string.bjmgf_sdk_sendMessageSuccessedStr));
                    break;
                default:
                    dismissProgressDialog();
                    ToastUtil.showMessage(context, getString(Resource.string.bjmgf_sdk_sendMessageFailStr));
                    if (getResultCode() == Activity.RESULT_CANCELED) {
                        // send sms cancel
                        //communicator.sendGameCollectionRequest(BJMGFGlobalData.BJM_GF_GAMECOLLECTION_SMS_SEND_CANCEL);
                    } else {
                        // send sms fail
                        //communicator.sendGameCollectionRequest(BJMGFGlobalData.BJM_GF_GAMECOLLECTION_SMS_SEND_FAIL);
                    }
                    oneKeyCheckPollingFail(false);
                    break;
            }
        }
    };

    protected BroadcastReceiver receiverMessage = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            ToastUtil.showMessage(context, getString(Resource.string.bjmgf_sdk_receiveMessageSuccessedStr));
        }
    };

    protected void oneKeyCheckPollingFail() {
        oneKeyCheckPollingFail(true);
    }

    protected void oneKeyCheckPollingFail(boolean sendCollection) {
        if (sendCollection) {
            // send onekey login fail
            //communicator.sendGameCollectionRequest(BJMGFGlobalData.BJM_GF_GAMECOLLECTION_ONEKEY_LOGIN_FAIL);
        }
        manager.backToTopPage();
    }

    protected void sendSms(String mobile) {
        SmsManager smsManager = SmsManager.getDefault();
        Intent sentIntent = new Intent(SENT_SMS_ACTION);
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
                sentIntent, 0);
        Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
        PendingIntent deliverPI = PendingIntent.getBroadcast(context, 0,
                deliverIntent, 0);

        smsTimeoutTask.startPolling();


        String smsText = StringUtility.getSmsText(context);
        if (smsText.length() > 70) {
            ArrayList<String> msgs = smsManager.divideMessage(smsText);
            for (String msg : msgs) {
                smsManager.sendTextMessage(mobile, null, msg, sentPI, deliverPI);
            }
        } else {
            smsManager.sendTextMessage(mobile, null, smsText, sentPI, deliverPI);
        }
    }

    @Override
    public void setView() {
           iAccountPresenter = new AccountPresenterImpl(context,this);
    }
}
