package com.bojoy.bjsdk_mainland_new.model.impl;

import android.content.Context;

import com.bojoy.bjsdk_mainland_new.api.BaseApi;
import com.bojoy.bjsdk_mainland_new.app.tools.ParamsTools;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseRequestEvent;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseResultCallbackListener;
import com.bojoy.bjsdk_mainland_new.model.ICustomerServiceModel;
import com.bojoy.bjsdk_mainland_new.model.entity.QuestionData;
import com.bojoy.bjsdk_mainland_new.support.http.HttpUtility;
import com.bojoy.bjsdk_mainland_new.support.http.callback.FileCallback;
import com.bojoy.bjsdk_mainland_new.support.http.callback.StringCallback;
import com.bojoy.bjsdk_mainland_new.support.http.config.FileInput;
import com.bojoy.bjsdk_mainland_new.support.http.config.HttpMethod;
import com.bojoy.bjsdk_mainland_new.utils.DomainUtility;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by wutao on 2016/1/22. 客服业务实现
 */
public class CustomerServiceModelImpl implements ICustomerServiceModel {

    private final String TAG = CustomerServiceModelImpl.class.getSimpleName();

    /**
     * 获取FAQ json 文件，把version判断放在presenter层
     *
     * @param context  上下文
     * @param listener 回调事件 将结果通知presenter
     */
    @Override
    public void getFaqJsonWithURL(final Context context, String version, final BaseResultCallbackListener listener) {
        // 1.下载
        // 2.解析
        // 3.返回值
        // 获取下载文件网址
        String url = DomainUtility.getInstance().getFAQDomain(context);
        LogProxy.d(TAG, "faq_url=" + url);
        LogProxy.d(TAG, "getFAQVersion=" + version);
        LogProxy.d(TAG, "file path=" + context.getFilesDir().toString());
        try {
            HttpUtility.getInstance().executeDownloadFile(url + version,
                    new FileCallback(context.getFilesDir().toString(), SysConstant.FAQ_JSON_FILE_NAME) {

                        @Override
                        public void inProgress(float progress) {

                        }

                        @Override
                        public void onError(Call call, Exception e) {
                            LogProxy.d(TAG, "onError=" + "request=" + call + "\texception=" + e);
                            // 从网络获取文件失败
                            listener.onError(call, e, BaseRequestEvent.Request_FAQ_JSON);
                        }

                        @Override
                        public void onResponse(File file) {
                            // response:文件路径
                            LogProxy.d(TAG, file.getPath());
                            // 调用文件读取
                            getFaqJsonWithFile(context, listener);
                        }
                    });
        } catch (Exception e) {
            LogProxy.d(TAG, e.getMessage());

        }
    }

    /**
     * 从文件系统中获取FAQ json 文件
     *
     * @param context  上下文
     * @param listener 回调事件 将结果通知presenter
     */
    @Override
    public void getFaqJsonWithFile(Context context, BaseResultCallbackListener listener) {
        File file = new File(context.getFilesDir().toString() + File.separator
                + SysConstant.FAQ_JSON_FILE_NAME);

        StringBuilder content = new StringBuilder();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream = new FileInputStream(file.getPath());
            if (inputStream != null) {
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                // 分行读取
                while ((line = bufferedReader.readLine()) != null) {
                    content.append(line + "\n");
                }
                inputStream.close();
            }
        } catch (java.io.FileNotFoundException e) {
            LogProxy.d(TAG, "The File doesn't not exist.");
        } catch (IOException e) {
            LogProxy.d(TAG, e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        LogProxy.d(TAG, content.toString());
        listener.onSuccess(content.toString(), BaseRequestEvent.Request_FAQ_JSON);

    }


    /**
     * 获取 我的问题 数据
     *
     * @param context   上下文
     * @param URL       地址
     * @param eventCode 回调事件码
     * @param listener  回调接口
     */
    public void getMyQuestionDataWithURL(final Context context, String URL, int eventCode, final BaseResultCallbackListener listener) {
        try {
            HttpUtility.getInstance().execute(HttpMethod.GET, URL, null, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    listener.onError(call, e, eventCode);
                }

                @Override
                public void onResponse(String response) {
                    LogProxy.d(TAG, "getMyQuestionDataWithURL response::" + response);
                    listener.onSuccess(response, eventCode);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 提交问题
     *
     * @param context          上下文
     * @param data             表单数据
     * @param file             文件流
     * @param callbackListener 回调 结果通知给presenter
     */
    @Override
    public void submitQues(Context context, QuestionData data, File file, BaseResultCallbackListener callbackListener) {
        Map<String, String> params = ParamsTools.getInstance().getQuestionParamsMap(context, data);
        List<FileInput> list = new ArrayList<FileInput>();
        if (file.isFile()) {
            FileInput fileInput = new FileInput("feedback_file", file.getPath(), file);
            list.add(fileInput);
        }
        try {
            HttpUtility.getInstance().execute(DomainUtility.getInstance().getCustomeServiceDomain(context) + BaseApi.APP_SUBMIT_QUES, params, list.isEmpty() ? null : list, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    callbackListener.onError(call, e, BaseRequestEvent.Request_Send_Question);
                }

                @Override
                public void onResponse(String response) {
                    callbackListener.onSuccess(response, BaseRequestEvent.Request_Send_Question);
                }
            });
        } catch (Exception e) {

        }
    }

    /**
     * 获取问题的客服聊天记录
     *
     * @param context   上下文
     * @param URL       发送地址
     * @param eventCode 回调事件回调码
     * @param listener  回调接口
     */
    @Override
    public void getMyQuestionDetailRecord(Context context, String URL, final int eventCode, BaseResultCallbackListener listener) {
        try {
            HttpUtility.getInstance().execute(HttpMethod.GET, URL, null, new StringCallback() {
                @Override
                public void onError(Call request, Exception e) {
                    LogProxy.e(TAG, "getMyQuestionDetailRecord onError=" + e.getMessage());
                    listener.onError(request, e, eventCode);
                }

                @Override
                public void onResponse(String response) {
                    LogProxy.d(TAG, "getMyQuestionDetailRecord onResponse=" + response);
                    listener.onSuccess(response, eventCode);
                }
            });
        } catch (Exception e) {
            LogProxy.e(TAG, "getMyQuestionDetailRecord exception=" + e.getMessage());
        }
    }

    /**
     * 发送问题评价，直接把问题位置传回去
     *
     * @param context   上下文
     * @param URL       发送地址
     * @param eventCode 回调事件回调码
     * @param listener  回调接口
     */
    @Override
    public void sendMyQuestionEvaluate(Context context, String URL, final int eventCode,
                                       final BaseResultCallbackListener listener) {
        try {
            HttpUtility.getInstance().execute(HttpMethod.GET, URL, null, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    listener.onError(call, e, eventCode);
                }

                @Override
                public void onResponse(String response) {
                    LogProxy.d(TAG, "sendMyQuestionEvaluate onResponse=" + response);
                    //只有问题不存在才会评价失败
                    listener.onSuccess(response, eventCode);
                }
            });
        } catch (Exception e) {
            LogProxy.e(TAG, "sendMyQuestionEvaluate Exception=" + e.getMessage());
        }
    }

