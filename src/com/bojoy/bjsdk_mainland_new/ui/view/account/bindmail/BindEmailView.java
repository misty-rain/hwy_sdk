package com.bojoy.bjsdk_mainland_new.ui.view.account.bindmail;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
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
import com.bojoy.bjsdk_mainland_new.widget.ClearEditText;

import java.util.HashMap;
import java.util.Map;

import static com.bojoy.bjsdk_mainland_new.utils.Resource.layout.bjmgf_sdk_dock_account_bindemail;

/**
 * Created by wutao on 2016/1/19.
 * 绑定邮箱视图
 */
public class BindEmailView extends BaseActivityPage implements IBaseView{

    private final String TAG = BindEmailView.class.getSimpleName();
    private ClearEditText mTvEmail;
    private String email = "";
    private IAccountCenterPresenter iAccountCenterPresenter;

    public BindEmailView(Context context, PageManager manager,
                             BJMGFActivity activity) {
        super(ReflectResourceId.getLayoutId(context,
                bjmgf_sdk_dock_account_bindemail), context, manager, activity);
    }

    public void onCreateView(View view) {
        setTitle();

        mTvEmail = getView(Resource.id.bjmgf_sdk_float_account_manager_bindemail_editTextId);

        setRightButtonClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mTvEmail.getEditText();
                if(StringUtility.isEmail(email)) {
                    showProgressDialog();
                    iAccountCenterPresenter.bindEmail(context,email);
                }else{
                    showError(getString(Resource.string.bjmgf_sdk_find_bind_email_input_email_faile));
                }
            }
        });
        super.onCreateView(view);

    }

    @Override
    public void setView() {
        iAccountCenterPresenter = new AccountCenterPresenterImpl(context,this);
    }

    @Override
    public void showError(String message) {
        dismissProgressDialog();
        ToastUtil.showMessage(context,message);

    }

    @Override
    public void showSuccess() {
        dismissProgressDialog();
        BaseActivityPage baseActivityPage = new VerifiedEmailView(context,manager,activity);
        Bundle bundle = new Bundle();
        bundle.putString("email",email);
        bundle.putString("isShowSendEmailDialog","true");
        baseActivityPage.setBundle(bundle);
        manager.addPage(baseActivityPage);

    }
}
