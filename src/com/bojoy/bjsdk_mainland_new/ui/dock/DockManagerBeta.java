package com.bojoy.bjsdk_mainland_new.ui.dock;


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import com.bojoy.bjsdk_mainland_new.app.tools.DockTypeTools;

/**
 * 悬浮窗 管理
 */
public class DockManagerBeta {

	private final String TAG = DockManagerBeta.class.getSimpleName();
	
	private BJMGFDockBeta dock;
	private WindowManager windowManager;
	private Activity activity;
	
	public DockManagerBeta() {
	}

	public boolean createDock(Context context, Activity activity) {
		this.activity = activity;
		if (DockTypeTools.getInstance().isNormalType()) {
			return false;

		}

		boolean create = false;
		if (windowManager == null || dock == null) {
			windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
			dock = new BJMGFDockBeta(context, activity, windowManager);
			create = true;

			/** 有个别手机悬浮窗退出应用后无法隐藏 暂时使用TYPE_APPLICATION_STARTING或者TYPE_APPLICATION 使悬浮窗绑定Activity */
//			try {
//				Class<?> clazz = Activity.class;
//			    Method method = clazz.getMethod("getActivityToken");
//			    method.setAccessible(true);
//			    IBinder token = (IBinder) method.invoke(activity);
//			    if (token != null) {
//				    LogProxy.i(TAG, "token = " + token.toString());
//				    dockParams.type = LayoutParams.TYPE_APPLICATION_STARTING;		// TYPE_APPLICATION 绑定应用
//				    dockParams.token = token;
//				    dock.setTypeApplication(true);
//				} else {
//					dockParams.type = LayoutParams.TYPE_PHONE;
//					dock.setTypeApplication(false);
//				}
//			} catch (Exception e) {
//			    LogProxy.e(TAG, "Failed to get Activity Token " + e.toString());
//			    dockParams.type = LayoutParams.TYPE_PHONE;
//			    dock.setTypeApplication(false);
//			}
		}
		return create;
	}

	public Activity getActivity() {
		return activity;
	}

	public void removeDock() {
		if (DockTypeTools.getInstance().isNormalType()) {
			return;
		}
		if (windowManager != null && dock != null) {
			dock.remove();
			dock = null;
			windowManager = null;
		}
	}
	
	public void openDock() {
		if (DockTypeTools.getInstance().isNormalType()) {
			return;
		}
		if (dock != null) {
			dock.setVisibility(View.VISIBLE);
			dock.showDock();
		}
	}
	
	public void hideDock() {
		if (DockTypeTools.getInstance().isNormalType()) {
			return;
		}
		if (dock != null) {
			dock.hideDock();
		}
	}
	
	public void closeDock() {
		if (DockTypeTools.getInstance().isNormalType()) {
			return;
		}
		if (dock != null) {
			dock.setVisibility(View.GONE);
			dock.dismissDock();
		}
	}
	
	public void notifyAccount() {
		if (DockTypeTools.getInstance().isNormalType()) {
			return;
		}
		if (dock != null) {
			dock.notifyAccount(true);
		}
	}
	
	public void notifyWish() {
		if (DockTypeTools.getInstance().isNormalType()) {
			return;
		}
		if (dock != null) {
			dock.notifyWish(true);
		}
	}
	
	public void notifyMessage() {
		if (DockTypeTools.getInstance().isNormalType()) {
			return;
		}
		if (dock != null) {
			dock.notifyMessage(true);
		}
	}
	
	public void notifyCustom() {
		if (DockTypeTools.getInstance().isNormalType()) {
			return;
		}
		if (dock != null) {
			dock.notifyCustom(true);
		}
	}

	public BJMGFDockBeta getDock() {
		return dock;
	}
}
