package com.bojoy.bjsdk_mainland_new.ui.view.init.impl;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.app.tools.BJMGFSDKTools;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BJMGFSdkEvent;
import com.bojoy.bjsdk_mainland_new.presenter.account.IAccountPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.account.impl.AccountPresenterImpl;
import com.bojoy.bjsdk_mainland_new.support.eventbus.EventBus;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseDialogPage;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.utils.*;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMGFDialog;
import com.bojoy.bjsdk_mainland_new.widget.dialog.WelComeDialog;

import okhttp3.internal.Util;

/**
 * Created by wutao on 2015/12/24.
 */
public class WelcomePage extends BaseDialogPage {

    private final String TAG = WelcomePage.class.getCanonicalName();
    EventBus eventBus = EventBus.getDefault();
    private TextView mTextView;
    private Handler handler = new Handler();
    private IAccountPresenter accountPresenter;

    private static final int DURATION = 3000;

    //	EventBus eventBus = EventBus.getDefault();
    private Runnable runnable;

    public WelcomePage(Context context, PageManager manager, WelComeDialog dialog) {
        super(ReflectResourceId.getLayoutId(context, Resource.layout.bjmgf_sdk_welcome_page),
                  context, manager, dialog);
    }

    @Override
    public void onCreateView(View view) {
        LogProxy.d("", "invalidateSystem()=" + Utility.invalidateSystem());
        mTextView = (TextView) view.findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_comeBack_login_dialog_textViewId));

        super.onCreateView(view);
        runnable = new Runnable() {

            @Override
            public void run() {
                showNextPage();
            }

        };
        handler.postDelayed(runnable, DURATION);
    }

    public void showNextPage() {
        hideWelcomeShowDock();
        if (Utility.invalidateSystem().equals("V5")) {
            //V5判断之前是否点击过应用详情页面
            if (!SpUtil.getBooleanValue(context, SysConstant.MIUI_WARN_FLAG, false)) {
                showGuidePage();
            }
        } else if (Utility.invalidateSystem().equals("V6")) {
            showGuidePage();
        }
        getWarn();
    }

    public void hide(BJMGFDialog bjmgfDialog) {
        handler.removeCallbacks(runnable);
        hideWelcomeShowDock();
        if (Utility.invalidateSystem().equals("V5")) {
            //V5判断之前是否点击过应用详情页面
            if (!SpUtil.getBooleanValue(context, SysConstant.MIUI_WARN_FLAG, false))
                showGuidePage();
        } else if (Utility.invalidateSystem().equals("V6")) {
            if (!SpUtil.getBooleanValue(context, SysConstant.MIUI_WARN_FLAG, false))
                showGuidePage();
        } else if (Utility.invalidateSystem().equals("V7")) {
            if (!SpUtil.getBooleanValue(context, SysConstant.MIUI_WARN_FLAG, false))
                showGuidePage();
        }
        getWarn();
    }

    @Override
    public boolean canBack() {
        eventBus.post(new BJMGFSdkEvent(BJMGFSdkEvent.APP_WELCOME_SHOW));
        return true;
    }

    /**
     * 隐藏欢迎页 显示悬浮窗
     */
    public void hideWelcomeShowDock() {
        quit();
        eventBus.post(new BJMGFSdkEvent(BJMGFSdkEvent.APP_WELCOME_SHOW));
        eventBus.post(new BJMGFSdkEvent(BJMGFSdkEvent.APP_LOGIN_SUCCESS));
    }


    @Override
    public void setView() {
        String userName = BJMGFSDKTools.getInstance().getCurrentPassPort().getPp();
        if (BJMGFSDKTools.getInstance().isRegister) {
            BJMGFSDKTools.getInstance().isRegister = false;
            mTextView.setText(String.format(getString(Resource.string.bjmgf_sdk_login_dialog_welcomeRegisterTextViewStr),
                      userName
            ));
        } else {
            mTextView.setText(String.format(getString(Resource.string.bjmgf_sdk_login_dialog_welcomeAgainTextViewStr),
                      userName
            ));
        }

    }

    @Override
    public void onResume() {
        dialog.setCancelable(true);
    }

    /**
     * 弹出引导页面
     */
    private void showGuidePage() {
        //判断当前是否打开悬浮窗
        if (!Utility.isSystemAlertWindowOpAllowed(getContext())) {
            dialog.dismiss();
            MiuiGuideDialog guideDialog = new MiuiGuideDialog(context, BJMGFDialog.Page_MiuiGuide);
            guideDialog.show();
        }
    }

    /**
     * 获取提示
     */
    private void getWarn() {
        boolean flag = true;
        //增加游客试玩转正提醒弹窗
        if (WarnUtil.isShowTryWarn(context)) {
            flag = false;
            WarnUtil.showTryWarnDialog(context);
        }
        //增加实名认证提醒
        if (flag) {
            if (WarnUtil.isShowAuthentWarn(context)) {
                flag = false;
                WarnUtil.showAuthentWarnDialog(context);
            }
        }
        //增加绑定提醒弹窗
        if (flag) {
            if (WarnUtil.isShowBindWarn(context)) {
                //bjmgfData.isNeedQuit = true;
                WarnUtil.showBindWarnDialog(context);
            }
        }
    }
}
