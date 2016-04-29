package com.bojoy.bjsdk_mainland_new.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 设备工具类
 * @author zhouhonghai
 *
 */
public class DeviceUtil {

	private static String TAG = DeviceUtil.class.getSimpleName();

	/**
	 * 手机是否有sim卡
	 * @param context
	 * @return true:有	false:没有
	 */
	public static boolean isHaveSimCard(Context context){
		try { 
	        TelephonyManager mgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE); 
	        return TelephonyManager.SIM_STATE_READY == mgr.getSimState(); 
	    } catch (Exception e) { 
	        e.printStackTrace(); 
	    } 
	    return false; 
	}

	/**
	 * 判断apk是否安装
	 *
	 * @param context
	 * @param packagename
	 * @return
	 */
	public static boolean isAppInstalled(Context context, String packagename) {
		PackageInfo packageInfo;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(
					packagename, 0);
		} catch (PackageManager.NameNotFoundException e) {
			packageInfo = null;
			e.printStackTrace();
		}
		if (packageInfo == null) {
			LogProxy.i(TAG, "未安装: " + packagename);
			return false;
		} else {
			LogProxy.i(TAG, "已安装: " + packagename);
			return true;
		}
	}
}
