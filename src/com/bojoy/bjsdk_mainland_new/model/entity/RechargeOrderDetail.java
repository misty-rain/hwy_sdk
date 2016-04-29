package com.bojoy.bjsdk_mainland_new.model.entity;


/**
 * 用于sdk服务器端生成充值订单的数据类
 * @author wutao
 *
 */
public class RechargeOrderDetail {
	
	private String appName;
	
	/**
	 * 应用订单号(只有是平台自己充值才可为空)
	 */
	private String appOrderNumber = "";
	
	/**
	 * 实际选择支付金额(元)(不可为空)[平台上选择的金额。可能大于等于小于当前订单金额]
	 */
	private double cash = 0;
	
	/**
	 * 当前应用的订单金额(元)(不可为空)
	 */
	private double money = 0;
	
	/**
	 * 实际充入虚拟币数量(元宝)(不可为空)[1元=10平台币=10元宝]
	 */
	private int amount = 0;
	
	/**
	 * 充入用户uid(可为空，空表示给自己充值)
	 */
	private String userID = "";
	
	/**
	 * 平台应用充值透传参数(原值返回给平台应用，可以是厂家的账号标识或者服务器ID之类)
	 */
	private String ext = "";

	/**
	 * 平台应用充值回调（发货）地址(如果不填，默认平台后台配置的发货地址)
	 */
	private String sendUrl = "";
	
	/**
	 * 三方用户渠道标识(可以为空)
	 */
	private String channel = "";
	
	/**
	 * 平台应用的区服ID(可以为空)
	 */
	private String appServerID = "";
	
	/**
	 * 其他扩展信息(可以为空，比如充值卡支付，extend存放页面填写的卡信息)
	 */
	private String extend = "";
	
	/**
	 * 订单类型(不能为空)【1 - 正常充值平台币; 2 - 充值并消费平台币(手游)】
	 */
	private String orderType = "";
	
	/**
	 * 其他扩展信息2(可以为空)目前存放好玩友充值用户电话号码
	 */
	private String otherExtend = "";
	
	/**
	 * 支付平台ID(不可为空)【115 - 支付宝无线快捷支付】
	 */
	private String payID = "";
	
	private String productName = "";
	
	public RechargeOrderDetail() {
		
	}
	
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public RechargeOrderDetail(String appOrderNumber, double cash, double money, int amount, 
			String userID, String ext, String sendUrl, String channel, String appServerID, 
			String extend, String orderType, String otherExtend, String payID, String productName) {
		this.appOrderNumber = appOrderNumber;
		this.cash = cash;
		this.money = money;
		this.amount = amount;
		this.userID = userID;
		this.ext = ext;
		this.sendUrl =sendUrl;
		this.channel = channel;
		this.appServerID = appServerID;
		this.extend = extend;
		this.orderType = orderType;
		this.otherExtend = otherExtend;
		this.payID = payID;
		this.productName = productName;
	}

	public double getCash() {
		return cash;
	}

	public void setCash(double cash) {
		this.cash = cash;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getPayID() {
		return payID;
	}

	public void setPayID(String payID) {
		this.payID = payID;
	}

	public String getOtherExtend() {
		return otherExtend;
	}

	public void setOtherExtend(String otherExtend) {
		this.otherExtend = otherExtend;
	}

	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getSendUrl() {
		return sendUrl;
	}

	public void setSendUrl(String sendUrl) {
		this.sendUrl = sendUrl;
	}

	public String getAppServerID() {
		return appServerID;
	}

	public void setAppServerID(String appServerID) {
		this.appServerID = appServerID;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public String getAppOrderNumber() {
		return appOrderNumber;
	}

	public void setAppOrderNumber(String appOrderNumber) {
		this.appOrderNumber = appOrderNumber;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

}
