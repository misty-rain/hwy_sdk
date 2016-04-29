package com.bojoy.bjsdk_mainland_new.ui.page.base;


import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.utils.DialogUtil;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;

/**
 * @author xuxiaobin BasePage 基类，用于子类基本的数据及规定其子类的一些行为 BaseDialogPage
 *         BaseActivityPage是该类的子类
 */
public abstract class BasePage extends ViewPage {

    @SuppressWarnings("unused")
    private final String TAG = BasePage.class.getSimpleName();
    protected Dialog mProgressDialog;//进度条dialog

    public BasePage(int layoutId, Context context, PageManager manager) {
        super(layoutId, context, manager);
    }

    @Override
    public void onCreateView(View view) {
        setTitle();
        setView();
    }


    /**
     * 启动页面时设置页面内容
     */
    public abstract void setView();

    /**
     * 隐藏输入法
     */
    public abstract void hideInput();


    @Override
    public void onPause() {
        hideInput();
    }

    @Override
    public void onResume() {

    }

    @Override
    public boolean canBack() {
        if (!isLoading()) {
            goBack();
        }
        return false;
    }

    public boolean isLoading() {
        return mProgressDialog != null && mProgressDialog.isShowing();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * 返回按钮的操作
     */
    public abstract void goBack();

    /**
     * 父窗口退出
     */
    public abstract void quit();

    /**
     * 设置标题
     */
    protected abstract void setTitle();


    /**
     * 新增获取bjmgf_sdk_strings.xml中的文字
     */

    public String getString(String resourceIdName) {
        return context.getResources().getString(
                ReflectResourceId.getStringId(context, resourceIdName));
    }

    /**
     * 显示进度条
     */
    public void showProgressDialog() {
        hideInput();
        if (this.equals(manager.getCurrentPage())) {
            if (mProgressDialog != null) {// 关闭之前的loading框
                LogProxy.i(TAG, "progress is showing");
                if (mProgressDialog.isShowing()) {
//					mProgressDialog.dismiss();
//					mProgressDialog = null;
                } else {
                    mProgressDialog = DialogUtil
                            .createTransparentProgressDialog(
                                    context,
                                    context.getResources()
                                            .getString(
                                                    ReflectResourceId
                                                            .getStringId(
                                                                    context,
                                                                    Resource.string.bjmgf_sdk_dataSubmitingStr)));
                    mProgressDialog.show();
                }
            } else {
                mProgressDialog = DialogUtil
                        .createTransparentProgressDialog(
                                context,
                                context.getResources()
                                        .getString(
                                                ReflectResourceId
                                                        .getStringId(
                                                                context,
                                                                Resource.string.bjmgf_sdk_dataSubmitingStr)));
                mProgressDialog.show();
            }
        }
    }

    /**
     * 关闭进度条
     */
    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            // if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            // }
        }
    }
    public void showToastCenter(String content) {
		Toast toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
		showToastCenter(toast);
	}
	private void showToastCenter(Toast toast) {
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
}
