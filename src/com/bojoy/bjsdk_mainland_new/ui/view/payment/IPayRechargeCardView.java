package com.bojoy.bjsdk_mainland_new.ui.view.payment;

import java.util.ArrayList;

import com.bojoy.bjsdk_mainland_new.model.entity.RechargeCardDetailData;
import com.bojoy.bjsdk_mainland_new.ui.view.IBaseView;


public interface IPayRechargeCardView extends IBaseView{
	/**
	 * 充值卡充值json解析后从presenter返回的数据
	 * @param list
	 */
	void showRechargeResult(ArrayList<RechargeCardDetailData> list);
}
