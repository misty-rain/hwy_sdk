package com.bojoy.bjsdk_mainland_new.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;

import com.bojoy.bjsdk_mainland_new.app.BJMGFSdk;
import com.bojoy.bjsdk_mainland_new.app.tools.BJMGFSDKTools;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.model.entity.PassPort;
import com.bojoy.bjsdk_mainland_new.support.fastjson.JSON;
import com.bojoy.bjsdk_mainland_new.support.fastjson.JSONObject;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.view.account.bindphone.impl.BindPhoneView;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMGFDialog;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMSdkDialog;

/**
 * 提醒弹窗工具类
 *
 * @author zhouhonghai
 */
public class WarnUtil {

    private static final String TAG = WarnUtil.class.getSimpleName();

    private static final String KEY_TRY_TIME = "tryTime";
    private static final String KEY_BIND_TIME = "bindTime";
    private static final String KEY_AUTHENT_TIME = "authentTime";
    private static int day = 3;
    private static int authentDay = 1;
    private static boolean isShowTry = false;
    private static boolean isShowBind = false;
    private static boolean isShowAuthent = false;

    /**
     * 是否需要提示需要转正
     *
     * @return
     */
    public static boolean isShowTryWarn(Context context) {
        String currentTime = getCurrentTime();
        if (BJMGFSDKTools.getInstance().isCurrUserTryPlay(context)) {
            String preTime = SpUtil.getStringValue(context,
                      KEY_TRY_TIME, "");
            LogProxy.i(TAG, "preTime = " + preTime + " & currentTime = "
                      + currentTime);
            if (preTime.equals("")) {
                setCurrentTime(context);
                return false;
            }
            if (!preTime.trim().equals(currentTime.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否需要提示需要绑定
     *
     * @param context
     * @return
     */
    public static boolean isShowBindWarn(Context context) {
        String currentTime = getCurrentTime();
        String preTime = SpUtil
                  .getStringValue(context, KEY_BIND_TIME, "")
                  .split("`")[0];
        LogProxy.i(TAG, "bind preTime = " + preTime);
        if (BJMGFSDKTools.getInstance().isCurrUserTryPlay(context)) {
            return false;
        }
        if (preTime.equals("")) {
            setCurrentTimeAdd(context, day, KEY_BIND_TIME, day + "");
            return false;
        }
        JSONObject jsonObject = JSON.parseObject(SpUtil.getStringValue(context, BJMGFSDKTools.getInstance().getCurrentPassPort().getUid(), ""));
        LogProxy.i(TAG, "UserPort : " + jsonObject);

        if (jsonObject != null) {
            if (jsonObject.getString("bindEmail") != null) {
                if (jsonObject.getString("bindEmail").equals("")
                          && jsonObject.getString("bindMobile").equals("")) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
                              Locale.CHINA);
                    Date preDate = new Date();
                    Date currentDate = new Date();
                    try {
                        preDate = sdf.parse(preTime);
                        currentDate = sdf.parse(currentTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    boolean flag = preDate.before(currentDate);
                    if (preTime.equals(currentTime) || flag) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 是否需要实名认证提醒
     *
     * @param context
     * @return
     */
    public static boolean isShowAuthentWarn(Context context) {
        String currentTime = getCurrentTime();
        String preTime = SpUtil
                  .getStringValue(context, KEY_AUTHENT_TIME, "")
                  .toString().split("`")[0];
        LogProxy.i(TAG, "authen preTime = " + preTime);
        if (BJMGFSDKTools.getInstance().isCurrUserTryPlay(context)) {
            return false;
        }
        if (preTime.equals("")) {
            setCurrentTimeAdd(context, authentDay, KEY_AUTHENT_TIME, authentDay
                      + "");
            return false;
        }
        String realConfirm = DomainUtility.getInstance().getRealConfirm(context);
        String authType = BJMGFSDKTools.getInstance().getCurrentPassPort().getAuthType();
        LogProxy.i(TAG, "realConfirm:" + realConfirm);
        LogProxy.i(TAG, "authType:" + authType);
        if (((Integer.valueOf(realConfirm) & 0x1) != 0)
                  && authType.equals("0")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
                      Locale.CHINA);
            Date preDate = new Date();
            Date currentDate = new Date();
            try {
                preDate = sdf.parse(preTime);
                currentDate = sdf.parse(currentTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            boolean flag = preDate.before(currentDate);
            if (preTime.equals(currentTime) || flag) {
                return true;
            }
        }
        return false;
    }

    /**
     * 弹出试玩转正提醒
     *
     * @param context
     */
    public static void showTryWarnDialog(final Context context) {
        if (!isShowTry) {
            isShowTry = true;
            final BJMSdkDialog dialog = new BJMSdkDialog(context);
            dialog.setTitle(StringUtility.getString(context,
                      Resource.string.bjmgf_sdk_warn_title));
            dialog.setMessage(StringUtility.getString(context,
                      Resource.string.bjmgf_sdk_warn_content_try));
            dialog.setPositiveButton(StringUtility.getString(context,
                      Resource.string.bjmgf_sdk_warn_ok_try),
                      new OnClickListener() {

                          @Override
                          public void onClick(View v) {
                              dialog.dismiss();
                              setCurrentTime(context);
                              BJMGFDialog bjmgfDialog = new BJMGFDialog(context,
                                        BJMGFSdk.getDefault().getDockManager()
                                                  .getActivity(),
                                        BJMGFDialog.Page_TryChange);
//                            BJMGFSdk.getDefault().getDockManagerBeta()
//                                    .closeDock();
                              bjmgfDialog.show();
                          }
                      });

            dialog.setNegativeButton(StringUtility.getString(context,
                      Resource.string.bjmgf_sdk_warn_cancel),
                      new OnClickListener() {

                          @Override
                          public void onClick(View v) {
                              setCurrentTime(context);
                              dialog.dismiss();
                          }

                      });
            dialog.show();
            isShowTry = false;
        }
    }

    /**
     * 弹出绑定提醒
     *
     * @param context
     */
    public static void showBindWarnDialog(final Context context) {
        if (!isShowBind) {
            isShowBind = true;
            final BJMSdkDialog dialog = new BJMSdkDialog(context);
            dialog.setTitle(StringUtility.getString(context,
                      Resource.string.bjmgf_sdk_warn_title));
            dialog.setMessage(StringUtility.getString(context,
                      Resource.string.bjmgf_sdk_warn_content_bind));
            dialog.setPositiveButton(StringUtility.getString(context,
                      Resource.string.bjmgf_sdk_warn_ok_bind),
                      new OnClickListener() {

                          @Override
                          public void onClick(View v) {
                              dialog.dismiss();
                              BJMGFActivity.canLandscape = true;
                              Intent intent = new Intent(BJMGFSdk.getDefault()
                                        .getDockManager().getActivity(),
                                        BJMGFActivity.class);
                              intent.putExtra(BJMGFActivity.Page_Class_Name_Key,
                                        BindPhoneView.class.getCanonicalName());
                              BJMGFSdk.getDefault().getDockManager()
                                        .getActivity().startActivity(intent);
                              setBindTimeWarn(context);
                          }
                      });

            dialog.setNegativeButton(StringUtility.getString(context,
                      Resource.string.bjmgf_sdk_warn_cancel),
                      new OnClickListener() {

                          @Override
                          public void onClick(View v) {
                              dialog.dismiss();
                              setBindTimeWarn(context);
                          }

                      });
            dialog.show();
            isShowBind = false;
        }
    }

    /**
     * 弹出实名认证提醒
     *
     * @param context
     */
    public static void showAuthentWarnDialog(final Context context) {
        if (!isShowAuthent) {
            isShowAuthent = true;
            final BJMSdkDialog dialog = new BJMSdkDialog(context);
            dialog.setTitle(StringUtility.getString(context,
                      Resource.string.bjmgf_sdk_warn_title));
            dialog.setMessage(StringUtility.getString(context,
                      Resource.string.bjmgf_sdk_warn_content_authent));
            dialog.setPositiveButton(StringUtility.getString(context,
                      Resource.string.bjmgf_sdk_warn_ok_authent),
                      new OnClickListener() {

                          @Override
                          public void onClick(View v) {
                              dialog.dismiss();
                              String authentUrl = BJMGFSDKTools.getInstance().getIdentityUrl(context, 2);//2为拼接链接时 authentUrl的redirect
                              Uri mUri = Uri.parse(authentUrl);
                              Intent intent = new Intent(Intent.ACTION_VIEW,
                                        mUri);
                              BJMGFSdk.getDefault().getDockManager()
                                        .getActivity().startActivity(intent);
                              setAuthentTimeWarn(context);
                          }
                      });

            dialog.setNegativeButton(StringUtility.getString(context,
                      Resource.string.bjmgf_sdk_warn_cancel),
                      new OnClickListener() {

                          @Override
                          public void onClick(View v) {
                              dialog.dismiss();
                              setAuthentTimeWarn(context);
                          }

                      });
            dialog.show();
            isShowAuthent = false;
        }
    }

    /**
     * 设置当前时间到本地
     *
     * @param context
     */
    public static void setCurrentTime(Context context) {
        SpUtil.setStringValue(context, KEY_TRY_TIME, getCurrentTime());
    }

    /**
     * 设置当前时间到本地(增加天数)
     *
     * @param context
     * @param day
     */
    public static void setCurrentTimeAdd(Context context, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentTime = sdf.format(cal.getTime());
        LogProxy.i(TAG, "add day is = " + currentTime);
        SpUtil.setStringValue(context, KEY_TRY_TIME, currentTime);
    }

    /**
     * 设置当前时间到本地(增加天数和标记)
     *
     * @param context
     * @param day
     * @param sign
     */
    public static void setCurrentTimeAdd(Context context, int day, String key,
                                         String sign) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentTime = sdf.format(cal.getTime());
        LogProxy.i(TAG, "add day is = " + currentTime);
        SpUtil.setStringValue(context, key, currentTime + "`" + sign);
    }

    /**
     * 设置绑定时间
     *
     * @param context
     */
    public static void setBindTimeWarn(Context context) {
        String pre = SpUtil.getStringValue(context, KEY_BIND_TIME, "")
                  .toString();
        String preSign = pre.split("`")[1];
        LogProxy.i(TAG, "bind preSign = " + preSign);
        if (preSign.equals("1")) {
            day = 3;
        } else if (preSign.equals("3")) {
            day = 6;
        } else {
            day = 9;
        }
        setCurrentTimeAdd(context, day, KEY_BIND_TIME, day + "");
    }

    /**
     * 设置认证提醒时间
     *
     * @param context
     */
    public static void setAuthentTimeWarn(Context context) {
        String pre = SpUtil.getStringValue(context, KEY_AUTHENT_TIME,
                  "").toString();
        String preSign = pre.split("`")[1];
        LogProxy.i(TAG, "bind preSign = " + preSign);
        setCurrentTimeAdd(context, authentDay, KEY_AUTHENT_TIME, authentDay
                  + "");
    }

    /**
     * 获取当前时间 格式：yyyy-MM-dd
     *
     * @return
     */
    public static String getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        LogProxy.i(TAG, "currentTime = " + sdf.format(cal.getTime()));
        return sdf.format(cal.getTime());
    }

    /**
     * 清除提醒所有本地信息
     *
     * @param context
     */
    public static void clearWarnInfo(Context context) {
        SpUtil.clean(context);
    }

    /**
     * 清除试玩提醒
     *
     * @param context
     *//*
    public static void clearTryWarn(Context context) {
        SPUtils.remove(SPUtils.WARN_NAME, context, KEY_TRY_TIME);
    }

    *//**
     * 清除绑定提醒
     *
     * @param context
     *//*
    public static void clearBindWarn(Context context) {
        SPUtils.remove(SPUtils.WARN_NAME, context, KEY_BIND_TIME);
    }

    *//**
     * 清除认证提醒
     *
     * @param context
     *//*
    public static void clearAuthentWarn(Context context) {
        SPUtils.remove(SPUtils.WARN_NAME, context, KEY_AUTHENT_TIME);
    }*/
}
