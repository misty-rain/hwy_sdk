package com.bojoy.bjsdk_mainland_new.ui.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.app.tools.BJMGFSDKTools;
import com.bojoy.bjsdk_mainland_new.app.tools.PayTools;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.view.payment.impl.PayRechargeCardView;
import com.bojoy.bjsdk_mainland_new.ui.view.payment.impl.PaymentCenterView;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;

public class RechargeCardPayPriceChooseAdapter extends BaseAdapter {
	public static final String TAG = RechargeCardPayPriceChooseAdapter.class
			.getSimpleName();
	private ArrayList<TextView> textViewList;
	private ArrayList<Integer> priceList;
	public int lastSelectedPrice = 0;
	private PayRechargeCardView rechargeCardView = null;
	private PaymentCenterView paymentCenterView = null;
	private boolean isRecharge;
	private Context context;
	private int balance = 0;
	public ViewHolder lastSelectedHolder = null;
	private TextView goodDescription;
	private PayTools payTools = PayTools.getInstance();

	public RechargeCardPayPriceChooseAdapter(Context context,
			ArrayList<Integer> priceList, PayRechargeCardView rechargeCardView,
			PaymentCenterView payCenterPage, boolean isRecharge,
			BJMGFActivity activity,TextView goodDescription) {
		this.rechargeCardView = rechargeCardView;
		paymentCenterView = payCenterPage;
		this.isRecharge = isRecharge;
		this.priceList = priceList;
		this.context = context;
		this.balance = payCenterPage.balance;
		textViewList = new ArrayList<TextView>();
		this.goodDescription=goodDescription;
	}

	@Override
	public int getCount() {

		return priceList.size();
	}

	@Override
	public Object getItem(int position) {

		return priceList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = View.inflate(context, ReflectResourceId.getLayoutId(
					context,
					Resource.layout.bjmgf_sdk_dock_recharge_cardpay_card_item),
					null);
			viewHolder = createHolder(convertView);
			viewHolder.denomination = priceList.get(position);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		bingHolder(viewHolder);
		return convertView;
	}
        /**
         * 创建Holder
         * @param view Layout布局转换成的View即convertView
         * @return
         */
	private ViewHolder createHolder(View view) {
		final ViewHolder holder = new ViewHolder();
		holder.priceTextView = (TextView) view.findViewById(ReflectResourceId
				.getViewId(context,
						Resource.id.bjmgf_sdk_dock_recharge_cardPay_price_Id));
         //文本框背景变白
		holder.priceTextView.setBackgroundResource(ReflectResourceId
				.getDrawableId(
						context,
						Resource.drawable.bjmgf_sdk_text_with_roundcorner_selector));




		textViewList.add(holder.priceTextView);
		setOnClickListeners(holder);
		return holder;
	}
     /**
      * 单击事件处理单击后充值面值的数值变化及背景改变
      * @param holder 当前选中的ViewHolder
      */
	public void setOnClickListeners(final ViewHolder holder) {
		holder.priceTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				lastSelectedPrice = holder.denomination;
				doSelected(holder);
			}
		});

	}
/**
 * 自定义ViewHolder
 *
 */
	public class ViewHolder {
		//充值面值
		public TextView priceTextView = null;
		//充值金额
		public int denomination = 0;
	}

	public void bingHolder(ViewHolder holder) {
		String str = new StringBuilder()
				.append(holder.denomination)
				.append(StringUtility
						.getString(
								context,
								Resource.string.bjmgf_sdk_dock_recharge_cardPay_yuan_unit_str))
				.toString();
		holder.priceTextView.setText(str);
		if (lastSelectedPrice == 0) {
			if (lastSelectedHolder == null) {
				if (isRecharge) {
					payTools.refreshViewByCash(0,rechargeCardView,context, goodDescription);
				} else {
					if (payTools.getPayOrderData().getCash()
							* payTools.PAY_RATIO <= (holder.denomination
							* payTools.PAY_RATIO + balance)) {
						doSelected(holder);
					} else {
						if (holder.denomination == (priceList.get(priceList
								.size() - 1).intValue())) {// 最大值 默认数组priceList
															// 从小到大排列
							doSelected(holder);
						}
					}
				}
			}
		} else if (lastSelectedPrice > 0) {
			if (holder.denomination == lastSelectedPrice
					&& lastSelectedHolder == null) {// 上次选中的值
				doSelected(holder);
			}
		}

	}

	private void doSelected(ViewHolder holder) {
		resetPriceLayout(holder);
		lastSelectedHolder = holder;// 新的选中项
	}
	/**
	 * 恢复价格设置状态  
	 * @param holder
	 */
	public void resetPriceLayout(ViewHolder holder) {
		if (lastSelectedHolder != null) {// 如果最终选定不为空
			for (TextView textView : textViewList) {
				// 将所有选过的textview变白
				textView.setBackgroundResource(ReflectResourceId
						.getDrawableId(
								context,
								Resource.drawable.bjmgf_sdk_text_with_roundcorner_selector));
				textView.setTextColor(context.getResources().getColor(
						ReflectResourceId.getColorId(context,
								Resource.color.bjmgf_sdk_black)));
			}
		}
		// 设置新的
		holder.priceTextView.setBackgroundResource(ReflectResourceId
				.getDrawableId(context,
						Resource.drawable.bjmgf_sdk_recharge_item_selected_bg));
		holder.priceTextView.setTextColor(context.getResources().getColor(
				ReflectResourceId.getColorId(context,
						Resource.color.bjmgf_sdk_white)));// 选中白色

		if (!StringUtility.isEmpty(holder.priceTextView.getText().toString())) {
			String str = holder.priceTextView.getText().toString();
			rechargeCardView.cash = Integer.parseInt(str.substring(0,
					str.length() - 1));

		}

		if (rechargeCardView.cash >= 0) {
			payTools.refreshViewByCash(rechargeCardView.cash,rechargeCardView,context, goodDescription);
		}
		payTools.setPayInfo("");
	}
}
