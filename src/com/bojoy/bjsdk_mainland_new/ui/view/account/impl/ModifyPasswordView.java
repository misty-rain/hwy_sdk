package com.bojoy.bjsdk_mainland_new.ui.view.account.impl;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.presenter.account.IAccountCenterPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.account.impl.AccountCenterPresenterImpl;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseActivityPage;
import com.bojoy.bjsdk_mainland_new.ui.view.IBaseView;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;
import com.bojoy.bjsdk_mainland_new.utils.ToastUtil;
import com.bojoy.bjsdk_mainland_new.widget.ClearEditTextForPassword;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMGFDialog;

/**
 * Created by wutao on 2016/1/11.
 * 修改密码视图
 */
public class ModifyPasswordView extends BaseActivityPage implements IBaseView {

    private final String TAG = ModifyPasswordView.class.getSimpleName();
    private ClearEditTextForPassword mOldPwdEditText, mNewPwdEditText, mConfirmPwdEditText;
    IAccountCenterPresenter iAccountCenterPresenter;
    /**
     * 新增随软键盘状态变化的控件
     */
    private View keyboardView = null;
    private TextView mForget = null;
    private Button mBtnOk = null;

    public ModifyPasswordView(Context context, PageManager manager,
                              BJMGFActivity activity) {
        super(ReflectResourceId.getLayoutId(context,
                  Resource.layout.bjmgf_sdk_dock_account_modifypwd), context, manager, activity);
    }

    public void onCreateView(View view) {
        mOldPwdEditText = (ClearEditTextForPassword) view.findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_float_account_manager_modifypwd_oldPwdEditTextId));
        mNewPwdEditText = (ClearEditTextForPassword) view.findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_float_account_manager_modifypwd_newPwdEditTextId));
        mConfirmPwdEditText = (ClearEditTextForPassword) view.findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_float_account_manager_modifypwd_newConfirmPwdEditTextId));
        keyboardView = (View) view.findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_float_account_manager_modifypwd_keyboard));
        mForget = getView(Resource.id.bjmgf_sdk_forget_pwd_button, view);
        mBtnOk = getView(Resource.id.bjmgf_sdk_float_account_manager_modifypwd_buttonId, view);

        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifyPwd();
            }
        });

        super.onCreateView(view);
    }


    @Override
    public void setView() {
        iAccountCenterPresenter = new AccountCenterPresenterImpl(context, this);
    }

    private void modifyPwd() {
        if (StringUtility.isEmpty(mOldPwdEditText.getEditText().trim())) {
            showError(getString(Resource.string.bjmgf_sdk_login_dialog_invalid_old_password));
            return;
        }

        if (StringUtility.isEmpty(mNewPwdEditText.getEditText().trim())) {
            showError(getString(Resource.string.bjmgf_sdk_new_text));
            return;
        }

        if (StringUtility.isEmpty(mConfirmPwdEditText.getEditText().trim())) {
            showError(getString(Resource.string.bjmgf_sdk_new_confirm_text));
            return;
        }

        if (!mNewPwdEditText.getEditText().trim().equals(mConfirmPwdEditText.getEditText().trim())) {
            showError(getString(Resource.string.bjmgf_sdk_login_dialog_confirm_password_unequal));
            return;
        }

        if (mOldPwdEditText.getEditText().trim().equals(mNewPwdEditText.getEditText().trim())){
            showError(getString(Resource.string.bjmgf_sdk_login_dialog_new_password_unequal));
            return;
        }
        showProgressDialog();
        iAccountCenterPresenter.modifyPassword(context, mOldPwdEditText.getEditText().trim(), mNewPwdEditText.getEditText().trim());
    }

    @Override
    public void showError(String message) {
        dismissProgressDialog();
        ToastUtil.showMessage(context, message);
    }

    /**
     * 修改成功
     */
    @Override
    public void showSuccess() {
        dismissProgressDialog();
        ToastUtil.showMessage(context,getString(Resource.string.bjmgf_sdk_PswModifySuccessedStr));
        IAccountCenterPresenter iAccountCenterPresenter = new AccountCenterPresenterImpl(context, null);
        iAccountCenterPresenter.logout(context);
        BJMGFDialog bjmgfDialog = new BJMGFDialog(context, (Activity) context, BJMGFDialog.Page_Login);
        bjmgfDialog.show();

    }
}
