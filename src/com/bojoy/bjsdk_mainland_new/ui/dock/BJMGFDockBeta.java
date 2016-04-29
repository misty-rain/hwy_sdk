package com.bojoy.bjsdk_mainland_new.ui.dock;


import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bojoy.bjsdk_mainland_new.support.task.PollingTimeoutTask;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.Utility;

public class BJMGFDockBeta extends FrameLayout implements BJMGFDockDrawer.DrawerListener {

	private final String TAG = BJMGFDockBeta.class.getSimpleName();

	private Activity activity;
	private Context context;
	private WindowManager windowManager;
	private WindowManager.LayoutParams params;

	private RelativeLayout dockView;
	private ImageView dockIcon, dockHint;
	private int statusBarHeight;
	private int screenWidth, screenHeight;
	private int rawX, rawY, rawStartX, rawStartY, startTouchX, startTouchY;
	private int borderXMax, borderYMax;
	private int drawerPickUpPart;
	/** 不贴边 */
	public final int Border_Margin = 10;
	/** 抽屉点击偏差值 */
	private final int Drawer_Click_Offset = 10;
	/** 收起悬浮窗偏差值 */
	private final int Pickup_Offset = 12;
	private boolean drawerOpen;

	/** 轮询计时器 */
	public static final int Time_Duration = 20;
	private final PollingTimeoutTask.PollingListener pollingListener = new PollingTimeoutTask.PollingListener() {

		@Override
		public void onTimeout() {}

		@Override
		public void onExecute() {
			pollingDo();
		}
	};
	private final PollingTimeoutTask pollingTask;

	public enum DockState {
		Idle,
		Move,
		SpringBack,
		Pickup,
		Transparent,
		Collapsed,
		Open,
		Hide,
	};
	private DockState state;
	private DockState hideLastState = DockState.Idle;
	private DockState springBackLastState;

	/** 空闲时间检测 */
	private int timeCounter = 0;
	private final int Time_Idle_Max = 2000;
	private final int Time_Transparent_Max = 3000;

	/** 收起效果参数 */
	private final int Dock_Spring_Move_Speed = 50;
	private final int Dock_Pickup_Speed = 5;
	/** 悬浮窗移动的方向 */
	private boolean doDirLeft;

	private BJMGFDockDrawer drawer;
	private int dockWidth;

	private boolean addFlag;

	public BJMGFDockBeta(Context context, Activity activity, WindowManager manager) {
		super(context);
		this.context = context;
		this.activity = activity;
		this.windowManager = manager;
		drawer = new BJMGFDockDrawer(context, activity, manager, this);
		pollingTask = new PollingTimeoutTask(Time_Duration,
				0, 0, pollingListener, new Handler(activity.getMainLooper()));
		View.inflate(context, ReflectResourceId.getLayoutId(context,
				Resource.layout.bjmgf_sdk_dock_beta), this);
		dockView = (RelativeLayout)findViewById(ReflectResourceId.getViewId(context,
				Resource.id.bjmgf_sdk_dock_icon_layout));
		dockIcon = (ImageView)findViewById(ReflectResourceId.getViewId(context,
				Resource.id.bjmgf_sdk_dock_icon));
		//
		dockWidth = dockView.getLayoutParams().width;
		statusBarHeight = Utility.getStatusBarHeight(activity);
		screenWidth = windowManager.getDefaultDisplay().getWidth();
		screenHeight = windowManager.getDefaultDisplay().getHeight();
		drawerPickUpPart = (int)(getDockParamWidth() / 2);
		borderXMax = screenWidth - getDockParamWidth() - Border_Margin;
		borderYMax = screenHeight - getDockParamHeight() - statusBarHeight;
		//
		pollingTask.startPolling();
		LogProxy.i(TAG, "BJMGFDockBeta create");
		setState(DockState.Idle);
		LogProxy.i(TAG, "drawerPickUpPart:" + drawerPickUpPart);
//		float marginTop = Util.dip2px(context, 35);
		//
		params = new WindowManager.LayoutParams();
		params.format = PixelFormat.RGBA_8888;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
		params.gravity = Gravity.LEFT | Gravity.TOP;
		params.width = getDockParamWidth();
		params.height = getDockParamHeight();
		params.x = Border_Margin;
		params.y = Utility.getStatusBarHeight(activity);
		params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
		//
		addFlag = false;
	}

	public int getDockParamWidth() {
//		LogProxy.i(TAG, "dock width = " + dockView.getLayoutParams().width);
		return dockWidth;
	}

	public int getDockParamHeight() {
		return dockView.getLayoutParams().height;
	}

