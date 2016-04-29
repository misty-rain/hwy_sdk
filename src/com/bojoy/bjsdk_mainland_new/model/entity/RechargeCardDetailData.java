package com.bojoy.bjsdk_mainland_new.model.entity;


import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 充值卡数据类
 * @author wutao
 *
 */
public class RechargeCardDetailData implements Serializable{

	public String id = "";
	/**
	 * 支付方式
	 */
	public String text = "";
	
	/**
	 * 充值卡类型
	 */
	public String tip = "";
	
	/**
	 * 所有的充值卡类型
	 */
	public ArrayList<RechargeCard> ct;
	
	public RechargeCardDetailData() {
	
	}
	
	@Override
	public String toString() {
		return "id " + id + " text " + text + " tip " + tip + " ct " + ct;
	}

}
