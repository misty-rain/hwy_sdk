package com.bojoy.bjsdk_mainland_new.ui.view.account.findpwdfullscreen;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.presenter.account.findpwd.IPswFindByEmailPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.account.findpwd.impl.PswFindByEmailPresenterImpl;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseActivityPage;
import com.bojoy.bjsdk_mainland_new.ui.view.IBaseView;
import com.bojoy.bjsdk_mainland_new.ui.view.account.findpwd.IPswFindByEmailView;
import com.bojoy.bjsdk_mainland_new.ui.view.account.findpwd.impl.FindPwdEmailConfirmPage;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;
import com.bojoy.bjsdk_mainland_new.utils.ToastUtil;
import com.bojoy.bjsdk_mainland_new.widget.ClearEditText;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMGFDialog;

/**
 * Created by wutao on 2016/5/25.
 */
public class FindPwdEmailView extends BaseActivityPage implements IPswFindByEmailView {

    private final String TAG = FindPwdEmailView.class.getSimpleName();

    private ClearEditText mEtPassport, mEtEmail;

    IPswFindByEmailPresenter mIPswFindByEmailPresenter;

    private String passport = "";
    private String email = "";


    public FindPwdEmailView(Context context,
                            PageManager manager, BJMGFActivity activity) {
        super(ReflectResourceId.getLayoutId(context,
                  Resource.layout.bjmgf_sdk_find_pwd_email_fullscreen), context, manager, activity);
    }

    @Override
    public void onCreateView(View view) {
        mEtPassport = (ClearEditText) getView(Resource.id.bjmgf_sdk_find_pwd_email_passport);
        mEtEmail = (ClearEditText) getView(Resource.id.bjmgf_sdk_find_pwd_email);

        super.onCreateView(view);
    }

    @Override
    public void setView() {
        setTopicStr(getString(Resource.string.bjmgf_sdk_floatWindow_findpwd_title));
        setRightButtonStr(getString(Resource.string.bjmgf_sdk_find_password_nextStepBtnStr));
        mIPswFindByEmailPresenter = new PswFindByEmailPresenterImpl(context,
                  this);
        setRightButtonClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passport = mEtPassport.getEditText();
                email = mEtEmail.getEditText();
                if (StringUtility.isEmpty(passport)) {
                    showError(getString(Resource.string.bjmgf_sdk_login_dialog_invalid_account));
                    return;
                }

                if (StringUtility.isEmpty(email)) {
                    showError(getString(Resource.string.bjmgf_sdk_InputEmailNullStr));
                    return;
                }

                if (!StringUtility.isEmail(email)) {
                    showError(getString(Resource.string.bjmgf_sdk_InputEmailErrorStr));
                    return;
                }
                showProgressDialog();
                mIPswFindByEmailPresenter.getEmailVerifyCode(context,
                          passport, email);
            }
        });

    }

    @Override
    public void emailFindSuccess(String email) {
        dismissProgressDialog();
        FindPwdEmailConfirmView page = new FindPwdEmailConfirmView(context,
                  manager, activity);
        page.setEmail(email);
        manager.addPage(page);
    }

    @Override
    public void goBack() {
        super.goBack();
    }

    @Override
    public boolean canBack() {
        return false;
    }

    @Override
    public void showError(String message) {
        dismissProgressDialog();
        ToastUtil.showMessage(context, message, true);
    }

    @Override
    public void showSuccess() {
        dismissProgressDialog();
    }


}
