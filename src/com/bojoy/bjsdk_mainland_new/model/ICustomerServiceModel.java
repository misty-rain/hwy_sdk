package com.bojoy.bjsdk_mainland_new.model;

import android.content.Context;

import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseResultCallbackListener;
import com.bojoy.bjsdk_mainland_new.model.entity.QuestionData;

import java.io.File;
import java.util.Map;

/**
 * Created by wutao on 2016/1/22. 客服业务 请求接口
 */
public interface ICustomerServiceModel {

    /**
     * 获取FAQ json 文件
     *
     * @param context  上下文
     * @param version  版本
     * @param listener 回调事件 将结果通知presenter
     */
    void getFaqJsonWithURL(Context context, String version, final BaseResultCallbackListener listener);

    /**
     * 从文件系统中获取json数据
     *
     * @param context
     * @param listener
     */
    void getFaqJsonWithFile(Context context, final BaseResultCallbackListener listener);

    /**
     * 从网络获取 我的问题 数据
     *
     * @param context   上下文
     * @param URL       地址
     * @param eventCode 回调事件码
     * @param listener  回调接口
     */
    void getMyQuestionDataWithURL(final Context context, String URL, int eventCode, final BaseResultCallbackListener listener);

    /**
     * 提交问题
     *
     * @param context          上下文
     * @param data             表单数据
     * @param file             文件流
     * @param callbackListener 回调 结果通知给presenter
     */
    void submitQues(Context context, QuestionData data, File file, final BaseResultCallbackListener callbackListener);


    /**
     * 获取问题的客服聊天记录
     *
     * @param context   上下文
     * @param URL       发送地址
     * @param eventCode 回调事件回调码
     * @param listener  回调接口
     */
    void getMyQuestionDetailRecord(Context context, String URL, int eventCode, final BaseResultCallbackListener listener);

    /**
     * 发送问题评价
     *
     * @param context   上下文
     * @param URL       发送地址
     * @param eventCode 回调事件回调码
     * @param listener  回调接口
     */
    void sendMyQuestionEvaluate(Context context, String URL, final int eventCode,
                                final BaseResultCallbackListener listener);

    /**
     * 发送问题阅读状态
     *
     * @param context   上下文
     * @param URL       发送地址
     * @param eventCode 回调事件回调码
     * @param listener  回调接口
     */
    void sendMyQuestionRead(Context context, String URL, int eventCode, final BaseResultCallbackListener listener);

    /**
     * 从网络获取图片文件，下载到本地
     *
     * @param context   上下文
     * @param URL       文件地址
     * @param fileDir   文件目录
     * @param fileName  文件名
     * @param eventCode 事件返回码
     * @param listener  回调函数
     */
    void getPicWithURL(Context context, String URL, String fileDir, String fileName, int eventCode,
                       final BaseResultCallbackListener listener);

    /**
     * 上传问题回复
     *
     * @param context   上下文
     * @param URL       网络地址
     * @param fileName  文件名
     * @param file      文件
     * @param eventCode 返回码
     * @param listener  回调接口
     */
    void sendMyQuestionDetailAppend(Context context, String URL, Map<String, String> params, String fileName,
                                    File file, int eventCode, final BaseResultCallbackListener listener);
}
