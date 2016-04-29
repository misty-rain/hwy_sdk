package com.bojoy.bjsdk_mainland_new.ui.view.payment.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.app.tools.PayTools;
import com.bojoy.bjsdk_mainland_new.model.entity.PayOrderData;
import com.bojoy.bjsdk_mainland_new.model.entity.RechargeOrderDetail;
import com.bojoy.bjsdk_mainland_new.presenter.payment.IPaymentPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.payment.impl.PaymentPresenterImpl;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseActivityPage;
import com.bojoy.bjsdk_mainland_new.ui.view.payment.IPaymentView;
import com.bojoy.bjsdk_mainland_new.utils.DeviceUtil;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.payment.SKStartSmsPay;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMSdkDialog;

/**
 * 斯凯短信充值方式界面
 *
 * @author sunhaoyang
 *
 */
public class DockSMSpaySKBasePage extends BaseActivityPage implements IPaymentView,OnClickListener {

	private final String TAG = DockSMSpaySKBasePage.class.getSimpleName();

	private LinearLayout llYd, llDx, llLt, llContent;
	private View vYd, vDx, vLt;
	private TextView txYd, txLt, txDx;

	private static final int TAG_YD = 1;
	private static final int TAG_LT = 2;
	private static final int TAG_DX = 3;

	public static final String SYSTEM_ID = "systemId";
	public static final String CHANNAL_ID = "channelId";
	public static final String PAY_POINT_NUM = "payPointNum";
	public static final String PAY_ORDER_ID = "orderId";
	public static final String PAY_RESERVED1 = "reserved1";
	public static final String PAY_RESERVED2 = "reserved2";
	public static final String PAY_RESERVED3 = "reserved3";
	public static final String PAY_APP_NAME = "appName";
	public static final String PAY_APP_VERSION = "appVersion";
	public static final String PAY_PRICE = "price";
	public static final String PAY_NOTIFY_ADDRESS = "notifyAddress";
	public static final String PAY_ORDER_DESC = "orderDesc";

	public double deltaMoney;

	public int balance;

	public boolean isRecharge;

	private String strSMSNotice = "";

	private SKStartSmsPay mSmsPay = null;

	/**
	 * 商户服务器交互地址
	 */
	public String spurl = "";

	/**
	 * 所购物品描述
	 */
	private int goodsCode = 0;

	private int cash = 0;

	private IPaymentPresenter iPaymentPresenter;
	private RechargeOrderDetail rechargeOrderDetail;
	private PayOrderData payOrderData;		//订单数据
	private PayTools mPayTools;

	private RelativeLayout rlBack;

	public DockSMSpaySKBasePage(Context context, PageManager manager, boolean isRecharge,
			BJMGFActivity activity) {
		super(ReflectResourceId.getLayoutId(context,
				Resource.layout.bjmgf_sdk_dock_recharge_sms_sk_base_page),
				context, manager, activity);
		this.isRecharge = isRecharge;
		this.mPayTools = PayTools.getInstance();
		this.payOrderData = mPayTools.getPayOrderData();
		iPaymentPresenter = new PaymentPresenterImpl(context, this);
		mSmsPay = new SKStartSmsPay(activity);
	}

	@Override
	public void onCreateView(View view) {
		super.onCreateView(view);
	}


	@Override
	public void setView() {
		llYd = getView(Resource.id.bjmgf_sdk_sms_pay_tag_yd);
		llDx = getView(Resource.id.bjmgf_sdk_sms_pay_tag_dx);
		llLt = getView(Resource.id.bjmgf_sdk_sms_pay_tag_lt);

		txYd = getView(Resource.id.bjmgf_sdk_sms_pay_tag_yd_str);
		txLt = getView(Resource.id.bjmgf_sdk_sms_pay_tag_lt_str);
		txDx = getView(Resource.id.bjmgf_sdk_sms_pay_tag_dx_str);

		vYd = getView(Resource.id.bjmgf_sdk_sms_pay_tag_yd_line);
		vDx = getView(Resource.id.bjmgf_sdk_sms_pay_tag_dx_line);
		vLt = getView(Resource.id.bjmgf_sdk_sms_pay_tag_lt_line);

		rlBack = getView(Resource.id.bjmgf_sdk_dock_recharge_sms_closeLlId);
		llContent = getView(Resource.id.bjmgf_sdk_dock_recharge_sms_content);

		setLine(TAG_YD);
		llContent.addView(new DockSMSpaySKYdPage(context,
				DockSMSpaySKBasePage.this).getView());

		llYd.setOnClickListener(this);
		llDx.setOnClickListener(this);
		llLt.setOnClickListener(this);
		rlBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		clearView();
		if (v.equals(llYd)) {
			setLine(TAG_YD);
			llContent.addView(new DockSMSpaySKYdPage(context,
					DockSMSpaySKBasePage.this).getView());
		} else if (v.equals(llDx)) {
			setLine(TAG_DX);
			llContent.addView(new DockSMSpaySKDxLtPage(context,
					DockSMSpaySKBasePage.this).getView());
		} else if (v.equals(llLt)) {
			setLine(TAG_LT);
			llContent.addView(new DockSMSpaySKDxLtPage(context,
					DockSMSpaySKBasePage.this).getView());
		} else if (v.equals(rlBack)) {
			goBack();
		}
	}

