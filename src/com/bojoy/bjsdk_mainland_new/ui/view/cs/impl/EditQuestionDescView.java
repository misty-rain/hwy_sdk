package com.bojoy.bjsdk_mainland_new.ui.view.cs.impl;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseActivityPage;
import com.bojoy.bjsdk_mainland_new.utils.BJMTextWatcher;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.ToastUtil;

/**
 * 输入问题详情
 * Created by sunhaoyang on 2016/1/29.
 */
public class EditQuestionDescView extends BaseActivityPage {

    private TextView numTextView = null;
    private EditText ediQuestion = null;
    private RelativeLayout mBackBtnLayout = null;
    private Button btFINISH = null;
    private String content;

    public EditQuestionDescView(Context context, PageManager manager, BJMGFActivity activity) {
        super(ReflectResourceId.getLayoutId(activity, "bjmgf_sdk_send_question_detailtext"), context,
                manager, activity);
    }

    @Override
    public void setView() {
        numTextView = getView(Resource.id.bjmgf_sdk_number);
        ediQuestion = getView(Resource.id.bjmgf_sdk_questiondetails);
        mBackBtnLayout = getView(Resource.id.bjmgf_sdk_customerCenter_closeLlId);
        btFINISH = getView(Resource.id.bjmgf_sdk_finish);

        /**
         * 返回
         * */
        mBackBtnLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                manager.previousPage();
            }
        });
        /**
         * 完成
         * */
        btFINISH.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                /** bug 修复1 */
                /** 增加少于10个字符的判断 **/
                if (content != null && content.length() < 10) {
                    ToastUtil.showMessage(context, getString(Resource.string.bjmgf_sdk_at_least_ten));
                }
                else {
                    mBundle.putString("info", ediQuestion.getText().toString().trim());
                    goBack();
                }
            }
        });
        ediQuestion.setText(getBundle().getString("edit"));

        /**
         * 编辑框统计写了几个字符
         * */

        ediQuestion.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                content = ediQuestion.getText().toString().trim();
                numTextView.setText(content.length() + "");
            }

        });
        ediQuestion.requestFocus();
		/*InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);*/

        ediQuestion.addTextChangedListener(new BJMTextWatcher(ediQuestion));
        ediQuestion.setFilters(new InputFilter[] { new InputFilter.LengthFilter(200) });
    }
}
