package com.bojoy.bjsdk_mainland_new.ui.view.payment.impl;

import java.util.ArrayList;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bojoy.bjsdk_mainland_new.app.tools.PayTools;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.model.entity.RechargeCard;
import com.bojoy.bjsdk_mainland_new.model.entity.RechargeCardDetailData;
import com.bojoy.bjsdk_mainland_new.presenter.payment.IPaymentPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.payment.impl.PaymentPresenterImpl;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BaseActivity;
import com.bojoy.bjsdk_mainland_new.ui.adapter.RechargeCardPayPriceChooseAdapter;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseActivityPage;
import com.bojoy.bjsdk_mainland_new.ui.view.payment.IPayRechargeCardView;
import com.bojoy.bjsdk_mainland_new.utils.DialogUtil;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;

public class PayRechargeCardView extends BaseActivityPage implements
		IPayRechargeCardView {

	private final String TAG = PayRechargeCardView.class.getSimpleName();

	private PayTools tools = PayTools.getInstance();
	// 返回按钮
	private RelativeLayout mBackLayout = null;
	// 购买商品描述
	private TextView goodDescription = null;
	// 下一步按钮
	private Button nextStepBtn = null;
	// 选择运营商
	private Button chooseOperatorsBtn = null;
	// 选择充值卡
	private Button chooseRechargeCardBtn = null;
	// 多家运营商充值卡详细信息
	private ArrayList<RechargeCardDetailData> rechargeData = null;
	// 运营商名称
	private String[] operatorsName = null;
	// 充值卡名称
	private String[] cardsName = null;
	// 最终选择充值卡的类型
	public RechargeCard rechargeCard = null;
	// 用户所选金额
	public int cash = 0;
	// 充值卡面额
	private GridView cardGridView = null;
	// 当前选择的运营商数据
	public RechargeCardDetailData currentRechargeCardDetailData = null;
	// 某种充值卡所有面额
	private ArrayList<Integer> denominationList = null;
	private ArrayList<RechargeCardDetailData> list;
	// 支付中心界面
	private PaymentCenterView paymentCenterView;
	private RechargeCardPayPriceChooseAdapter adapter;
	//充值的金额是否足够支付所需货币的金额
	private boolean isRecharge;
	private int balance;
	private int detalty;
	private IPaymentPresenter iPaymentPresenter;
	private Context mContext;
	private PayTools payTools = PayTools.getInstance();

	public PayRechargeCardView(Context context, PageManager manager,
			PaymentCenterView paymentCenterView, boolean isRecharge,
			BJMGFActivity activity) {

		super(ReflectResourceId.getLayoutId(context,
				Resource.layout.bjmgf_sdk_dock_recharge_cardpay_page), context,
				manager, activity);
		this.detalty=paymentCenterView.deltaMoney;
		this.isRecharge = isRecharge;
		this.paymentCenterView = paymentCenterView;
		this.mContext = context;
		balance = this.paymentCenterView.balance;
		iPaymentPresenter = new PaymentPresenterImpl(context, this);
	}

	@Override
	public void onCreateView(View view) {
		mBackLayout = (RelativeLayout) view.findViewById(ReflectResourceId
				.getViewId(context,
						Resource.id.bjmgf_sdk_dock_recharge_cardPay_closeLlId));
		goodDescription = (TextView) view
				.findViewById(ReflectResourceId
						.getViewId(
								context,
								Resource.id.bjmgf_sdk_dock_recharge_cardPay_recharge_goodsStrId));
		goodDescription.setVisibility(View.INVISIBLE);

		nextStepBtn = (Button) view.findViewById(ReflectResourceId.getViewId(
				context,
				Resource.id.bjmgf_sdk_dock_recharge_cardPay_nextStepbtnId));

		chooseOperatorsBtn = (Button) view
				.findViewById(ReflectResourceId.getViewId(context,
						Resource.id.bjmgf_sdk_dock_recharge_cardPay_corp_BtnId));

		chooseRechargeCardBtn = (Button) view
				.findViewById(ReflectResourceId.getViewId(context,
						Resource.id.bjmgf_sdk_dock_recharge_cardPay_card_BtnId));

		cardGridView = (GridView) view
				.findViewById(ReflectResourceId
						.getViewId(
								context,
								Resource.id.bjmgf_sdk_dock_recharge_cardPay_chooseMoney_gridviewId));
		chooseOperatorsBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (operatorsName == null || operatorsName.length == 0) {
					return;
				}
				DialogUtil.showRechargeCardDialog(context, operatorsName,
						chooseOperatorsBtn, list, chooseRechargeCardBtn,
						PayRechargeCardView.this);
			}
		});
		chooseRechargeCardBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!chooseOperatorsBtn.getText().toString().equals(getString(Resource.string.bjmgf_sdk_chooseoperator))) {
					DialogUtil.showRechargeCardName(context,
							chooseRechargeCardBtn, PayRechargeCardView.this);

				}

			}
		});
		nextStepBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (cash <= 0) {
					showToastCenter(getString(Resource.string.bjmgf_sdk_dock_recharge_pleaseChooseMoney));
					return;
				}
				BaseActivityPage page = new PayRechargeCardViewNext(context,
						manager, PayRechargeCardView.this, paymentCenterView,
						isRecharge, activity);
				manager.addPage(page);
			}
		});
		mBackLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				tools.setRechargeOrderDetail(null);
				if (StringUtility.isEmpty(tools.getPayInfo())) {
					goBack();
				} else {
					tools.clearPayDatas();
					quit();
				}
			}
		});
		super.onCreateView(view);
	}

	/**
	 * 请求获得充值卡信息json解析数据
	 */
	@Override
	public void setView() {
		showProgressDialog();
		iPaymentPresenter.getRechargeCardDes(mContext);

	}

	@Override
	public boolean canBack() {

		return super.canBack();
	}

	@Override
	public void showError(String message) {

	}

	@Override
	public void showSuccess() {

	}

	/**
	 * 充值卡信息数据回调
	 */
	@Override
	public void showRechargeResult(ArrayList<RechargeCardDetailData> list) {
		if (list != null) {
			dismissProgressDialog();
			operatorsName = payTools.getRechargeCardInfo(list);
			this.list = list;

		}

	}
    
       /**
        *  充值卡所有面额
        * @param denominationList 用户可选的充值面额
        */
	public void showCardPriceList(ArrayList<Integer> denominationList) {
		cash = 0;
		payTools.refreshViewByCash(0,this,context, goodDescription);

		if (denominationList != null && denominationList.size() > 0) {
			if (paymentCenterView != null) {
				adapter = new RechargeCardPayPriceChooseAdapter(mContext,
						denominationList, this, paymentCenterView, isRecharge,
						activity,goodDescription);
			}

		}
		if (cardGridView != null) {
			cardGridView.setAdapter(adapter);
		}
		adapter.notifyDataSetChanged();
	}

}
