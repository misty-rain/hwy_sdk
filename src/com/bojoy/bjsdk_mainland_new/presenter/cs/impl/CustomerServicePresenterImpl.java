package com.bojoy.bjsdk_mainland_new.presenter.cs.impl;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.bojoy.bjsdk_mainland_new.api.BaseApi;
import com.bojoy.bjsdk_mainland_new.app.tools.BJMGFSDKTools;
import com.bojoy.bjsdk_mainland_new.app.tools.ParamsTools;
import com.bojoy.bjsdk_mainland_new.congfig.ErrorCodeConstants;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseRequestEvent;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseResultCallbackListener;
import com.bojoy.bjsdk_mainland_new.model.ICustomerServiceModel;
import com.bojoy.bjsdk_mainland_new.model.entity.BackResultBean;
import com.bojoy.bjsdk_mainland_new.model.entity.CommonQuestionListViewBean;
import com.bojoy.bjsdk_mainland_new.model.entity.MyQuestionBean;
import com.bojoy.bjsdk_mainland_new.model.entity.MyQuestionDetailBean;
import com.bojoy.bjsdk_mainland_new.model.entity.PassPort;
import com.bojoy.bjsdk_mainland_new.model.entity.QuestionData;
import com.bojoy.bjsdk_mainland_new.model.impl.CustomerServiceModelImpl;
import com.bojoy.bjsdk_mainland_new.presenter.cs.ICustomerServicePresenter;
import com.bojoy.bjsdk_mainland_new.support.fastjson.JSON;
import com.bojoy.bjsdk_mainland_new.support.fastjson.JSONObject;
import com.bojoy.bjsdk_mainland_new.ui.view.IBaseView;
import com.bojoy.bjsdk_mainland_new.ui.view.cs.ICommonQuestionView;
import com.bojoy.bjsdk_mainland_new.ui.view.cs.IMyQuestionDetailView;
import com.bojoy.bjsdk_mainland_new.ui.view.cs.IMyQuestionListView;
import com.bojoy.bjsdk_mainland_new.ui.view.cs.IMyQuestionView;
import com.bojoy.bjsdk_mainland_new.ui.view.cs.IShowPictureView;
import com.bojoy.bjsdk_mainland_new.utils.DomainUtility;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.SpUtil;
import com.bojoy.bjsdk_mainland_new.utils.Utility;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by wutao on 2016/1/22. 客服业务视图控制器实现
 */