	public void setParams(WindowManager.LayoutParams mParams) {
		params = mParams;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int)event.getRawX();
		int y = (int)event.getRawY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startTouchX = (int)event.getX();
			startTouchY = (int)event.getY();
			rawStartX = x;
			rawStartY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			updateViewPosition(x, y);
			break;
		case MotionEvent.ACTION_UP:
			if (!checkClickDock(x, y)) {
				if (isPickupGesture(x, y)) {
					pickup();
				} else {
					springBack();
				}
			}
			startTouchX = startTouchY = 0;
			break;
		}
		return true;
	}

	private void setIconImage(boolean transparent) {
		dockIcon.setImageResource(ReflectResourceId.getDrawableId(context,
				transparent ? Resource.drawable.bjmgf_sdk_float_logo_transparent
				: Resource.drawable.bjmgf_sdk_float_logo_selector));
//		dockIcon.invalidate();
		dockIcon.postInvalidate();
	}

	private boolean checkClickDock(int x, int y) {
		if (isInDrawerClickEvent(x, y)) {
			if (isState(DockState.Idle) || isState(DockState.Transparent)
					|| isState(DockState.Open)) {
				clickDrawer();
				return true;
			}
		}
		return false;
	}

	private void clickDrawer() {
		LogProxy.i(TAG, "clickDrawer");
		drawerOpen = !drawerOpen;
		if (drawerOpen) {
			setState(DockState.Open);
		}
		if (drawerOpen) {
			drawer.openDrawer(params.x, params.y, getDockParamHeight(), checkDoDirection());
		} else {
			drawer.closeDrawer();
		}
	}

	private void updateViewPosition(int x, int y) {
		if (rawX == x && rawY == y) {
			LogProxy.d(TAG, "updateViewPosition-----1--------");

			return;
		}
		if (isInDrawerClickEvent(x, y)) {
			LogProxy.d(TAG, "updateViewPosition-----2--------");

			return;
		}
		if (isPickupGesture(x, y)) {
			LogProxy.d(TAG, "updateViewPosition-----3--------");
			return;
		}
		if (isState(DockState.Idle) || isState(DockState.Transparent)
				|| isState(DockState.Collapsed)) {
			setState(DockState.Move);
		}
		if (isState(DockState.Move)) {
			rawX = x;
			rawY = y;
			params.x = rawX - startTouchX;
			params.y = rawY - startTouchY;
			//
			if (params.x < Border_Margin) {
				params.x = Border_Margin;
			} else
			if (params.x > borderXMax) {
				params.x = borderXMax;
			}
			if (params.y < statusBarHeight) {
				params.y = statusBarHeight;
			} else
			if (params.y > borderYMax) {
				params.y = borderYMax;
			}
			refresh();
		}
	}

	private void setState(DockState state) {
		if (isState(state)) {
			return;
		}
		if (state.equals(DockState.Hide)) {
			hideLastState = this.state;
		}
		if (state.equals(DockState.SpringBack)) {
			springBackLastState = this.state;
		}
		boolean isTransparent = isTransparentState();
		this.state = state;
		LogProxy.i(TAG, "state " + state);
		if (isTransparentState()) {
			if (isState(DockState.Collapsed)) {
				setRingImage(true);
			} else {
				setIconImage(true);
			}

		}
		if (isTransparent) {
			if (!isTransparentState()) {
				setIconImage(false);
			}
		}
	}

	private boolean isTransparentState() {
		return isState(DockState.Transparent) || isState(DockState.Collapsed)
				|| isState(DockState.Pickup);
	}

	public boolean isState(DockState state) {
		return state.equals(this.state);
	}

	private void pollingDo() {
		if (addFlag) {
			idleDo();
			springDo();
			pickupDo();
			drawer.drawerDo();
		}
	}

	private void idleDo() {
		if (isState(DockState.Idle)) {
			timeCounter += Time_Duration;
			if (timeCounter >= Time_Idle_Max) {
				timeCounter = 0;
				transparent();
			}
		} else
		if (isState(DockState.Transparent)) {
			timeCounter += Time_Duration;
			if (timeCounter >= Time_Transparent_Max) {
				timeCounter = 0;
				pickup();
			}
		} else {
			timeCounter = 0;
		}
	}

	private void springBack() {
		if (isState(DockState.Move) || isState(DockState.Collapsed)) {
			setState(DockState.SpringBack);
			checkDoDirection();
		}
	}

	private void transparent() {
		setState(DockState.Transparent);
	}

	private void pickup() {
		setState(DockState.Pickup);
		checkDoDirection();
	}

	private void springDo() {
		if (isState(DockState.SpringBack)) {
			params.x += doDirLeft ? -Dock_Spring_Move_Speed : Dock_Spring_Move_Speed;
			if (params.x < Border_Margin) {
				params.x = Border_Margin;
				if (DockState.Collapsed.equals(springBackLastState)) {
					clickDrawer();
				} else {
					LogProxy.i(TAG, "springBack over 1");
					setState(DockState.Idle);
				}
			} else
			if (params.x > borderXMax) {
				params.x = borderXMax;
				if (DockState.Collapsed.equals(springBackLastState)) {
					clickDrawer();
				} else {
					LogProxy.i(TAG, "springBack over 2");
					setState(DockState.Idle);
				}
			}
//			LogProxy.i(TAG, "springMove x = " + params.x);
			refresh();
		}
	}

	private void pickupDo() {
		if (isState(DockState.Pickup)) {
			params.x += doDirLeft ? -Dock_Pickup_Speed : Dock_Pickup_Speed;
			if (params.x < -drawerPickUpPart) {
				params.x = -drawerPickUpPart;
				setState(DockState.Collapsed);
			} else
			if (params.x > screenWidth - getDockParamWidth() + drawerPickUpPart) {
				params.x = screenWidth - getDockParamWidth() + drawerPickUpPart;
				setState(DockState.Collapsed);
			}
			refresh();
		}
	}

	private boolean checkDoDirection() {
		doDirLeft = params.x < (screenWidth - getDockParamWidth()) / 2;
		return doDirLeft;
	}

	/**
	 * 是否是悬浮窗点击事件
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean isInDrawerClickEvent(int x, int y) {
		return Math.abs(x - rawStartX) <= Drawer_Click_Offset &&
				Math.abs(y - rawStartY) <= Drawer_Click_Offset;
	}

	/**
	 * 是否是悬浮窗收起手势
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean isPickupGesture(int x, int y) {
		if (isState(DockState.Idle) || isState(DockState.Transparent)) {
//			LogProxy.i(TAG, "is pickup range x=" + x + ", rowx=" + rawStartX);
			/** 判断悬浮窗当前的位置 */
			if (checkDoDirection()) {
				/** 左边 */
				if (x - rawStartX < -Pickup_Offset) {
					return true;
				}
			} else {
				/** 右边 */
				if (x - rawStartX > Pickup_Offset) {
					return true;
				}
			}
		}
		return false;
	}

	private void refresh() {
		windowManager.updateViewLayout(this, params);
		postInvalidate();
	}

	public void showDock() {
		if (!addFlag) {
			LogProxy.i(TAG, "show dock");
			addFlag = true;
			windowManager.addView(drawer, drawer.getParams());
			windowManager.addView(this, params);
			drawer.setVisibility(View.VISIBLE);
		}
		setState(hideLastState);
		refresh();
	}

	public void dismissDock() {
		if (addFlag) {
			LogProxy.i(TAG, "dismiss dock");
			addFlag = false;
			windowManager.removeView(this);
			windowManager.removeView(drawer);
			drawer.setVisibility(View.GONE);
			setState(DockState.Hide);
		}
	}

	public void hideDock() {
		dismissDock();
	}

	public void remove() {
		dismissDock();
		pollingTask.suspendPolling();
	}

	public void notifyAccount(boolean flag) {
		notifyDock(flag);
		drawer.notifyAccount(flag);
	}

	public void notifyWish(boolean flag) {
		notifyDock(flag);
		drawer.notifyWish(flag);
	}

	public void notifyMessage(boolean flag) {
		notifyDock(flag);
		drawer.notifyMessage(flag);
	}

	public void notifyCustom(boolean flag) {
		notifyDock(flag);
		drawer.notifyCustom(flag);
	}

	private void notifyDock(boolean flag) {
		ImageView leftHint = (ImageView)findViewById(ReflectResourceId.getViewId(context,
				Resource.id.bjmgf_sdk_dock_hint_left));
		ImageView rightHint = (ImageView)findViewById(ReflectResourceId.getViewId(context,
				Resource.id.bjmgf_sdk_dock_hint_right));
		if (flag) {
			dockHint = checkDoDirection() ? rightHint : leftHint;
			dockHint.setVisibility(View.VISIBLE);
			dockHint.invalidate();
		} else {
			rightHint.setVisibility(View.INVISIBLE);
			leftHint.setVisibility(View.INVISIBLE);
			rightHint.invalidate();
			leftHint.invalidate();
		}
	}

	@Override
	public void onClosed() {
		LogProxy.i(TAG, "drawer close over");
		setState(DockState.Idle);
	}

	@Override
	public void openActivity(boolean notify) {
		LogProxy.i(TAG, "open activity");
		setState(DockState.Idle);
		notifyDock(notify);
	}

	private void setRingImage(boolean flag) {
		if (dockIcon != null) {
			dockIcon.setImageResource(flag ?
					ReflectResourceId.getDrawableId(context, Resource.drawable.bjmgf_sdk_float_logo_ring_whole) :
					ReflectResourceId.getDrawableId(context, Resource.drawable.bjmgf_sdk_float_logo_selector));
			dockIcon.invalidate();
		}
	}

	public boolean isAddFlags() {
		return addFlag;
	}

	@Override
	public void onTouchOutSideClosed() {
		drawerOpen = false;
	}
}
