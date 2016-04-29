package com.bojoy.bjsdk_mainland_new.ui.view.cs;

import com.bojoy.bjsdk_mainland_new.model.entity.MyQuestionBean;

import java.util.ArrayList;

/**
 * Created by shenliuyong on 2016/1/28.
 */
public interface IMyQuestionView extends ICustomerView {

    /**
     * 显示“我的问题”列表
     *
     * @param datas ListView数据
     */
    void showMyQuestion(ArrayList<MyQuestionBean> datas);
}
