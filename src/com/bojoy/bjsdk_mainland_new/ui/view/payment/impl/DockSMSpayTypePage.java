package com.bojoy.bjsdk_mainland_new.ui.view.payment.impl;

import android.app.Activity;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.app.tools.PayTools;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BJMGFSdkEvent;
import com.bojoy.bjsdk_mainland_new.model.entity.PayOrderData;
import com.bojoy.bjsdk_mainland_new.model.entity.RechargeOrderDetail;
import com.bojoy.bjsdk_mainland_new.presenter.payment.IPaymentPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.payment.impl.PaymentPresenterImpl;
import com.bojoy.bjsdk_mainland_new.support.eventbus.EventBus;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseActivityPage;
import com.bojoy.bjsdk_mainland_new.ui.view.payment.IPaymentView;
import com.bojoy.bjsdk_mainland_new.utils.DeviceUtil;
import com.bojoy.bjsdk_mainland_new.utils.DomainUtility;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;
import com.bojoy.bjsdk_mainland_new.utils.ToastUtil;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMSdkDialog;
import com.vsofo.vsofopay.SDKAPI;
import com.vsofo.vsofopay.util.IPayResultCallback;

/**
 * 盈华短信充值
 * @author zhouhonghai
 *
 */
public class DockSMSpayTypePage extends BaseActivityPage implements IPaymentView, IPayResultCallback, OnClickListener{
	
	private static String TAG = DockSMSpayTypePage.class.getSimpleName();
	
	/**
	 * 视图控件
	 */
	private RelativeLayout mBackLayout = null;
	private TextView goodDescription = null;	//描述
	private TextView rechargePrompt = null;		//充值前提示语
	private Button rechargeAndPayBtn = null;	//充值并支付按钮
	private TextView payTextView_5, payTextView_10, payTextView_15,
			payTextView_20, payTextView_30;
	private TextView smsNoticeTextView;

	private IPaymentPresenter iPaymentPresenter;
	private PayOrderData payOrderData;		//订单数据
	private String spurl = "";		//商户服务器交互地址
	private String goods = "";		//所购物品描述
	private String smsNotice = "";
	private int cash = 0;		//用户所选的充值金额
	private RechargeOrderDetail rechargeOrderDetail;
	private double deltaMoney;
	private int balance;
	private boolean isRecharge = false;
	private PayTools mPayTools;
	private EventBus eventBus;

	public DockSMSpayTypePage(Context context,
			PageManager manager, boolean isRecharge, BJMGFActivity activity) {
		super(ReflectResourceId.getLayoutId(context,
				Resource.layout.bjmgf_sdk_dock_recharge_sms_page), context,
				manager, activity);
		this.isRecharge = isRecharge;
		this.mPayTools = PayTools.getInstance();
		this.payOrderData = mPayTools.getPayOrderData();
		iPaymentPresenter = new PaymentPresenterImpl(context, this);
		eventBus = EventBus.getDefault();
	}
	
