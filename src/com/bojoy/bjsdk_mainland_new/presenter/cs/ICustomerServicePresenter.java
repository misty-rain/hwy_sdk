package com.bojoy.bjsdk_mainland_new.presenter.cs;

import android.content.Context;

import com.bojoy.bjsdk_mainland_new.model.entity.QuestionData;

/**
 * Created by wutao on 2016/1/22.
 * 客服业务视图控制器接口
 */
public interface ICustomerServicePresenter {

    /**
     * 获取FAQ json
     *
     * @param context 上下文
     */
    void getFAQJson(Context context);

    /**
     * 获取“我的问题”列表数据
     *
     * @param context
     */
    void getMyQuestionDatas(Context context);

    /**
     * 发送我的问题评价
     *
     * @param context    上下文
     * @param questionId 问题ID
     * @param score      评分
     * @param content    评价内容（可以为空）
     * @param position   问题在列表中的位置
     */
    void sendMyQuestionEvaluate(Context context, String questionId, int score, String content, int position);

    /**
     * 问题阅读状态
     *
     * @param context    上下文
     * @param questionId 问题ID
     * @param position   问题在列表中的位置
     */
    void sendMyQuestionRead(Context context, String questionId, int position);


    /**
     * 提交问题
     *
     * @param context      上下文
     * @param questionData 问题数据
     * @param filePath     文件地址
     */
    void submitQuestion(Context context, QuestionData questionData, String filePath);

    /**
     * 获取问题的客服聊天记录
     *
     * @param context
     * @param questionId
     */
    void getMyQuestionDetailRecord(Context context, String questionId);

    /**
     * 为我的问题详细界面设置问题类型
     *
     * @param context          上下文
     * @param questionRootType 父问题类型
     * @param questionSubType  子问题类型
     */
    void setMyQuestionDetailQuestionType(Context context, String questionRootType, String questionSubType);

    /**
     * 下载图片，下载完成后调用系统打开图片
     *
     * @param context 上下文
     * @param picURL  图片地址
     */
    void getPicWithURL(Context context, String picURL);

    /**
     * @param context    上下文
     * @param questionId 问题ID
     * @param type       问题父类型
     * @param subType    问题子类型
     * @param picPath    图片附件地址，可以为空
     * @param content    回复内容，不可为空
     */
    void sendMyQuestionDetailAppend(Context context, String questionId, String type, String subType,
                                    String picPath, String content);
}
