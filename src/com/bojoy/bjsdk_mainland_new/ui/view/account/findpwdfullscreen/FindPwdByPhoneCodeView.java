package com.bojoy.bjsdk_mainland_new.ui.view.account.findpwdfullscreen;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bojoy.bjsdk_mainland_new.presenter.account.findpwd.IPswFindPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.account.findpwd.impl.PswFindPresenterImpl;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseActivityPage;
import com.bojoy.bjsdk_mainland_new.ui.view.IBaseView;
import com.bojoy.bjsdk_mainland_new.ui.view.account.findpwd.IPswFindView;
import com.bojoy.bjsdk_mainland_new.ui.view.account.findpwd.impl.FindPwdCheckPage;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;
import com.bojoy.bjsdk_mainland_new.utils.ToastUtil;
import com.bojoy.bjsdk_mainland_new.widget.ClearEditText;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wutao on 2016/5/24.
 * 找回密码通过手机验证码
 */
public class FindPwdByPhoneCodeView extends BaseActivityPage implements IPswFindView {

    private final String TAG = FindPwdByPhoneCodeView.class.getSimpleName();
    private ClearEditText mAccountEditText;
    private IPswFindPresenter mIPswFindPresenter;
    private String currentAccount = "";


    public FindPwdByPhoneCodeView(Context context,
                                  PageManager manager, BJMGFActivity activity) {
        super(ReflectResourceId.getLayoutId(context,
                  Resource.layout.bjmgf_sdk_find_password_by_input_phonenum_fullscreen), context, manager, activity);
    }

    @Override
    public void onCreateView(View view) {
        mAccountEditText = (ClearEditText) view.findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_find_pwd_nameEditTextId));
        super.onCreateView(view);
    }


    @Override
    public void setView() {
        mIPswFindPresenter = new PswFindPresenterImpl(context, this);
        setTopicStr(getString(Resource.string.bjmgf_sdk_floatWindow_findpwd_title));
        setRightButtonStr(getString(Resource.string.bjmgf_sdk_find_password_nextStepBtnStr));
        setRightButtonClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!StringUtility.checkFindPasswordPageAccountValid(context, mAccountEditText.getEditText())) {
                    return;
                }
                currentAccount = mAccountEditText.getEditText();
                showProgressDialog();
                mIPswFindPresenter.getVerifyCode(context, mAccountEditText.getEditText());
            }
        });

    }

    @Override
    public void showError(String message) {
        dismissProgressDialog();
        ToastUtil.showMessage(context, message,true);
    }

    @Override
    public void mobileFindSuccess(String mobileKey) {
        dismissProgressDialog();
        LogProxy.i(TAG, "mobileFindSuccess");
        Bundle bundle = new Bundle();
        bundle.putString("mobileKey", mobileKey);
        bundle.putString("account", currentAccount);
        FindPwdCheckView findPwdCheckPage = new FindPwdCheckView(context, manager, activity);
        findPwdCheckPage.setBundle(bundle);
        manager.replacePage(findPwdCheckPage);
    }

    @Override
    public void showSuccess() {
        // TODO 自动生成的方法存根

    }
}
