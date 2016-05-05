package com.bojoy.bjsdk_mainland_new.model.impl;

import android.content.Context;

import com.bojoy.bjsdk_mainland_new.api.BaseApi;
import com.bojoy.bjsdk_mainland_new.app.tools.ParamsTools;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseRequestEvent;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseResultCallbackListener;
import com.bojoy.bjsdk_mainland_new.model.IPaymentModel;
import com.bojoy.bjsdk_mainland_new.model.entity.PayOrderData;
import com.bojoy.bjsdk_mainland_new.model.entity.RechargeOrderDetail;
import com.bojoy.bjsdk_mainland_new.support.http.HttpUtility;
import com.bojoy.bjsdk_mainland_new.support.http.callback.FileCallback;
import com.bojoy.bjsdk_mainland_new.support.http.callback.StringCallback;
import com.bojoy.bjsdk_mainland_new.support.http.config.HttpMethod;
import com.bojoy.bjsdk_mainland_new.utils.DomainUtility;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by wutao on 2016/1/22. 支付相关业务类请求实现
 */
public class PaymentModelImpl implements IPaymentModel {
    private FileInputStream instream;
    private final String TAG = PaymentModelImpl.class.getSimpleName();

    /**
     * U币余额查询 查询结果回调给presenter
     *
     * @param context  上下文
     * @param listener 回调 将结果通知presenter
     */
    @Override
    public void getUBalance(Context context,
                            final BaseResultCallbackListener listener) {

        Map<String, String> params = ParamsTools.getInstance()
                  .getBaseParamsMap(context, false);
        params.put("formatObjType", SysConstant.PAYMENT_FORMATOBJTYPE);
        try {
            HttpUtility.getInstance().execute(
                      HttpMethod.POST,
                      DomainUtility.getInstance().getServiceSDKDomain(context)
                                + BaseApi.APP_U_BLANCE, params,
                      new StringCallback() {

                          @Override
                          public void onError(Call call, Exception e) {
                              listener.onError(call, e,
                                        BaseRequestEvent.REQUEST_USER_BALANCE);
                          }

                          @Override
                          public void onResponse(String response) {
                              listener.onSuccess(response,
                                        BaseRequestEvent.REQUEST_USER_BALANCE);
                          }
                      });
        } catch (Exception e) {
            e.printStackTrace();
            LogProxy.d(TAG, e.getMessage());
        }
    }

