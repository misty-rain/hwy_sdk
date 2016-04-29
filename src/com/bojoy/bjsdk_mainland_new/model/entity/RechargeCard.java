package com.bojoy.bjsdk_mainland_new.model.entity;

import java.util.ArrayList;

/**
 * 运营商的某一种充值卡
 * "CMJFK00010001" : {
 *					"cardNumberLength" : 17,
 *					"cardPwdLength" : 18,
 *					"rule" : [10, 20, 30, 50, 100, 200, 300, 500]
 *				},
 * @author qiuzhiyuan
 *
 */
public class RechargeCard {
	
	/**
	 * 卡的代号  e.g. CMJFK00010001
	 */
	public String id;
	
	/**
	 * 卡的中文名称 e.g. 全国移动充值卡
	 */
	public String name;
	
	/**
	 * 充值卡卡号位数
	 */
	public int cardNumberLength;
	
	/**
	 * 充值卡密码长度
	 */
	public int cardPwdLength;
	
	/**
	 * 充值卡面额列表
	 */
	public ArrayList<Integer> rule;
	
	public RechargeCard() {
		
	}
	
	@Override
	public String toString() {
		return "id " + id + " name " + name + " cardNumberLength " 
				+ cardNumberLength + " cardPwdLength " + cardPwdLength + " rule " + rule;
	}
	
	

}
