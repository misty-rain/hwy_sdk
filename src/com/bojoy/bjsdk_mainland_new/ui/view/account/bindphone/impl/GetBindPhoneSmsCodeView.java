package com.bojoy.bjsdk_mainland_new.ui.view.account.bindphone.impl;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bojoy.bjsdk_mainland_new.presenter.account.IAccountCenterPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.account.impl.AccountCenterPresenterImpl;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseActivityPage;
import com.bojoy.bjsdk_mainland_new.ui.view.account.bindphone.IBindPhoneView;
import com.bojoy.bjsdk_mainland_new.utils.*;
import com.bojoy.bjsdk_mainland_new.widget.ClearEditText;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMSdkDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wutao on 2016/1/15.
 * 绑定  or 修改绑定 视图
 */
public class GetBindPhoneSmsCodeView extends BaseActivityPage implements IBindPhoneView {

    private final String TAG = GetBindPhoneSmsCodeView.class.getSimpleName();

    private RelativeLayout mBackLayout = null;
    private TextView mCheckPhoneTextView = null;
    private ClearEditText inputPhoneNumber = null;
    private EditText mCountryNumberEditText = null;
    private String inputPhoneNumberStr = null;
    /**
     * 新增随软键盘状态变化的控件
     */
    private View keyboardView;

    private IAccountCenterPresenter iAccountCenterPresenter;


    /**
     * 初始化视图
     *
     * @param
     * @param context
     * @param manager
     * @param activity
     *******************/
    public GetBindPhoneSmsCodeView(Context context, PageManager manager,
                                   BJMGFActivity activity) {
        super(ReflectResourceId.getLayoutId(context,
                Resource.layout.bjmgf_sdk_dock_account_bindphone), context, manager, activity);
    }

    public void onCreateView(View view) {
        mBackLayout = (RelativeLayout) view.findViewById(ReflectResourceId.getViewId(context,
                Resource.id.bjmgf_sdk_float_account_manager_bindphone_backLlId));
        mCheckPhoneTextView = (TextView) view.findViewById(ReflectResourceId.getViewId(context,
                Resource.id.bjmgf_sdk_account_manager_bindphone_nextId));
        mCountryNumberEditText = (EditText) view.findViewById(ReflectResourceId.getViewId(context,
                Resource.id.bjmgf_sdk_float_account_manager_bindphone_CountryNumberEditTextId));
        inputPhoneNumber = (ClearEditText) view.findViewById(ReflectResourceId.getViewId(context,
                Resource.id.bjmgf_sdk_float_account_manager_bindphone_editTextId));
        keyboardView = (View) view.findViewById(ReflectResourceId.getViewId(context,
                Resource.id.bjmgf_sdk_float_account_manager_bindphone_keyboard));

        mCountryNumberEditText.setEnabled(false);
        mCountryNumberEditText.setFocusable(false);
        inputPhoneNumber.setFocusable(true);

        //返回操作
        mBackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                manager.previousPage();

            }
        });

        //验证手机号合法性
        mCheckPhoneTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                checkPhoneNum();
            }

        });
        super.onCreateView(view);
    }


    /**
     * 验证手机号合法性
     */
    private void checkPhoneNum() {

        String msgStr = String.format(context.getResources().getString(ReflectResourceId.getStringId(context,
                Resource.string.bjmgf_sdk_floatWindow_accountManager_dialog_msg)), mCountryNumberEditText.getText().toString()) +
                inputPhoneNumber.getEditText().toString();
        inputPhoneNumberStr = inputPhoneNumber.getEditText().toString();
        if (StringUtility.isEmpty(inputPhoneNumberStr)) {
            ToastUtil.showMessage(context, getString(Resource.string.bjmgf_sdk_floatWindow_accountManager_inputPhone_is_null_Str));
            return;
        }

        if (inputPhoneNumberStr.length() != 11) {
            ToastUtil.showMessage(context, getString(Resource.string.bjmgf_sdk_InputPhoneNumberErrorStr));
            return;
        }

        if (!Utility.checkPhoneNumberFormat(context, inputPhoneNumberStr)) {
            return;
        }

        showSureDialog(context,
                Utility.getString(Resource.string.bjmgf_sdk_floatWindow_accountManager_dialog_title, context),
                msgStr,
                Utility.getString(Resource.string.bjmgf_sdk_floatWindow_accountManager_dialog_modify, context),
                Utility.getString(Resource.string.bjmgf_sdk_floatWindow_accountManager_dialog_sure, context));


    }


    /**
     * 确认发送验证码 框
     *
     * @param context
     * @param titleStr
     * @param messageStr
     * @param modifyStr
     * @param sureBtnStr
     */
    private void showSureDialog(final Context context, String titleStr, String messageStr,
                                String modifyStr, String sureBtnStr) {
        final BJMSdkDialog dialog = new BJMSdkDialog(context);
        dialog.setTitle(titleStr);
        dialog.setMessage(messageStr);
        dialog.setPositiveButton(sureBtnStr, new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                iAccountCenterPresenter.getSmsCodeByBindPhone(context, inputPhoneNumberStr);


            }
        });
        dialog.setNegativeButton(modifyStr, new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                inputPhoneNumber.setFocusable(true);
            }
        });
        dialog.show();

    }

    @Override
    public void showError(String message) {
        ToastUtil.showMessage(context, message);
    }

    @Override
    public void showSuccess() {
        BindPhoneView bindPhoneView = new BindPhoneView(context, manager, activity);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("phoneNum", inputPhoneNumberStr);
        bindPhoneView.putParams(params);
        manager.addPage(bindPhoneView);


    }

    @Override
    public void setView() {
        iAccountCenterPresenter = new AccountCenterPresenterImpl(context, this);

    }
}