    /**
     * 请求生成支付订单
     *
     * @param context  上下文
     * @param listener 回调 将结果通知presenter
     */
    @Override
    public void getSubmitOrder(Context context,
                               RechargeOrderDetail rechargeOrderDetail,
                               final BaseResultCallbackListener listener) {

        Map<String, String> params = ParamsTools.getInstance()
                  .getPaymentParamsMap(context, rechargeOrderDetail);

        try {
            HttpUtility.getInstance().execute(
                      HttpMethod.POST,
                      DomainUtility.getInstance().getServiceSDKDomain(context)
                                + BaseApi.APP_SUBMIT_ORDER, params,
                      new StringCallback() {

                          @Override
                          public void onError(Call call, Exception e) {

                              listener.onError(call
                                        , e,
                                        BaseRequestEvent.REQUEST_RECHARGE_ORDER);
                          }

                          @Override
                          public void onResponse(String response) {
                              listener.onSuccess(response,
                                        BaseRequestEvent.REQUEST_RECHARGE_ORDER);

                              LogProxy.d(TAG, response + "");

                          }
                      });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 余额足够支付时 支付U币
     *
     * @param context  上下文
     * @param listener 回调 将结果通知presenter
     */
    @Override
    public void getRequestPayModel(Context context,
                                 PayOrderData  payOrderData,
                                   final BaseResultCallbackListener listener) {

        Map<String, String> params = ParamsTools.getInstance()
                  .getPayOrderParams(context, payOrderData);

        try {

            HttpUtility.getInstance().execute(
                      HttpMethod.POST,
                      DomainUtility.getInstance().getServiceSDKDomain(context)
                                + BaseApi.APP_PAY_ORDER, params,
                      new StringCallback() {

                          @Override
                          public void onError(Call call, Exception e) {
                              listener.onError(call, e,
                                        BaseRequestEvent.REQUEST_PAY_ORDER);

                          }

                          @Override
                          public void onResponse(String response) {
                              listener.onSuccess(response,
                                        BaseRequestEvent.REQUEST_PAY_ORDER);
                          }

                      });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    ;

    /**
     * 充值卡数据信息json解析
     *
     * @param context  上下文
     * @param listener 将结果通知presenter
     */
    @Override
    public void getRechargeCardDetailDataWithURL(final Context context,
                                                 final BaseResultCallbackListener listener) {

        String url = DomainUtility.getInstance().getRechargCardUrl(context);
        String version = DomainUtility.getInstance().getRechargCardVersion(
                  context);
        if (StringUtility.isEmpty(url) && StringUtility.isEmpty(version)) {
            return;
        }
        File file = new File(context.getFilesDir().toString() + File.separator
                  + SysConstant.PAYMENT_RECHARGE_CARDS_CONFIG);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        try {
            HttpUtility.getInstance().executeDownloadFile(url + version, new FileCallback(context.getFilesDir().toString(), SysConstant.PAYMENT_RECHARGE_CARDS_CONFIG) {
                @Override
                public void inProgress(float progress) {

                }

                @Override
                public void onError(Call call, Exception e) {
                    LogProxy.d(TAG, "onError=" + "request=" + call
                              + "\texception=" + e);
                    // 从网络获取文件失败
                    listener.onError(
                              call,
                              e,
                              BaseRequestEvent.REQUEST_PAYMENT_RECHARGE_CARDS_CONFIG_JSON);
                }

                @Override
                public void onResponse(File response) {
                    // response:文件路径
                    LogProxy.d(TAG, response.getPath());
                    // 调用文件读取
                    getRechargeCardDetailDataWithFile(context, listener);
                }
            });
        } catch (Exception e) {
            LogProxy.d(TAG, e.getMessage());
        }
    }


    /**
     * 从文件系统中获取json文件
     *
     * @param context  上下文
     * @param listener 回调 将结果通知presenter
     */
    @Override
    public void getRechargeCardDetailDataWithFile(Context context,
                                                  final BaseResultCallbackListener listener) {
        File file = new File(context.getFilesDir().toString() + File.separator
                  + SysConstant.PAYMENT_RECHARGE_CARDS_CONFIG);
        StringBuilder content = new StringBuilder();

        try {
            instream = new FileInputStream(file.getPath());

            if (instream != null) {

                InputStreamReader inputreader = new InputStreamReader(instream);
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line;
                // 分行读取
                while ((line = buffreader.readLine()) != null) {
                    content.append(line + "\n");
                }
                instream.close();
            }
        } catch (java.io.FileNotFoundException e) {
            LogProxy.d(TAG, "The File doesn't not exist.");
        } catch (IOException e) {
            LogProxy.d(TAG, e.getMessage());
        }
        LogProxy.d(TAG, content.toString());
        if (!StringUtility.isEmpty(content.toString())) {
            listener.onSuccess(content.toString(),
                      BaseRequestEvent.REQUEST_PAYMENT_RECHARGE_CARDS_CONFIG_JSON);
        } else {
            //如果没有取到网络数据取本地数据
            String localstr = "{rechargecard: [{111: {text: \"移动充值卡支付\",tip: \"移动充值卡\",ct: [{id: \"CMJFK00010001\",name: \"全国移动充值卡\",cardNumberLength: 17,cardPwdLength: 18,rule: [10, 20, 30, 50, 100, 200, 300, 500]},{id: \"CMJFK00010112\", name: \"浙江移动缴费券\", cardNumberLength: 10, cardPwdLength: 8, rule: [10, 20, 30, 50, 100, 200]}, {id: \"CMJFK00010014\", name: \"福建呱呱通充值卡\", cardNumberLength: 16, cardPwdLength: 17, rule: [10, 20, 30, 50, 100]}]}}, {112: {text: \"联通充值卡支付\", tip: \"联通充值卡\", ct: [{id: \"LTJFK00020000\", name: \"全国联通一卡充\", cardNumberLength: 15, cardPwdLength: 19, rule: [10,20,30,50,100,200,300,500]}]}}, {113: { text: \"电信充值卡支付\", tip: \"电信充值卡\", ct: [{id : \"DXJFK00010001\", name: \"全国电信卡\", cardNumberLength: 19, cardPwdLength: 18, rule: [10, 20, 30, 50, 100, 200]}]}}]}";
            listener.onSuccess(localstr,
                      BaseRequestEvent.REQUEST_PAYMENT_RECHARGE_CARDS_CONFIG_JSON);
        }

    }


}