    /**
     * 发送问题阅读状态,可以和上面的那个合起来
     *
     * @param context   上下文
     * @param URL       发送地址
     * @param eventCode 回调事件回调码
     * @param listener  回调接口
     */
    @Override
    public void sendMyQuestionRead(Context context, String URL, int eventCode, BaseResultCallbackListener listener) {
        try {
            HttpUtility.getInstance().execute(HttpMethod.GET, URL, null, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    LogProxy.e(TAG, "sendMyQuestionRead onError=" + e.getMessage());
                    listener.onError(call, e, eventCode);
                }

                @Override
                public void onResponse(String response) {
                    LogProxy.d(TAG, "sendMyQuestionRead  response=" + response);
                    listener.onSuccess(response, eventCode);
                }
            });
        } catch (Exception e) {
            LogProxy.e(TAG, e.getMessage());
        }
    }

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
    @Override
    public void getPicWithURL(Context context, String URL, String fileDir, String fileName, int eventCode,
                              BaseResultCallbackListener listener) {
        try {
            HttpUtility.getInstance().executeDownloadFile(URL, new FileCallback(fileDir, fileName) {
                @Override
                public void inProgress(float progress) {

                }

                @Override
                public void onError(Call call, Exception e) {
                    LogProxy.d(TAG, "getPicWithURL onError::" + e.getMessage());
                    //图片正在审核时会失败
                    listener.onError(call, e, eventCode);
                }

                @Override
                public void onResponse(File response) {
                    listener.onSuccess(response, eventCode);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogProxy.e(TAG, "getPicWithURL error::" + e.getMessage());
        }
    }

    /**
     * 上传问题回复
     *
     * @param context   上下文
     * @param URL       网络地址
     * @param params    post参数
     * @param fileName  文件名
     * @param file      文件
     * @param eventCode 返回码
     * @param listener  回调接口
     */
    @Override
    public void sendMyQuestionDetailAppend(Context context, String URL, Map<String, String> params,
                                           String fileName, File file, int eventCode, BaseResultCallbackListener listener) {
        //图片上传
        try {
            List<FileInput> list = new ArrayList<FileInput>();
            if (file != null && file.isFile()) {
                FileInput fileInput = new FileInput("feedback_file",
                        fileName, file);
                list.add(fileInput);
            }
            HttpUtility.getInstance().execute(URL, params, list.isEmpty() ? null : list, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    e.printStackTrace();
                    LogProxy.d(TAG, "questionDetailAppend onError");
                    listener.onError(call, e, eventCode);
                }

                @Override
                public void onResponse(String response) {
                    LogProxy.d(TAG, "questionDetailAppend onResponse " + response);
                    listener.onSuccess(response, eventCode);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();

        }
    }


}
