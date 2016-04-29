package com.bojoy.bjsdk_mainland_new.ui.view.account.findpwd.impl;


import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.bojoy.bjsdk_mainland_new.presenter.account.findpwd.IPswFindPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.account.findpwd.impl.PswFindPresenterImpl;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseDialogPage;
import com.bojoy.bjsdk_mainland_new.ui.view.account.findpwd.IPswFindView;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;
import com.bojoy.bjsdk_mainland_new.utils.ToastUtil;
import com.bojoy.bjsdk_mainland_new.widget.ClearEditText;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMGFDialog;

import java.util.HashMap;
import java.util.Map;

import static com.bojoy.bjsdk_mainland_new.utils.Resource.layout.bjmgf_sdk_find_password_page;

/**
 * @author zhouhonghai
 *         FindPasswordPage 找回密码页面获取验证码
 */
public class FindPasswordPage extends BaseDialogPage implements IPswFindView {

    private final String TAG = FindPasswordPage.class.getSimpleName();

    private Button mNextStepButton;
    private ClearEditText mAccountEditText;

    private IPswFindPresenter mIPswFindPresenter;
    private String currentAccount = "";

    public FindPasswordPage(Context context, PageManager manager,
                            BJMGFDialog dialog) {
        super(ReflectResourceId.getLayoutId(context, bjmgf_sdk_find_password_page),
                  context, manager, dialog);
    }

    @Override
    public void onCreateView(View view) {
        mNextStepButton = (Button) view.findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_find_password_nextStepBtnId));
        mAccountEditText = (ClearEditText) view.findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_find_pwd_nameEditTextId));
        /** 下一步 */
        mNextStepButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!StringUtility.checkFindPasswordPageAccountValid(context, mAccountEditText.getEditText())) {
                    return;
                }
                currentAccount = mAccountEditText.getEditText();
                showProgressDialog();
                mIPswFindPresenter.getVerifyCode(context, mAccountEditText.getEditText());
            }
        });
        super.onCreateView(view);
    }

    @Override
    public void setView() {
        mIPswFindPresenter = new PswFindPresenterImpl(context, this);
    }

    @Override
    public void showError(String message) {
        dismissProgressDialog();
        ToastUtil.showMessage(context, message);
    }

    @Override
    public void mobileFindSuccess(String mobileKey) {
        dismissProgressDialog();
        LogProxy.i(TAG, "mobileFindSuccess");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("mobileKey", mobileKey);
        params.put("account", currentAccount);
        FindPwdCheckPage findPwdCheckPage = new FindPwdCheckPage(context, manager, dialog);
        findPwdCheckPage.putParams(params);
        manager.replacePage(findPwdCheckPage);
    }

    @Override
    public void showSuccess() {
        // TODO 自动生成的方法存根

    }
}
