package com.bojoy.bjsdk_mainland_new.presenter.payment.impl;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;

import com.bojoy.bjsdk_mainland_new.app.tools.PayTools;
import com.bojoy.bjsdk_mainland_new.congfig.ErrorCodeConstants;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseRequestEvent;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseResultCallbackListener;
import com.bojoy.bjsdk_mainland_new.model.IPaymentModel;
import com.bojoy.bjsdk_mainland_new.model.entity.BackResultBean;
import com.bojoy.bjsdk_mainland_new.model.entity.PayOrderData;
import com.bojoy.bjsdk_mainland_new.model.entity.RechargeCardDetailData;
import com.bojoy.bjsdk_mainland_new.model.entity.RechargeOrderDetail;
import com.bojoy.bjsdk_mainland_new.model.impl.PaymentModelImpl;
import com.bojoy.bjsdk_mainland_new.presenter.payment.IPaymentPresenter;
import com.bojoy.bjsdk_mainland_new.support.fastjson.JSON;
import com.bojoy.bjsdk_mainland_new.support.fastjson.JSONArray;
import com.bojoy.bjsdk_mainland_new.support.fastjson.JSONObject;
import com.bojoy.bjsdk_mainland_new.ui.view.payment.IPayRechargeCardView;
import com.bojoy.bjsdk_mainland_new.ui.view.payment.IPayRechargeCardViewNext;
import com.bojoy.bjsdk_mainland_new.ui.view.payment.IPaymentView;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;

import okhttp3.Call;

/**
 * Created by wutao on 2016/1/22. 支付业务 控制器实现
 */
public class PaymentPresenterImpl implements IPaymentPresenter, BaseResultCallbackListener {

    private final String TAG = PaymentPresenterImpl.class.getSimpleName();
    private Context mContext;
    private IPaymentModel iPaymentModel;
    private IPaymentView mIPaymentView;
    private String payType; // 支付类型
    private IPayRechargeCardView ipayRechargeCardView;
    private IPayRechargeCardViewNext iPayRechargeCardViewNext;

    public PaymentPresenterImpl(Context context, IPaymentView iPaymentView) {
        this.mContext = context;
        this.mIPaymentView = iPaymentView;
        this.iPaymentModel = new PaymentModelImpl();
    }

    public PaymentPresenterImpl(Context context,
                                IPayRechargeCardView ipayRechargeCardView) {
        this.mContext = context;
        this.ipayRechargeCardView = ipayRechargeCardView;
        this.iPaymentModel = new PaymentModelImpl();

    }

    public PaymentPresenterImpl(Context context,
                                IPayRechargeCardViewNext iPayRechargeCardViewNext) {
        this.mContext = context;
        this.iPayRechargeCardViewNext = iPayRechargeCardViewNext;
        this.iPaymentModel = new PaymentModelImpl();

    }