	private void clearView() {
		llContent.removeAllViews();
	}

	private void setLine(int Tag) {
		vYd.setVisibility(View.GONE);
		vDx.setVisibility(View.GONE);
		vLt.setVisibility(View.GONE);

		txYd.setTextColor(getContext().getResources().getColor(
				ReflectResourceId.getColorId(activity,
						Resource.color.bjmgf_sdk_sms_pay_sk_title)));
		txLt.setTextColor(getContext().getResources().getColor(
				ReflectResourceId.getColorId(activity,
						Resource.color.bjmgf_sdk_sms_pay_sk_title)));
		txDx.setTextColor(getContext().getResources().getColor(
				ReflectResourceId.getColorId(activity,
						Resource.color.bjmgf_sdk_sms_pay_sk_title)));

		switch (Tag) {
		case TAG_YD:
			vYd.setVisibility(View.VISIBLE);
			txYd.setTextColor(getContext().getResources().getColor(
					ReflectResourceId.getColorId(activity,
							Resource.color.bjmgf_sdk_white)));
			break;
		case TAG_LT:
			vLt.setVisibility(View.VISIBLE);
			txLt.setTextColor(getContext().getResources().getColor(
					ReflectResourceId.getColorId(activity,
							Resource.color.bjmgf_sdk_white)));
			break;
		case TAG_DX:
			vDx.setVisibility(View.VISIBLE);
			txDx.setTextColor(getContext().getResources().getColor(
					ReflectResourceId.getColorId(activity,
							Resource.color.bjmgf_sdk_white)));
			break;
		default:
			break;
		}
	}

	/**
	 * 发送充值请求
	 */
	private void sendRechargeRequest(int cash) {
		// 金额不足以支付订单 弹出提示 请用户确定
		if (payOrderData == null) {
			LogProxy.i(TAG, "send recharge a");
			LogProxy.i(TAG, "payOrderDatad is null");
			quit();
			return;
		}
		if (!isRecharge) {
			double leftMoney = (balance + cash * PayTools.PAY_SMS_RATIO)
					- payOrderData.getCash() * PayTools.PAY_RATIO;
			if (leftMoney < 0) {
				showPayConfirmDialog();
				return;
			}
		}

		LogProxy.d(TAG, "cash=" + cash);
		doPay();
	}

	private void doPay() {
		//setRequestParams();
		showProgressDialog();
		iPaymentPresenter.startSmsPay(context, rechargeOrderDetail);
	}

