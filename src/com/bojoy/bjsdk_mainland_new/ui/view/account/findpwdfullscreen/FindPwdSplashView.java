package com.bojoy.bjsdk_mainland_new.ui.view.account.findpwdfullscreen;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseActivityPage;
import com.bojoy.bjsdk_mainland_new.ui.view.IBaseView;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;

/**
 * Created by wutao on 2016/5/23.
 * 全屏化找回密码模块
 */
public class FindPwdSplashView extends BaseActivityPage implements IBaseView {

    private final String TAG = FindPwdSplashView.class.getSimpleName();

    private Button mBtnPhone, mBtnEmail = null;

    private TextView mTvFindPersonal = null;
    private BaseActivityPage activityPage;

    public FindPwdSplashView(Context context,
                             PageManager manager, BJMGFActivity activity) {
        super(ReflectResourceId.getLayoutId(context,
                  Resource.layout.bjmgf_sdk_account_forget_pwd_splash_fullscreen), context, manager, activity);
    }

    @Override
    public void onCreateView(View view) {
        mBtnPhone = (Button) getView(Resource.id.bjmgf_sdk_find_password_splash_phone);
        mBtnEmail = (Button) getView(Resource.id.bjmgf_sdk_find_password_splash_email);
        mTvFindPersonal = (TextView) getView(Resource.id.bjmgf_sdk_find_password_bottom_personal);
        //手机号找回
        mBtnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityPage = new FindPwdByPhoneCodeView(context, manager, activity);
                manager.addPage(activityPage);
            }
        });
        //邮箱找回
        mBtnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        super.onCreateView(view);
    }

    @Override
    public void setView() {
        hideRightBtn();
        setTopicStr(getString(Resource.string.bjmgf_sdk_floatWindow_findpwd_title));
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void showSuccess() {

    }
}
