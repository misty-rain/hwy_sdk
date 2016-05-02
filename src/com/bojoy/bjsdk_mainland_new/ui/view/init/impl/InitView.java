package com.bojoy.bjsdk_mainland_new.ui.view.init.impl;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.*;
import com.bojoy.bjsdk_mainland_new.app.tools.BJMGFSDKTools;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseReceiveEvent;
import com.bojoy.bjsdk_mainland_new.presenter.account.IAccountPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.account.impl.AccountPresenterImpl;
import com.bojoy.bjsdk_mainland_new.presenter.init.IInitPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.init.impl.InitSDKPresenterImpl;
import com.bojoy.bjsdk_mainland_new.support.eventbus.EventBus;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseDialogPage;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.view.init.IInitView;
import com.bojoy.bjsdk_mainland_new.ui.view.login.impl.AccountLoginListView;
import com.bojoy.bjsdk_mainland_new.ui.view.login.impl.AccountLoginView;
import com.bojoy.bjsdk_mainland_new.utils.*;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMGFDialog;

/**
 * Created by wutao on 2015/12/21.
 */
public class InitView extends BaseDialogPage implements IInitView {

    private final String TAG = InitView.class.getSimpleName();

    private LinearLayout mUpdateLinearLayout;
    private TextView mTextView;
    private ProgressBar mProgressBar;
    private Button mButtonByOne, mUpdateButton, mUpdate2Button, mNotUpdateButton, mNotUpdate2Button;
    private EventBus eventBus = EventBus.getDefault();


    private Handler handler = new Handler();
    private IInitPresenter iInitPresenter;

    public InitView(Context context, PageManager manager, BJMGFDialog dialog) {
        super(ReflectResourceId.getLayoutId(context, Resource.layout.bjmgf_sdk_init_page),
                context, manager, dialog);
        eventBus.register(this);
    }


    @Override
    public void onCreateView(View view) {
        mTextView = (TextView) view.findViewById(ReflectResourceId.getViewId(context,
                Resource.id.bjmgf_sdk_init_dialog_textView));
        mProgressBar = (ProgressBar) view.findViewById(ReflectResourceId.getViewId(context,
                Resource.id.bjmgf_sdk_init_dialog_progressBar));
        mButtonByOne = (Button) view.findViewById(ReflectResourceId.getViewId(context,
                Resource.id.bjmgf_sdk_init_dialog_BtnByone));
        mUpdateLinearLayout = (LinearLayout) view.findViewById(ReflectResourceId.getViewId(context,
                Resource.id.bjmgf_sdk_init_dialog_updateLinearLayout));
        mUpdateButton = (Button) view.findViewById(ReflectResourceId.getViewId(context,
                Resource.id.bjmgf_sdk_init_dialog_updateBtn));
        mNotUpdateButton = (Button) view.findViewById(ReflectResourceId.getViewId(context,
                Resource.id.bjmgf_sdk_init_dialog_notUpdateBtn));
        mUpdate2Button = (Button) view.findViewById(ReflectResourceId.getViewId(context,
                Resource.id.bjmgf_sdk_init_dialog_update2Btn));
        mNotUpdate2Button = (Button) view.findViewById(ReflectResourceId.getViewId(context,
                Resource.id.bjmgf_sdk_init_dialog_notUpdate2Btn));
        /** 检查网络、强制关闭按钮  */
        mButtonByOne.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mButtonByOne.getText().toString().equalsIgnoreCase(
                        context.getResources().getString(ReflectResourceId.getStringId(context,
                                Resource.string.bjmgf_sdk_init_dialog_checknet_btnStr)))) {
                    /** ?暂时先关闭初始化对话框 */

                    quit();
                } else {
                    closeApp();
                }
            }
        });
        /** 非强制性更新按钮 */
        mUpdateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mUpdateButton.getText().toString().equalsIgnoreCase(
                        context.getResources().getString(ReflectResourceId.getStringId(context,
                                Resource.string.bjmgf_sdk_init_dialog_updateVersion_btnStr)))) {
                    if (Utility.isWifi(context)) {
                        downloadApk();
                    } else {
                        mUpdateButton.setText(ReflectResourceId.getStringId(context,
                                Resource.string.bjmgf_sdk_init_dialog_continue_updateVersion_btnStr));
                        mTextView.setText(ReflectResourceId.getStringId(context,
                                Resource.string.bjmgf_sdk_init_dialog_isNotWifiNet_TxtViewStr));
                        mNotUpdateButton.setVisibility(View.GONE);
                        mNotUpdate2Button.setVisibility(View.VISIBLE);
                    }
                } else {
                    downloadApk();
                }
            }
        });
        /** 强制更新按钮 */
        mUpdate2Button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                downloadApk();
            }
        });
        /** 强制更新取消按钮、非强制性更新取消按钮 */
        mNotUpdateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mNotUpdateButton.getText().toString().equalsIgnoreCase(
                        context.getResources().getString(ReflectResourceId.getStringId(context,
                                Resource.string.bjmgf_sdk_init_dialog_closeAppStr)))) {
                    closeApp();
                } else {
                    //eventBus.post(new BJMGFSdkEvent(BJMGFSdkEvent.APP_INIT_SUCCESS));//通知游戏登录
                    quit();
                }
            }
        });
        /** 非强制更新取消按钮 */
        mNotUpdate2Button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //eventBus.post(new BJMGFSdkEvent(BJMGFSdkEvent.APP_INIT_SUCCESS));//通知游戏登录
                quit();
            }
        });
        super.onCreateView(view);

