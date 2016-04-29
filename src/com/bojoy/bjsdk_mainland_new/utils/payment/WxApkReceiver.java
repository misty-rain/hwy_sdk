package com.bojoy.bjsdk_mainland_new.utils.payment;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bojoy.bjsdk_mainland_new.app.tools.PayTools;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;

public class WxApkReceiver extends BroadcastReceiver {

	private String TAG = WxApkReceiver.class.getSimpleName();
	private String mPackageName = "com.GF.platform.wx.pay";

	@Override
	public void onReceive(Context context, Intent intent) {
		LogProxy.i(TAG, "wx pay receiver");
		if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
			String packageName = intent.getDataString().substring(8);
			System.out.println("---------------" + packageName);

			if (packageName.equals(mPackageName)) {
				LogProxy.i(TAG, "wx pay start");
				Intent mIntent = new Intent();
				mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				ComponentName comp = new ComponentName(
						WeiChatPayUtil.wxApkPackageName,
						WeiChatPayUtil.wxApkClassName);
				mIntent.setComponent(comp);
				mIntent.setAction("android.intent.action.VIEW");
				Bundle b = new Bundle();
				b.putString("payorder", PayTools.getInstance()
						.getPayInfo());
				mIntent.putExtras(b);
				context.startActivity(mIntent);
			}
		}
	}

}
