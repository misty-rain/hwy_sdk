package com.bojoy.bjsdk_mainland_new.ui.activity.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bojoy.bjsdk_mainland_new.app.tools.BJMGFSDKTools;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BasePage;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.SoftKeyboardStateUtil;
import com.bojoy.bjsdk_mainland_new.widget.ClearEditText;

/**
 * Created by wutao on 2016/1/7.
 * Activity 基类
 */
public abstract class BaseActivity extends BasePage {
    private final String TAG = BaseActivity.class.getSimpleName();

    protected BJMGFActivity activity;
    protected Handler handler = new Handler();

    protected RelativeLayout rlBackView = null;
    protected TextView tvTopic = null;
    protected Button btnTopicRight = null;

    DatePickerDialog datePicker;

    /** 增加输入软键盘状态监听功能 */
    protected SoftKeyboardStateUtil keyboardStateHelper;
    protected SoftKeyboardStateUtil.SoftKeyboardStateListener keyboardStateListener = new SoftKeyboardStateUtil.SoftKeyboardStateListener() {

        @Override
        public void onSoftKeyboardOpened(int keyboardHeightInPx) {
            LogProxy.i(TAG, "onSoftKeyboardOpened");
            changeKeyboardViewHeight(keyboardHeightInPx);
        }

        @Override
        public void onSoftKeyboardClosed() {
            LogProxy.i(TAG, "onSoftKeyboardClosed");
            changeKeyboardViewHeight(0);
        }
    };

    /*********************/

    public BaseActivity(int layoutId, Context context, PageManager manager,
                            BJMGFActivity activity) {
        super(layoutId, context, manager);
        this.activity = activity;
        /** 创建软键盘监听对象 */
        keyboardStateHelper = new SoftKeyboardStateUtil(
                activity.getRoot());
    }

    @Override
    public abstract void setView();

    /** bug 修复2 */
    /** 需要随软键盘变化的控件 */
    public View getKeyboardView() {
        return null;
    }

    @Override
    public void hideInput() {
        activity.hideInputMethod();
        /** bug 修复2 */
        /**
         * 如果ProgressDialog先出现时 BJMSystemSoftKeyboardStateHelper将无法捕捉到
         * OnGlobalLayoutListener变化事件 所以在此确保将keyboardView隐藏掉
         **/
        changeKeyboardViewHeight(0);
    }

    @Override
    public void goBack() {
        manager.previousPage();
    }

    @Override
    public void quit() {
        activity.finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        /** 增加输入软键盘状态监听功能 */
        keyboardStateHelper.addSoftKeyboardStateListener(keyboardStateListener);
        /*********************/
        handler.postDelayed(new Runnable() {

            @TargetApi(Build.VERSION_CODES.CUPCAKE)
            @Override
            public void run() {
                View view = ((Activity) context).getCurrentFocus();
                if (view == null) {
                    return;
                }
                /**
                 * 定位焦点控件是否为EditText
                 */
                final EditText edit;
                if (view instanceof EditText) {
                    edit = (EditText) view;
                } else if (view instanceof ClearEditText) {
                    ((ClearEditText) view).getEdit().requestFocus();
                    edit = ((ClearEditText) view).getEdit();
                } else {
                    return;
                }
                /**
                 * 打开输入法
                 */
                InputMethodManager inputManager = (InputMethodManager) context
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(edit, 0);
            }
        }, 500);
    }


    /** 增加输入软键盘状态监听功能 */
    @Override
    public void onPause() {
        super.onPause();
        LogProxy.i(TAG, "removeSoftKeyboardStateListener");
        keyboardStateHelper
                .removeSoftKeyboardStateListener(keyboardStateListener);
    }

    /** 改变随软键盘状态变化的控件高度 */
    private void changeKeyboardViewHeight(int softKeyboardHeight) {
        /** 横屏不需要变化控件高度 */
        if (BJMGFSDKTools.getInstance().getScreenOrientation() == SysConstant.BJMGF_Screen_Orientation_Landscape) {
            return;
        }
        View view = getKeyboardView();
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = softKeyboardHeight;
        view.setLayoutParams(params);
    }

    /**
     * 泛型简化findviewById方法
     *
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    protected final <E extends View> E getView(String id) {
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
     * @return
     */
    @SuppressWarnings("unchecked")
    protected final <E extends View> E getView(String id, View view) {
        try {
            return (E) view.findViewById(ReflectResourceId.getViewId(context,
                    id));
        } catch (ClassCastException ex) {
            LogProxy.e("BJMEngine", "Could not cast View to concrete class."
                    + ex);
            throw ex;
        }
    }

    @Override
    protected void setTitle() {
        rlBackView = getView(Resource.id.bjmgf_sdk_float_account_manager_backLlId);
        tvTopic = getView(Resource.id.bjmgf_sdk_float_account_manager_titleTextViewId);
        btnTopicRight = getView(Resource.id.bjmgf_sdk_finish);

        if(rlBackView != null) {
            rlBackView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    manager.previousPage();
                }
            });
        }
    }

    /**
     * 设置标题内容 需要写在setview方法中
     *
     * @param str
     */
    protected void setTopicStr(String str) {
        if(tvTopic != null) {
            tvTopic.setText(str);
        }
    }

    /**
     * 设置右侧按钮功能 需要写在setview方法中
     *
     * @param listener
     */
    protected void setRightButtonClick(View.OnClickListener listener) {
        if(btnTopicRight != null) {
            btnTopicRight.setOnClickListener(listener);
        }
    }

    /**
     * 设置返回按钮功能 需要写在setview方法中
     *
     * @param listener
     */
    protected void setBackListener(View.OnClickListener listener) {
        if(rlBackView != null) {
            rlBackView.setOnClickListener(listener);
        }
    }

    /**
     * 隐藏返回按钮 需要写在setview方法中
     */
    protected void hideBackView() {
        if(rlBackView != null) {
            rlBackView.setVisibility(View.GONE);
        }
    }

    /**
     * 隐藏右侧按钮 需要写在setview方法中
     */
    protected void hideRightBtn() {
        if(btnTopicRight != null) {
            btnTopicRight.setVisibility(View.GONE);
        }
    }

    /**
     * 设置右侧按钮文字 需要写在setView方法中
     *
     * @param str
     */
    protected void setRightButtonStr(String str) {
        if(btnTopicRight != null) {
            btnTopicRight.setText(str);
        }
    }

}
