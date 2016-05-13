package com.bojoy.bjsdk_mainland_new.presenter.account.logout;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bojoy.bjsdk_mainland_new.app.tools.BJMGFSDKTools;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BJMGFSdkEvent;
import com.bojoy.bjsdk_mainland_new.support.eventbus.EventBus;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseDialogPage;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMGFDialog;

/**
 * Created by wutao on 2016/5/3.
 */
public class LogoutView extends BaseDialogPage {

    @SuppressWarnings("unused")
    private final String TAG = LogoutView.class.getSimpleName();

    private ImageView mQuitPageButton;
    private Button mQuitSDKButton, mQuitGameButton;
    private EventBus eventBus = EventBus.getDefault();

    public LogoutView(Context context, PageManager manager, BJMGFDialog dialog) {
            super(ReflectResourceId.getLayoutId(context, Resource.layout.bjmgf_sdk_login_out_page),
                  context, manager, dialog);
    }

    @Override
    public void onCreateView(View view) {
        mQuitPageButton = (ImageView)view.findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_loginOut_quitCurrentPageImageViewId));
        mQuitSDKButton = (Button)view.findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_loginOut_quitButtonId));
        mQuitGameButton = (Button)view.findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_loginOut_quitGameButtonId));
        /** 退出按钮 */
        mQuitPageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                quit();
            }
        });
        mQuitSDKButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (BJMGFSDKTools.getInstance().isCurrUserStatusOnLine()) {
                    BJMGFSDKTools.getInstance().setCurrUserStatusOnLine(false);
                    BJMGFSDKTools.getInstance().setCurrUserData(null);
                    eventBus.post(new BJMGFSdkEvent(BJMGFSdkEvent.APP_LOGOUT));
                    quit();
                } else {
                    quit();
                }
            }
        });
        mQuitGameButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                eventBus.post(new BJMGFSdkEvent(BJMGFSdkEvent.APP_EXIT));
                quit();
            }
        });
        super.onCreateView(view);
    }


    @Override
    public void setView() {}
}
