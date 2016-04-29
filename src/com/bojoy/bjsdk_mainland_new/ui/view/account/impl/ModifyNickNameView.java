package com.bojoy.bjsdk_mainland_new.ui.view.account.impl;


import android.content.Context;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseActivityPage;
import com.bojoy.bjsdk_mainland_new.ui.page.base.ViewPage;
import com.bojoy.bjsdk_mainland_new.ui.view.IBaseView;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.ToastUtil;
import com.bojoy.bjsdk_mainland_new.widget.ClearEditText;

import java.util.HashMap;
import java.util.Map;

public class ModifyNickNameView extends BaseActivityPage implements IBaseView {

    private ClearEditText nickEdit;
    private RelativeLayout backLayout;
    private Button Okbtn;

    public ModifyNickNameView(Context context, PageManager manager,
                              BJMGFActivity activity) {
        super(ReflectResourceId.getLayoutId(context, Resource.layout.bjmgf_sdk_dock_account_nick),
                context, manager, activity);
    }

    @Override
    public void onCreateView(View view) {
        backLayout = (RelativeLayout) view.findViewById(ReflectResourceId.getViewId(context,
                Resource.id.bjmgf_sdk_dock_account_nick_closeLlId));
        nickEdit = (ClearEditText) view.findViewById(ReflectResourceId.getViewId(context,
                Resource.id.bjmgf_sdk_input_nick));
        Okbtn = (Button) view.findViewById(ReflectResourceId.getViewId(context,
                Resource.id.bjmgf_sdk_finish));

        backLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                manager.previousPage();
            }
        });

        Okbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!checkNickName(nickEdit.getEditText().trim())) {
                    return;
                }
                Map<String,Object> params = new HashMap<String, Object>();
                params.put("nickName",nickEdit.getEditText().trim());
                ViewPage viewPage=manager.getPreviousPage();
                if(viewPage!=null)
                viewPage.putParams(params);
                manager.previousPage();

            }
        });

        //nickEdit.getEdit().addTextChangedListener(new BJMTextWatcher(nickEdit.getEdit()));

        nickEdit.getEdit().setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});

        super.onCreateView(view);
    }


    @Override
    public void setView() {
        nickEdit.setEditText(getParams().get("nickName") != null ? (String)getParams().get("nickName") : "");
        nickEdit.requestFocus();
    }

    /**
     * 验证昵称合法性
     *
     * @param nickName
     * @return
     */
    public boolean checkNickName(String nickName) {
        if (nickName == null) {
            ToastUtil.showMessage(context, getString(Resource.string.bjmgf_sdk_input_nick_len_hint));
            return false;
        }
        int length = nickName.length();
        if (length < 2 || length > 8) {
            ToastUtil.showMessage(context, getString(Resource.string.bjmgf_sdk_input_nick_len_hint));
            return false;
        }
        return true;
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void showSuccess() {

    }
}
