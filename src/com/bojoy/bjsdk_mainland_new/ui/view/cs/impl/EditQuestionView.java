package com.bojoy.bjsdk_mainland_new.ui.view.cs.impl;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseActivityPage;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.widget.ClearEditText;

/**
 * 输入问题内容
 * Created by sunhaoyang on 2016/1/29.
 */
public class EditQuestionView extends BaseActivityPage {

    private Button btnSave = null;
    private ClearEditText etInfo = null;
    private TextView tvTitle = null;
    private RelativeLayout rlClose = null;

    public EditQuestionView(Context context, PageManager manager, BJMGFActivity activity) {
        super(ReflectResourceId.getLayoutId(activity, "bjmgf_sdk_send_question_inputservice"), context,
                manager, activity);
    }

    @Override
    public void setView() {
        btnSave = getView(Resource.id.bjmgf_sdk_finish);
        etInfo = getView(Resource.id.bjmgf_sdk_input_service);
        tvTitle = getView(Resource.id.bjmgf_sdk_customerCenterTextViewId);
        rlClose = getView(Resource.id.bjmgf_sdk_customerCenter_closeLlId);

        tvTitle.setText(getBundle().getString("title"));
        etInfo.getEdit().setHint(getBundle().getString("desc"));
        etInfo.getEdit().setText(getBundle().getString("edit"));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBundle.putString("info", etInfo.getEditText());
                goBack();
            }
        });

        rlClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.previousPage();
            }
        });
    }
}
