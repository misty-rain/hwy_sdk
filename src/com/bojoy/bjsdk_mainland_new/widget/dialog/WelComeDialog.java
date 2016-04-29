package com.bojoy.bjsdk_mainland_new.widget.dialog;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.bojoy.bjsdk_mainland_new.app.tools.BJMGFSDKTools;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.ui.page.base.ViewPage;
import com.bojoy.bjsdk_mainland_new.ui.view.init.impl.WelcomePage;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.Utility;

public class WelComeDialog extends BJMGFDialog {

	public WelComeDialog(Context context) {
		super(context, null, Page_Welcome);
	}

	public WelComeDialog(Context context, int theme) {
		super(context, theme, null, Page_Welcome);
	}
	
	@Override
	protected void createPage() {
		LogProxy.d("", "create WelcomePage");
		WelcomePage page = new WelcomePage(getContext(), manager, this);
		manager.addPage(page);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		ViewPage page = manager.getCurrentPage();
		if (page != null) {
			((WelcomePage) page).hide(this);
		}
		return super.onTouchEvent(event);
	}

	
	@Override
	protected void fitScreenMode() {
		fitScreen();
	}
	
	@Override
	public void show() {
		fitScreenMode();
		super.show();
	}
	
	public void fitScreen() {
		WindowManager windowManager = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(dm);
		WindowManager.LayoutParams params = getWindow().getAttributes();
		boolean landscape = BJMGFSDKTools.getInstance().getScreenOrientation() ==
				SysConstant.BJMGF_Screen_Orientation_Landscape;
		params.width = getFitWidth(dm.widthPixels, landscape);
		params.height = getFitHeight(dm.heightPixels, landscape);
		LogProxy.i("", "screen width=" + dm.widthPixels + ", height=" + dm.heightPixels
				+ "width=" + params.width + ", height=" + params.height);
		manager.setSize(params.width, params.height);
		getWindow().setAttributes(params);
		LogProxy.d("", "manager.getCurrentPage()=" + manager.getCurrentPage());
		getWindow().setGravity(Gravity.TOP);
		params.y = Utility.dip2px(getContext(), 15);
		
	}
	
	private int getFitWidth(int screenWidth, boolean landscape) {
		int width = 0;
		width = (int)(landscape ? screenWidth * 0.6
					: screenWidth * 0.9);
		LogProxy.d("", "getFitWidth=" + width);
		return width;
	}
	
	private int getFitHeight(int screenHeight, boolean landscape) {
		/** 窗体高度自适应 */
		return ViewGroup.LayoutParams.WRAP_CONTENT;
	}

}
