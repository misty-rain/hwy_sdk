package com.bojoy.bjsdk_mainland_new.utils.payment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.bojoy.bjsdk_mainland_new.eventhandler.event.BJMGFSdkEvent;
import com.bojoy.bjsdk_mainland_new.model.entity.RechargeOrderDetail;
import com.bojoy.bjsdk_mainland_new.support.eventbus.EventBus;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;
import com.skymobi.pay.sdk.normal.zimon.EpsApplication;
import com.skymobi.pay.sdk.normal.zimon.EpsEntry;
import com.skymobi.pay.sdk.normal.zimon.util.SkyPaySignerInfo;


public class SKStartSmsPay {
	private static final String TAG = SKStartSmsPay.class.getSimpleName();

	// 订单参数
	private static final String ORDER_INFO_PAY_METHOD = "payMethod";
	private static final String ORDER_INFO_SYSTEM_ID = "systemId";
	private static final String ORDER_INFO_CHANNEL_ID = "channelId";
	private static final String ORDER_INFO_PAY_POINT_NUM = "payPointNum";
	private static final String ORDER_INFO_ORDER_DESC = "orderDesc";
	private static final String ORDER_INFO_GAME_TYPE = "gameType";

	private static final String STRING_MSG_CODE = "msg_code";
	private static final String STRING_ERROR_CODE = "error_code";
	private static final String STRING_PAY_STATUS = "pay_status";
	private static final String STRING_PAY_PRICE = "pay_price";

	private static final String ORDER_INFO_MERCHANT_ID = "merchantId";
	private static final String ORDER_INFO_APP_ID = "appId";
	private static final String ORDER_INFO_APP_NAME = "appName";
	private static final String ORDER_INFO_APP_VER = "appVersion";
	private static final String ORDER_INFO_PAY_TYPE = "payType";
	private static final String ORDER_INFO_ACCOUNT = "appUserAccount";
	private static final String ORDER_INFO_PRICENOTIFYADDRESS = "priceNotifyAddress";
	// zz$r0oiljy 这是测试密钥
	private static String SKYMOBI_MERCHANT_PASSWORD = "zz$r0oiljy";

	// 请CP替换成在斯凯申请的商户密钥
	private static String MerchantPasswd = SKYMOBI_MERCHANT_PASSWORD;

	private static EpsEntry mEpsEntry = null;
	private static Activity mActivity = null;

	private RechargeOrderDetail rechargeOrderDetail;

	private static String merchantId = "";
	private static String appId = "";

	public SKStartSmsPay(Activity activity) {
		mActivity = activity;
	}

	/**
	 * 初始化斯凯短信支付平台
	 * 
	 * @param mMerchantId
	 *            商户ID，由斯凯提供
	 * @param mAppId
	 * @param mPassword
	 */
	public static void init(String mMerchantId, String mAppId, String mPassword) {
		merchantId = mMerchantId;
		appId = mAppId;
		MerchantPasswd = mPassword;
	}

