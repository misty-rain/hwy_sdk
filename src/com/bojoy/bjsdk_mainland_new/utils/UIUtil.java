package com.bojoy.bjsdk_mainland_new.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import com.bojoy.bjsdk_mainland_new.model.entity.UserData;
import com.bojoy.bjsdk_mainland_new.widget.CustomProgressDialog;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMSdkDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UIUtil {
    public static DatePickerDialog sDatePicker;
    public static final String TAG = UIUtil.class.getSimpleName();

    public static void showWaitDialog(Activity aty) {
        CustomProgressDialog.show(aty);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static void dismissDlg() {
        CustomProgressDialog.hidden();
    }

    public static void showToast(Activity aty, String msg) {
        Toast.makeText(aty, msg, Toast.LENGTH_LONG).show();
    }

    private static long lastClickTime = 0;


    // 防止按钮重复点击
    public static boolean isFastDoubleClick(float ts) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        lastClickTime = time;
        if (0 < timeD && timeD < ts * 1000) {
            return true;
        }
        return false;
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static String isNull(String msg) {
        if (null == msg || "null".equals(msg)) {
            return "0";
        } else {
            return msg;
        }
    }


    /**
     * 打开日历控件，选取事件
     *
     * @param context
     * @param text
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void openDatePick(final Context context, final UserData userData, final int themeResId, final TextView txtDisplay) {
        Calendar c = Calendar.getInstance();
        try {
            if (userData.getBirth() == null)
                userData.setBirth("");
            Date d = !StringUtility.isEmpty(userData.getBirth()) ? new SimpleDateFormat(
                    "yyyy-MM-dd").parse(userData.getBirth()) : new Date(
                    System.currentTimeMillis());
            c.setTime(d);
        } catch (ParseException e) {
        }

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= 11) {
            context.setTheme(android.R.style.Theme_Holo_Light_NoActionBar);
        }
        LogProxy.i(TAG, "month = " + c.get(Calendar.MONTH) + " day = " + c.get(Calendar.DAY_OF_MONTH));
        sDatePicker = new DatePickerDialog(context, null, c.get(Calendar.YEAR),
                c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // 12岁时间戳
        Long maxTime = new Long(
                (((int) (System.currentTimeMillis() / 1000)) - 60 * 60 * 24
                        * 365 * 12));
        // 100岁时间戳
        Long minTime = new Long(
                (((int) (System.currentTimeMillis() / 1000)) - 60 * 60 * 24
                        * 365 * 100));
        // 获取年份
        final String maxDate = format.format(maxTime * 1000).split("-")[0];
        final String minDate = format.format(minTime * 1000).split("-")[0];
        // 设置日期选取范围
        sDatePicker
                .getDatePicker()
                .init(userData.getBirth().length() == 0 ? 2000 : Integer
                                .parseInt(userData.getBirth().split("-")[0]),
                        c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                            int MAX = Integer.parseInt(maxDate);
                            int MIN = Integer.parseInt(minDate);

                            public void onDateChanged(DatePicker view,
                                                      int year, int monthOfYear, int dayOfMonth) {
                                if (year > MAX) {
                                    view.updateDate(MAX, monthOfYear,
                                            dayOfMonth);
                                } else if (year < MIN) {
                                    view.updateDate(MIN, monthOfYear,
                                            dayOfMonth);
                                }
                            }
                        });

        if (currentapiVersion >= 11) {
            context.setTheme(themeResId);
        }
        sDatePicker.setButton(DatePickerDialog.BUTTON_POSITIVE,
                context.getString(ReflectResourceId.getStringId(context, Resource.string.bjmgf_sdk_dock_dialog_btn_ok_str)),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        sDatePicker.getDatePicker().clearFocus();
                        // by sunhaoyang
                        // 修复BUG 选择生日不显示界面上不显示日期
                        DatePicker datePicker = sDatePicker.getDatePicker();
                        int year = datePicker.getYear();
                        int month = datePicker.getMonth();
                        int day = datePicker.getDayOfMonth();
                        Calendar c = Calendar.getInstance();
                        c.set(year, month, day);
                        String sDate = new SimpleDateFormat("yyyy-MM-dd").format(c
                                .getTime());
                        txtDisplay.setText(sDate);
                        txtDisplay.setTextColor(context
                                .getResources()
                                .getColor(
                                        ReflectResourceId
                                                .getColorId(
                                                        context,
                                                        Resource.color.bjmgf_sdk_black)));
                        userData.setBirth(sDate);
                    }

                });


        sDatePicker.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface arg0) {
                sDatePicker.getDatePicker().clearFocus();
                sDatePicker.cancel();
            }
        });
        sDatePicker.show();
    }


    /**
     *  显示现在对话框
     * @param context 上下文
     * @param activity activity
     * @param downloadURL 下载地址
     * @param titleStr 标题
     * @param messageStr 消息内容
     * @param sureBtnStr 确定
     * @param cancleBtnStr 取消
     */
    public static void showDownloadDialog(Context context, final Activity activity, final String downloadURL, String titleStr,
                                          String messageStr, String sureBtnStr, String cancleBtnStr) {
        final BJMSdkDialog dialog = new BJMSdkDialog(context);
        dialog.setTitle(titleStr);
        dialog.setMessage(messageStr);
        dialog.setPositiveButton(sureBtnStr, new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                Utility.openUrl(activity, downloadURL);
            }
        });
        dialog.setNegativeButton(cancleBtnStr, new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
