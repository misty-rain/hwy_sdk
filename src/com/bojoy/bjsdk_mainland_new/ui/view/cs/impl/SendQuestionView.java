package com.bojoy.bjsdk_mainland_new.ui.view.cs.impl;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.eventhandler.event.FileRevEvent;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.NormalEvent;
import com.bojoy.bjsdk_mainland_new.model.entity.QuestionData;
import com.bojoy.bjsdk_mainland_new.presenter.cs.ICustomerServicePresenter;
import com.bojoy.bjsdk_mainland_new.presenter.cs.impl.CustomerServicePresenterImpl;
import com.bojoy.bjsdk_mainland_new.support.eventbus.EventBus;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseActivityPage;
import com.bojoy.bjsdk_mainland_new.ui.view.cs.IQuestionView;
import com.bojoy.bjsdk_mainland_new.utils.DialogUtil;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.ToastUtil;
import com.bojoy.bjsdk_mainland_new.utils.Utility;

import java.util.Map;

import static com.bojoy.bjsdk_mainland_new.support.eventbus.EventBus.*;

/**
 * 提交问题界面
 * Created by sunhaoyang on 2016/1/29.
 */
public class SendQuestionView extends BaseActivityPage implements View.OnClickListener, IQuestionView {

    private TextView mHAVESEND, textService, textRoleName, textTitle,
            textContent, textTypeOne, textTypeTwo;
    private RelativeLayout reCHANGEID, reSERVICE, reROLENAME,
            reQUESTIONTITLE, reQUEXTIONDETAIL, mSEND, reSELECTTWO;
    private final int PAGE_SERVERNAME = 1;
    private final int PAGE_ROLENAME = 2;
    private final int PAGE_TITLE = 3;
    private final int PAGE_DESC = 4;
    private String[] Type_Contents = new String[]{};
    private QuestionData questionData = new QuestionData();
    private Button mCHAButton = null;
    private ICustomerServicePresenter iCustomerServicePresenter = null;

    public SendQuestionView(Context context, PageManager manager, BJMGFActivity activity) {
        super(ReflectResourceId.getLayoutId(activity, "bjmgf_sdk_send_question"), context,
                manager, activity);
        Type_Contents = context.getResources().getStringArray(ReflectResourceId.getArrayId(context,
                Resource.array.bjmgf_sdk_question_type));
    }