	/**
	 * Dialog请用户确认支付 当用户选择的金额 不足以支付订单时弹出
	 */
	private void showPayConfirmDialog() {
		LogProxy.i(TAG, "open dialog");
		try {
			final BJMSdkDialog dialog = new BJMSdkDialog(activity);
			dialog.setTitle(getString(Resource.string.bjmgf_sdk_dock_pay_center_dialog_title_str));
			dialog.setMessage(getString(Resource.string.bjmgf_sdk_dock_recharge_prompt_paylessStr));
			dialog.setNegativeButton(
					getString(Resource.string.bjmgf_sdk_dock_dialog_btn_cancel_str),
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
			dialog.setPositiveButton(
					getString(Resource.string.bjmgf_sdk_dock_dialog_btn_ok_str),
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
							doPay();
						}
					});
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
			LogProxy.i(TAG, "dialog is error");
		}
	}

	public void startRechargeAndPay(int goodsCode, int cash) {
		this.goodsCode = goodsCode;
		this.cash = cash;
		if (cash <= 0) {
			showToastCenter(getString(Resource.string.bjmgf_sdk_dock_recharge_pleaseChooseMoney));
			return;
		}
		if (!DeviceUtil.isHaveSimCard(context)) {
			showToastCenter(getString(Resource.string.bjmgf_sdk_no_sim_card));
			return;
		}
		payOrderData = mPayTools.getPayOrderData();
		if (payOrderData == null) {
			LogProxy.i(TAG, "payOrderDataa is null");
			quit();
			return;
		}
		rechargeOrderDetail = new RechargeOrderDetail(
				payOrderData.getAppOrderNumber(),
				cash,
				payOrderData.getCash() * 2,
				(int) (payOrderData.getCash() * 2 * PayTools.PAY_SMS_RATIO),
				payOrderData.getUserID(), payOrderData.getExt(), payOrderData
						.getSendUrl(), payOrderData.getChannel(), payOrderData
						.getAppServerID(), payOrderData.getExtend(),
				payOrderData.getOrderType(), "18656508068",
                PayTools.SK_SMSPAY_TYPE, payOrderData.getProductName());
		if (isRecharge) {
			rechargeOrderDetail.setMoney((int) cash);
			rechargeOrderDetail.setAmount((int) cash
					* PayTools.PAY_SMS_RATIO);
		}
        mPayTools.setRechargeOrderDetail(rechargeOrderDetail);

		LogProxy.i(TAG, "click recharge");
		sendRechargeRequest(cash);
	}

	private void smsPay(String systemId, String channalId, String orderId,
			String pointNum, String appName, String appVersion, String price,
			String notifyAddress, String orderDesc, String reserved1, String reserved2,
			String reserved3) {
		mSmsPay.startPay(goodsCode, cash * 100, true, systemId, channalId,
				orderId, pointNum, appName, appVersion, price, notifyAddress,
				orderDesc, reserved1, reserved2, reserved3);
		dismissProgressDialog();
	}

	public void resetOrderInfo() {
        mPayTools.setPayInfo("");// 充值金额变化 重新请求订单
		LogProxy.i(TAG, "OrderInfo Reset!");
	}

	public void resettRechargeOrderInfo() {
        mPayTools.setRechargeOrderDetail(null);
	}

	public String getSMSNotice() {
		return strSMSNotice;
	}

	private String getPayInfoValue(String key, String payInfo) {
		String value = "";
		Pattern pattern = Pattern.compile(key + "=([\\s\\S]*?)(&|$)");
		Matcher matcher = pattern.matcher(payInfo);
		if (matcher.find()) {
			value = matcher.group(1);
		}
		LogProxy.i(TAG, "Pattern:" + value);
		return value;
	}

	@Override
	public void showResult(String data) {
		
	}

	@Override
	public void getPayInfo(String payInfo) {
		mPayTools.setPayInfo(payInfo);
		LogProxy.d(TAG, "SMS : Request_Recharge_Order payinfo="
				+ payInfo);
		smsPay(getPayInfoValue(SYSTEM_ID, payInfo),
				getPayInfoValue(CHANNAL_ID, payInfo),
				getPayInfoValue(PAY_ORDER_ID, payInfo),
				getPayInfoValue(PAY_POINT_NUM, payInfo),
				getPayInfoValue(PAY_APP_NAME, payInfo),
				getPayInfoValue(PAY_APP_VERSION, payInfo),
				getPayInfoValue(PAY_PRICE, payInfo),
				getPayInfoValue(PAY_NOTIFY_ADDRESS, payInfo),
				getPayInfoValue(PAY_ORDER_DESC, payInfo),
				getPayInfoValue(PAY_RESERVED1, payInfo),
				getPayInfoValue(PAY_RESERVED2, payInfo),
				getPayInfoValue(PAY_RESERVED3, payInfo));
	}

	@Override
	public void getUnionInfo(String tn, String serverMode) {

	}

	@Override
	public void getPayResult(String data,String msg) {

	}

	@Override
	public void showError(String message) {
		dismissProgressDialog();
		showToastCenter(message);// 错误提示
	}

	@Override
	public void showSuccess() {

	}
}
