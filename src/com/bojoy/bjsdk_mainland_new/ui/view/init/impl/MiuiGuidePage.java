package com.bojoy.bjsdk_mainland_new.ui.view.init.impl;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.bojoy.bjsdk_mainland_new.app.BJMGFSdk;
import com.bojoy.bjsdk_mainland_new.app.tools.BJMGFSDKTools;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BJMGFSdkEvent;
import com.bojoy.bjsdk_mainland_new.support.eventbus.EventBus;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseDialogPage;
import com.bojoy.bjsdk_mainland_new.ui.page.base.ViewPage;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.SpUtil;
import com.bojoy.bjsdk_mainland_new.utils.Utility;
import com.bojoy.bjsdk_mainland_new.utils.WarnUtil;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMGFDialog;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.internal.Util;

/**
 * Created by wutao on 2016/5/3.
 * 小米系统引导页
 */
public class MiuiGuidePage  extends BaseDialogPage {

    private static final String TAG = MiuiGuidePage.class.getSimpleName();

    private LinearLayout mLinearLayout;
    private EventBus eventBus = EventBus.getDefault();

    public MiuiGuidePage(Context context, PageManager manager,
                         BJMGFDialog dialog) {
        super(ReflectResourceId.getLayoutId(context, Resource.layout.bjmgf_sdk_miui_warn_dialog),
                  context, manager, dialog);
    }

    @Override
    public void onCreateView(View view) {
        mLinearLayout = (LinearLayout)view.findViewById(ReflectResourceId.getViewId(
                  context, Resource.id.bjmgf_sdk_miui_warn_dialog_containId));

        mLinearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                eventBus.post(new BJMGFSdkEvent(BJMGFSdkEvent.APP_WELCOME_SHOW));
                dialog.setCanceledOnTouchOutside(false);
                quit();
                //将结果保存到本地
                SpUtil.setBooleanValue(context, SysConstant.MIUI_WARN_FLAG,true);
                //跳转到应用详情页面
                Utility.goToMiuiSettingPage(getContext());
                openDock();
            }
        });
        super.onCreateView(view);
    }

    /**
     * 隐藏引导页 显示悬浮窗
     */
    public void hide() {
        eventBus.post(new BJMGFSdkEvent(BJMGFSdkEvent.APP_WELCOME_SHOW));
        quit();
        boolean flag = true;
        //增加游客试玩转正提醒弹窗
        if(WarnUtil.isShowTryWarn(context)) {
            flag = false;
            WarnUtil.showTryWarnDialog(context);
        }
        //增加实名认证提醒
        if(flag){
            if(WarnUtil.isShowAuthentWarn(context)){
                flag = false;
                WarnUtil.showAuthentWarnDialog(context);
            }
        }
        //增加绑定提醒弹窗
        if(flag) {
            if(WarnUtil.isShowBindWarn(context)) {
                //bjmgfData.isNeedQuit = true;
                WarnUtil.showBindWarnDialog(context);
            }
        }
    }

    @Override
    public boolean canBack() {
        dialog.setCanceledOnTouchOutside(false);
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

    @Override
    public void setView() {}

    private Timer timer;

    private Handler handler = null;

    private final int OPEN_DOCK = 0;

    private TimerTask mTimerTask;

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    /**
     * 实时检测并开启悬浮窗
     */
    private void openDock() {
        timer = new Timer();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case OPEN_DOCK:
                        if (BJMGFSDKTools.getInstance().isCurrUserStatusOnLine() && !BJMGFSdk.getDefault().getDockManager().getDock().isAddFlags()) {
                            eventBus.post(new BJMGFSdkEvent(BJMGFSdkEvent.APP_WELCOME_SHOW));
                            timer.cancel();
                            timer = null;
                        }
                        break;
                    default:
                        break;
                }
            }
        };

        mTimerTask = new TimerTask() {

            @Override
            public void run() {
                LogProxy.d(TAG, "sendEmptyMessage OPEN_DOCK");
                handler.sendEmptyMessage(OPEN_DOCK);
            }
        };
        timer.schedule(mTimerTask, 0, 1000);
    }
}
