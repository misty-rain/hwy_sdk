package com.bojoy.bjsdk_mainland_new.utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.R;
import com.bojoy.bjsdk_mainland_new.app.BJMGFSdk;
import com.bojoy.bjsdk_mainland_new.model.entity.RechargeCard;
import com.bojoy.bjsdk_mainland_new.model.entity.RechargeCardDetailData;
import com.bojoy.bjsdk_mainland_new.ui.view.payment.impl.PayRechargeCardView;
import com.bojoy.bjsdk_mainland_new.ui.view.payment.impl.PayRechargeCardViewNext;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMGFDialog;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMSdkDialog;

import java.util.ArrayList;


/**
 * 对话框工具类 ，调用时只需 DialogUtil.
 *
 * @author Administrator
 */
public class DialogUtil {

    private static final String TAG = DialogUtil.class.getSimpleName();
    //当前选择的运营商数据
    public static RechargeCardDetailData currentRechargeCardDetailData = null;

    private static BJMGFDialog ftDialog;

    // 充值卡名称
    private static String[] cardsName = null;

    // 某种充值卡的所有面额
    private static ArrayList<Integer> denominationList = null;
    //最终选定的充值卡类型
    public static RechargeCard rechargeCard = null;

    protected static Dialog mProgressDialog;

    public static RechargeCardDetailData currentRechargeCardDetailData() {

        return currentRechargeCardDetailData;

    }

    public static RechargeCard currentRechargeCard() {

        return rechargeCard;

    }

    /**
     * 黑色主题 的请求对话框 ，如无需改动 显示消息 ，传null
     *
     * @param context
     * @param displayMessage
     * @return
     */
    public static Dialog getRequestDialogForBlack(Activity context,
                                                  String displayMessage) {
        final Dialog dialog = new Dialog(context, R.style.Dialog);
        dialog.setContentView(R.layout.request_dialog_black);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        if (displayMessage != null) {
            TextView titleTxtv = (TextView) dialog.findViewById(R.id.tips_msg);
            titleTxtv.setText(displayMessage);
        }
        return dialog;

    }


    /**
     * 普通对话框
     *
     * @param context
     * @return
     */
    public static Dialog getCustomeDilogForNormal(final Activity context) {

        final Dialog dialog = new Dialog(context, R.style.Dialog);
        dialog.setContentView(R.layout.is_customer_dialog);
        return dialog;
    }

    public static Dialog getCustomDialog(Activity context) {
        final Dialog dialog = new Dialog(context, R.style.Dialog);
        return dialog;
    }

    public static int getScreenWidth(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }


    /**
     * 创建一个透明进度条dialog
     *
     * @param context
     * @param message
     * @return
     */

