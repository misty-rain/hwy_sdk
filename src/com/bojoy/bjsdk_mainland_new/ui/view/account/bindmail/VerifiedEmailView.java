package com.bojoy.bjsdk_mainland_new.ui.view.account.bindmail;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.bojoy.bjsdk_mainland_new.app.tools.BJMGFSDKTools;
import com.bojoy.bjsdk_mainland_new.presenter.account.IAccountCenterPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.account.impl.AccountCenterPresenterImpl;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseActivityPage;
import com.bojoy.bjsdk_mainland_new.ui.view.IBaseView;
import com.bojoy.bjsdk_mainland_new.utils.*;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMSdkDialog;

/**
 * Created by wutao on 2016/1/19.
 * 验证邮箱地址
 */
public class VerifiedEmailView extends BaseActivityPage implements IBaseView {

    private final String TAG = VerifiedEmailView.class.getSimpleName();

    private Button mBtnResend;
    private TextView mTvShowEmail;
    private String email = "";
    private IAccountCenterPresenter iAccountCenterPresenter;


    public VerifiedEmailView(Context context, PageManager manager,
                             BJMGFActivity activity) {
        super(ReflectResourceId.getLayoutId(context,
                Resource.layout.bjmgf_sdk_dock_account_noverified_email),
                context, manager, activity);
    }

    public void onCreateView(View view) {
        mBtnResend = getView(Resource.id.bjmgf_sdk_floatWindow_accountManager_resendEmail_ok);
        mTvShowEmail = getView(Resource.id.bjmgf_sdk_float_account_manager_resendemail_show);

        setTitle();

        super.onCreateView(view);
    }


    @Override
    public void setView() {
        iAccountCenterPresenter = new AccountCenterPresenterImpl(context, this);

        setTopicStr(getString(Resource.string.bjmgf_sdk_floatWindow_accountManager_bindInvalidateEmailStr));
        setRightButtonStr(getString(Resource.string.bjmgf_sdk_floatWindow_accountManager_editStr));

        email = (String) getParams().get("email");
        mTvShowEmail.setText(email);

        setRightButtonClick(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                BindEmailView page = new BindEmailView(context, manager, activity);
                manager.replacePage(page);
            }
        });

        //返回操作
        setBackListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                manager.backToTopPage();
            }
        });

        //重新发送绑定邮件
        mBtnResend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showProgressDialog();
                iAccountCenterPresenter.bindEmail(context, mTvShowEmail.getText().toString().trim());

            }
        });

      //  if((boolean)getParams().get("isShowSendEmailDialog")){
          //  showDialog();
      //  }

        SpUtil.setStringValue(context, String.valueOf(BJMGFSDKTools.getInstance().getCurrUserData().getUid()),email);

    }

    private void showDialog() {
        String sEmailFormat = getString(Resource.string.bjmgf_sdk_floatWindow_accountManager_bindEmail_resend_str);
        String sFinalStr = String.format(sEmailFormat, email);

        final BJMSdkDialog dialog = new BJMSdkDialog(context);
        dialog.setTitle(Utility
                .getString(
                        Resource.string.bjmgf_sdk_floatWindow_accountManager_bindEmail_resend_title,
                        context));
        dialog.setMessage(sFinalStr);
        dialog.setPositiveButton(
                Utility.getString(
                        Resource.string.bjmgf_sdk_dock_public_wish_to_select_confirmStrLeftStr,
                        context), new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    @Override
    public void showError(String message) {
        dismissProgressDialog();
        ToastUtil.showMessage(context, message);

    }

    @Override
    public void showSuccess() {
        dismissProgressDialog();
        showDialog();


    }
}
