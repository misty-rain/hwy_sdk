package com.bojoy.bjsdk_mainland_new.ui.view.cs.impl;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.model.entity.CommonQuestionListViewBean;
import com.bojoy.bjsdk_mainland_new.presenter.cs.ICustomerServicePresenter;
import com.bojoy.bjsdk_mainland_new.presenter.cs.impl.CustomerServicePresenterImpl;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.adapter.CommonQuestionListViewAdapter;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseActivityPage;
import com.bojoy.bjsdk_mainland_new.ui.view.cs.ICommonQuestionView;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.ToastUtil;

import java.util.ArrayList;

/**
 * <p>
 * 常见问题页面
 * </p>
 * Created by shenliuyong on 2016/1/13.
 */
public class CommonQuestionView extends BaseActivityPage implements ICommonQuestionView {

    private final String TAG = CommonQuestionView.class.getSimpleName();
    // 当前view，layout布局
    private View mView;
    // 未获取到数据提示页
    private View mQuestionNullView;
    // 未获取到数据提示文字
    private TextView mQuestionNullTextView;
    private View mListViewContenView;

    private Activity mActivity;
    // 根布局页面管理器，返回上一页时要用
    private PageManager mRootPageManager;

    private ListView mListView;
    private CommonQuestionListViewAdapter adapter;
    private ICustomerServicePresenter iCustomerServicePresenter;

    /**
     * 构造器
     *
     * @param context  上下文
     * @param manager  此PageManager为CustomerServiceView类中新建的PageManager，非RootPageManager
     * @param activity BJMGFActivity
     */
    public CommonQuestionView(Context context, PageManager manager, BJMGFActivity activity) {
        super(ReflectResourceId.getLayoutId(activity, Resource.layout.bjmgf_sdk_common_question_page),
                context, manager, activity);
        this.mActivity = activity;
        this.mRootPageManager = manager;
        iCustomerServicePresenter = new CustomerServicePresenterImpl(mActivity, this);
    }

    public void onCreateView(View parentView) {
        super.onCreateView(parentView);
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
        // 关闭输入法软盘
        super.onResume();
        // 更新页面数据
        updateView();
    }

    public void initView() {
        mListView = (ListView) mView.findViewById(ReflectResourceId.getViewId(mActivity,
                Resource.id.bjmgf_sdk_common_question_list_view));
        mQuestionNullView = mView.findViewById(ReflectResourceId.getViewId(mActivity,
                Resource.id.bjmgf_sdk_common_question_is_null_id));
        mQuestionNullTextView = (TextView) mQuestionNullView.findViewById(ReflectResourceId
                .getViewId(mActivity, Resource.id.bjmgf_sdk_submit_questionNullId));
        mListViewContenView = mView.findViewById(ReflectResourceId.getViewId(mActivity,
                Resource.id.bjmgf_sdk_id_common_question_list_view_linear_layout));
    }

    public void updateView() {
        showProgressDialog();
        iCustomerServicePresenter.getFAQJson(mActivity);
    }

    @Override
    public void showError(String message) {
        dismissProgressDialog();
        ToastUtil.showMessage(mActivity, message);
        showQuestionNullView();
    }

    @Override
    public void showSuccess() {
        dismissProgressDialog();
        LogProxy.d(TAG, "on success");
    }

    @Override
    public void setView() {
    }

    /**
     * @param datas ListView 数据
     */
    @Override
    public void showCommonQuestion(ArrayList<CommonQuestionListViewBean> datas) {
        dismissProgressDialog();
        LogProxy.d(TAG, "showCommonQuestion");
        if (datas == null || datas.size() == 0) {
            showQuestionNullView();
        } else {
            adapter = new CommonQuestionListViewAdapter(mActivity, datas);
            mListView.setAdapter(adapter);
            adapter.setListView(mListView);
        }

    }

    /**
     * 显示错误界面
     */
    private void showQuestionNullView() {
        // 显示茶杯界面
        mListView.setVisibility(View.GONE);
        mListViewContenView.setVisibility(View.GONE);
        mQuestionNullView.setVisibility(View.VISIBLE);
        mQuestionNullTextView.setText(mActivity.getString(ReflectResourceId.getStringId(mActivity,
                Resource.string.bjmgf_sdk_common_question_error)));
    }
}
