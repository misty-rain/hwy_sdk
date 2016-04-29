package com.bojoy.bjsdk_mainland_new.ui.view.cs;

import com.bojoy.bjsdk_mainland_new.model.entity.CommonQuestionListViewBean;

import java.util.ArrayList;

public interface ICommonQuestionView extends ICustomerView {
    /**
     * 显示常见问题
     *
     * @param datas 常见问题列表
     */
    void showCommonQuestion(ArrayList<CommonQuestionListViewBean> datas);
}
