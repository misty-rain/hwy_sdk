package com.bojoy.bjsdk_mainland_new.widget.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.bojoy.bjsdk_mainland_new.app.tools.BJMGFSDKTools;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.presenter.account.logout.LogoutView;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.view.account.impl.TryChangePage;
import com.bojoy.bjsdk_mainland_new.ui.view.init.impl.InitView;
import com.bojoy.bjsdk_mainland_new.ui.view.login.impl.AccountLoginListView;
import com.bojoy.bjsdk_mainland_new.ui.view.login.impl.AccountLoginView;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.Utility;

/**
 * @author xuxiaobin
 *         好玩友sdk 对话框窗口 主要用于登陆、注册业务
 */
public class BJMGFDialog extends Dialog {

    private final String TAG = BJMGFDialog.class.getSimpleName();

    protected PageManager manager;
    protected ViewGroup root;
    @SuppressWarnings("unused")
    private Activity activity;
    private Context context;

    private float Landscape_Width_Percent_Normal = 0.6f;
    private float Portrait_Width_Percent_Normal = 0.9f;
    private final float Width_Percent_Full = 1.0f;
    private final int Size_Min = 300;
    private final int Size_Max = 850;

    public static final int Page_Init = 1;
    public static final int Page_Logout = 2;
    public static final int Page_Protocol = 3;
    public static final int Page_Sex = 4;
    public static final int Page_Welcome = 5;
    public static final int Page_MiuiGuide = 6;
    public static final int Page_Login = 7;
    public static final int Page_TryChange = 8;
    public static final int Page_ForgetPwd = 9;
    public static final int Page_AccountLogin = 10;

    private int pageType;

    public BJMGFDialog(Context context, Activity activity, int type) {
        super(context, ReflectResourceId.getStyleId(context, Resource.style.bjmgf_sdk_Dialog));
        initDialog(activity, type, context);
    }

    public BJMGFDialog(Context context, int theme, Activity activity, int type) {
        super(context, theme);
        initDialog(activity, type, context);
    }

    private void initDialog(Activity activity, int type, Context context) {
        this.activity = activity;
        this.context = context;
        pageType = type;
        manager = new PageManager();
        setCanceledOnTouchOutside(false);

        Portrait_Width_Percent_Normal = Utility.isTablet(context) ? 0.5f : 0.9f;
        Landscape_Width_Percent_Normal = Utility.isTablet(context) ? 0.4f : 0.6f;
        LogProxy.d(TAG, "Portrait_Width_Percent_Normal=" + Portrait_Width_Percent_Normal);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ReflectResourceId.getLayoutId(getContext(), Resource.layout.bjmgf_sdk_dialog));
        root = (ViewGroup) findViewById(ReflectResourceId.getViewId(getContext(),
                  Resource.id.bjmgf_sdk_dialog_root));
        manager.setRoot(root);


