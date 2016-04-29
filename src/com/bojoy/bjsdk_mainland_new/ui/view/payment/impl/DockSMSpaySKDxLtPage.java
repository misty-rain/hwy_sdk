package com.bojoy.bjsdk_mainland_new.ui.view.payment.impl;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.utils.DomainUtility;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.ToastUtil;

public class DockSMSpaySKDxLtPage extends DockSmsBasePage implements OnClickListener{
	
	private final String TAG = DockSMSpaySKDxLtPage.class.getSimpleName();
	
	/**
	 * 充值金额需在XML中配置tag属性
	 */
	private TextView payTextView_6,payTextView_8,payTextView_10,goodsStrId, smsNoticeTextView;
	private Button payButton;
	private Boolean isRechargeValueSelected;
	DockSMSpaySKBasePage mDockSMSpaySKBasePage;
	Context mContext;
	
	private int rechargeValue = 0;
	private int goodsCode = 0;

	public DockSMSpaySKDxLtPage(Context context, DockSMSpaySKBasePage dockSMSpaySKBasePage) {
		super(context, Resource.layout.bjmgf_sdk_dock_recharge_sms_sk_dx_lt_page);
		this.mDockSMSpaySKBasePage = dockSMSpaySKBasePage;
		this.mContext = context;
		isRechargeValueSelected = false;
	}

	@Override
	public void setView() {
		payTextView_6 = getView(Resource.id.bjmgf_sdk_dock_recharge_sms_price_6_Id);
		payTextView_8 = getView(Resource.id.bjmgf_sdk_dock_recharge_sms_price_8_Id);
		payTextView_10 = getView(Resource.id.bjmgf_sdk_dock_recharge_sms_price_10_Id);
		goodsStrId = getView(Resource.id.bjmgf_sdk_dock_recharge_sms_recharge_goodsStrId);
		payButton = getView(Resource.id.bjmgf_sdk_dock_recharge_sms_buybtnId);
		smsNoticeTextView = getView(Resource.id.bjmgf_sdk_dock_recharge_sms_notice_layoutId);
		
		payTextView_6.setOnClickListener(this);
		payTextView_8.setOnClickListener(this);
		payTextView_10.setOnClickListener(this);
		payButton.setOnClickListener(this);
		
		updateView();
	}

	@Override
	public void onClick(View v) {
		if (v.equals(payTextView_6) || v.equals(payTextView_10)
				|| v.equals(payTextView_8) || v.equals(payTextView_10)) {
			String[] arrTag = String.valueOf(v.getTag()).split("-");
			if (arrTag.length >= 2) {
				clearButtonStatus();
				isRechargeValueSelected = true;
				goodsCode = Integer.valueOf(arrTag[0]);
				rechargeValue = Integer.valueOf(arrTag[1]);
				v.setEnabled(false);
				goodsStrId.setText(String.valueOf(rechargeValue * 5));
				Log.i(TAG, "rachargeMoney:" + rechargeValue);
			} else {
				ToastUtil.showMessage(mContext, "当前按钮未配置参数！");
			}
		} else if (v.equals(payButton)) {
			Log.i(TAG, "click recharge a");
			if (!isRechargeValueSelected) {
				return;
			}
			mDockSMSpaySKBasePage.startRechargeAndPay(goodsCode, rechargeValue);
		}
	}
	
	private void updateView(){
		String smsNotice = DomainUtility.getInstance().getSmsNotice(context);
		if(smsNotice.isEmpty()||smsNotice.equals("")){
			smsNoticeTextView.setVisibility(View.GONE);
		}else{
			smsNoticeTextView.setVisibility(View.VISIBLE);
			smsNoticeTextView.setText(smsNotice);
		}
	}
	
	private void clearButtonStatus(){
		payTextView_6.setEnabled(true);
		payTextView_8.setEnabled(true);
		payTextView_10.setEnabled(true);
		mDockSMSpaySKBasePage.resetOrderInfo();
	}
}
