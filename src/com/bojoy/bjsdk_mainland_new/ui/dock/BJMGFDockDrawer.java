package com.bojoy.bjsdk_mainland_new.ui.dock;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bojoy.bjsdk_mainland_new.app.tools.BJMGFSDKTools;
import com.bojoy.bjsdk_mainland_new.app.tools.DockTypeTools;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BJMGFSdkEvent;
import com.bojoy.bjsdk_mainland_new.support.eventbus.EventBus;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.view.account.impl.AccountCenterView;
import com.bojoy.bjsdk_mainland_new.ui.view.cs.impl.CustomerServiceView;
import com.bojoy.bjsdk_mainland_new.ui.view.message.impl.MessageNoticeView;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;

public class BJMGFDockDrawer extends FrameLayout implements OnClickListener {

    private final String TAG = BJMGFDockDrawer.class.getSimpleName();

    private final String MESSAGE_READED = "2";
    private EventBus eventBus = EventBus.getDefault();


    private Activity activity;
    private Context context;
    private WindowManager windowManager;
    private WindowManager.LayoutParams params;

    private FrameLayout drawerView;
    private LinearLayout drawerLayout;
    private View drawerLeftPad, drawerRightPad;
    private View drawerLeftMargin, drawerRightMargin;
    private View drawerLeftHalf, drawerRightHalf;
    private ImageView accountView, wishView, messageView, serviceView;
    private ImageView accountHint, wishHint, messageHint, serviceHint;
    private LayoutParams parentParams;
    private Handler handler;

    public enum DrawerState {
        Open, Hide, PickOut, Pickup,
    }

    ;

    private DrawerState state;

    private int drawerScaleWidth;
    private int drawerWidth;
    private final int Drawer_Width_Scale_Speed = 80;
    private int scaleWidth = 0;
    private boolean openLeft;
    private DrawerListener listener;
    private int timeCounter;

    private boolean accountNotify, wishNotify, messageNotify, customNotify,
              aboutNotify;


