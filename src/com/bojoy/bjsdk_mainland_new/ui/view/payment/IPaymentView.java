package com.bojoy.bjsdk_mainland_new.ui.view.payment;

import com.bojoy.bjsdk_mainland_new.ui.view.IBaseView;

/**
 * Created by wutao on 2016/1/22.
 * 支付中心视图接口
 */
public interface IPaymentView extends IBaseView {
	/**
	 * 显示U币余额查询的结果
	 * @param data U币余额
	 */
	
	void showResult(String data);
	/**
	 * 创建订单成功后返回的信息
	 * @param payInfo 返回的信息
	 */
	void getPayInfo(String payInfo);
	/**
	 * 银联创建订单成功后返回的信息
	 * @param tn 交易流水号
	 * @param serverMode 银联支付环境
	 */
	void getUnionInfo(String tn,String serverMode);
	/**
	 * 支付U币后返回剩余U币
	 * @param data 支付成功后剩余的U币
	 */
	void getPayResult(String data,String msg);

	
}
