package com.bojoy.bjsdk_mainland_new.ui.view.cs;

import com.bojoy.bjsdk_mainland_new.model.entity.MyQuestionDetailBean;

import java.util.ArrayList;

/**
 * Created by shenliuyong on 2016/2/2.
 * 我的问题 详情页 与客服妹子聊天页
 */
public interface IMyQuestionDetailView extends ICustomerView {

    /**
     * 显示聊天记录列表
     *
     * @param datas 聊天数据
     */
    void showMyQuestionDetailRecord(ArrayList<MyQuestionDetailBean> datas);

    /**
     * 显示问题根类型和子类型，只是计算比较烦，把计算逻辑代码移到presenter层，并没有网络访问
     *
     * @param text 问题类型
     */
    void showMyQuestionType(String text);

    /**
     * 聊天界面发送问题回调，根据返回码判断，如果返回码为0，不回调，返回码为1，回调
     */
    void showQuestionAppendSendResult();

}
