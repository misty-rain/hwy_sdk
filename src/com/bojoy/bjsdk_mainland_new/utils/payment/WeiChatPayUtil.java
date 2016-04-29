package com.bojoy.bjsdk_mainland_new.utils.payment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import com.bojoy.bjsdk_mainland_new.app.tools.PayTools;
import com.bojoy.bjsdk_mainland_new.utils.DeviceUtil;
import com.bojoy.bjsdk_mainland_new.utils.FileUtil;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;

/**
 * 微信支付工具类
 *
 * Created by zhouhonghai on 2016/2/3.
 */
public class WeiChatPayUtil {

    private static String TAG = WeiChatPayUtil.class.getSimpleName();

    public static String wxApkPackageName = "com.GF.platform.wx.pay";
    public static String wxApkClassName = "com.GF.platform.wx.pay.PayActivity";
    public static String wxPackageName = "com.GF.platform.wx.pay";
    public static String wxApkName = "Wxpay.apk";

    /**
     * 微信支付入口
     * @param context
     */
    public static void startWxPay(Context context){
        if(DeviceUtil.isAppInstalled(context, wxPackageName)){
            doWxPay(context);
        }else{
            installWxApk(context);
        }
    }

    /**
     * 支付
     * @param context
     */
    private static void doWxPay(Context context){
        LogProxy.i(TAG, "wx pay begin info " + PayTools.getInstance().getPayInfo());
        Intent mIntent = new Intent();
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ComponentName comp = new ComponentName(wxApkPackageName, wxApkClassName);
        mIntent.setComponent(comp);
        mIntent.setAction("android.intent.action.VIEW");
        Bundle b = new Bundle();
        b.putString("payorder", PayTools.getInstance().getPayInfo());
        mIntent.putExtras(b);
        context.startActivity(mIntent);
    }

    /**
     * 检测是否安装支付插件
     * @param context
     */
    private static void installWxApk(Context context){
        if (FileUtil.copyApkFromAssets(context, wxApkName,
                Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/" + wxApkName)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://"
                            + Environment.getExternalStorageDirectory()
                            .getAbsolutePath() + "/"
                            + wxApkName),
                    "application/vnd.android.package-archive");
            context.startActivity(intent);
        }
    }
}