	@Override
	public void onCreateView(View view) {
		
		mBackLayout = (RelativeLayout) view.findViewById(ReflectResourceId
				.getViewId(context,
						Resource.id.bjmgf_sdk_dock_recharge_sms_closeLlId));
		goodDescription = (TextView) view
				.findViewById(ReflectResourceId
						.getViewId(
								context,
								Resource.id.bjmgf_sdk_dock_recharge_sms_recharge_goodsStrId));

		// 运营商短代支付渠道关闭时，给用户提示
		smsNotice = DomainUtility.getInstance().getSmsNotice(context);
		smsNoticeTextView = (TextView) view
				.findViewById(ReflectResourceId
						.getViewId(
								context,
								Resource.id.bjmgf_sdk_dock_recharge_sms_notice_layoutId));
		if (smsNotice.equals("0")
				|| smsNotice.equals("")
				|| smsNotice.isEmpty()) {
			smsNoticeTextView.setVisibility(View.GONE);
		} else {
			smsNoticeTextView.setText(smsNotice);
			smsNoticeTextView.setVisibility(View.VISIBLE);
		}

		rechargePrompt = (TextView) view
				.findViewById(ReflectResourceId
						.getViewId(
								context,
								Resource.id.bjmgf_sdk_dock_recharge_sms_recharge_promptId));

		rechargeAndPayBtn = (Button) view.findViewById(ReflectResourceId
				.getViewId(context,
						Resource.id.bjmgf_sdk_dock_recharge_sms_buybtnId));

		mBackLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mPayTools.setRechargeOrderDetail(null);
				if (StringUtility.isEmpty(mPayTools.getPayInfo())) {// 如果创建过订单
					goBack();
				} else {// 回到应用
					mPayTools.clearPayDatas();// 清除订单信息
					quit();
				}
			}
		});

		rechargeAndPayBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LogProxy.i(TAG, "click recharge a");
				if (cash <= 0) {
					ToastUtil.showMessage(context, getString(Resource.string.bjmgf_sdk_dock_recharge_pleaseChooseMoney));
					return;
				}
				if (!DeviceUtil.isHaveSimCard(context)) {
					ToastUtil.showMessage(context, getString(Resource.string.bjmgf_sdk_no_sim_card));
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
						payOrderData.getUserID(), payOrderData.getExt(),
						payOrderData.getSendUrl(), payOrderData.getChannel(),
						payOrderData.getAppServerID(),
						payOrderData.getExtend(), payOrderData.getOrderType(),
						"18656508068", PayTools.SMSPAY_TYPE,//##
						payOrderData.getProductName());
				if (isRecharge) {
					rechargeOrderDetail.setMoney((int) cash);
					rechargeOrderDetail.setAmount((int) cash
							* PayTools.PAY_SMS_RATIO);
				}
				mPayTools.setRechargeOrderDetail(rechargeOrderDetail);
				LogProxy.i(TAG, "click recharge");
				sendRechargeRequest();
			}
		});
		
		createPrice(view);

		if (isRecharge) {
			rechargePrompt.setVisibility(View.GONE);
		} else {
			rechargePrompt.setVisibility(View.VISIBLE);
		}
		super.onCreateView(view);
	}
	
	/**
	 * 选择既定价格
	 * 
	 * @param view
	 */
	private void createPrice(View view) {
		payTextView_5 = (TextView) view.findViewById(ReflectResourceId
				.getViewId(context,
						Resource.id.bjmgf_sdk_dock_recharge_sms_price_5_Id));
		payTextView_10 = (TextView) view.findViewById(ReflectResourceId
				.getViewId(context,
						Resource.id.bjmgf_sdk_dock_recharge_sms_price_10_Id));
		payTextView_15 = (TextView) view.findViewById(ReflectResourceId
				.getViewId(context,
						Resource.id.bjmgf_sdk_dock_recharge_sms_price_15_Id));
		payTextView_20 = (TextView) view.findViewById(ReflectResourceId
				.getViewId(context,
						Resource.id.bjmgf_sdk_dock_recharge_sms_price_20_Id));
		payTextView_30 = (TextView) view.findViewById(ReflectResourceId
				.getViewId(context,
						Resource.id.bjmgf_sdk_dock_recharge_sms_price_30_Id));

		payTextView_5.setOnClickListener(this);
		payTextView_10.setOnClickListener(this);
		payTextView_15.setOnClickListener(this);
		payTextView_20.setOnClickListener(this);
		payTextView_30.setOnClickListener(this);

		if (isRecharge) { 	//##if (isRecharge && !bjmgfData.isPlatform()) {
			LogProxy.i(TAG, "aaaaaaa");
			refreshViewByCashValue(0);
		} else {
			LogProxy.i(TAG, "bbbbbbbb");
			setDefaultRechargePrice();
		}
	}
	
	/**
	 * 默认选中充值金额元
	 */
	private void setDefaultRechargePrice() {
		if (deltaMoney <= 5 * PayTools.PAY_SMS_RATIO && deltaMoney > 0) {
			selectedPrice(payTextView_5);
		} else if (deltaMoney <= 10 * PayTools.PAY_SMS_RATIO
				&& deltaMoney > 5 * PayTools.PAY_SMS_RATIO) {
			selectedPrice(payTextView_10);
		} else if (deltaMoney <= 15 * PayTools.PAY_SMS_RATIO
				&& deltaMoney > 10 * PayTools.PAY_SMS_RATIO) {
			selectedPrice(payTextView_15);
		} else if (deltaMoney <= 20 * PayTools.PAY_SMS_RATIO
				&& deltaMoney > 10 * PayTools.PAY_SMS_RATIO) {
			selectedPrice(payTextView_20);
		} else {
			selectedPrice(payTextView_30);
		}
	}
	
	private void selectedPrice(TextView textView) {
		resetPriceLayout(textView, false);
	}
	
	/**
	 * 恢复价格设置状态
	 * 
	 * @param textView
	 *            当前选中的既定价格
	 * @param isEditSelected
	 *            当前选中的是否是EditText
	 */
	private void resetPriceLayout(TextView textView, boolean isEditSelected) {
		unselectAllPrice();
		if (!isEditSelected) {
			textView.setBackgroundResource(ReflectResourceId.getDrawableId(
					context,
					Resource.drawable.bjmgf_sdk_recharge_item_selected_bg));
			textView.setTextColor(context.getResources().getColor(
					ReflectResourceId.getColorId(context,
							Resource.color.bjmgf_sdk_white)));
			if (!StringUtility.isEmpty(textView.getText().toString())) {
				String str = textView.getText().toString();
				cash = Integer.parseInt(str.substring(0, str.length() - 1));
			}
		} else {
			cash = 0;
		}
		if (cash >= 0) {
			LogProxy.i(TAG, "bbbbbbb");
			refreshViewByCashValue(cash);
		}
		mPayTools.setPayInfo("");// 充值金额变化 重新请求订单
	}

	/**
	 * 根据充值金额 设置页面显示
	 * 
	 * @param money
	 *            用户输入的充值金额
	 */
	private void refreshViewByCashValue(int money) {
		LogProxy.i(TAG, "money is " + money);
		if (payOrderData == null) {
			LogProxy.i(TAG, "payOrderDatac is null");
			quit();
			return;
		}
		// 充值后账户增加的平台币数量
		String sCashFormat = getString(Resource.string.bjmgf_sdk_dock_recharge_goods_countStr);
		String sFinalStr = String.format(sCashFormat, money
				* PayTools.PAY_SMS_RATIO);
		goodDescription.setText(sFinalStr);
		setPriceTitleColor(sFinalStr, goodDescription);
		if (!isRecharge) {
			// 提示语变更
			int leftMooney = (int) ((balance + money
					* PayTools.PAY_SMS_RATIO) - payOrderData.getCash()
					* PayTools.PAY_RATIO);
			if (leftMooney > 0) {
				String sbalanceFormat = getString(Resource.string.bjmgf_sdk_dock_recharge_prompt_paymoreStr);
				String str = String.format(sbalanceFormat, leftMooney);
				rechargePrompt.setText(str);
			} else if (leftMooney < 0) {
				rechargePrompt
						.setText(getString(Resource.string.bjmgf_sdk_dock_recharge_prompt_paylessStr));
			} else {
				rechargePrompt.setText("");
			}
			if (cash == 0) {
				rechargePrompt.setText("");
			}
		}
	}

	/**
	 * 设置文字颜色
	 * 
	 * @param sFinalStr
	 * @param textView
	 */
	private void setPriceTitleColor(String sFinalStr, TextView textView) {
		SpannableStringBuilder builder = new SpannableStringBuilder(sFinalStr);
		ForegroundColorSpan priceSpan = new ForegroundColorSpan(context
				.getResources().getColor(
						ReflectResourceId.getColorId(context,
								Resource.color.bjmgf_sdk_textgray)));
		builder.setSpan(priceSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textView.setText(builder);
	}
	
	/**
	 * 发送充值请求
	 */
	private void sendRechargeRequest() {
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
	
	private void doPay() {
		//setRequestParams();
		// GFHelpler.smsRechargeOrderSDK();
		showProgressDialog();
		iPaymentPresenter.startSmsPay(context, rechargeOrderDetail);
	}
	
	/**
	 * 短信支付接口
	 */
	protected void smsPay() {
		try {
			spurl = mPayTools.getPayInfo();
			goods = cash
					* PayTools.PAY_SMS_RATIO
					+ getString(Resource.string.bjmgf_sdk_dock_pay_center_unitStr);
			// Log.d("1", "goods:" + goods + " spurl" + spurl);
			// spurl = spurl + "&mz=" + cash;
			LogProxy.d(TAG, "goods=" + goods + "\nspurl=" + spurl);
			LogProxy.i(TAG, "activity is " + activity.toString());
			Activity a = activity.getParent();
			if (a == null) {
				a = activity;
			}
			LogProxy.d(TAG, "spurl=" + spurl);
			SDKAPI.startPay(a, spurl, goods, DockSMSpayTypePage.this);
			mPayTools.clearPayDatas();
		} catch (Exception e) {
			e.printStackTrace();
			LogProxy.i(TAG, "smsPay error");
		}
	}

	/**
	 * 取消所有价格的选中状态
	 */
	private void unselectAllPrice() {
		resetTextViewStatus(payTextView_5);
		resetTextViewStatus(payTextView_10);
		resetTextViewStatus(payTextView_15);
		resetTextViewStatus(payTextView_20);
		resetTextViewStatus(payTextView_30);
	}

	private void resetTextViewStatus(TextView textView) {
		textView.setBackgroundResource(ReflectResourceId.getDrawableId(context,
				Resource.drawable.bjmgf_sdk_text_with_roundcorner_selector));
		textView.setTextColor(context.getResources().getColor(
				ReflectResourceId.getColorId(context,
						Resource.color.bjmgf_sdk_black)));
	}

	@Override
	public void setView() {

	}

	@Override
	public void onClick(View v) {
		if (v.equals(payTextView_5)) {
			selectedPrice(payTextView_5);
		} else if (v.equals(payTextView_10)) {
			selectedPrice(payTextView_10);
		} else if (v.equals(payTextView_20)) {
			selectedPrice(payTextView_20);
		} else if (v.equals(payTextView_30)) {
			selectedPrice(payTextView_30);
		} else if (v.equals(payTextView_15)) {
			selectedPrice(payTextView_15);
		}
	}

	@Override
	public void showError(String message) {
		// TODO 自动生成的方法存根
		dismissProgressDialog();
		ToastUtil.showMessage(context,message);
	}

	@Override
	public void showSuccess() {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void showResult(String data) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void getPayInfo(String payInfo) {
		dismissProgressDialog();
		LogProxy.i(TAG, "payInfo:" + payInfo);
		mPayTools.setPayInfo(payInfo);
		smsPay();
	}

	@Override
	public void getUnionInfo(String tn, String serverMode) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void getPayResult(String data,String msg) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void onPayResult(int i, String s, String s1) {
		dismissProgressDialog();
		activity.setNeedOpenDock(false);
		spurl = "";
		goods = "";
		if (100 == i) {
			LogProxy.i("1--传入状态说明:", "100短信成功等待扣费中 ");
			LogProxy.i("2--说明数据是:", s);
			LogProxy.i("3--商户唯一订单号:", s1);
			s = "支付请求已提交，请返回账户查询余额。";
			eventBus.post(new BJMGFSdkEvent(BJMGFSdkEvent.RECHARGE_SUCCESS));
			 ToastUtil.showMessage(activity, s);
		} else if (101 == i) {
			LogProxy.i("1--传入状态数据是i:", "是101代表该手机不能支付 ");
			LogProxy.i("2--说明数据是:", s);
			LogProxy.i("3--商户唯一订单号:", s1);
			eventBus.post(new BJMGFSdkEvent(BJMGFSdkEvent.RECHARGE_FAIL));
			s="该手机不能支付";
			ToastUtil.showMessage(activity, s);
		} else if (102 == i) {
			LogProxy.i("1--传入状态数据是i:", "是102代表该手动模式 ");
			LogProxy.i("2--说明数据是:", s);
			LogProxy.i("3--商户唯一订单号:", s1);
			eventBus.post(new BJMGFSdkEvent(BJMGFSdkEvent.RECHARGE_FAIL));
			ToastUtil.showMessage(activity, s);
		} else if (103 == i) {
			LogProxy.i("1--传入状态数据是i:", "是103需要用户去短信箱子收短信 回复确认 或 移动限制了 ");
			LogProxy.i("2--说明数据是:", s);
			LogProxy.i("3--商户唯一订单号:", s1);
			// s="请到收件箱接收短信，并回复确认支付。";
			s = "支付失败,未产生任何费用。";
			eventBus.post(new BJMGFSdkEvent(BJMGFSdkEvent.RECHARGE_FAIL));
			showResultDialog(s + "\n订单号：" + s1);
			return;
		} else if (104 == i) {
			LogProxy.i("1--传入状态数据是i:", "104数据异常了 ");
			LogProxy.i("2--说明数据是:", s);
			LogProxy.i("3--商户唯一订单号:", s1);
			eventBus.post(new BJMGFSdkEvent(BJMGFSdkEvent.RECHARGE_FAIL));
			// 处理该订单的业务逻辑。
			// Toast.makeText(activity, s,
			// Toast.LENGTH_LONG).show();
		} else if (109 == i) {
			LogProxy.i("1--传入状态数据是i:", "用户退出支付了 ");
			LogProxy.i("2--说明数据是:", s);
			LogProxy.i("3--商户唯一订单号:", s1);
			eventBus.post(new BJMGFSdkEvent(BJMGFSdkEvent.RECHARGE_FAIL));
			// 处理该订单的业务逻辑。
			s = "用户已取消支付";
			ToastUtil.showMessage(activity, s);
		}
		showResultDialog(s + "\n订单号：" + s1);
		LogProxy.d(TAG, "message id= " + i + ": s= " + s + ": s1= " + s1);
	}
	
	private void showResultDialog(String resultMsg) {
		final BJMSdkDialog dialog = new BJMSdkDialog(context);
		dialog.setTitle(getString(
				Resource.string.bjmgf_sdk_dock_pay_center_smsPayStr));
		dialog.setMessage(resultMsg);
		dialog.setPositiveButton(getString(
				Resource.string.bjmgf_sdk_dock_dialog_btn_ok_str),
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						quit();
					}
				});
		try {
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
			LogProxy.i(TAG, "show error");
		}

	}

}
