package com.bojoy.bjsdk_mainland_new.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 
 * @author xuxiaobin
 * 永久数据存储类，为了在App中不重复创建
 *
 */
public class SavePreferences {

	@SuppressWarnings("unused")
	private final String SharedPrefName;
	private SharedPreferences pref;
	
	public SavePreferences(Context context, String sharedPrefName) {
		this.SharedPrefName = sharedPrefName;
		pref = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
	}

	public SharedPreferences getPref() {
		return pref;
	}
	
}
