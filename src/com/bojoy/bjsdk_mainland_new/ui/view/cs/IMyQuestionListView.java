package com.bojoy.bjsdk_mainland_new.ui.view.cs;

/**
 * Created by shenliuyong on 2016/2/1.
 * 使用于MyQuestionListViewAdapter
 */
public interface IMyQuestionListView extends ICustomerView {
    /**
     * 问题评价发送成功
     *
     * @param position 问题在列表中的位置
     */
    void onSendMyQuestionEvaluateSuccess(int position);

    /**
     * 问题已读状态改变回调
     *
     * @param position 问题在列表中的位置
     */
    void onSendMyQuestionReadSuccess(int position);
}