        createPage();


    }

    @Override
    public void show() {
        fitScreenMode();
        super.show();
    }

    @Override
    public void dismiss() {

        manager.clean();
        LogProxy.i(TAG, "dismiss");
        super.dismiss();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /**
         * 点击输入框外使输入框隐藏
         */
        hideInputWindow();
//		if(hideInputWindow()) {
//			return true;
//		}
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (manager != null && manager.getCurrentPage() != null) {
                if (!manager.getCurrentPage().canBack()) {
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void fitScreen(boolean full) {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        boolean landscape = BJMGFSDKTools.getInstance().getScreenOrientation() ==
                  SysConstant.BJMGF_Screen_Orientation_Landscape;
        params.width = getFitWidth(dm.widthPixels, landscape, full);
        params.height = getFitHeight(dm.heightPixels, landscape, full);
        LogProxy.i(TAG, "screen width=" + dm.widthPixels + ", height=" + dm.heightPixels
                  + "width=" + params.width + ", height=" + params.height);
        manager.setSize(params.width, params.height);
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.CENTER);


    }

    private int getFitWidth(int screenWidth, boolean landscape, boolean full) {
        int width = 0;
        if (full) {
            width = (int) (screenWidth * Width_Percent_Full);
        } else {
            width = (int) (landscape ? screenWidth * Landscape_Width_Percent_Normal
                      : screenWidth * Portrait_Width_Percent_Normal);
        }
        width = checkMax(width);
        width = checkMin(width);
        return width;
    }

    private int getFitHeight(int screenHeight, boolean landscape, boolean full) {
        /** 窗体高度自适应 */
        return ViewGroup.LayoutParams.WRAP_CONTENT;
//		int height = 0;
//		if (full) {
//			height = (int)(screenHeight * Height_Percent_Full);
//		} else {
//			height = (int)(landscape ? screenHeight * Landscape_Height_Percent_Normal
//					: screenHeight * Portrait_Height_Percent_Normal);
//		}
//		return checkMin(height);
    }

    private int checkMin(int size) {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        LogProxy.d(TAG, "dialogPage dm.density=" + dm.density);
        LogProxy.d(TAG, "dialogPage min=" + Size_Min);
        return size < Size_Min ? Size_Min : size;
    }

    private int checkMax(int size) {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        LogProxy.d(TAG, "dialogPage with size=" + size);
        if (!Utility.isTablet(context)) {
            return size;
        }
        return size > Size_Max ? Size_Max : size;
    }

    protected void fitScreenMode() {
//		LogProxy.i(TAG, "show()");
        fitScreen(false);
    }

    protected void createPage() {
        switch (pageType){
            case Page_Init://初始化窗口
                InitView initPage = new InitView(getContext(), manager, this);
                manager.addPage(initPage);
                break;
            case Page_Logout://登出窗口
               LogoutView logoutPage = new LogoutView(getContext(), manager, this);
                manager.addPage(logoutPage);
                break;
            case Page_Login://登陆窗口
                AccountLoginView loginPage = new AccountLoginView(getContext(), manager, this);
                manager.addPage(loginPage);
                break;
            case Page_TryChange://试玩窗口
                TryChangePage tryChangePage = new TryChangePage(context, manager, this);
                manager.addPage(tryChangePage);
                break;
            case Page_AccountLogin://账户列表窗口
                AccountLoginListView loginListView = new AccountLoginListView(context, manager, this);
                manager.addPage(loginListView);
                break;


        }
     /*   if (pageType == Page_Init) {
            InitView initPage = new InitView(getContext(), manager, this);
            manager.addPage(initPage);
        } else if (pageType == Page_Logout) {
            LogoutPage logoutPage = new LogoutPage(getContext(), manager, this);
            manager.addPage(logoutPage);
        } else if (pageType == Page_Login) {
            LoginPage loginPage = new LoginPage(getContext(), manager, this);
            manager.addPage(loginPage);
        } else if (pageType == Page_TryChange) {
            TryChangePage tryChangePage = new TryChangePage(context, manager, this);
            manager.addPage(tryChangePage);
        } else if (pageType == Page_ForgetPwd) {
            FindPwdSplashPage forgetPage = new FindPwdSplashPage(context, manager, this);
            manager.addPage(forgetPage);
        } else if (pageType == Page_AccountLogin) {
            AccountLoginPage loginPage = new AccountLoginPage(context, manager, this);
            manager.addPage(loginPage);
        }*/
    }

    public boolean hideInputWindow() {
        return Utility.hideInputMethod(getContext(), getCurrentFocus());
//		InputMethodManager im = (InputMethodManager) getContext().
//				getSystemService(Context.INPUT_METHOD_SERVICE);
//		if (getCurrentFocus() != null) {
//			im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), 
//					InputMethodManager.HIDE_NOT_ALWAYS);
//		}
    }

    public int getPageType() {
        return pageType;
    }

    public Activity getActivity() {
        return activity;
    }
}