//		LogProxy.i(TAG, "device=" + BJMGFCommon.getDevice() + ", version=" + BJMGFCommon.getSystemVersion() +
//				", model=" + BJMGFCommon.getModel() + ", mac=" + BJMGFCommon.getMac());
    }


    private void setInitView() {
        /** 网络不可用 **/
        if (!Utility.isConnected(context)) {
            //if (!BJMFoundationHelpler.isNetworkAvailable()) {
            /** 联网的应用或者游戏 **/
            if (BJMGFSDKTools.getInstance().isOnline()) {
                mTextView.setText(ReflectResourceId.getStringId(context, Resource.string.bjmgf_sdk_netWorkErrorStr));
                mButtonByOne.setText(ReflectResourceId.getStringId(context, Resource.string.bjmgf_sdk_init_dialog_checknet_btnStr));
                openOneButtonView();

            } else {
                /**
                 * 延时关闭，因为此消息是在dialog.onCreate的时候抛出来
                 */
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                      //  eventBus.post(new BJMGFSdkEvent(BJMGFSdkEvent.App_Init_Offline));
                        quit();
                    }
                }, 1000);
            }
        }

    }


    @Override
    public void setView() {
        setInitView();
        iInitPresenter=new InitSDKPresenterImpl(context,this);
    }


    @Override
    public void hideInput() {

    }

    @Override
    public void goBack() {

    }

    @Override
    public void quit() {

    }

    @Override
    protected void setTitle() {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        eventBus.unregister(this);
    }

    /**
     * 根据eventbus post的事件作出相应处理
     *
     * @param revEvent 返回事件
     */
    public void onEventMainThread(BaseReceiveEvent revEvent) {
        if (revEvent.getFlag() == BaseReceiveEvent.Flag_Success) {
            //iInitPresenter.appCheck(context);
            if (AccountSharePUtils.getLocalAccountList(context).size() > 0) {
                dialog.cancel();
                IAccountPresenter iAccountPresenter = new AccountPresenterImpl(context, this);
                iAccountPresenter.autoLogin(context);
            } else {
                setAccountLoginView();
            }
        } else {
            dialog.cancel();
            showError((String) revEvent.getRespMsg());

        }
    }






    /**
     * 显示登陆视图
     */
    @Override
    public void setAccountLoginView() {
        BaseDialogPage dialogPage=null;
        if(AccountSharePUtils.getAll(context).size()>0) {
            dialogPage = new AccountLoginListView(context, manager, dialog);
        }else{
            dialogPage =new AccountLoginView(context,manager,dialog);
        }
        manager.clearTopPage(dialogPage);
    }

    @Override
    public void closeApp() {

    }

    @Override
    public void downloadApk() {

    }

    @Override
    public void openUpdateView() {
        mTextView.setText(ReflectResourceId.getStringId(context,
                Resource.string.bjmgf_sdk_init_dialog_autologin));
        mProgressBar.setVisibility(View.VISIBLE);
        mButtonByOne.setVisibility(View.GONE);
        mUpdateLinearLayout.setVisibility(View.GONE);
    }


    @Override
    public void showError(String message) {
        ToastUtil.showMessage(context, message);
    }

    @Override
    public void showSuccess() {

    }

    private void openOneButtonView() {
        mProgressBar.setVisibility(View.GONE);
        mButtonByOne.setVisibility(View.VISIBLE);
        mUpdateLinearLayout.setVisibility(View.GONE);
    }



}
