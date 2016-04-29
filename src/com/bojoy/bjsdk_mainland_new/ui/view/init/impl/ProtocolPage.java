package com.bojoy.bjsdk_mainland_new.ui.view.init.impl;


import android.content.Context;
import android.view.View;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseDialogPage;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMGFDialog;

/**
 * @author xuxiaobin
 *         ProtocolPage 用户协议
 */
public class ProtocolPage extends BaseDialogPage {

    public ProtocolPage(Context context, PageManager manager, BJMGFDialog dialog) {
        super(ReflectResourceId.getLayoutId(context, Resource.layout.bjmgf_sdk_protocol_page),
                context, manager, dialog);
        this.dialog = dialog;
    }

    @Override
    public void onCreateView(View view) {
        view.findViewById(ReflectResourceId.getViewId(context,
                Resource.id.bjmgf_sdk_protocolCloseImageViewId)).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        dialog.dismiss();
                    }

                });
        super.onCreateView(view);
    }


    @Override
    public void setView() {
    }


}
