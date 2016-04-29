package com.bojoy.bjsdk_mainland_new.app.tools;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.bojoy.bjsdk_mainland_new.model.entity.PayOrderData;
import com.bojoy.bjsdk_mainland_new.model.entity.RechargeCard;
import com.bojoy.bjsdk_mainland_new.model.entity.RechargeCardDetailData;
import com.bojoy.bjsdk_mainland_new.model.entity.RechargeData;
import com.bojoy.bjsdk_mainland_new.model.entity.RechargeOrderDetail;
import com.bojoy.bjsdk_mainland_new.ui.view.payment.impl.PayRechargeCardView;
import com.bojoy.bjsdk_mainland_new.ui.view.payment.impl.PayRechargeCardViewNext;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;
import java.util.ArrayList;

/**
 * Created by wutao on 2016/1/7. 好玩友支付充值业务工具类 ps.请后续开发者将充值支付相关业务公共代码，放在此类中 ，
 */
public class PayTools {


	public static final String ALIPAY_TYPE = "115";		//115-支付宝无线快捷支付 
	public static final String SMSPAY_TYPE = "116";		//116-大额短信支付SDK
	public static final String SK_SMSPAY_TYPE = "114";		//114-斯凯短信充值SDK
	public static final String UNIONPAY_TYPE = "117";		//117-银联支付
	public static final String CM_CARD_PAY_TYPE = "111";	//111-移动卡支付
	public static final String CU_CARD_PAY_TYPE = "112";	//112-联通卡支付
	public static final String CT_CARD_PAY_TYPE = "113";	//113-电信卡支付
	public static final String WXPAY_TYPE = "118";		//118-微信支付
	public static final int ORDER_TYPE_RECHARGE = 1;
	public static final int ORDER_TYPE_RECHARGE_PAY = ORDER_TYPE_RECHARGE + 1;
	private String orderType = "2";		//订单类型(不能为空)【1 - 正常充值平台币;2 - 充值并消费平台币(手游)】
	private String payInfo = "";	//支付宝支付传入参数
	public static final double PAY_RATIO = 10;		//充值渠道比例
	public static final int PAY_SMS_RATIO = 5;		//短代支付比例
	public static final int MAX_ALIPAY_MONEY = 5000;	//客户端支付宝充值最大金额
	private String mCurrentType;
	private static PayTools payTools;
	private RechargeData rechargeData;
    // 用户账户余额足够支付订单的数据类(用应用传入的订单数据对其各个属性赋值)
    private PayOrderData payOrderData;
	// 充值订单的数据类
	private RechargeOrderDetail rechargeOrderDetail;
	// 充值卡信息类
	private ArrayList<RechargeCardDetailData> rechargeCardsInfo;

	public static PayTools getInstance() {
		if (payTools == null) {
			synchronized (PayTools.class) {
				if (payTools == null) {
					payTools = new PayTools();
				}
			}
		}
		return payTools;
	}

	public void setCurrentType(String type){this.mCurrentType = type;}

	public String getCurrentType() {return mCurrentType;}

	public void setPayOrderData(PayOrderData payOrderData) {
		this.payOrderData = payOrderData;
	}

	public PayOrderData getPayOrderData() {
		return payOrderData;
	}

	public RechargeOrderDetail getRechargeOrderDetail() {
		return rechargeOrderDetail;
	}

	public void setRechargeOrderDetail(RechargeOrderDetail rechargeOrderDetail) {
		this.rechargeOrderDetail = rechargeOrderDetail;
	}

	/**
	 * 清除支付相关信息
	 */
	public void clearPayDatas() {
		setPayOrderData(null);
		setRechargeOrderDetail(null);
		setPayInfo("");

	}

	public String getPayInfo() {
		return payInfo;
	}

	public void setPayInfo(String payInfo) {
		this.payInfo = payInfo;

	}
	
	/**
	 * 充值参数数据类
	 * @param deltaMoney 充值金额
	 * @param mType  支付类型
	 * @param orderData 支付数据类
	 * @return
	 */
	public RechargeOrderDetail getRequestParms(int deltaMoney,String mType,PayOrderData orderData) {
		double money = deltaMoney / payTools.PAY_RATIO;
		RechargeOrderDetail rechargeOrderDetail = new RechargeOrderDetail(
				"" + System.currentTimeMillis(), money,
				orderData.getCash(), deltaMoney, orderData.getUserID(),

				orderData.getExt(), orderData.getSendUrl(),
				orderData.getChannel(), orderData.getAppServerID(),
				orderData.getExtend(), orderData.getOrderType(), "", mType,

				orderData.getProductName());
		return rechargeOrderDetail;

	}
	
