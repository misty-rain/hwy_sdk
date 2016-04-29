package com.bojoy.bjsdk_mainland_new.ui.view.account.bindmail;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseActivityPage;
import com.bojoy.bjsdk_mainland_new.ui.view.IBaseView;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.SpUtil;

/**
 * Created by wutao on 2016/1/19.
 * 修改绑定邮箱 视图
 */
public class DisplayEmailView extends BaseActivityPage implements IBaseView {

    private final String TAG = DisplayEmailView.class.getSimpleName();
    private TextView mTvShow;

    public DisplayEmailView(Context context, PageManager manager,
                            BJMGFActivity activity) {
        super(ReflectResourceId.getLayoutId(context,
                Resource.layout.bjmgf_sdk_dock_account_modifyemail), context,
                manager, activity);
    }

    public void onCreateView(View view) {
        setTitle();
        mTvShow = getView(Resource.id.bjmgf_sdk_float_account_manager_bindemail_show);
        super.onCreateView(view);
    }


    @Override
    public void setView() {
        hideRightBtn();
        setTopicStr(getString(Resource.string.bjmgf_sdk_floatWindow_accountManager_bindEmailStr_topic));
        mTvShow.setText(getParams().get("email").toString());
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void showSuccess() {

    }
}
