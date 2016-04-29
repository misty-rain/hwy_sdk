package com.bojoy.bjsdk_mainland_new.ui.view.message.impl;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.app.tools.BJMGFSDKTools;
import com.bojoy.bjsdk_mainland_new.model.entity.MessageNotifyData;
import com.bojoy.bjsdk_mainland_new.presenter.message.IMessagePresenter;
import com.bojoy.bjsdk_mainland_new.presenter.message.impl.MessagePresenterImpl;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.adapter.MessageListAdapter;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseActivityPage;
import com.bojoy.bjsdk_mainland_new.ui.view.account.impl.UserInfoView;
import com.bojoy.bjsdk_mainland_new.ui.view.message.IMessageView;
import com.bojoy.bjsdk_mainland_new.utils.DomainUtility;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.ToastUtil;
import com.bojoy.bjsdk_mainland_new.utils.UIUtil;
import com.bojoy.bjsdk_mainland_new.utils.Utility;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMSdkDialog;

import java.util.ArrayList;

import static com.bojoy.bjsdk_mainland_new.utils.Resource.layout.bjmgf_sdk_dock_message_page;

/**
 * Created by wutao on 2016/1/22.
 * 消息通知视图
 */
public class MessageNoticeView extends BaseActivityPage implements IMessageView, MessageListAdapter.MessageItemListener {

    private final String TAG = MessageNoticeView.class.getSimpleName();
    private int SHOW_ITEM_LIMITE = 5;
    private Activity activity;
    private ListView myListView;
    private TextView noMessageTextView;
    private Button loadMoreBtn;
    private ImageView noMessageImage;
    private IMessagePresenter iMessagePresenter;
    private RelativeLayout mBackLayout = null;


    public MessageNoticeView(Context context,
                             PageManager manager, BJMGFActivity activity) {
        super(ReflectResourceId.getLayoutId(context,
                  bjmgf_sdk_dock_message_page), context, manager, activity);
        this.activity = activity;
    }

    @Override
    public void onCreateView(View view) {
        noMessageImage = (ImageView) view.findViewById(ReflectResourceId
                  .getViewId(context,
                            Resource.id.bjmgf_sdk_message_no_message_icon));
        noMessageTextView = (TextView) view.findViewById(ReflectResourceId
                  .getViewId(context,
                            Resource.id.bjmgf_sdk_no_message_tv));
        loadMoreBtn = (Button) view.findViewById(ReflectResourceId
                  .getViewId(context,
                            Resource.id.bjmgf_sdk_add_message_btn));
        myListView = (ListView) view.findViewById(ReflectResourceId
                  .getViewId(context,
                            Resource.id.bjmgf_sdk_messageNotify_listview));
        mBackLayout = (RelativeLayout) view.findViewById(ReflectResourceId.getViewId(context,
                  Resource.id.bjmgf_sdk_dock_closeMessage_backLlId));
        mBackLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                quit();
            }
        });
        loadMoreBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

            }
        });

        super.onCreateView(view);
    }


    @Override
    public void showError(String message) {
        dismissProgressDialog();
        ToastUtil.showMessage(context, message);

    }

    @Override
    public void showSuccess() {
        dismissProgressDialog();
    }

    @Override
    public void setView() {
        //如果当前用户数据为空 ，说明给用户未在好玩友平台注册，
        if (BJMGFSDKTools.getInstance().getCurrUserData() != null) {
            iMessagePresenter = new MessagePresenterImpl(context, this);
            showProgressDialog();
            iMessagePresenter.loadUnReadMsgList(context);
        } else {
            showNeedUpdateDialog();
        }

    }

    /**
     * 没有消息时，显示默认图片
     */
    @Override
    public void showNoResultView() {
        dismissProgressDialog();
        myListView.setVisibility(View.GONE);
        loadMoreBtn.setVisibility(View.GONE);
        noMessageTextView.setVisibility(View.VISIBLE);
        noMessageImage.setVisibility(View.VISIBLE);
    }

    /**
     * 显示消息列表
     *
     * @param messageNotifyDataArrayList 回传的消息list
     */
    @Override
    public void showMsgListView(ArrayList<MessageNotifyData> messageNotifyDataArrayList) {
        dismissProgressDialog();
        MessageListAdapter adapter = new MessageListAdapter(context, this, messageNotifyDataArrayList);
        myListView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(myListView);


    }


    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(listItem.getWidth(), listItem.getHeight());
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                  + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        listView.setLayoutParams(params);
    }

    /**
     * 完善资料dialog
     */
    private void showNeedUpdateDialog() {
        final BJMSdkDialog dialog = new BJMSdkDialog(context);
        dialog.setTitle(Utility.getString(Resource.string.bjmgf_sdk_dock_message_game_notify_update_user_title, context));
        dialog.setMessage(Utility.getString(Resource.string.bjmgf_sdk_dock_message_game_notify_update_user_content, context));
        dialog.setPositiveButton(Utility.getString(Resource.string.bjmgf_sdk_dock_dialog_btn_ok_str,
                  context), new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                UserInfoView page = new UserInfoView(context, manager, (BJMGFActivity) activity);
                manager.addPage(page);
            }
        });

        dialog.setNegativeButton(Utility.getString(Resource.string.bjmgf_sdk_dock_dialog_btn_cancel_str,
                  context), new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                quit();
            }
        });
        dialog.show();
    }

    @Override
    public void onClickMessageItem(int position) {
        goHaoWanYouApp();

    }


    /**
     * 前往打开好玩友应用
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public void goHaoWanYouApp() {
        String packageName = DomainUtility.getInstance().getHwyPackageName(context);
        final String downloadURL = DomainUtility.getInstance().getHwyAppDownloadUrl(context);
        Intent intent = null;
        PackageManager packageManager = context.getPackageManager();
        if (Utility.isInstall(context, packageName)) {
            intent = packageManager.getLaunchIntentForPackage(packageName);
            activity.startActivity(intent);
        } else {
            UIUtil.showDownloadDialog(
                      context, activity, downloadURL,
                      Utility.getString(Resource.string.bjmgf_sdk_dock_dialog_to_download_app_title, context),
                      Utility.getString(Resource.string.bjmgf_sdk_dock_dialog_to_download_app_str, context),
                      Utility.getString(Resource.string.bjmgf_sdk_dock_dialog_to_download_gonow_title, context),
                      Utility.getString(Resource.string.bjmgf_sdk_dock_dialog_btn_cancel_str, context));
        }

    }
}
