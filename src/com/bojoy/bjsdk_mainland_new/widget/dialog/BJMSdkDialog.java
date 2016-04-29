package com.bojoy.bjsdk_mainland_new.widget.dialog;


import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.Utility;

/**
 * 一般对话框 ，主要用于正常业务提醒弹出框
 */

public class BJMSdkDialog extends Dialog {

    private static final String TAG = BJMSdkDialog.class.getCanonicalName();

    private RelativeLayout mDialogLayout;
    private TextView mTitleTextView;
    private TextView mMessageTextView;
    private Button mPositiveButton, mNegativeButton;

    private Window mDialogWindow = null;
    private WindowManager.LayoutParams mLayoutParams = null;

    public boolean mIsPositiveButton = false;
    public boolean mIsNegativeButton = false;

    private float Landscape_Width_Percent_Normal = 0.5f;
    private float Landscape_Height_Percent_Normal = 0.4f;
    private float Portrait_Width_Percent_Normal = 0.8f;
    private float Portrait_Height_Percent_Normal = 0.3f;

    private final int Size_Max = 850;

    public BJMSdkDialog(Context context) {
        super(context);

        this.setCancelable(false);

        InitUI(context);
    }

    private void InitUI(Context context) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(ReflectResourceId.getLayoutId(getContext(), Resource.layout.bjmgf_sdk_real_dialog));
        mTitleTextView = (TextView) findViewById(ReflectResourceId.getViewId(getContext(),
                Resource.id.bjmgf_sdk_dialog_title));
        mMessageTextView = (TextView) findViewById(ReflectResourceId.getViewId(getContext(),
                Resource.id.bjmgf_sdk_dialog_content));
        mPositiveButton = (Button) findViewById(ReflectResourceId.getViewId(getContext(),
                Resource.id.bjmgf_sdk_dialog_positive_btn));
        mNegativeButton = (Button) findViewById(ReflectResourceId.getViewId(getContext(),
                Resource.id.bjmgf_sdk_dialog_negative_btn));

        mPositiveButton.getPaint().setFakeBoldText(true);
        mNegativeButton.getPaint().setFakeBoldText(true);

        Portrait_Width_Percent_Normal = Utility.isTablet(context) ? 0.45f : 0.8f;
        Landscape_Width_Percent_Normal = Utility.isTablet(context) ? 0.35f : 0.5f;

        Portrait_Height_Percent_Normal = Utility.isTablet(context) ? 0.25f : 0.45f;
        Landscape_Height_Percent_Normal = Utility.isTablet(context) ? 0.4f : 0.65f;

        Log.i("BJMSdkDialog", "init UI");
    }

    @Override
    public void show() {
        fitScreen();
        super.show();
    }

    public void setTitle(String text) {
        mTitleTextView.setText(text);
    }

    public void setTitleSize(float size) {
        mTitleTextView.setTextSize(size);
    }

    public void setTitleColor(int color) {
        mTitleTextView.setTextColor(color);
    }

    public void setMessage(String text) {
        mMessageTextView.setText(text);
    }

    public void setMessageCenter(String text) {
        mMessageTextView.setGravity(Gravity.CENTER);
        mMessageTextView.setText(text);
    }

    public void setMessageSize(float size) {
        mMessageTextView.setTextSize(size);
    }

    public void setMessageColor(int color) {
        mMessageTextView.setTextColor(color);
    }

    public void setPositiveButton(String text, View.OnClickListener onClickListener) {
        mPositiveButton.setText(text);
        mPositiveButton.setVisibility(View.VISIBLE);
        mPositiveButton.setOnClickListener(onClickListener);
        mIsPositiveButton = true;
    }

    public void setNegativeButton(String text, View.OnClickListener onClickListener) {
        mNegativeButton.setText(text);
        mNegativeButton.setVisibility(View.VISIBLE);
        mNegativeButton.setOnClickListener(onClickListener);
        mIsNegativeButton = true;
    }


    public void setDialogSize(int width, int height) {
        mLayoutParams.width = width;
        mLayoutParams.height = height;
        mDialogWindow.setAttributes(mLayoutParams);
    }

    public void setDialogBackgroundColor(int color) {
        mDialogLayout.setBackgroundColor(color);
    }

    private void fitScreen() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        mDialogWindow = this.getWindow();
        mLayoutParams = mDialogWindow.getAttributes();
        boolean landscapeFlag = dm.widthPixels > dm.heightPixels;
        Log.d("BJMSystemDialog", "landscapeFlag " + landscapeFlag);
//		mLayoutParams.width = (int)(dm.widthPixels * (landscapeFlag ? 
//				Landscape_Width_Percent_Normal : Portrait_Width_Percent_Normal));
//		mLayoutParams.height = (int)(dm.heightPixels * (landscapeFlag ? 
//				Landscape_Height_Percent_Normal : Portrait_Height_Percent_Normal));
//		mLayoutParams.width = checkMax(mLayoutParams.width);
//		mLayoutParams.height = checkMax(mLayoutParams.height);
//		mLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mDialogWindow.setAttributes(mLayoutParams);
        mDialogWindow.setGravity(Gravity.CENTER);
    }

    private int checkMax(int size) {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        if (!Utility.isTablet(getContext())) {
            return size;
        }
        return size > Size_Max ? Size_Max : size;
    }
}
