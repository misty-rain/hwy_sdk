package com.bojoy.bjsdk_mainland_new.presenter.payment;

import android.content.Context;

import com.bojoy.bjsdk_mainland_new.model.entity.PayOrderData;
import com.bojoy.bjsdk_mainland_new.model.entity.RechargeOrderDetail;

/**
 * Created by wutao on 2016/1/22.
 * 支付相关业务操作相关 视图控制器 接口
 */
public interface IPaymentPresenter {
	/**
	 * 请求U币查询
	 * 
	 * @param context 上下文
	 */
	void requestUBalance(Context context);
	/**
	 * 提交订单
	 * @param context 上下文
	 * @param rechargeOrderDetail 支付信息数据类
	 */
	void submitOrder(Context context,RechargeOrderDetail rechargeOrderDetail);
	/**
	 * 支付U币 
	 * @param context 上下文
	 * @param payOrderData 支付信息类
	 */
	void  requestPay(Context context,PayOrderData payOrderData);
	
	/**
	 * 短信支付
	 * @param context
	 * @param rechargeOrderDetail
	 */
	void startSmsPay(Context context,RechargeOrderDetail rechargeOrderDetail);

	/**
	 * 充值卡
	 * @param context
	 */
	void getRechargeCardDes(Context context);




}
