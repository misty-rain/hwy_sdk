package com.bojoy.bjsdk_mainland_new.ui.view.account.findpwdfullscreen;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.app.BJMGFSdk;
import com.bojoy.bjsdk_mainland_new.presenter.account.findpwd.IFindPswResetPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.account.findpwd.impl.FindPswResetPresenterImpl;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseActivityPage;
import com.bojoy.bjsdk_mainland_new.ui.view.IBaseView;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;
import com.bojoy.bjsdk_mainland_new.utils.ToastUtil;
import com.bojoy.bjsdk_mainland_new.widget.ClearEditText;

/**
 * Created by wutao on 2016/5/24.
 * 密码重置
 */
public class FindPwdResetView extends BaseActivityPage implements IBaseView {

    private final String TAG = FindPwdResetView.class.getSimpleName();

    IFindPswResetPresenter mFindPswResetPresenter;

    private TextView mPhoneNumEditText;
    private ClearEditText mPwdEditText;

    public FindPwdResetView(Context context,
                            PageManager manager, BJMGFActivity activity) {
        super(ReflectResourceId.getLayoutId(context,
                  Resource.layout.bjmgf_sdk_find_password_reset_page_fullscreen), context, manager, activity);
    }

    @Override
    public void onCreateView(View view) {
        mPhoneNumEditText = (TextView) view.findViewById(ReflectResourceId
                  .getViewId(context,
                            Resource.id.bjmgf_sdk_findPwd_nameEditTextId));
        mPwdEditText = (ClearEditText) view.findViewById(ReflectResourceId
                  .getViewId(context,
                            Resource.id.bjmgf_sdk_findPwd_passwordEditTextId));
        super.onCreateView(view);
    }

    @Override
    public void setView() {
        setTopicStr(getString(Resource.string.bjmgf_sdk_find_password_resetPasswordBtnStr));
        String hint = mPhoneNumEditText.getText().toString();

        mFindPswResetPresenter = new FindPswResetPresenterImpl(context, this);
       // mPhoneNumEditText.setText(String.format(hint, StringUtility
         //         .getMoblieKey(getBundle().getString("mobileKey"), 1)));


        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager.backToTopPage();
            }
        });

        setRightButtonClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!StringUtility.checkPasswordValid(context,
                          mPwdEditText.getEditText())) {
                    return;
                }

                showProgressDialog();
                mFindPswResetPresenter.findPswReset(context, getBundle().getString("mobileKey"), getBundle().getString("checkCode"),
                          mPwdEditText.getEditText());
            }
        });


    }

    @Override
    public void showError(String message) {
        dismissProgressDialog();
        ToastUtil.showMessage(context, message, true);
        LogProxy.i(TAG, message);
    }

    @Override
    public void showSuccess() {
        dismissProgressDialog();
        ToastUtil
                  .showMessage(
                            context,
                            getString(Resource.string.bjmgf_sdk_find_password_resetSuccessedTextViewStr));
        manager.clean();
        BJMGFSdk.getDefault().switchAccount(context, Resource.string.bjmgf_sdk_floatWindow_accountManager_mofifypwdsuccess_relogin);
    }
}