    /**
     * 解析常见的json字符串 充值卡数据类的json解析
     */
    @Override
    public void onSuccess(Object response, int requestSessionEvent) {

        try {
            if (requestSessionEvent == BaseRequestEvent.Request_PAYMENT_RECHARGE_CARDS_CONFIG_JSON) {

                if (!StringUtility.isEmpty(String.valueOf(response))) {
                    Object obj = JSONObject.parseObject(
                            String.valueOf(response)).get("rechargecard");
                    JSONArray array = JSONArray.parseArray(obj.toString());

                    ArrayList<RechargeCardDetailData> list = new ArrayList<RechargeCardDetailData>();
                    for (int i = 0; i < array.size(); i++) {
                        list.add(JSONObject.parseObject(array.getJSONObject(i)
                                        .get(String.valueOf(111 + i)).toString(),
                                RechargeCardDetailData.class));

                    }

                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).id = String.valueOf(111 + i);

                    }
                    ipayRechargeCardView.showRechargeResult(list);
                } else {
                    LogProxy.d(TAG, "response is null");
                }

            } else {
                BackResultBean backResultBean = JSON.parseObject(
                        (String) response, BackResultBean.class);
                //因为充值卡成功返回的结果码不为0故重写以和其他充值结果区分
                if(backResultBean.getCode()==ErrorCodeConstants.ERROR_CODE_RECHARGECARDRESULTCODE_SUCCESS){
                    if(requestSessionEvent==BaseRequestEvent.Request_Recharge_Order){
                      String msg=backResultBean.getMsg().toString();
                       iPayRechargeCardViewNext.showReturn(msg);
                    }
                }
                //如果u币余额不足时支付调用此方法 因为U币余额不足支付与余额充足支付返回码不同故重写
                if(backResultBean.getCode()==ErrorCodeConstants.ERROR_CODE_PAYORDERCODE){
                    if(requestSessionEvent==BaseRequestEvent.Request_Pay_Order){
                        String backMsg = backResultBean.getMsg().toString();
                        mIPaymentView.getPayResult("",backMsg);
                    }
                }

                if (backResultBean.getCode() == ErrorCodeConstants.ERROR_CODE_SUCCESS) {
                    switch (requestSessionEvent) {
                        case BaseRequestEvent.Request_User_Balance:// 获取U币余额
                            String balance = JSON
                                    .parseObject(backResultBean.getObj(), Map.class)
                                    .get("data").toString();
                            if (!StringUtility.isEmpty(balance)) {
                                mIPaymentView.showResult(balance);
                            }
                            break;
                        case BaseRequestEvent.Request_Recharge_Order:

                            if (payType.equals(PayTools.ALIPAY_TYPE) || payType.equals(PayTools.SMSPAY_TYPE) || payType.equals(PayTools.WXPAY_TYPE)) {
                                String payInfo = JSON
                                        .parseObject(backResultBean.getObj(),
                                                Map.class).get("payInfo")
                                        .toString();
                                mIPaymentView.getPayInfo(payInfo);
                            } else if (payType
                                    .equals(SysConstant.PAYMENT_UNIONPAY_TYPE)) {
                                String tn = JSON
                                        .parseObject(backResultBean.getObj(),
                                                Map.class).get("tn").toString();
                                String serverMode = JSON
                                        .parseObject(backResultBean.getObj(),
                                                Map.class).get("serverMode")
                                        .toString();
                                mIPaymentView.getUnionInfo(tn, serverMode);
                            } else if (payType.equals(PayTools.CM_CARD_PAY_TYPE)) {



                            } else if (payType.equals(PayTools.CT_CARD_PAY_TYPE)) {

                            } else if (payType.equals(PayTools.CU_CARD_PAY_TYPE)) {

                            }
                            break;
                        case BaseRequestEvent.Request_Pay_Order:
                            String data = JSON
                                    .parseObject(backResultBean.getObj(),
                                            Map.class).get("data").toString();

                            String backMsg = backResultBean.getMsg().toString();
                            mIPaymentView.getPayResult(data,backMsg);
                            break;
                    }

                } else {

                }

            }

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    @Override
    public void onError(Call request, Exception exception,
                        int requestSessionEvent) {

        mIPaymentView.showError(exception.getMessage());

    }

    /**
     * 查询余额 context 上下文
     */

    @Override
    public void requestUBalance(Context context) {

        iPaymentModel.getUBalance(context, this);
    }

    /**
     * 请求创建订单
     */
    @Override
    public void submitOrder(Context context,
                            RechargeOrderDetail rechargeOrderDetail) {
        this.payType = rechargeOrderDetail.getPayID();
        LogProxy.d("this payType is",payType);
        iPaymentModel.getSubmitOrder(context, rechargeOrderDetail, this);

    }

    /**
     * 请求支付
     */
    @Override
    public void requestPay(Context context,
                         PayOrderData payOrderData) {
        iPaymentModel.getRequestPayModel(context, payOrderData, this);

    }

    /**
     * 请求获得充值数据信息
     */
    @Override
    public void getRechargeCardDes(Context context) {
        iPaymentModel.getRechargeCardDetailDataWithURL(context, this);

    }

    /**
     * 短信支付
     */
    @Override
    public void startSmsPay(Context context,
                            RechargeOrderDetail rechargeOrderDetail) {
        this.payType = rechargeOrderDetail.getPayID();
        iPaymentModel.getSubmitOrder(context, rechargeOrderDetail, this);

    }

}
