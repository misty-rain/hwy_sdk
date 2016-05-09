package com.bojoy.bjsdk_mainland_new.ui.view.payment.impl;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bojoy.bjsdk_mainland_new.app.BJMGFSdk;
import com.bojoy.bjsdk_mainland_new.app.tools.BJMGFSDKTools;
import com.bojoy.bjsdk_mainland_new.app.tools.PayTools;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.model.entity.PayOrderData;
import com.bojoy.bjsdk_mainland_new.model.entity.PayResult;
import com.bojoy.bjsdk_mainland_new.model.entity.RechargeOrderDetail;
import com.bojoy.bjsdk_mainland_new.presenter.payment.IPaymentPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.payment.impl.PaymentPresenterImpl;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BaseActivity;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseActivityPage;
import com.bojoy.bjsdk_mainland_new.ui.view.payment.IPaymentView;
import com.bojoy.bjsdk_mainland_new.utils.DomainUtility;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.payment.WeiChatPayUtil;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by wutao on 2016/1/22. 支付中心 视图
 */
public class PaymentCenterView extends BaseActivityPage implements IPaymentView {

    private final String TAG = PaymentCenterView.class.getSimpleName();

    private RelativeLayout mBackLayout = null;
    private PayOrderData payOrderData = null;
    private TextView goodsName = null;
    private TextView goodsPrice = null;
    private TextView myBalanceTextView = null;
    private TextView mPayBalance = null;
    private PayTools payTools = PayTools.getInstance();
    private RelativeLayout rechargeBtnLayout = null;
    private TextView mTitle = null;
    private View smsPayGapLineLayout;//短信支付分割线
    // 充值方式选择
    private LinearLayout payTypeLayout = null;
    //支付宝充值
    private TextView alipayTextView = null;
    //银联充值
    private TextView unionPayTextView = null;
    //短信充值
    private TextView smsPayTextView = null;
    //充值卡充值
    private TextView rechargeCardPayTextView = null;
    //微信充值
    private TextView rechargeWxPayTextView = null;
    //支付按钮
    private Button payBtn = null;
    //账户余额  U币数量
    public int balance = 0;
    //支付方式选择
    private String mType;
    //需要充值的U币数量
    public int deltaMoney = 0;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;
    IPaymentPresenter iPaymentPresenter = null;
    RechargeOrderDetail rechargeOrderDetail = null;

