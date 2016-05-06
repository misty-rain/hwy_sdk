package com.bojoy.bjsdk_mainland_new.ui.view.cs.impl;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.eventhandler.event.NormalEvent;
import com.bojoy.bjsdk_mainland_new.support.eventbus.EventBus;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseActivityPage;
import com.bojoy.bjsdk_mainland_new.ui.view.cs.ICustomerView;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;

/**
 * Created by wutao on 2016/1/22.
 */
public class CustomerServiceView extends BaseActivityPage implements ICustomerView {
    private final int TAB_COMMON_QUESTION = 0;
    private final int TAB_SEND_QUESTION = 1;
    private final int TAB_MY_QUESTION = 2;
    private View mView;
    private PageManager mRootPageManager;
    private PageManager mQuestionPageManager;
    private BJMGFActivity mActivity;
    private ViewPager mViewPager;// 可以使用ViewPager，也可以使用LinearLayout对View处理
    private LinearLayout mLinearLayout;// 子页面的根布局
    // 当前页面的各个子页面
    private CommonQuestionView mCommonQuestionPage;
    private SendQuestionView mSendQuestionPage;
    private MyQuestionView mMyQuestionPage;
    private ImageView mBackImageView;
    private TextView mCommonQuestionTextView;
    private TextView mSendQuestionTextView;
    private TextView mMyQuestionTextView;
    private ImageView mCommonQuestionLineImageView;
    private ImageView mSendQuestionLineImageView;
    private ImageView mMyQuestionLineImageView;
    // 为了第一次改变能生效
    private int mCurrentTab = -1;

    /**
     * @param context
     * @param manager
     * @param activity
     *******************/
    public CustomerServiceView(Context context, PageManager manager, BJMGFActivity activity) {
        super(ReflectResourceId
                        .getLayoutId(activity, "bjmgf_sdk_dock_customer_service_center_page"), context,
                manager, activity);
        mActivity = activity;
        mRootPageManager = manager;
    }

    /**
     * <p>
     * 重写onCreateView方法，并在方法体结尾调用父类的onCreateView
     * </P>
     * <p>
     * 父类在onCreateView中调用setView()和setTitle()方法
     * </P>
     */
    @Override
    public void onCreateView(View view) {
        this.mView = view;
        initView();
        super.onCreateView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        // 更新页面数据
        updateView();
    }

    /**
     * 选择性更新
     */
    public void updateView() {
        // 默认展示CommonQuestionPage:常见问题页
        if (mCurrentTab != -1) {
            onTabChanged(mCurrentTab);
        } else {
            showCommonQuestionPage();
        }
    }