public class CustomerServicePresenterImpl implements ICustomerServicePresenter,
        BaseResultCallbackListener {

    private final String TAG = CustomerServicePresenterImpl.class.getSimpleName();
    private ICustomerServiceModel iCustomerServiceModel;
    private IBaseView iBaseView;
    private Context context;
    private ArrayList<CommonQuestionListViewBean> commonQuestionListVIewDatas;
    //保存ListView位置信息，为 我的问题 更新页面时使用：本打算只声明一个position变量，但如果网络有延迟或者..会发生两者的position混乱
    //评价
    private int evaluatePosition = 0;
    //小红点
    private int readStatePosition = 0;

    public CustomerServicePresenterImpl(Context context, IBaseView iBaseView) {

        this.iBaseView = iBaseView;
        this.context = context;
        iCustomerServiceModel = new CustomerServiceModelImpl();
    }

    /**
     * 获得FAQ json
     *
     * @param context
     */
    @Override
    public void getFAQJson(Context context) {

        if (commonQuestionListVIewDatas != null) {
            ((ICommonQuestionView) iBaseView).showCommonQuestion(commonQuestionListVIewDatas);
        } else {
            File file = new File(context.getFilesDir().toString() + File.separator
                    + SysConstant.FAQ_JSON_FILE_NAME);
            String jsonVersion = DomainUtility.getInstance().getFAQVersion(context);
            String spVersion = SpUtil.getStringValue(context, "faq_version", "");
            if (spVersion.equals(jsonVersion) && file.exists()) {
                // 如果json文件存在，从文件系统中读取
                iCustomerServiceModel.getFaqJsonWithFile(context, this);
            } else {
                iCustomerServiceModel.getFaqJsonWithURL(context, jsonVersion, this);
            }
        }
    }

    /**
     * 为MyQuestionView获取数据,此为处理逻辑
     *
     * @param context
     */
    @Override
    public void getMyQuestionDatas(Context context) {
        PassPort passPort = BJMGFSDKTools.getInstance().getCurrentPassPort();
        String URL = DomainUtility.getInstance().getCustomeServiceDomain(context) + BaseApi.APP_MY_QUESTION_LIST;
        LogProxy.d(TAG, "my question url=" + URL);
        String passportString = passPort.getPp();
        //以下参数可以为空
        String gameid = "";
        String rolename = "";
        String uid = passPort.getUid();
        LogProxy.d(TAG, "passport=  " + passportString);
        iCustomerServiceModel.getMyQuestionDataWithURL(context, URL + "passport=" + passportString,
                BaseRequestEvent.REQUEST_MY_QUESTION_JSON, this);
    }


    /**
     * 发送我的问题评价
     *
     * @param context    上下文
     * @param questionId 问题ID
     * @param score      评分
     * @param content    评价内容（可以为空）
     * @param position   问题在列表中的位置
     */
    @Override
    public void sendMyQuestionEvaluate(Context context, String questionId, int score, String content, int position) {
        this.evaluatePosition = position;
        String URL = DomainUtility.getInstance().getCustomeServiceDomain(context) + BaseApi.APP_MY_QUESTION_EVALUATE
                + "fid=" + questionId + "&score=" + score + "&content=" + content;
        iCustomerServiceModel.sendMyQuestionEvaluate(context, URL, BaseRequestEvent.REQUEST_MY_QUESTION_EVALUATE, this);
    }

    /**
     * 问题阅读状态
     *
     * @param context    上下文
     * @param questionId 问题ID
     * @param position   问题在列表中的位置
     */
    @Override
    public void sendMyQuestionRead(Context context, String questionId, int position) {
        this.readStatePosition = position;
        iCustomerServiceModel.sendMyQuestionRead(context, DomainUtility.getInstance().getCustomeServiceDomain(context) + BaseApi.APP_MY_QUESTION_READ_STATE + "fids=" + questionId,
                BaseRequestEvent.REQUEST_MY_QUESTION_READ_STATE, this);
    }

    /**
     * 提交问题
     *
     * @param context      上下文
     * @param questionData 问题数据
     * @param filePath     文件地址
     */
    @Override
    public void submitQuestion(Context context, QuestionData questionData, String filePath) {
        File file = new File(filePath);
        iCustomerServiceModel.submitQues(context, questionData, file, this);
    }


    /**
     * 获取问题的客服聊天记录
     *
     * @param context    上下文
     * @param questionId 问题ID
     */
    @Override
    public void getMyQuestionDetailRecord(Context context, String questionId) {
        String URL = DomainUtility.getInstance().getCustomeServiceDomain(context)
                + BaseApi.APP_MY_QUESTION_GET_DETAIL_RECORD + "fid=" + questionId;
        LogProxy.d(TAG, "getMyQuestionDetailRecord URL=" + URL);
        iCustomerServiceModel.getMyQuestionDetailRecord(context, URL, BaseRequestEvent.REQUEST_MY_QUESTION_GET_DETAIL_RECORD, this);
    }

    /**
     * 为我的问题详细界面设置问题类型，无网络请求
     *
     * @param context          上下文
     * @param questionRootType 父问题类型
     * @param questionSubType  子问题类型
     */
    @Override
    public void setMyQuestionDetailQuestionType(Context context, String questionRootType, String questionSubType) {
        String text = "";
        String[] subQuestionType = QuestionData.Question_SubType_Names;
        //获取父问题类型所有类型
        String[] qType = context.getResources().getStringArray(ReflectResourceId.getArrayId(context,
                Resource.array.bjmgf_sdk_question_type));
        //获取父问题类型编号
        int qTypeInt = Integer.parseInt(questionRootType) / 100 - 1;
        int qSubTypeInt = 0;
        if (!"0".equals(questionSubType)) {
            //获取子问题类型编号
            qSubTypeInt = (Integer.parseInt(questionSubType) - Integer.parseInt(questionRootType) - 1);
            //父类型
            LogProxy.d(TAG, "question type=" + qType[qTypeInt]);

            LogProxy.d(TAG, "question sub type=" + qSubTypeInt);
            //获取子类型所有类型
            String[] subQType = context.getResources().getStringArray(ReflectResourceId.getArrayId(context,
                    subQuestionType[qTypeInt]));
            //获取子类型
            LogProxy.d(TAG, "sub question =" + subQType[qSubTypeInt]);
            text = qType[qTypeInt] + "-" + subQType[qSubTypeInt];
        } else {
            //不显示子问题
            text = qType[qTypeInt];
        }
        ((IMyQuestionDetailView) iBaseView).showMyQuestionType(text);
    }

    /**
     * 下载图片，下载完成后调用系统打开图片
     *
     * @param context 上下文
     * @param picURL  图片地址
     */
    @Override
    public void getPicWithURL(Context context, String picURL) {
        iCustomerServiceModel.getPicWithURL(context, picURL, context.getFilesDir().toString(),
                SysConstant.MY_QUESTION_DETAIL_ATTACH_FILE_NAME, BaseRequestEvent.REQUEST_MY_QUESTION_ATTACH_IMAGE, this);
    }

    /**
     * @param context    上下文
     * @param questionId 问题ID
     * @param type       问题父类型
     * @param subType    问题子类型
     * @param picPath    图片附件地址，可以为空
     * @param content    回复内容，不可为空
     */
    @Override
    public void sendMyQuestionDetailAppend(Context context, String questionId, String type,
                                           String subType, String picPath, String content) {
        Map<String, String> params = ParamsTools.getInstance().getAppendQuestionParamsMap(context, questionId, content);
        String URL = DomainUtility.getInstance().getCustomeServiceDomain(context)
                + BaseApi.APP_MY_QUESTION_APPEND;

        //String url = "http://m-service.haowanyou.com/service/feedback/append.do";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        String fileName = "";
        File file = null;
        if (Utility.checkFilePath(picPath)) {
            //file = new File(picPath);
            fileName = questionId + "_" + type + "_" + subType + "_" + str + ".jpg";
            LogProxy.d(TAG, "sendMyQuestionDetailAppend URL::" + URL + "\nfile name::" + fileName);
            String tempFilePath = context.getFilesDir() + File.separator + "temp_compress_pic.jpg";
            Utility.compressBitmapToFile(BitmapFactory.decodeFile(picPath), tempFilePath);
            file = new File(tempFilePath);
        }
        iCustomerServiceModel.sendMyQuestionDetailAppend(context, URL, params, fileName,
                file, BaseRequestEvent.REQUEST_MY_QUESTION_APPEND, this);
    }

    @Override
    public void onSuccess(Object response, int requestSessionEvent) {
        LogProxy.d(TAG, "onSuccess" + "\trequestSessionEvent=" + requestSessionEvent);
        BackResultBean backResultBean = null;
        switch (requestSessionEvent) {
            case BaseRequestEvent.REQUEST_FAQ_JSON: //常见问题页面
                //返回json数据没有code节点，不能使用BackResultBean转换
                // 1.储存FAQ版本号
                // 2.返回json解析后的数据
                commonQuestionListVIewDatas = new ArrayList<CommonQuestionListViewBean>();
                commonQuestionListVIewDatas = (ArrayList<CommonQuestionListViewBean>) JSON.parseArray(response.toString(),
                        CommonQuestionListViewBean.class);
                SpUtil.setStringValue(context, "faq_version", DomainUtility.getInstance()
                        .getFAQVersion(context));
                ((ICommonQuestionView) iBaseView).showCommonQuestion(commonQuestionListVIewDatas);
                break;
            case BaseRequestEvent.REQUEST_MY_QUESTION_JSON:  //我的问题页面
                //每次点击标题栏都要重新请求
                backResultBean = JSON.parseObject((String) response, BackResultBean.class);
                if (ErrorCodeConstants.ERROR_CODE_SUCCESS == backResultBean.getCode()) {
                    LogProxy.d(TAG, "IMyQuestionView response==" + response);
                    ArrayList<MyQuestionBean> list = null;
                    try {
                        JSONObject jsonObject = JSON.parseObject((String) response);
                        list = (ArrayList<MyQuestionBean>) JSON.parseArray(JSON.toJSONString(jsonObject.get("data")),
                                MyQuestionBean.class);
                    } catch (Exception e) {
                        LogProxy.e(TAG, "parse json exception=" + e.getMessage());
                    }
                    ((IMyQuestionView) iBaseView).showMyQuestion(list);
                } else {
                    iBaseView.showError(backResultBean.getMsg());
                }

                break;
            case BaseRequestEvent.REQUEST_MY_QUESTION_EVALUATE:   //我的问题 评价
                backResultBean = JSON.parseObject((String) response, BackResultBean.class);
                if (ErrorCodeConstants.ERROR_CODE_SUCCESS == backResultBean.getCode()) {
                    //评价成功
                    ((IMyQuestionListView) iBaseView).onSendMyQuestionEvaluateSuccess(evaluatePosition);
                } else {
                    iBaseView.showError(backResultBean.getMsg());
                }
                break;
            case BaseRequestEvent.REQUEST_MY_QUESTION_READ_STATE://我的问题 阅读状态
                backResultBean = JSON.parseObject((String) response, BackResultBean.class);
                if (ErrorCodeConstants.ERROR_CODE_SUCCESS == backResultBean.getCode()) {
                    //提交成功
                    ((IMyQuestionListView) iBaseView).onSendMyQuestionReadSuccess(readStatePosition);
                } else {
                    iBaseView.showError(backResultBean.getMsg());
                }
                break;
            case BaseRequestEvent.REQUEST_SEND_QUESTION://提交问题
                backResultBean = JSON.parseObject((String) response, BackResultBean.class);
                if (backResultBean.getCode() == ErrorCodeConstants.ERROR_CODE_SUCCESS)
                    iBaseView.showSuccess();
                else
                    iBaseView.showError(backResultBean.getMsg());
                break;
            case BaseRequestEvent.REQUEST_MY_QUESTION_GET_DETAIL_RECORD: //我的问题详细记录
                backResultBean = JSON.parseObject((String) response, BackResultBean.class);
                if (ErrorCodeConstants.ERROR_CODE_SUCCESS == backResultBean.getCode()) {
                    ArrayList<MyQuestionDetailBean> datas = null;
                    try {
                        JSONObject jsonObject = JSON.parseObject((String) response);
                        datas = (ArrayList<MyQuestionDetailBean>) JSON.parseArray(JSON.toJSONString(jsonObject.get("data")),
                                MyQuestionDetailBean.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogProxy.e(TAG, "parse json error showMyQuestionDetailRecord=" + e.getMessage());
                    }
                    ((IMyQuestionDetailView) iBaseView).showMyQuestionDetailRecord(datas);
                } else {
                    iBaseView.showError(backResultBean.getMsg());
                }
                break;
            case BaseRequestEvent.REQUEST_MY_QUESTION_ATTACH_IMAGE://我的问题图片
                //我的问题 详情 下载图片完成后回调显示图片
                ((IShowPictureView) iBaseView).showPicture((File) response);
                break;
            case BaseRequestEvent.REQUEST_MY_QUESTION_APPEND://问题详情聊天页面，追加回复
                JSONObject jsonObject = JSON.parseObject((String) response);
                if ("0".equals(jsonObject.get("code"))) {
                    ((IMyQuestionDetailView) iBaseView).showQuestionAppendSendResult();
                } else {
                    //输出code不等于0的其他信息,获取信息失败
                    iBaseView.showError(jsonObject.get("msg").toString());
                }
                break;
            default:
                break;

        }
    }

    @Override
    public void onError(Call request, Exception exception, int requestSessionEvent) {
        switch (requestSessionEvent) {
            case BaseRequestEvent.REQUEST_MY_QUESTION_ATTACH_IMAGE:
                //聊天详情页面，聊天详细信息，聊天ListView的HeaderView获取图片失败，图片正在审核
                ((IShowPictureView) iBaseView).showPictureNotAvailableError(exception.getMessage(), requestSessionEvent);
                break;
            default:
                iBaseView.showError(exception.getMessage());
                break;
        }


    }

}