    /**
     * 支付宝支付返回的支付结果
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(
                                activity,
                                getString(Resource.string.bjmgf_sdk_dock_recharge_ali_pay_order_success),
                                Toast.LENGTH_SHORT).show();
                        payTools.clearPayDatas();// 清除订单信息

                        quit();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误

                        }

                        quit();
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    break;
                }
                default:
                    break;
            }

        }

    };

    public PaymentCenterView(Context context, PageManager manager,
                             BJMGFActivity activity) {
        super(ReflectResourceId.getLayoutId(context,
                Resource.layout.bjmgf_sdk_dock_pay_center_page), context,
                manager, activity);
        payOrderData = payTools.getPayOrderData();
        deltaMoney = 0;
        iPaymentPresenter = new PaymentPresenterImpl(context, this);

    }

    @Override
    public void onCreateView(View view) {
        mBackLayout = (RelativeLayout) getView(Resource.id.bjmgf_sdk_dock_pay_center_closeLlId);
        goodsName = (TextView) view.findViewById(ReflectResourceId.getViewId(
                context, Resource.id.bjmgf_sdk_pay_center_prop_count));
        goodsPrice = (TextView) view.findViewById(ReflectResourceId.getViewId(
                context, Resource.id.bjmgf_sdk_pay_center_priceId));
        myBalanceTextView = (TextView) view.findViewById(ReflectResourceId
                .getViewId(context,
                        Resource.id.bjmgf_sdk_dock_pay_center_balanceId));
        mPayBalance = getView(Resource.id.bjmgf_sdk_dock_pay_center_pay_balance);
        mTitle = getView(Resource.id.bjmgf_sdk_dock_pay_center_id);
        smsPayGapLineLayout = getView(Resource.id.bjmgf_sdk_smspaygaplineLayoutId);

        myBalanceTextView.setText("0");

        payTypeLayout = (LinearLayout) view
                .findViewById(ReflectResourceId
                        .getViewId(
                                context,
                                Resource.id.bjmgf_sdk_dock_pay_center_choosePayType_layoutId));
        payBtn = (Button) view.findViewById(ReflectResourceId.getViewId(
                context, Resource.id.bjmgf_sdk_dock_pay_center_buybtnId));

        alipayTextView = (TextView) view
                .findViewById(ReflectResourceId
                        .getViewId(
                                context,
                                Resource.id.bjmgf_sdk_dock_pay_center_pay_type_alipayId));

        unionPayTextView = (TextView) view
                .findViewById(ReflectResourceId
                        .getViewId(
                                context,
                                Resource.id.bjmgf_sdk_dock_pay_center_pay_type_unionpayId));

        smsPayTextView = (TextView) view
                .findViewById(ReflectResourceId
                        .getViewId(
                                context,
                                Resource.id.bjmgf_sdk_dock_pay_center_pay_type_smsPayId));

        rechargeCardPayTextView = (TextView) view
				.findViewById(ReflectResourceId
						.getViewId(
								context,
								Resource.id.bjmgf_sdk_dock_pay_center_pay_type_rechargeCardPayId));

        rechargeWxPayTextView = (TextView) view.findViewById(ReflectResourceId
                .getViewId(
                        context, Resource.id.bjmgf_sdk_dock_pay_center_recharge_pay_type_rechargeWxPayId));
        rechargeBtnLayout = (RelativeLayout) view
                .findViewById(ReflectResourceId.getViewId(context,
                        Resource.id.bjmgf_sdk_dock_pay_center_recharge_btnllId));// 充值按钮
        dealWithEvent();

        super.onCreateView(view);
    }

    private void dealWithEvent() {
        rechargeBtnLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {// 充值按钮

            }
        });
        mBackLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                payTools.clearPayDatas();// 清除订单信息
                quit(); // REVIEW 是否取消订单 弹出对话框
            }
        });

        /**
         * 支付宝支付
         */

        alipayTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mType = PayTools.ALIPAY_TYPE;
                payTools.setCurrentType(mType);
                RechargeOrderDetail rechargeOrderDetail = payTools.getRequestParms(deltaMoney, mType, payOrderData);
                iPaymentPresenter.submitOrder(context, rechargeOrderDetail);

            }
        });

        /**
         * 手机短信支付
         */

        smsPayTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 0:盈华讯方
                LogProxy.i(TAG, "channel is " + DomainUtility.getInstance().getSmsChannel(context));
                if (DomainUtility.getInstance().getSmsChannel(context).equals("0")) {
                    // 盈华讯方短信支付方式界面
                    BaseActivityPage page = new DockSMSpayTypePage(context,
                            manager, false, activity);
                    manager.addPage(page);
                }
            }
        });

        /**
         * 充值卡支付
         */
        rechargeCardPayTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				BaseActivityPage page = new PayRechargeCardView(context, manager,
					PaymentCenterView.this, false, activity);
                if(page!=null) {
                    manager.addPage(page);
                }
			}
		});

        /**
         * 微信支付
         */
        rechargeWxPayTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showProgressDialog();
                mType = PayTools.WXPAY_TYPE;
                payTools.setCurrentType(mType);
                RechargeOrderDetail rechargeOrderDetail = payTools.getRequestParms(deltaMoney, mType, payOrderData);
                iPaymentPresenter.submitOrder(context, rechargeOrderDetail);
            }
        });
        /**
         * U币足够时支付
         */
        payBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                    showProgressDialog();
                    iPaymentPresenter.requestPay(context, payOrderData);
                }


        });
        /**
         * 银联支付
         */
        unionPayTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mType = SysConstant.PAYMENT_UNIONPAY_TYPE;
                rechargeOrderDetail = payTools.getRequestParms(deltaMoney, mType, payOrderData);
                iPaymentPresenter.submitOrder(context, rechargeOrderDetail);

            }
        });

    }

    @Override
    public boolean canBack() {
        return super.canBack();
    }

    @Override
    public void onResume() {
        //发送余额请求
        requestBlance();

        super.onResume();
    }

    public void requestBlance() {
                      showProgressDialog();
        iPaymentPresenter.requestUBalance(context);
    }

    @Override
    public void setView() {
        if (!BJMGFSDKTools.getInstance().isOpenSmsPay) {
            smsPayTextView.setVisibility(View.GONE);
            smsPayGapLineLayout.setVisibility(View.GONE);
        }


        rechargeBtnLayout.setVisibility(View.GONE);
    }

    public void readerView() {
        if (balance >= 0) {
            myBalanceTextView.setText(String.valueOf(balance));
        } else {
            myBalanceTextView.setText("");
        }
        deltaMoney = (int) (payOrderData.getCash() * payTools.PAY_RATIO - balance);
        goodsName.setText(payOrderData.getProductName());
        goodsPrice.setText(String.valueOf(payOrderData.getCash()
                * payTools.PAY_RATIO));
        if (deltaMoney <= 0) {// 可立即支付
            payBtn.setVisibility(View.VISIBLE);// 支付按钮可见
            payTypeLayout.setVisibility(View.GONE);// 充值方式按钮不见
            mTitle.setText(getString(Resource.string.bjmgf_sdk_dock_pay_center_balance_pay));// 标题设置为余额支付
            mPayBalance
                    .setText(String.valueOf((int) (payOrderData.getCash() * payTools.PAY_RATIO)));
        } else {// 显示充值界面入口
             payBtn.setVisibility(View.GONE);
            payTypeLayout.setVisibility(View.VISIBLE);
            mTitle.setText(getString(Resource.string.bjmgf_sdk_dock_pay_center_balance_pay_need));
            mPayBalance.setText(deltaMoney + "");
        }
    }


    @Override
    public void showError(String message) {
        dismissProgressDialog();
        LogProxy.d(TAG, message);
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSuccess() {


    }

    /**
     * U币余额查询返回结果
     */
    @Override
    public void showResult(String data) {
        if(!StringUtility.isEmpty(data)) {
            balance = Integer.parseInt(data);// 返回结果
            dismissProgressDialog();
        }
        if (myBalanceTextView != null) {
            myBalanceTextView.setVisibility(View.VISIBLE);
        }
        readerView();

    }

    /**
     * 支付宝、充值卡、手机短信、微信支付需要用到的支付信息payinfo
     * payinfo由 presenter回调返回
     *
     * @param payInfo 支付信息
     */
    @Override
    public void getPayInfo(String payInfo) {

        dismissProgressDialog();
        payTools.setPayInfo(payInfo);
        if(payTools.getCurrentType().equals(PayTools.ALIPAY_TYPE)){
            aliPay();
        }else if(payTools.getCurrentType().equals(PayTools.WXPAY_TYPE)){
            WeiChatPayUtil.startWxPay(context);
        
        }
    }

    /**
     * 支付宝支付
     */
    private void aliPay() {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(activity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payTools.getPayInfo());
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                dismissProgressDialog();
            }
        }, 0);
    }

    /**
     * 银联创建订单后返回的参数 用于银联第三方服务器验证
     *
     * @param tn         交易流水号
     * @param serverMode 银联支付环境 00-正式环境 01-测试环境
     */

    @Override
    public void getUnionInfo(String tn, String serverMode) {


        UPPayAssistEx.startPayByJAR(activity, PayActivity.class, null, null,
                tn, serverMode);

    }

    /**
     * U币支付成功后支付结果回调
     *
     * @param data 支付成功后剩余的U币数量
     */
    @Override
    public void getPayResult(String data,String msg) {
        dismissProgressDialog();
        Toast.makeText(context,msg, Toast.LENGTH_LONG).show();
        if(!data.equals("")) {
            quit();
        }

    }


}