	public void startPay(int payPoint, int payPrice, boolean useAppUi,
			String systemId, String channelId, String orderId, String pointNum,
			String appName, String appVersion, String price,
			String notifyAddress, String orderDesc, String reserved1, String reserved2,
			String reserved3) {
		mEpsEntry = EpsEntry.getInstance();
		if (merchantId == null) {
			Log.e(TAG, "Fail to pay for not merchantId!");
			return;
		}
		if (merchantId.equals("ZMMerchantId")) {
			Log.w(TAG, "警告！当前商户号为斯凯测试商户号!");
		}
		String merchantPasswd = MerchantPasswd;
		if (merchantPasswd == null) {
			Log.w(TAG, "请添加商户密钥!");
		}
		if (merchantPasswd.equals(SKYMOBI_MERCHANT_PASSWORD)) {
			Log.w(TAG, "警告！当前商户密钥为斯凯测试商户密钥!");
		}

		if (appId == null) {
			Log.e(TAG, "Fail to startPay for not appId!");
			return;
		}

		if (appId.equals("300001")) {
			Log.w(TAG, "警告！当前APP ID为斯凯测试APP ID!");
		}

		String paymethod = "sms";

		/*
		 * 7.价格 短信付费定价（日限75元，月限150元，单次请求上限20元） 第三方付费定价
		 * 目前第三方不支持指定价格，传进来的price会被忽略，实际付费金额跟用户选择充值卡面额有关，以服务端通知为准。
		 */
		// int price = payPrice;

		// 9.计费类型： 0=注册 1=道具 2=积分 3=充值，50=网游小额支付（如果不填，默认是道具）
		String payType = "1";

		// 10.自动生成订单签名
		SkyPaySignerInfo skyPaySignerInfo = new SkyPaySignerInfo();

		skyPaySignerInfo.setMerchantPasswd(merchantPasswd);
		skyPaySignerInfo.setMerchantId(merchantId);
		skyPaySignerInfo.setAppId(appId);
		skyPaySignerInfo.setNotifyAddress(notifyAddress);
		skyPaySignerInfo.setAppName(appName);
		skyPaySignerInfo.setAppVersion(appVersion);
		skyPaySignerInfo.setPayType(payType);
		skyPaySignerInfo.setPrice(price);
		skyPaySignerInfo.setOrderId(orderId);

		skyPaySignerInfo.setReserved1(reserved1, false);
		skyPaySignerInfo.setReserved2(reserved2, false);
		skyPaySignerInfo.setReserved3(reserved3, true);

		int payPointNum = payPoint;
		String gameType = "1"; // 0-单机、1-联网、2-弱联网
		String signOrderInfo = skyPaySignerInfo.getOrderString();

		String orderInfo = ORDER_INFO_PAY_METHOD + "=" + paymethod + "&"
				+ ORDER_INFO_SYSTEM_ID + "=" + systemId + "&"
				+ ORDER_INFO_CHANNEL_ID + "=" + channelId + "&"
				+ ORDER_INFO_PAY_POINT_NUM + "=" + payPointNum + "&"
				+ ORDER_INFO_GAME_TYPE + "=" + gameType + "&" + "useAppUI="
				+ useAppUi + "&" + signOrderInfo;

		orderDesc += "需支付N.NN元。";
		orderInfo += "&" + ORDER_INFO_ORDER_DESC + "=" + orderDesc;

		Log.i(TAG, "orderInfo::" + orderInfo);

		// 开始计费
		int payRet = mEpsEntry.startPay(mActivity, orderInfo, mPayHandler);
		if (EpsEntry.PAY_RETURN_SUCCESS == payRet) {
			// 初始化成功
			LogProxy.i(TAG, "接口斯凯付费调用成功");
		} else {
			// 未初始化 \ 传入参数有误 \ 服务正处于付费状态
			LogProxy.i(TAG, "调用接口失败" + payRet);
		}
	}

	private Handler mPayHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			LogProxy.i(TAG, "msg.what = " + msg.what);
			if (msg.what == EpsEntry.MSG_WHAT_TO_APP) {
				String retInfo = (String) msg.obj;
				Map<String, String> map = new HashMap<String, String>();

				String[] keyValues = retInfo.split("&|=");
				for (int i = 0; i < keyValues.length; i = i + 2) {
					map.put(keyValues[i], keyValues[i + 1]);
					LogProxy.i(TAG, "key: " + keyValues[i] + "; value: "
							+ keyValues[i + 1]);
				}

				int msgCode = Integer.parseInt(map.get(STRING_MSG_CODE));
				LogProxy.i(TAG, "retInfo = " + retInfo);
				if (msgCode == 100) {

					// 短信付费返回
					if (map.get(STRING_PAY_STATUS) != null) {
						int payStatus = Integer.parseInt(map
								.get(STRING_PAY_STATUS));
						int payPrice = Integer.parseInt(map
								.get(STRING_PAY_PRICE));
						int errcrCode = 0;
						if (map.get(STRING_ERROR_CODE) != null) {
							errcrCode = Integer.parseInt(map
									.get(STRING_ERROR_CODE));
						}

						switch (payStatus) {
						case 102:
							// 充值成功

							EventBus.getDefault().post(
									new BJMGFSdkEvent(
											BJMGFSdkEvent.RECHARGE_SUCCESS));
							break;
						case 101:
							EventBus.getDefault().post(
									new BJMGFSdkEvent(
											BJMGFSdkEvent.RECHARGE_FAIL));
							break;
						}
					}
				} else {
					// 解析错误码
					int errorCode = Integer
							.parseInt(map.get(STRING_ERROR_CODE));
					LogProxy.i(TAG, "errorCode: " + errorCode + ";");
				}
			}
			mActivity.finish();
		}
	};
}
