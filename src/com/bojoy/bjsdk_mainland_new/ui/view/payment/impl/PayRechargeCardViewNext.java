package com.bojoy.bjsdk_mainland_new.ui.view.payment.impl;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bojoy.bjsdk_mainland_new.app.BJMGFSdk;
import com.bojoy.bjsdk_mainland_new.app.tools.PayTools;
import com.bojoy.bjsdk_mainland_new.model.entity.PayOrderData;
import com.bojoy.bjsdk_mainland_new.model.entity.RechargeCard;
import com.bojoy.bjsdk_mainland_new.model.entity.RechargeCardDetailData;
import com.bojoy.bjsdk_mainland_new.model.entity.RechargeOrderDetail;
import com.bojoy.bjsdk_mainland_new.presenter.payment.IPaymentPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.payment.impl.PaymentPresenterImpl;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseActivityPage;
import com.bojoy.bjsdk_mainland_new.ui.view.payment.IPayRechargeCardViewNext;
import com.bojoy.bjsdk_mainland_new.utils.DialogUtil;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMGFDialog;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMSdkDialog;

public class PayRechargeCardViewNext extends BaseActivityPage implements
		IPayRechargeCardViewNext {

	private final String TAG = PayRechargeCardViewNext.class.getCanonicalName();

	//返回按钮
	private RelativeLayout mBackLayout = null;
	//购买商品描述
	private TextView goodDescription = null;
	//充值前提示语
	private TextView rechargePrompt = null;
	//选择充值的信息
	private TextView selectedInfoTextView = null;
	//充值卡类型
	private String payid="";
	//充值卡实体类
    private RechargeCard rechargeCard;
	//充值卡支付按钮
	private Button rechargeAndPayBtn = null;
	//支付参数类
	private PayOrderData payOrderData;
	//充值卡支付Activity
	private PayRechargeCardView payRechargeCardView;
   //充值卡信息类
    private RechargeCardDetailData currentRechargeCardDetailData ;
	//用户所选的充值金额
	public int cash = 0;
	//支付工具类
	private PayTools payTools = PayTools.getInstance();
	//充值订单数据类
	private RechargeOrderDetail rechargeOrderDetail = null;
   //充值金额显示
	private TextView goodDescriptionUnit;
    //卡号
	private EditText cardNoEditText;
    //密码
	private EditText cardPWEditText;
     //支付中心界面
	private PaymentCenterView payCenterPage;
     //余额
	private int balance;
	private IPaymentPresenter iPaymentPresenter;
	private boolean isRecharge;
	private RelativeLayout bgLayout;
	private ScrollView scrollView;

	public PayRechargeCardViewNext(Context context, PageManager manager,
			PayRechargeCardView page, PaymentCenterView payCenterPage,
			boolean isRecharge, BJMGFActivity activity) {
		super(ReflectResourceId.getLayoutId(context,
				Resource.layout.bjmgf_sdk_dock_recharge_cardpay_next_page),
				context, manager, activity);
		this.isRecharge=isRecharge;
		this.cash=page.cash;
		payRechargeCardView = page;
		payOrderData=payTools.getPayOrderData();
		currentRechargeCardDetailData=DialogUtil.currentRechargeCardDetailData();
		rechargeCard=DialogUtil.currentRechargeCard();

	}

	@Override
	public void onCreateView(View view) {
		rechargePrompt = (TextView)view.findViewById(ReflectResourceId.getViewId(context,
				Resource.id.bjmgf_sdk_dock_recharge_cardPay_next_recharge_promptId));
		bgLayout = (RelativeLayout) view
				.findViewById(ReflectResourceId
						.getViewId(
								context,
								Resource.id.bjmgf_sdk_dock_recharge_cardPay_next_layoutId));

		scrollView = (ScrollView) view
				.findViewById(ReflectResourceId
						.getViewId(
								context,
								Resource.id.bjmgf_sdk_dock_recharge_cardPay_next_scrollview));

		mBackLayout = (RelativeLayout) view
				.findViewById(ReflectResourceId
						.getViewId(
								context,
								Resource.id.bjmgf_sdk_dock_recharge_cardPay_next_closeLlId));
		goodDescriptionUnit = (TextView) view
				.findViewById(ReflectResourceId
						.getViewId(
								context,
								Resource.id.bjmgf_sdk_dock_recharge_cardPay_next_recharge_goods_unitStrId));
		goodDescription = (TextView) view
				.findViewById(ReflectResourceId
						.getViewId(
								context,
								Resource.id.bjmgf_sdk_dock_recharge_cardPay_next_recharge_goodsStrId));
		selectedInfoTextView = (TextView) view.findViewById(ReflectResourceId.getViewId(context, 
				Resource.id.bjmgf_sdk_dock_recharge_cardPay_next_chooseResultStrId));

		goodDescriptionUnit.setVisibility(View.GONE);
		goodDescription.setVisibility(View.GONE);
	payTools.cardNextrefreshViewByCash(context,cash,this,goodDescriptionUnit,goodDescription,balance,payOrderData,cash,rechargePrompt);
		payTools.setSelectedInfoTextViewStr(context,this,rechargeCard,selectedInfoTextView,cash);
		cardNoEditText = (EditText) view
				.findViewById(ReflectResourceId
						.getViewId(
								context,
								Resource.id.bjmgf_sdk_dock_recharge_cardPay_next_cardNoValueId));
		String strFormat = getString(Resource.string.bjmgf_sdk_dock_recharge_cardPay_next_recharge_cardNoValue_str);
		String sFinalStr = String.format(strFormat, rechargeCard.cardNumberLength);
		cardNoEditText.setHint(sFinalStr);
		cardNoEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(rechargeCard.cardNumberLength)});

		rechargeAndPayBtn = (Button) view
				.findViewById(ReflectResourceId
						.getViewId(
								context,
								Resource.id.bjmgf_sdk_dock_recharge_cardPay_next_nextStepbtnId));
		cardPWEditText = (EditText) view
				.findViewById(ReflectResourceId
						.getViewId(
								context,
								Resource.id.bjmgf_sdk_dock_recharge_cardPay_next_cardPWValueId));
		String pwFormat = getString(Resource.string.bjmgf_sdk_dock_recharge_cardPay_next_recharge_cardPWValue_str);
		String pwFinalStr = String.format(pwFormat, rechargeCard.cardPwdLength);
		cardPWEditText.setHint(pwFinalStr);
		cardPWEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(rechargeCard.cardPwdLength)});
		bgLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideInput();
			}
		});

		scrollView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					hideInput();
				}
				return false;
			}
		});
		/**
		 * 充值并支付
		 */
		rechargeAndPayBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (cash <= 0) {
					showToastCenter(getString(Resource.string.bjmgf_sdk_dock_recharge_pleaseChooseMoney));
					return;
				}
				if (StringUtility.isEmpty(cardNoEditText.getText().toString())
						|| StringUtility.isEmpty(cardPWEditText.getText().toString())) {
					showToastCenter(getString(Resource.string.bjmgf_sdk_dock_recharge_cardPay_next_recharge_input_prompt_str));
					return;
				}
				if(!payTools.checkCardNoLength(rechargeCard,cardNoEditText)) {
					showToastCenter(getString(Resource.string.bjmgf_sdk_dock_recharge_cardPay_next_recharge_input_prompt_cardno_str));
					return;
				}
				if(!payTools.checkCardPwLength(rechargeCard,cardPWEditText)) {
					showToastCenter(getString(Resource.string.bjmgf_sdk_dock_recharge_cardPay_next_recharge_input_prompt_cardpw_str));
					return;
				}
				String extendStr = cardNoEditText.getText() + "|" + cardPWEditText.getText() + "|" + rechargeCard.id;
				if(currentRechargeCardDetailData.id.equals(payTools.CM_CARD_PAY_TYPE)){
					payid=payTools.CM_CARD_PAY_TYPE;
				}else if(currentRechargeCardDetailData.id.equals(payTools.CT_CARD_PAY_TYPE)){
					payid=payTools.CT_CARD_PAY_TYPE;
				}else if(currentRechargeCardDetailData.id.equals(payTools.CU_CARD_PAY_TYPE)){
					payid=payTools.CU_CARD_PAY_TYPE;
				}
			rechargeOrderDetail=payTools.getRechargeOrderDEtailDta(payOrderData,payid,extendStr,cash);
				if(!isRecharge) {
					int leftMoney = (int) (balance + (cash - payOrderData.getCash()) * payTools.PAY_RATIO);
					if (leftMoney < 0) {
						showPayConfirmDialog();
						return;

					}
				}
				showProgressDialog();
				iPaymentPresenter.submitOrder(context, rechargeOrderDetail);
			}
		});
		/**
		 * 返回按钮
		 */
		mBackLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
            payTools.setRechargeOrderDetail(null);
			if(StringUtility.isEmpty(payTools.getPayInfo())){
				goBack();
			}else{
				payTools.clearPayDatas();
				quit();
			}
				
			}
		});
		//当isRecharge为true时隐藏充值提示
		if (isRecharge) {
			rechargePrompt.setVisibility(View.GONE);
		} else {
			rechargePrompt.setVisibility(View.VISIBLE);
		}

		super.onCreateView(view);
	}

	@Override
	public void setView() {
		iPaymentPresenter = new PaymentPresenterImpl(context, this);
	}

	@Override
	public void showError(String message) {

	}

	@Override
	public void showSuccess() {

	}

	/**
	 * 当充值余额不足以支付时提示用户
	 */
    public void showPayConfirmDialog(){
	final BJMSdkDialog dialog=new BJMSdkDialog(context);
	dialog.setTitle(getString(Resource.string.bjmgf_sdk_dock_pay_center_dialog_title_str));
	dialog.setMessage(getString(Resource.string.bjmgf_sdk_dock_recharge_prompt_paylessStr));
	dialog.setNegativeButton(getString(Resource.string.bjmgf_sdk_dock_dialog_btn_return_str), new OnClickListener() {

		@Override
		public void onClick(View v) {
			dialog.dismiss();
		}
	});
	dialog.setPositiveButton(getString(Resource.string.bjmgf_sdk_dock_dialog_btn_continue_str), new OnClickListener() {

		@Override
		public void onClick(View v) {
			showProgressDialog();
			iPaymentPresenter.submitOrder(context, rechargeOrderDetail);
			dialog.dismiss();
		}
	});
	dialog.show();
}
    /**
	 * 充值卡充值结果反馈
	 * @param msg 充值卡充值结果
	 */
	public  void showReturn(String msg){
		dismissProgressDialog();
	Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
	DialogUtil.showDialog(getString(Resource.string.bjmgf_sdk_dock_recharge_cardPay_next_recharge_dialog_prompt_str),context,this);//提示用户支付操作受理对话框

}
}
