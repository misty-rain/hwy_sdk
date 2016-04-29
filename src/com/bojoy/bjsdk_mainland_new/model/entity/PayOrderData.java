package com.bojoy.bjsdk_mainland_new.model.entity;

/**
 * 用于sdk服务器端生成充值订单的数据类
 * @author wutao
 *
 */
public class PayOrderData {
	
	/**
	 * 商品名称
	 */
	private String productName = "";
	
	/**
	 * 需要支付订单金额(元)(不能为空)
	 */
	private double cash = 0;
	
	/**
	 * 可获得虚拟币数量(不能为空)
	 */
	private int amount = 0;

	/**
	 * 应用订单号(不能为空)
	 */
	private String appOrderNumber = "";	
	
	/**
	 * 平台应用充值透传参数(原值返回给平台应用，可以是厂家的账号标识或者服务器ID之类)
	 */
	private String ext = "";

	/**
	 * 平台应用充值回调（发货）地址(如果不填，默认平台后台配置的发货地址)
	 */
	private String sendUrl = "";
	
	/**
	 * 支付用户平台UID(不能为空)
	 */
	private String payUserID = "";
	
	/**
	 * 充值用户平台UID(不能为空)(自己充值同payuserid)
	 */
	private String userID = "";
	
	/**
	 * 充值用户openid(不能为空)(自己充值同支付用户的openid)
	 */
	private String passport = "";
	
	/**
	 * 其他扩展信息(可以为空，比如充值卡支付，extend存放页面填写的卡信息)
	 */
	private String extend = "";
	
	/**
	 * 订单类型(不能为空)【1 - 正常充值平台币; 2 - 充值并消费平台币(手游)】
	 */
	private String orderType = "";
	
	/**
	 * 三方用户渠道标识(可以为空)
	 */
	private String channel = "";
	
	/**
	 * 平台应用的区服ID(可以为空)
	 */
	private String appServerID = "";
	
	public PayOrderData() {

	}
	
	
	public PayOrderData(String productName, double cash, int amount, String appOrderNumber, String ext, 
			String sendUrl, String payUserID, String userID, String passport,
			String extend, String orderType, String channel, String appServerID) {
		this.productName = productName;
		this.cash = cash;
		this.amount = amount;
		this.appOrderNumber = appOrderNumber;
		this.ext = ext;
		this.sendUrl =sendUrl;
		this.payUserID = payUserID;
		this.userID = userID;
		this.passport = passport;
		this.extend = extend;
		this.orderType = orderType;
		this.channel = channel;
		this.appServerID = appServerID;
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

	public String getAppOrderNumber() {
		return appOrderNumber;
	}

	public void setAppOrderNumber(String appOrderNumber) {
		this.appOrderNumber = appOrderNumber;
	}


	public String getPayUserID() {
		return payUserID;
	}


	public void setPayUserID(String payUserID) {
		this.payUserID = payUserID;
	}


	public String getPassport() {
		return passport;
	}


	public void setPassport(String passport) {
		this.passport = passport;
	}


	public String getProductName() {
		return productName;
	}


	public void setProductName(String productName) {
		this.productName = productName;
	}


}