	/**
	 * 充值卡数据类获得
	 */
	public String[] getRechargeCardInfo(ArrayList<RechargeCardDetailData> list){
		String[] operatorsName = new String[list.size()];
		for (int i = 0; i < operatorsName.length; i++) {
			operatorsName[i] = list.get(i).tip;
		}
		return operatorsName;
	}
	/**
	 * 充值卡支付
	 * 判断用户输入的卡号长度是否合法
	 * @return true:合法 可以提交请求
	 */
	public static boolean checkCardNoLength(RechargeCard rechargeCard, EditText cardNoEditText) {
		if(StringUtility.isEmpty(cardNoEditText.getText().toString())
				|| cardNoEditText.getText().length() != rechargeCard.cardNumberLength) {
			return false;
		}
		return true;
	}

	/**
	 * 充值卡支付
	 * 判断用户输入的密码长度是否合法
	 * @return true:合法 可以提交请求
	 */
public static  boolean checkCardPwLength(RechargeCard rechargeCard, EditText cardPWEditText ) {
		if(StringUtility.isEmpty(cardPWEditText.getText().toString())
				|| cardPWEditText.getText().length() != rechargeCard.cardPwdLength) {
			return false;
		}
		return true;
	}

	/**
	 * 设置用户充值信息
	 * @param context 上下文
	 * @param payRechargeCardViewNext
	 * @param rechargeCard 充值卡类
	 * @param selectedInfoTextView 用户所选金额显示
     * @param cash 用户所选金额
     */
	public void setSelectedInfoTextViewStr(final Context context, PayRechargeCardViewNext payRechargeCardViewNext, RechargeCard rechargeCard, TextView selectedInfoTextView, int cash){
		String part1=payRechargeCardViewNext.getString(Resource.string.bjmgf_sdk_dock_recharge_cardPay_next_chose_str);
		String part2 = payRechargeCardViewNext.getString(Resource.string.bjmgf_sdk_dock_recharge_cardPay_next_recharge_str);
		String unit = payRechargeCardViewNext.getString(Resource.string.bjmgf_sdk_dock_recharge_cardPay_yuan_space_unit_str);
		String selectedStr = part1 +rechargeCard.name + part2 + cash + unit;
		SpannableStringBuilder builder=new SpannableStringBuilder(selectedStr);
		ForegroundColorSpan titleSpan = new ForegroundColorSpan(
				context.getResources().getColor(ReflectResourceId.getColorId(context, Resource.color.bjmgf_sdk_black)));
		ForegroundColorSpan nameSpan = new ForegroundColorSpan(
				context.getResources().getColor(ReflectResourceId.getColorId(context, Resource.color.bjmgf_sdk_text_wish_role_color)));
		ForegroundColorSpan unitSpan = new ForegroundColorSpan(
				context.getResources().getColor(ReflectResourceId.getColorId(context, Resource.color.bjmgf_sdk_text_wish_role_color)));
		ForegroundColorSpan priceSpan = new ForegroundColorSpan(
				context.getResources().getColor(ReflectResourceId.getColorId(context, Resource.color.bjmgf_sdk_text_wish_role_color)));
		int index0 = part1.length();
		int index1 = part1.length() + rechargeCard.name.length();
		int index2 = part1.length() + rechargeCard.name.length() + part2.length();
		int index3 = part1.length() + rechargeCard.name.length() + part2.length() + String.valueOf(cash).length();
		builder.setSpan(titleSpan, 0, index0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder.setSpan(nameSpan, index0, index1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		builder.setSpan(titleSpan, index1, index2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder.setSpan(priceSpan, index2, index3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder.setSpan(unitSpan, index3, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		selectedInfoTextView.setText(builder);
	}
	/**
	 * 设置文字颜色
	 * @param sFinalStr 充值金额
	 * @param textView 充值金额文本框
	 */
	private void setPriceTitleColor(String sFinalStr, TextView textView,Context context) {
		SpannableStringBuilder builder = new SpannableStringBuilder(sFinalStr);
		ForegroundColorSpan priceSpan = new ForegroundColorSpan(
				context.getResources().getColor(ReflectResourceId.getColorId(context, Resource.color.bjmgf_sdk_textgray)));
		builder.setSpan(priceSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textView.setText(builder);
	}

	/**
	 * 显示用户支付后剩余的U币数
	 * @param context 上下文
	 * @param money 用户支付金额
	 * @param payRechargeCardViewNext  充值卡界面
	 * @param goodDescriptionUnit
	 * @param goodDescription 商品描述
	 * @param balance 余额
	 * @param payOrderData 支付数据
     * @param cash 用户充值金额
     * @param rechargePrompt 充值提示语
     */
	public void cardNextrefreshViewByCash(final Context context,int money,PayRechargeCardViewNext payRechargeCardViewNext, final TextView goodDescriptionUnit,final TextView goodDescription ,int balance,PayOrderData payOrderData,int cash, TextView rechargePrompt) {
		String sCashFormat = payRechargeCardViewNext.getString(Resource.string.bjmgf_sdk_dock_recharge_goods_countStr);
		String sFinalStr = String.format(sCashFormat, (int) (money * PAY_RATIO));
		goodDescriptionUnit.setVisibility(View.VISIBLE);
		goodDescription.setVisibility(View.VISIBLE);
		setPriceTitleColor(sFinalStr, goodDescription,context);
		int leftMoney = (int) (balance + money * payTools.PAY_RATIO - payOrderData.getCash() * payTools.PAY_RATIO);
		if (leftMoney >= 0) {
			String sbalanceFormat = payRechargeCardViewNext.getString(Resource.string.bjmgf_sdk_dock_recharge_prompt_paymoreStr);
			String str = String.format(sbalanceFormat, leftMoney);
			//支付结余
			rechargePrompt.setText(str);

		} else if (leftMoney < 0) {
			rechargePrompt.setText(payRechargeCardViewNext.getString(Resource.string.bjmgf_sdk_dock_recharge_prompt_paylessStr));
		} else {
			rechargePrompt.setText("");
		}
		if (cash == 0) {
			rechargePrompt.setText("");
		}
	}

    /**
	 *获得支付所需数据
	 * @param payOrderData  支付数据类
	 * @param payid  充值卡类型id
	 * @param extendStr 支付数据
	 * @param cash  用户所选金额
     * @return
     */
	public RechargeOrderDetail getRechargeOrderDEtailDta(PayOrderData payOrderData,String payid,String extendStr,int cash){
		RechargeOrderDetail rechargeOrderDetail=new RechargeOrderDetail(payOrderData.getAppOrderNumber(),
				payOrderData.getCash(), cash, (int) (cash* payTools.PAY_RATIO), payOrderData.getUserID(),
				payOrderData.getExt(), payOrderData.getSendUrl(), payOrderData.getChannel(),
				payOrderData.getAppServerID(), extendStr, payOrderData.getOrderType(),
				"", payid, payOrderData.getProductName());
		return rechargeOrderDetail;
	}

	/**
	 * 设置U币
	 * @param money 需要支付的金额
	 * @param payRechargeCardView 充值界面
	 * @param context  上下文
	 * @param goodDescription  商品描述
     */
	public void refreshViewByCash(int money,PayRechargeCardView payRechargeCardView,final Context context, TextView goodDescription){
		String rechargeCashStr=payRechargeCardView.getString(Resource.string.bjmgf_sdk_dock_recharge_goods_countStr);
		String rechargeCash= String.format(rechargeCashStr,(int)(money*payTools.PAY_RATIO));
		String unit = payRechargeCardView.getString(Resource.string.bjmgf_sdk_dock_pay_center_unitStr);
		goodDescription.setVisibility(View.VISIBLE);
		SpannableStringBuilder builder=new SpannableStringBuilder(rechargeCash+unit);
		//ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
		ForegroundColorSpan titleSpan=new ForegroundColorSpan(context.getResources().getColor(ReflectResourceId.getColorId(context, Resource.color.bjmgf_sdk_textgray)));
		ForegroundColorSpan unitSpan = new ForegroundColorSpan(
				context.getResources().getColor(ReflectResourceId.getColorId(context, Resource.color.bjmgf_sdk_textgray)));
		ForegroundColorSpan priceSpan = new ForegroundColorSpan(
				context.getResources().getColor(ReflectResourceId.getColorId(context, Resource.color.bjmgf_sdk_text_price_color)));
		builder.setSpan(titleSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder.setSpan(priceSpan, 5, rechargeCash.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		builder.setSpan(unitSpan, rechargeCash.length(), builder.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		builder.setSpan(new AbsoluteSizeSpan(16,true), 0,rechargeCash.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder.setSpan(new AbsoluteSizeSpan(12, true), rechargeCash.length(), builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		goodDescription.setText(builder);



	}

    /**
	 * 设置充值数据类
	 * @param rechargeData 充值数据类
	 */
	public void setRechargeData(RechargeData rechargeData){
		this.rechargeData=rechargeData;
	}

	/**
	 * 取得充值数据类
	 * @return
     */
	public RechargeData getRechargeData(){
		return rechargeData;

	}
}