    @Override
    public void onAttach(ViewGroup parent) {
        super.onAttach(parent);
        //注册事件监听，将来或许会在ViewPager中统一注册监听器，那么就要把这行代码删除
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void setView() {
        mCHAButton = getView(Resource.id.bjmgf_sdk_change_file);// 附件按钮
        mSEND = getView(Resource.id.bjmgf_sdk_submit_group);// 提交

        textService = getView(Resource.id.bjmgf_sdk_inputservice); //服务器
        textRoleName = getView(Resource.id.bjmgf_sdk_inputname);// 角色名
        textTitle = getView(Resource.id.bjmgf_sdk_inputtitle);// 标题
        textContent = getView(Resource.id.bjmgf_sdk_inputquestion);// 描述
        textTypeOne = getView(Resource.id.bjmgf_sdk_changetype);// 类型一
        textTypeTwo = getView(Resource.id.bjmgf_sdk_none);// 类型二
        mHAVESEND = getView(Resource.id.bjmgf_sdk_havesend);// 已上传和未上传

        reSERVICE = getView(Resource.id.bjmgf_sdk_selectservice);// 服务器
        reROLENAME = getView(Resource.id.bjmgf_sdk_playername);// 角色名
        reQUESTIONTITLE = getView(Resource.id.bjmgf_sdk_question_title);// 问题标题
        reQUEXTIONDETAIL = getView(Resource.id.bjmgf_sdk_question_detail);// 问题详细
        reCHANGEID = getView(Resource.id.bjmgf_sdk_idfilechanged);// 问题类型step1
        reSELECTTWO = getView(Resource.id.bjmgf_sdk_selectpettwo);// 问题类型step2

        reSERVICE.setOnClickListener(this);
        reROLENAME.setOnClickListener(this);
        reQUESTIONTITLE.setOnClickListener(this);
        reQUEXTIONDETAIL.setOnClickListener(this);
        //类型第一步 默认账号资料修改
        reCHANGEID.setOnClickListener(this);
        //类型第二步
        reSELECTTWO.setOnClickListener(this);
        //上传附件的按钮
        mCHAButton.setOnClickListener(this);
        //提交
        mSEND.setOnClickListener(this);

//        if (bjmgfData.isPlatform()) {
//            reSERVICE.setVisibility(View.GONE);
//            reROLENAME.setVisibility(View.GONE);
//        } else {
//            reSERVICE.setVisibility(View.VISIBLE);
//            reROLENAME.setVisibility(View.VISIBLE);
//        }
        reSERVICE.setVisibility(View.VISIBLE);
        reROLENAME.setVisibility(View.VISIBLE);

        iCustomerServicePresenter = new CustomerServicePresenterImpl(context, this);
        // iAccountCenterPresenter.getAccountVipInfo(context);

        //获取vip等级
        // iAccountCenterPresenter.getAccountVipInfo(context);

    }

    @Override
    public void onClick(View v) {
        if (v == reCHANGEID) {
            showTypedDialog();
            return;
        } else if (v == reSELECTTWO) {
            if (!Utility.stringIsEmpty(textTypeOne.getText().toString())) {
                showSubTypedDialog();
            }
            return;
        } else if (v == reQUEXTIONDETAIL) {
            EditQuestionDescView editDescView = new EditQuestionDescView(context, manager, activity);
            Bundle b = new Bundle();
            b.putString("edit", textContent.getText().toString());
            manager.addViewForResult(this, editDescView, PAGE_DESC, b);
            return;
        } else if (v == mCHAButton) {
            Utility.pickPicture(activity);
            return;
        } else if (v == mSEND) {
            submitQuestion();
            return;
        }
        //输入
        inputEdit(v);
    }

    /**
     * 提交问题
     */
    private void submitQuestion() {
        if (Utility.stringIsEmpty(textTypeOne.getText().toString())) {
            ToastUtil.showMessage(context, getString(Resource.string.bjmgf_sdk_selectquestiontype));
        } else if (Utility.stringIsEmpty(textTitle.getText().toString())) {
            ToastUtil.showMessage(context, getString(Resource.string.bjmgf_sdk_question_send_title_null));
        } else if (Utility.stringIsEmpty(textContent.getText().toString())) {
            ToastUtil.showMessage(context, getString(Resource.string.bjmgf_sdk_question_send_content_null));
        } else {
            DialogUtil.showProgressDialog(context);
            questionData.serverName = textService.getText().toString();
            questionData.roleName = textRoleName.getText().toString();
            questionData.title = textTitle.getText().toString();
            questionData.content = textContent.getText().toString();
            if (!Utility.stringIsEmpty(questionData.filePath)) {
                String path = Utility.saveBitmap(activity, questionData.filePath);
                questionData.setFilaPath(path);
            }
            //questionData.roleID = "1";
            questionData.serverID = "0";
            iCustomerServicePresenter.submitQuestion(context, questionData, questionData.filePath);
        }
    }

    /**
     * 显示一级类型
     */
    private void showTypedDialog() {
        new AlertDialog.Builder(context)
                .setItems(Type_Contents, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        questionData.setType(arg1);
                        textTypeOne.setText(Type_Contents[arg1]);
                        setSubType(0, getSubTypeString(arg1, 0));
                    }
                }).create().show();
    }

    /**
     * 显示二级类型
     */
    private void showSubTypedDialog() {
        final String[] items = questionData.getQuestionSubTypes(context, questionData.typeIndex);

        new AlertDialog.Builder(context)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override

                    public void onClick(DialogInterface arg0, int arg1) {
                        setSubType(arg1, items[arg1]);
                    }
                }).create().show();
    }

    private void setSubType(int subIndex, String text) {
        questionData.setSubType(subIndex, text);
        textTypeTwo.setText(text);
    }

    /**
     * 获取次级问题类型
     *
     * @param typeIndex
     * @param subIndex
     * @return
     */
    private String getSubTypeString(int typeIndex, int subIndex) {
        String[] items = questionData.getQuestionSubTypes(context, typeIndex);
        if (subIndex < 0 || subIndex >= items.length) {
            return null;
        }
        return items[subIndex];
    }

    /**
     * 跳转到输入页面
     *
     * @param v
     */
    private void inputEdit(View v) {
        EditQuestionView editView = new EditQuestionView(context, manager, activity);
        int type = -1;
        Bundle b = new Bundle();
        String title = "";
        String desc = "";
        String edit = "";
        if (v == reSERVICE) {
            type = PAGE_SERVERNAME;
            title = Utility.getString(Resource.string.bjmgf_sdk_service, context);
            desc = Utility.getString(Resource.string.bjmgf_sdk_inputservice, context);
            edit = textService.getText().toString();
        } else if (v == reROLENAME) {
            type = PAGE_ROLENAME;
            title = Utility.getString(Resource.string.bjmgf_sdk_rolename, context);
            desc = Utility.getString(Resource.string.bjmgf_sdk_inputrolename, context);
            edit = textRoleName.getText().toString();
        } else if (v == reQUESTIONTITLE) {
            type = PAGE_TITLE;
            title = Utility.getString(Resource.string.bjmgf_sdk_questiontitle, context);
            desc = Utility.getString(Resource.string.bjmgf_sdk_inputquestiontitle, context);
            edit = textTitle.getText().toString();
        }

        b.putString("title", title);
        b.putString("desc", desc);
        b.putString("edit", edit);
        manager.addViewForResult(this, editView, type, b);
    }

    @Override
    public void onViewResult(int sign) {
        String str = mBundle.getString("info");
        switch (sign) {
            case PAGE_SERVERNAME:
                textService.setText(str);
                questionData.serverName = str;
                break;
            case PAGE_ROLENAME:
                textRoleName.setText(str);
                questionData.roleName = str;
                break;
            case PAGE_TITLE:
                textTitle.setText(str);
                questionData.title = str;
                break;
            case PAGE_DESC:
                textContent.setText(str);
                questionData.content = str;
                break;
            default:
                break;
        }
    }

    private void attachFile(boolean attachFlag) {
        if (attachFlag) {
            mCHAButton.setText(getString(Resource.string.bjmgf_sdk_change));        // 按鈕标识为修改
            mHAVESEND.setText(getString(Resource.string.bjmgf_sdk_havesendfile));    // 文字标识为已添加
        } else {
            mCHAButton.setText(getString(Resource.string.bjmgf_sdk_sendbutton));    // 按鈕标识为上传
            mHAVESEND.setText(getString(Resource.string.bjmgf_sdk_havenotsendfile));// 文字标识为未上传s
        }
    }

    private void refresh() {
        if (!Utility.stringIsEmpty(questionData.title)) {
            textTitle.setText(questionData.title.trim());
        } else {
            textTitle.setText(null);
        }
        if (!Utility.stringIsEmpty(questionData.serverName)) {
            textService.setText(questionData.serverName.trim());
        } else {
            textService.setText(null);
        }
        if (!Utility.stringIsEmpty(questionData.roleName)) {
            textRoleName.setText(questionData.roleName.trim());
        } else {
            textRoleName.setText(null);
        }
        if (!Utility.stringIsEmpty(questionData.content)) {
            textContent.setText(questionData.content.trim());
        } else {
            textContent.setText(null);
        }
        if (!Utility.stringIsEmpty(questionData.filePath)) {
            attachFile(true);
        } else {
            attachFile(false);
        }
        if (questionData.typeIndex >= 0) {
            textTypeOne.setText(Type_Contents[questionData.typeIndex]);
        } else {
            textTypeOne.setText(null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void showError(String message) {
        DialogUtil.dismissProgressDialog();
        ToastUtil.showMessage(context,message);
    }

    @Override
    public void showSuccess() {
        ToastUtil.showMessage(context, getString(Resource.string.bjmgf_sdk_question_send_success));
        questionData = new QuestionData();
        refresh();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                DialogUtil.dismissProgressDialog();
                getDefault().post(new NormalEvent(NormalEvent.Go_To_My_Question_Event));
            }
        }, 1000);
    }

    @Override
    public void showUserVipInfo(Map<String, String> map) {
        dismissProgressDialog();
    }

    public void onEventMainThread(FileRevEvent event) {
        attachFile(true);
        questionData.setFilaPath(event.getFilePath());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //EventBus取消注册
        if (getDefault().isRegistered(this)) {
            getDefault().unregister(this);
        }
    }
}
