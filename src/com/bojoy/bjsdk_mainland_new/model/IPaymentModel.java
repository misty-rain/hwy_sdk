package com.bojoy.bjsdk_mainland_new.model;

import android.content.Context;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseResultCallbackListener;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseResultCallbackListener;
import com.bojoy.bjsdk_mainland_new.model.entity.PayOrderData;
import com.bojoy.bjsdk_mainland_new.model.entity.RechargeOrderDetail;
/**
 * Created by wutao on 2016/1/22.
 * 支付相关业务接口，
 */
public interface IPaymentModel {
	
    /**
     * 查询U币余额
     * @param context 上下文
     * @param listener 回调  将结果通知presenter
     */
	void getUBalance(Context context ,final BaseResultCallbackListener listener);
	/**
	 * 请求生成订单
	 * @param context 上下文
	 * @param rechargeOrderDetail 充值信息数据类
	 * @param listener 回调 结果毁掉给presenter
	 */
    void getSubmitOrder(Context context, RechargeOrderDetail rechargeOrderDetail,final BaseResultCallbackListener listener);
    /**
	 * 请求支付
	 * @param context 上下文
	 * @param payOrderData 充值信息数据类
	 * @param listener 回调 结果毁掉给presenter
	 */
	void getRequestPayModel(Context context,PayOrderData payOrderData,final BaseResultCallbackListener listener);
	/**
	 *
	 * @param context
	 * @param listener
     */
	void getRechargeCardDetailDataWithURL(Context context,BaseResultCallbackListener listener);

	/**
	 *
	 * @param context
	 * @param listener
     */
	void getRechargeCardDetailDataWithFile(Context context,BaseResultCallbackListener listener);



}