    public static final Dialog createTransparentProgressDialog(Context context, String message) {
        Dialog dialog = new Dialog(context);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        LinearLayout viewGroup = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                  ViewGroup.LayoutParams.WRAP_CONTENT);
        viewGroup.setBackgroundColor(0x10000000);
        viewGroup.setLayoutParams(params);
        viewGroup.setPadding(10, 10, 20, 10);
        viewGroup.setOrientation(LinearLayout.HORIZONTAL);
        ProgressBar bar = new ProgressBar(context);
        TextView msgText = new TextView(context);
        msgText.setTextColor(Color.WHITE);
        msgText.setText(message);
        msgText.setGravity(Gravity.CENTER_VERTICAL);
        viewGroup.addView(bar, params);
        params.height = ViewGroup.LayoutParams.FILL_PARENT;
        params.leftMargin = 10;
        viewGroup.addView(msgText, params);
        dialog.setContentView(viewGroup);
        dialog.setCancelable(false);
        return dialog;
    }

    /**
     * 充值卡选择运营商选择对话框
     *
     * @param context               上下文
     * @param operatorsName         运营商名字
     * @param chooseOperatorsBtn    选择运营商按钮
     * @param list                  充值卡信息数据类
     * @param chooseRechargeCardBtn 选择充值卡
     */
    public static AlertDialog showRechargeCardDialog(Context context,
                                                     final String[] operatorsName, final Button chooseOperatorsBtn,
                                                     final ArrayList<RechargeCardDetailData> list,
                                                     final Button chooseRechargeCardBtn, final PayRechargeCardView rechargeCardView) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                  .setTitle(
                            ReflectResourceId.getStringId(context, Resource.string.bjmgf_sdk_dock_recharge_cardPay_choose_operators_str))
                  .setItems(operatorsName, new DialogInterface.OnClickListener() {

                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          chooseOperatorsBtn.setText(operatorsName[which]);
                          chooseOperatorsBtn.setTag(which);
                          for (int i = 0; i < list.size(); i++) {
                              if ((operatorsName[which]).equals(list.get(i).tip)) {
                                  currentRechargeCardDetailData = list.get(i);
                                  // 显示默认选中的卡类型的所有面额
                                  chooseRechargeCardBtn.setText(currentRechargeCardDetailData.ct.get(0).name);
                                  denominationList = currentRechargeCardDetailData.ct.get(0).rule;
                                  rechargeCard = currentRechargeCardDetailData.ct.get(0);
                                  rechargeCardView.showCardPriceList(denominationList);
                                  cardsName = new String[currentRechargeCardDetailData.ct.size()];

                                  for (int j = 0; j < currentRechargeCardDetailData.ct.size(); j++) {
                                      cardsName[j] = currentRechargeCardDetailData.ct.get(j).name;
                                  }
                                  break;
                              }
                          }
                      }
                  }).show();
        return dialog;

    }

    /**
     * 充值卡选择充值卡对话框
     *
     * @param context               上下文
     * @param chooseRechargeCardBtn 选择充值卡按钮
     * @return
     */
    public static AlertDialog showRechargeCardName(Context context, final Button chooseRechargeCardBtn, final PayRechargeCardView rechargeCardView) {

        AlertDialog cardNamedialog = new AlertDialog.Builder(context)
                  .setTitle(ReflectResourceId.getStringId(context, Resource.string.bjmgf_sdk_dock_recharge_cardPay_choose_cardtype_str))
                  .setItems(cardsName, new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dia, int which) {
                          if (cardsName == null
                                    || cardsName.length == 0
                                    || currentRechargeCardDetailData == null) {
                              return;
                          }
                          chooseRechargeCardBtn.setText(cardsName[which]);
                          for (int i = 0; i < currentRechargeCardDetailData.ct.size(); i++) {
                              if (cardsName[which]
                                        .equals(currentRechargeCardDetailData.ct.get(i).name)) {
                                  denominationList = currentRechargeCardDetailData.ct.get(i).rule;
                                  rechargeCard = currentRechargeCardDetailData.ct.get(i);
                                  rechargeCardView.showCardPriceList(denominationList);
                                  break;
                              }
                          }
                      }
                  }).show();

        return cardNamedialog;

    }

    /**
     * 显示进度条
     */
    public static void showProgressDialog(Context context) {
        // hideInput();
        mProgressDialog = DialogUtil
                  .createTransparentProgressDialog(
                            context,
                            context.getResources()
                                      .getString(
                                                ReflectResourceId
                                                          .getStringId(
                                                                    context,
                                                                    Resource.string.bjmgf_sdk_dataSubmitingStr)));
        mProgressDialog.show();
    }

    /**
     * 关闭进度条
     */
    public static void dismissProgressDialog() {
        if (mProgressDialog != null) {
            // if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            // }
        }
    }

    /**
     * 充值后提示语对话框
     *
     * @param msg                     充值提示语
     * @param context                 上下文
     * @param payRechargeCardViewNext payRechargeCardViewNext界面
     */
    public static void showDialog(String msg, Context context, PayRechargeCardViewNext payRechargeCardViewNext) {
        final BJMSdkDialog dialog = new BJMSdkDialog(context);
        dialog.setTitle(payRechargeCardViewNext.getString(Resource.string.bjmgf_sdk_dock_pay_center_dialog_title_str));
        dialog.setMessage(msg);
        dialog.setPositiveButton(payRechargeCardViewNext.getString(Resource.string.bjmgf_sdk_dock_dialog_btn_ok_str), new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                payRechargeCardViewNext.quit();
            }
        });
        dialog.show();
    }

    /**
     * 试玩账号 转正对话框
     *
     * @param activity
     * @param delay
     */
    public static void showTryChangeDialog(final Activity activity, int delay, BJMGFDialog bjmgfDialog) {
        Handler handler = new Handler(activity.getMainLooper());
        ftDialog = bjmgfDialog;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!checkDialogType(ftDialog, BJMGFDialog.Page_TryChange)) {
                    ftDialog = new BJMGFDialog(activity, activity,
                              BJMGFDialog.Page_TryChange);
                    BJMGFSdk.getDefault().getDockManager().removeDock();
                    ftDialog.show();
                }
            }
        }, delay);

    }

    public static boolean checkDialogType(BJMGFDialog bjmgfDialog, int type) {
        if (bjmgfDialog == null) {
            return false;
        }
        if (type == BJMGFDialog.Page_Init) {
            return false;
        }
        if (type == BJMGFDialog.Page_Login) {
            return false;
        }
        if (type == BJMGFDialog.Page_TryChange) {
            return false;
        }
        if (bjmgfDialog.getPageType() == type) {
            LogProxy.i(TAG, "has open same dialog " + type);
            return true;
        }
        if (bjmgfDialog.isShowing()) {
            if (bjmgfDialog.getPageType() == BJMGFDialog.Page_Init) {
                LogProxy.i(TAG, "has open init dialog");
                return true;
            }
            if (bjmgfDialog.getPageType() == BJMGFDialog.Page_Login) {
                LogProxy.i(TAG, "has open login dialog");
                return true;
            } else if (bjmgfDialog.getPageType() == BJMGFDialog.Page_Logout) {
                LogProxy.i(TAG, "has open logout dialog");
                bjmgfDialog.dismiss();
                return false;
            }
        }
        return false;
    }


}
