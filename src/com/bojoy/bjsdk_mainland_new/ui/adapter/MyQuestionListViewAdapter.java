package com.bojoy.bjsdk_mainland_new.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.model.entity.MyQuestionBean;
import com.bojoy.bjsdk_mainland_new.presenter.cs.ICustomerServicePresenter;
import com.bojoy.bjsdk_mainland_new.presenter.cs.impl.CustomerServicePresenterImpl;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.view.cs.IMyQuestionListView;
import com.bojoy.bjsdk_mainland_new.ui.view.cs.impl.MyQuestionDetailView;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.ToastUtil;

import java.util.ArrayList;

/**
 * 我的问题页(不是问题详情页)适配器
 *
 * @author shenliuyong
 */
public class MyQuestionListViewAdapter extends BaseAdapter implements IMyQuestionListView {

    private final String TAG = MyQuestionListViewAdapter.class.getSimpleName();
    private ArrayList<MyQuestionBean> mDatas;
    private Context mContext;
    private LayoutInflater inflater;
    private PageManager mRootPageManager;
    private MyQuestionDetailView mMyQuestionDetailPage;
    private BJMGFActivity mActivity;
    private ICustomerServicePresenter iCustomerServicePresenter;

    public MyQuestionListViewAdapter(Context context, ArrayList<MyQuestionBean> datas,
                                     PageManager pageManager, BJMGFActivity activity) {
        this.mDatas = datas;
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.mRootPageManager = pageManager;
        this.mActivity = activity;
        iCustomerServicePresenter = new CustomerServicePresenterImpl(context, this);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(ReflectResourceId.getLayoutId(mContext,
                    "bjmgf_sdk_dock_customer_my_questions_item"), null);
            // 初始化一些控件
            viewHolder.myQuestionTitleTextView = (TextView) convertView
                    .findViewById(ReflectResourceId.getViewId(mContext,
                            "bjmgf_sdk_id_my_question_title_text_view"));
            viewHolder.myQuestionTimeTextView = (TextView) convertView
                    .findViewById(ReflectResourceId.getViewId(mContext,
                            "bjmgf_sdk_id_my_question_created_time_text_view"));
            viewHolder.myQuestionEvaluateButton = (Button) convertView
                    .findViewById(ReflectResourceId.getViewId(mContext,
                            "bjmgf_sdk_myquestion_evaluateBtnId"));
            viewHolder.flagImageView = (ImageView) convertView.findViewById(ReflectResourceId.getViewId(
                    mContext, "bjmgf_sdk_id_my_question_server_flag"));
            viewHolder.myQuestionStateTextView = (TextView) convertView.findViewById(ReflectResourceId.getViewId(
                    mContext, "bjmgf_sdk_myquestion_stateTvId"));
            viewHolder.myQuestionEvaluateTextView = (TextView) convertView.findViewById(ReflectResourceId.getViewId(
                    mContext, "bjmgf_sdk_myquestion_evaluatedTvId"));

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 相当于listview item的事件响应
                handleItemClick(position);
            }
        });
        viewHolder.myQuestionEvaluateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 为buttton设置事件监听相应
                handleEvaluateButtonClick(position);
            }
        });
        // 为控件设置资源
        //设置标题
        viewHolder.myQuestionTitleTextView.setText(mDatas.get(position).getTitle());
        //设置时间
        viewHolder.myQuestionTimeTextView.setText(mDatas.get(position).getCreate());
        //已读或未读判断
        if ("0".equals(mDatas.get(position).getRead())) {
            //未读
            viewHolder.flagImageView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.flagImageView.setVisibility(View.GONE);
        }
        //是否可以评价
        if ("1".equals(mDatas.get(position).getEvaluate())) {
            //可以评价
            viewHolder.myQuestionEvaluateButton.setVisibility(View.VISIBLE);
            viewHolder.myQuestionEvaluateTextView.setVisibility(View.GONE);
        } else if ("0".equals(mDatas.get(position).getEvaluate())) {
            //不可以评价
            viewHolder.myQuestionEvaluateButton.setVisibility(View.GONE);
            viewHolder.myQuestionEvaluateTextView.setVisibility(View.VISIBLE);
        }
        //viewHolder.myQuestionEvaluateButton.setVisibility(View.VISIBLE);
        //问题状态
        if ("0".equals(mDatas.get(position).getState())) {
            //处理中，不可评价
            viewHolder.myQuestionStateTextView.setText(mActivity.getString(ReflectResourceId.getStringId(mActivity,
                    "bjmgf_sdk_myQuestionDealingStr")));
            //不可以评价
            viewHolder.myQuestionEvaluateButton.setVisibility(View.GONE);
            viewHolder.myQuestionEvaluateTextView.setVisibility(View.GONE);
        } else if ("1".equals(mDatas.get(position).getState())) {
            //已结单
            viewHolder.myQuestionStateTextView.setText(mActivity.getString(ReflectResourceId.getStringId(mActivity,
                    "bjmgf_sdk_myQuestionDealedStr")));
        }

        return convertView;
    }

    /**
     * 处理问题列表点击事件
     *
     * @param position 点击位置
     */
    private void handleItemClick(int position) {
        //已读，未读事件
        if ("0".equals(mDatas.get(position).getRead())) {
            LogProxy.d(TAG, "handle item click");
            iCustomerServicePresenter.sendMyQuestionRead(mActivity, mDatas.get(position).getId(), position);
        }

        mMyQuestionDetailPage = new MyQuestionDetailView(mContext, mRootPageManager,
                mActivity, mDatas.get(position));
        mRootPageManager.addPage(mMyQuestionDetailPage);
    }

    /**
     * 处理“评价”按钮点击事件
     *
     * @param position
     */
    private void handleEvaluateButtonClick(final int position) {
        //获取数组资源
        final String[] evaluate = mContext.getResources().getStringArray(ReflectResourceId.getArrayId(mActivity,
                "bjmgf_sdk_evaluate_type"));
        LogProxy.d(TAG, "evaluate length=" + evaluate.length);
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setItems(evaluate, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                //分数计算公式
                int score = (100 / evaluate.length) * (evaluate.length - which);
                LogProxy.d(TAG, "you have click =" + evaluate[which] + "\twhich=" + which +
                        "\tscore=" + score + "\ttime=" + mDatas.get(position).getCreate());
                //发送评价
                iCustomerServicePresenter.sendMyQuestionEvaluate(mActivity, mDatas.get(position).getId(),
                        score, String.valueOf(which), position);
            }
        });
        dialog.show();

    }

    @Override
    public void showError(String message) {
        ToastUtil.showMessage(mContext, message);
    }

    @Override
    public void showSuccess() {

    }

    /**
     * 问题评价发送成功，修改界面
     *
     * @param position 问题在列表中的位置
     */
    @Override
    public void onSendMyQuestionEvaluateSuccess(int position) {
        MyQuestionBean myQuestionBean = mDatas.get(position);
        //不可评价
        myQuestionBean.setEvaluate("0");
        notifyDataSetChanged();
    }

    /**
     * 问题已读状态改变回调
     *
     * @param position 问题在列表中的位置
     */
    @Override
    public void onSendMyQuestionReadSuccess(int position) {
        MyQuestionBean myQuestionBean = mDatas.get(position);

        LogProxy.d(TAG, "onSendMyQuestionReadSuccess::" + position + "\tquestion title::" + myQuestionBean.getTitle());

        myQuestionBean.setRead("1");
        notifyDataSetChanged();
    }

    class ViewHolder {
        //问题标题
        TextView myQuestionTitleTextView;
        //问题创建时间
        TextView myQuestionTimeTextView;
        //问题评价按钮
        Button myQuestionEvaluateButton;
        //问题是否已读
        ImageView flagImageView;
        //问题状态,"state":"状态(0-处理中，1-已结单)"
        TextView myQuestionStateTextView;
        //是否评价
        TextView myQuestionEvaluateTextView;
    }
}
