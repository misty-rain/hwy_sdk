package com.bojoy.bjsdk_mainland_new.ui.view.cs.impl;

import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.model.entity.MyQuestionBean;
import com.bojoy.bjsdk_mainland_new.presenter.cs.ICustomerServicePresenter;
import com.bojoy.bjsdk_mainland_new.presenter.cs.impl.CustomerServicePresenterImpl;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.adapter.MyQuestionListViewAdapter;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseActivityPage;
import com.bojoy.bjsdk_mainland_new.ui.view.cs.IMyQuestionView;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.ToastUtil;

import java.util.ArrayList;

/**
 * <p/>
 * 我的问题页
 * </P>
 * Created by shenliuyong on 2016/1/13.
 */
public class MyQuestionView extends BaseActivityPage implements IMyQuestionView {

    private final String TAG = MyQuestionView.class.getSimpleName();
    private View mView;// 当前view，layout布局
    private BJMGFActivity mActivity;
    private PageManager mRootPageManager;// 根布局页面管理器，返回上一页时要用
    private PageManager mQuestionPageManager;

    private ListView mMyQuestionListView;
    private MyQuestionListViewAdapter mAdapter;
    private ICustomerServicePresenter mICustomerServicePresenter;
    private View mShowErrorView;
    private View mListViewContentView;
    private TextView mErrorTextView;

    /**
     * rootPageManager用来展示问题详细时添加View，questionPageManager目前主要用来显示ProcessDialog
     *
     * @param context             上下文
     * @param rootPageManager     用来展示问题详细时添加View
     * @param questionPageManager 主要用来显示ProcessDialog
     * @param activity            BJMGFActivity
     */
    public MyQuestionView(Context context, PageManager rootPageManager, PageManager questionPageManager, BJMGFActivity activity) {
        super(ReflectResourceId.getLayoutId(activity, "bjmgf_sdk_dock_customer_my_questions_page"),
                context, questionPageManager, activity);
        this.mActivity = activity;
        this.mRootPageManager = rootPageManager;
        this.mQuestionPageManager = questionPageManager;
        mICustomerServicePresenter = new CustomerServicePresenterImpl(context, this);
    }

    public void onCreateView(View parentView) {
        this.mView = parentView;
        initView();
    }

    /**
     * 做数据刷新操作，在PageManager中判断，在onAttach()时，如果layout没有初始化，
     * 则调用onCreateView()后调用onResume()， 否则只调用onResume()，
     * 因为在CustomerServiceView中只创建了一个这个类的实例， onCreateView()只会调用一次，
     * 所以数据的刷新工作就放在onResume()中来做
     */
    @Override
    public void onResume() {
        super.onResume();
        // 做数据刷新操作
        updateView();
    }

    public void initView() {
        mMyQuestionListView = (ListView) mView.findViewById(ReflectResourceId.getViewId(mActivity,
                "bjmgf_sdk_id_my_question_list_view"));
        mShowErrorView = mView.findViewById(ReflectResourceId.getViewId(mActivity,
                "bjmgf_sdk_my_question_is_null_id"));
        mErrorTextView = (TextView) mShowErrorView.findViewById(ReflectResourceId.getViewId(mActivity,
                "bjmgf_sdk_submit_questionNullId"));
        mListViewContentView = mView.findViewById(ReflectResourceId.getViewId(mActivity,
                "bjmgf_sdk_listView_LlId"));
    }

    public void updateView() {
//        Log.d(TAG, "updateView");
        showProgressDialog();
        mICustomerServicePresenter.getMyQuestionDatas(mActivity);

    }


    @Override
    public void showError(String message) {
        dismissProgressDialog();
        //显示错误界面
        showQuestionNullView(mActivity.getString(ReflectResourceId.getStringId(mActivity,
                "bjmgf_sdk_common_question_error")));
        ToastUtil.showMessage(mActivity, message);
    }

    @Override
    public void showSuccess() {
        dismissProgressDialog();
    }

    @Override
    public void setView() {

    }

    /**
     * 显示我的问题列表
     *
     * @param datas
     */
    @Override
    public void showMyQuestion(ArrayList<MyQuestionBean> datas) {
        dismissProgressDialog();
        LogProxy.d(TAG, "showMyQuestion");
        if (datas == null || datas.size() == 0) {
            if (datas == null) {
                LogProxy.d(TAG, "question is null" + (datas == null));
            } else if (datas.size() == 0) {
                LogProxy.d(TAG, "question size=" + datas.size());
            }
            //未提交任何问题
            showQuestionNullView(mActivity.getString(ReflectResourceId.getStringId(mActivity,
                    "bjmgf_sdk_submit_questionIsNullStr")));
        } else {
            showQuestionListView();
            mAdapter = new MyQuestionListViewAdapter(mActivity, datas, mRootPageManager, mActivity);
            mMyQuestionListView.setAdapter(mAdapter);
        }
    }

    /**
     * 如果问题为空，或者发生错误则显示此界面
     */
    private void showQuestionNullView(String text) {
        //显示茶杯
        mMyQuestionListView.setVisibility(View.GONE);
        mListViewContentView.setVisibility(View.GONE);
        mShowErrorView.setVisibility(View.VISIBLE);
        mErrorTextView.setVisibility(View.VISIBLE);
        mErrorTextView.setText(text);
    }

    /**
     * 隐藏茶杯，显示listView
     */
    private void showQuestionListView() {
        mShowErrorView.setVisibility(View.GONE);
        mListViewContentView.setVisibility(View.VISIBLE);
        mMyQuestionListView.setVisibility(View.VISIBLE);
    }
}
