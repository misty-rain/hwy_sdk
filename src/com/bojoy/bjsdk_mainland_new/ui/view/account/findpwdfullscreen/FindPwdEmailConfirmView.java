package com.bojoy.bjsdk_mainland_new.ui.view.account.findpwdfullscreen;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseActivityPage;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;

/**
 * Created by wutao on 2016/5/25.
 */
public class FindPwdEmailConfirmView extends BaseActivityPage {

    private final String TAG = FindPwdEmailConfirmView.class.getSimpleName();

    private String mEmail = "";

    private TextView mTvContent;

    public FindPwdEmailConfirmView(Context context,
                                   PageManager manager, BJMGFActivity activity) {
        super(ReflectResourceId.getLayoutId(context,
                  Resource.layout.bjmgf_sdk_find_pwd_email_confirm_fullscreen), context, manager, activity);
    }

    @Override
    public void onCreateView(View view) {
        mTvContent = (TextView) getView(Resource.id.bjmgf_sdk_find_pwd_email_confirm_text);


        super.onCreateView(view);
    }

    @Override
    public void setView() {
        setTopicStr(getString(Resource.string.bjmgf_sdk_new_confirm_text));
        mTvContent.setText(StringUtility.convertStringFormat(mEmail, getString(Resource.string.bjmgf_sdk_floatWindow_accountManager_bindEmail_resend_str)));

        setRightButtonClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager.backToTopPage();
            }
        });
    }

    @Override
    public void goBack() {
        super.goBack();
    }

    @Override
    public boolean canBack() {
        return false;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

}