    public void initView() {
        showPageWithLinearLayout();
        // 使用ViewPager展示
        mViewPager = (ViewPager) mView.findViewById(ReflectResourceId.getViewId(mActivity,
                "bjmgf_sdk_id_about_question_content_view_pager"));

        mBackImageView = (ImageView) mView.findViewById(ReflectResourceId.getViewId(mActivity,
                "bjmgf_sdk_id_close_customer_center_image_view"));

        mCommonQuestionTextView = (TextView) mView.findViewById(ReflectResourceId.getViewId(
                mActivity, "bjmgf_sdk_id_common_question_text_view"));
        mSendQuestionTextView = (TextView) mView.findViewById(ReflectResourceId.getViewId(
                mActivity, "bjmgf_sdk_id_send_question_text_view"));
        mMyQuestionTextView = (TextView) mView.findViewById(ReflectResourceId.getViewId(mActivity,
                "bjmgf_sdk_id_my_question_text_view"));

        // 初始化标题栏下面的分割线
        mCommonQuestionLineImageView = (ImageView) mView.findViewById(ReflectResourceId.getViewId(
                mActivity, "bjmgf_sdk_id_common_question_line"));
        mSendQuestionLineImageView = (ImageView) mView.findViewById(ReflectResourceId.getViewId(
                mActivity, "bjmgf_sdk_id_send_question_line"));
        mMyQuestionLineImageView = (ImageView) mView.findViewById(ReflectResourceId.getViewId(
                mActivity, "bjmgf_sdk_id_my_question_line"));

        mBackImageView = (ImageView) mView.findViewById(ReflectResourceId.getViewId(mActivity,
                "bjmgf_sdk_id_close_customer_center_image_view"));

        // 为标题栏文字设置监听器
        mCommonQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTabChanged(TAB_COMMON_QUESTION);
            }
        });
        mSendQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTabChanged(TAB_SEND_QUESTION);
            }
        });
        mMyQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTabChanged(TAB_MY_QUESTION);
            }
        });
        mBackImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 可以清空所有view显示root view,也可以结束activity
                // mRootPageManager.showPreviousView();
                mActivity.finish();
            }
        });
    }

    /**
     * 使用LinearLayout动态添加布局，不使用ViewPager滑动
     */
    private final void showPageWithLinearLayout() {
        mLinearLayout = (LinearLayout) mView.findViewById(ReflectResourceId.getViewId(mActivity,
                "bjmgf_sdk_id_about_question_content_linear_layout"));
        mLinearLayout.setVisibility(View.VISIBLE);
        // 把此页面中的LinearLayout设置为子页面的根布局
        mQuestionPageManager = new PageManager(mLinearLayout);
    }

    /**
     * 标题栏点击监听
     *
     * @param index 点击栏
     */
    private void onTabChanged(int index) {
        if (index == mCurrentTab) {
            return;
        }
        mCommonQuestionLineImageView.setVisibility(View.INVISIBLE);
        mSendQuestionLineImageView.setVisibility(View.INVISIBLE);
        mMyQuestionLineImageView.setVisibility(View.INVISIBLE);
        switch (index) {
            case TAB_COMMON_QUESTION:
                mCurrentTab = TAB_COMMON_QUESTION;
                mCommonQuestionLineImageView.setVisibility(View.VISIBLE);
                // 每次点击进去后都会更新界面，在onResume()中写update
                if (mCommonQuestionPage == null) {
                    mCommonQuestionPage = new CommonQuestionView(context, mQuestionPageManager, activity);
                }
                mQuestionPageManager.clean();
                mQuestionPageManager.addPage(mCommonQuestionPage);
                break;
            case TAB_SEND_QUESTION:
                mCurrentTab = TAB_SEND_QUESTION;
                mSendQuestionLineImageView.setVisibility(View.VISIBLE);
                if (mSendQuestionPage == null) {
                    mSendQuestionPage = new SendQuestionView(context, manager, activity);
                }
                mQuestionPageManager.clean();
                mQuestionPageManager.addPage(mSendQuestionPage);
                break;
            case TAB_MY_QUESTION:
                mCurrentTab = TAB_MY_QUESTION;
                mMyQuestionLineImageView.setVisibility(View.VISIBLE);
                if (mMyQuestionPage == null) {
                    mMyQuestionPage = new MyQuestionView(context, mRootPageManager, mQuestionPageManager, activity);
                }
                mQuestionPageManager.clean();
                mQuestionPageManager.addPage(mMyQuestionPage);
                break;
            default:
                break;
        }
    }

    public void onEventMainThread(NormalEvent normalEvent) {
        if (NormalEvent.Go_To_My_Question_Event.equals(normalEvent.getTag())) {
            onTabChanged(TAB_MY_QUESTION);
        }
    }


    /**
     * 清除父类容器的页面内容
     */
    @Override
    public boolean canBack() {
        mQuestionPageManager.clean();

        return true;
    }

    /**
     * 默认展示CommonQuestionPage
     */
    private void showCommonQuestionPage() {
        onTabChanged(TAB_COMMON_QUESTION);
    }

    @Override
    public void setView() {
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void showSuccess() {

    }

}
