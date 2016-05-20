package com.bojoy.bjsdk_mainland_new.ui.page.base;


import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.bojoy.bjsdk_mainland_new.app.tools.BJMGFSDKTools;
import com.bojoy.bjsdk_mainland_new.support.eventbus.EventBus;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.view.login.impl.AccountLoginView;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMGFDialog;
import com.bojoy.bjsdk_mainland_new.widget.dialog.WelComeDialog;

/**
 * @author xuxiaobin BaseDialogPage 对话框Page基类
 */
public abstract class BaseDialogPage<T> extends BasePage {

    @SuppressWarnings("unused")
    private final String TAG = BaseDialogPage.class.getSimpleName();
    protected BJMGFDialog dialog;
    protected LinearLayout backView;

    public BaseDialogPage(int layoutId, Context context, PageManager manager,
                          BJMGFDialog dialog) {
        super(layoutId, context, manager);
        this.dialog = dialog;
    }

    @Override
    public void onCreateView(View view) {
        setTitle();
        super.onCreateView(view);
    }

    /**
     * 启动页面时设置页面内容
     */
    public abstract void setView();


    @Override
    public void onResume() {
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    /**
     * 返回按钮的操作
     */
    public void goBack() {


    }

    @Override
    public void hideInput() {
        dialog.hideInputWindow();
    }

    public void openWelcomePage() {
        LogProxy.d(TAG, "openWelcomePage----------------------");
        dialog.dismiss();
        WelComeDialog welcomeDialog = new WelComeDialog(context,
                  BJMGFDialog.Page_Welcome);
        welcomeDialog.show();
    }

    @Override
    public void quit() {
        dialog.dismiss();
    }

    public void closeApp() {
        quit();
    }

    /**
     * 泛型简化findviewById方法
     *
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public final <E extends View> E getView(String id) {
        try {
            return (E) pageView.findViewById(ReflectResourceId.getViewId(
                      context, id));
        } catch (ClassCastException ex) {
            LogProxy.e("BJMEngine", "Could not cast View to concrete class."
                      + ex);
            throw ex;
        }
    }

    /**
     * 泛型简化findviewById方法
     *
     * @param id
     * @param view
     * @return
     */
    @SuppressWarnings("unchecked")
    public final <E extends View> E getView(String id, View view) {
        try {
            return (E) view.findViewById(ReflectResourceId.getViewId(context,
                      id));
        } catch (ClassCastException ex) {
            LogProxy.e("BJMEngine", "Could not cast View to concrete class."
                      + ex);
            throw ex;
        }
    }

    /**
     * 给页面增加头部标题
     */
    protected void setTitle() {
        backView = getView(Resource.id.bjmgf_sdk_back);
        if (backView != null) {
            if (manager.getStack().size() != 0) {
                backView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        manager.previousPage();
                    }
                });
            }
        }
    }

    /**
     * 隐藏返回键
     * 在setView方法中调用
     */
    protected void hideBack() {
        if (backView != null) {
            backView.setVisibility(View.GONE);
        }
    }

    /**
     * 显示返回键
     * 在setView方法中调用
     */
    protected void showBack() {
        if (backView != null) {
            backView.setVisibility(View.VISIBLE);
        }
    }
}