    public BJMGFDockDrawer(Context context, Activity activity, WindowManager manager,
                           DrawerListener listener) {
        super(context);
        this.context = context;
        this.activity = activity;
        this.windowManager = manager;
        this.listener = listener;
        handler = new Handler(activity.getMainLooper());
        View.inflate(context, ReflectResourceId.getLayoutId(context,
                  Resource.layout.bjmgf_sdk_dock_drawer), this);
        drawerView = (FrameLayout) findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_dock_beta_drawer));
        drawerLayout = (LinearLayout) findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_dock_beta_drawer_layout));
        drawerLeftHalf = (View) findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_dock_beta_drawer_left_half));
        drawerRightHalf = (View) findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_dock_beta_drawer_right_half));
        drawerLeftPad = (View) findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_dock_beta_drawer_left_pad));
        drawerRightPad = (View) findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_dock_beta_drawer_right_pad));
        drawerLeftMargin = (View) findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_dock_beta_drawer_left_margin));
        drawerRightMargin = (View) findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_dock_beta_drawer_right_margin));
        accountView = (ImageView) findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_dock_account_manager_image));
        accountHint = (ImageView) findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_dock_account_manager_hint));
        wishView = (ImageView) findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_dock_wish_image));
        wishHint = (ImageView) findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_dock_wish_hint));
        messageView = (ImageView) findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_dock_message_image));
        messageHint = (ImageView) findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_dock_message_hint));
        serviceView = (ImageView) findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_dock_custom_service_image));
        serviceHint = (ImageView) findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_dock_custom_service_hint));
        accountView.setOnClickListener(this);
        wishView.setOnClickListener(this);
        messageView.setOnClickListener(this);
        serviceView.setOnClickListener(this);
        // 普通悬浮窗版本重新设置悬浮窗抽屉的宽度
        if (DockTypeTools.getInstance().isNormalDockType()) {
            LayoutParams drawerParams = (LayoutParams) drawerView.getLayoutParams();
            drawerParams.width = (int) activity.getResources().getDimension(ReflectResourceId.getDimenId(context,
                      Resource.dimen.bjmgf_sdk_dock_board_beta_normal_width));
            drawerView.setLayoutParams(drawerParams);

            LinearLayout.LayoutParams drawerContentParams = (LinearLayout.LayoutParams) drawerLayout.getLayoutParams();
            drawerContentParams.width = (int) activity.getResources().getDimension(ReflectResourceId.getDimenId(context,
                      Resource.dimen.bjmgf_sdk_dock_board_beta_normal_scale_width));
            drawerLayout.setLayoutParams(drawerContentParams);
        }
        //
        params = new WindowManager.LayoutParams();
        params.format = PixelFormat.RGBA_8888;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                  | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                  | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.width = drawerView.getLayoutParams().width;
        params.height = drawerView.getLayoutParams().height;
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        //
        drawerWidth = params.width;
        float scaleWidth = activity.getResources().getDimension(ReflectResourceId.getDimenId(context,
                  Resource.dimen.bjmgf_sdk_dock_board_beta_width)) -
                  activity.getResources().getDimension(ReflectResourceId.getDimenId(context,
                            Resource.dimen.bjmgf_sdk_dock_beta_half_size));
        // 普通悬浮窗重新计算抽屉缩放的宽度
        if (DockTypeTools.getInstance().isNormalDockType()) {
            scaleWidth = activity.getResources().getDimension(ReflectResourceId.getDimenId(context,
                      Resource.dimen.bjmgf_sdk_dock_board_beta_normal_width)) -
                      activity.getResources().getDimension(ReflectResourceId.getDimenId(context,
                                Resource.dimen.bjmgf_sdk_dock_beta_half_size));
        } else {
            //新版本中已关闭 心愿 功能
    /*		if(!bjmgfData.isOpenWish()) {
                findViewById(ReflectResourceId.getViewId(context, Resource.id.bjmgf_sdk_dock_wish))
				.setVisibility(View.GONE);
			} else {
				findViewById(ReflectResourceId.getViewId(context, Resource.id.bjmgf_sdk_dock_wish))
				.setVisibility(View.VISIBLE);
			}*/
        }
        drawerScaleWidth = (int) scaleWidth;
        initState();
        LogProxy.i(TAG, "drawerScaleWidth = " + drawerScaleWidth);
        // 普通悬浮窗版本去掉心愿和消息
        if (DockTypeTools.getInstance().isNormalDockType()) {
            findViewById(ReflectResourceId.getViewId(context, Resource.id.bjmgf_sdk_dock_wish))
                      .setVisibility(View.GONE);
            findViewById(ReflectResourceId.getViewId(context, Resource.id.bjmgf_sdk_dock_message))
                      .setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(accountView)) {
            goToAccountManager();
        } else if (v.equals(wishView)) {
            goToWishList();
        } else if (v.equals(messageView)) {
            goToMessage();
        } else if (v.equals(serviceView)) {
            goToCustomCenter();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            // LogProxy.i(TAG, "action outside");
            closeDrawer();
            if (listener != null) {
                listener.onTouchOutSideClosed();
            }
            return false;
        }
        return super.onTouchEvent(event);
    }

    public WindowManager.LayoutParams getParams() {
        return params;
    }

    public void notifyAccount(boolean flag) {
        accountNotify = flag;
        setVisible(accountHint, flag);
    }

    public void notifyWish(boolean flag) {
        wishNotify = flag;
        setVisible(wishHint, flag);
    }

    public void notifyMessage(boolean flag) {
        messageNotify = flag;
        setVisible(messageHint, flag);
    }

    public void notifyCustom(boolean flag) {
        customNotify = flag;
        setVisible(serviceHint, flag);
    }

    private void setVisible(ImageView hintView, boolean flag) {
        hintView.setVisibility(flag ? View.VISIBLE : View.INVISIBLE);
        hintView.invalidate();
    }

    public void openDrawer(int dock_x, int dock_y, int dockSize, boolean left) {
        if (drawerView.getVisibility() != View.INVISIBLE) {
            setDrawerView(left);
            openLeft = left;
            float margin = activity.getResources().getDimension(
                      ReflectResourceId.getDimenId(context,
                                Resource.dimen.bjmgf_sdk_margin5));
            int x = left ? dock_x + (int) margin : dock_x - drawerWidth
                      + dockSize + (int) margin;
            // LogProxy.i(TAG, "open drawer x=" + x + ", y=" + dock_y +
            // ", w=" + params.width + ", h=" + params.height);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) drawerLayout
                      .getLayoutParams();
            layoutParams.width = 0;
            layoutParams.leftMargin = left ? 0 : drawerScaleWidth;
            drawerLayout.setLayoutParams(layoutParams);
            if (!left) {
                drawerLayout.setVisibility(INVISIBLE);
            }
            drawerView.setVisibility(View.INVISIBLE);
            refresh(x, dock_y, drawerWidth, params.height);
            //
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    setState(DrawerState.PickOut);
                }
            }, 200);

        }
    }

    private void setDrawerView(boolean left) {
        setViewGone(drawerLeftPad, !left);
        setViewGone(drawerLeftMargin, !left);
        setViewGone(drawerLeftHalf, !left);
        setViewGone(drawerRightPad, left);
        setViewGone(drawerRightMargin, left);
        setViewGone(drawerRightHalf, left);
        drawerLayout.setBackgroundResource(ReflectResourceId.getDrawableId(
                  context, left ? Resource.drawable.bjmgf_sdk_float_left_bg
                            : Resource.drawable.bjmgf_sdk_float_right_bg));
        setHalfPadBackground(left, true);
    }

    private void setViewsGone() {
        setViewGone(drawerLeftPad, true);
        setViewGone(drawerLeftMargin, true);
        setViewGone(drawerLeftHalf, true);
        setViewGone(drawerRightPad, true);
        setViewGone(drawerRightMargin, true);
        setViewGone(drawerRightHalf, true);
    }

    private void setViewGone(View view, boolean gone) {
        view.setVisibility(gone ? GONE : VISIBLE);
    }

    public void closeDrawer() {
        if (isState(DrawerState.Open)) {
            setState(DrawerState.Pickup);
            LogProxy.d(TAG, "closeDrawer");
        }
    }

    public void refresh(int x, int y, int w, int h) {
        params.x = x;
        params.y = y;
        params.width = w;
        params.height = h;
        windowManager.updateViewLayout(this, params);
        if (w == 0) {
            setVisibility(View.GONE);
        } else {
            setVisibility(View.VISIBLE);
        }
    }

    private void initState() {
        drawerView.setVisibility(View.GONE);
        params.width = 0;
        state = DrawerState.Hide;
    }

    public void setState(DrawerState state) {
        if (isState(state)) {
            return;
        }
        LogProxy.i(TAG, "drawer state " + state);
        this.state = state;
        if (isState(DrawerState.Hide)) {
            drawerView.setVisibility(View.GONE);
            refresh(params.x, params.y, 0, params.height);

        } else if (isState(DrawerState.Pickup)) {
            scaleWidth = drawerScaleWidth;
            drawerView.setVisibility(View.VISIBLE);
        } else if (isState(DrawerState.PickOut)) {
            scaleWidth = 0;
            drawerView.setVisibility(View.VISIBLE);
        } else if (isState(DrawerState.Open)) {
            timeCounter = 0;
        }
    }

    private boolean isState(DrawerState state) {
        return state.equals(this.state);
    }

    public void drawerDo() {
        pickupDo();
        pickOutDo();
        openWaitDo();
    }

    private void pickOutDo() {
        if (isState(DrawerState.PickOut)) {
            scaleWidth += Drawer_Width_Scale_Speed;
            // LogProxy.i(TAG, "scaleWidth = " + scaleWidth);
            if (scaleWidth > drawerScaleWidth) {
                scaleWidth = drawerScaleWidth;
                setState(DrawerState.Open);
            }
            if (!openLeft) {
                drawerLayout.setVisibility(VISIBLE);
            }
            scaleDrawer();
        }
    }

    private void pickupDo() {
        if (isState(DrawerState.Pickup)) {
            scaleWidth -= Drawer_Width_Scale_Speed;
            // LogProxy.i(TAG, "scaleWidth = " + scaleWidth);
            if (scaleWidth < 0) {
                scaleWidth = 0;
                scaleDrawer();
                setHalfPadBackground(openLeft, false);
                // 去掉延时，不然listener.onClosed()会执行多次（不解）
                // handler.postDelayed(new Runnable() {
                //
                // @Override
                // public void run() {
                setState(DrawerState.Hide);
                if (listener != null) {
                    listener.onClosed();
                }
                // }
                // }, 200);
            } else {
                scaleDrawer();
            }
        }
    }

    private void openWaitDo() {
        if (isState(DrawerState.Open)) {
            timeCounter += BJMGFDockBeta.Time_Duration;
            if (timeCounter >= SysConstant.DOCK_TIME_OPEN_WAIT_MAX) {
                timeCounter = 0;
                setState(DrawerState.Pickup);
            }
        }
    }

    private void scaleDrawer() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) drawerLayout
                  .getLayoutParams();
        if (!openLeft) {
            int margin_x = drawerScaleWidth - scaleWidth;
            layoutParams.leftMargin = margin_x;
        }
        layoutParams.width = scaleWidth;
        drawerLayout.setLayoutParams(layoutParams);
        drawerLayout.invalidate();
    }

    private void goToAccountManager() {
        if (accountNotify) {
            notifyAccount(false);
        }
        closeDirectDrawer();
        open();
    }

    /**
     * 心愿功能关闭
     */
    private void goToWishList() {
/*		if (wishNotify) {
            notifyWish(false);
		}
		closeDirectDrawer();
		openWishPage();
		BJMGFCommon.setOfflineMsgFlag(MESSAGE_READED);*/
    }

    /**
     * 打开消息
     */
    private void goToMessage() {
        if (messageNotify) {
            notifyMessage(false);
        }
        closeDirectDrawer();
        if (BJMGFSDKTools.getInstance().isCurrUserTryPlay(context)) {
            postFillInfoEvent();
        } else {
            openMessagePage();
            //// TODO: 2016/1/7  消息实体 暂 不管
            //BJMGFCommon.setOfflineMsgFlag(MESSAGE_READED);
        }


    }

    /**
     * 打开用户page
     */
    private void open() {
        if (BJMGFSDKTools.getInstance().isCurrUserTryPlay(context)) {
            postFillInfoEvent();
        } else {
            openActivityPage(AccountCenterView.class.getCanonicalName());
        }
    }

    /**
     * 打开许愿page
     */
    private void openWishPage() {
        //eventBus.post(new BJMGFSdkEvent(BJMGFSdkEvent.App_Open_Wish_Page));
        //openActivityPage(DockWishGoodsListPage.class.getCanonicalName());
    }

    /**
     * 打开消息通知
     */
    private void openMessagePage() {
        openActivityPage(
                  MessageNoticeView.class.getCanonicalName());
    }

    /**
     * 打开客服中心
     */
    private void goToCustomCenter() {
        if (customNotify) {
            notifyCustom(false);
        }        closeDirectDrawer();
        openCustomCenterPage();
//		if (BJMGFGlobalData.TRY_LOGIN_PASSPORT_POSTFIX.equals(BJMGFGlobalData
//				.getTryPassportPostfix())) {
//			postFillInfoEvent();
//		} else {
//			openCustomCenterPage();
//		}
    }

    /**
     * 打开客服中心
     */
    private void openCustomCenterPage() {
        openActivityPage(CustomerServiceView.class.getCanonicalName());
    }

    private void openActivityPage(String pageClassName) {
        BJMGFActivity.canLandscape = true;
        Intent intent = new Intent(activity, BJMGFActivity.class);
        intent.putExtra(BJMGFActivity.Page_Class_Name_Key, pageClassName);
        activity.startActivity(intent);
    }

    public void closeDirectDrawer() {
        int currentAPILevel = Build.VERSION.SDK_INT;
        LogProxy.d(TAG, "currentapiVersion = " + currentAPILevel);
        if (currentAPILevel > Build.VERSION_CODES.KITKAT) {
            closeDrawer();
        } else {
            setState(DrawerState.Hide);
        }
        if (listener != null) {
            listener.openActivity(accountNotify && customNotify && aboutNotify);
        }
        drawerView.invalidate();
        if (listener != null) {
            listener.onTouchOutSideClosed();
        }
    }

    private void setHalfPadBackground(boolean left, boolean visible) {
        if (left) {
            drawerLeftHalf.setBackgroundResource(!visible ? 0
                      : ReflectResourceId.getDrawableId(context,
                      Resource.drawable.bjmgf_sdk_float_left_half_bg));
            drawerLeftHalf.invalidate();
        } else {
            drawerRightHalf.setBackgroundResource(!visible ? 0
                      : ReflectResourceId.getDrawableId(context,
                      Resource.drawable.bjmgf_sdk_float_right_half_bg));
            drawerRightHalf.invalidate();
        }
    }

    private void postFillInfoEvent() {
        eventBus.post(new BJMGFSdkEvent(BJMGFSdkEvent.APP_PERFECT_DATA));
    }

    public interface DrawerListener {

        public void onClosed();

        public void openActivity(boolean notify);

        public void onTouchOutSideClosed();// 点击非悬浮窗区域 关闭悬浮窗

    }
}
