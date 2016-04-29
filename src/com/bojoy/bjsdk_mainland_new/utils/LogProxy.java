package com.bojoy.bjsdk_mainland_new.utils;

import android.util.Log;

/**
 * @author xuxiaobin
 * LogProxy 代理Log，用于控制调试信息
 */
public class LogProxy {
	
	private static boolean isDebug = true;
	private static String prefixName = "";

	public static void setDebugMode(boolean debug) {
		isDebug = debug;
	}
	
	public static void setPrefixName(String prefixName) {
		LogProxy.prefixName = prefixName;
	}
	
	public static void d(String tag, String msg) {
		if (isDebug) {
			Log.d(getLogTag(tag), msg);
		}
	}
	
	public static void e(String tag, String msg) {
		if (isDebug) {
			Log.e(getLogTag(tag), msg);
		}
	}
	
	public static void i(String tag, String msg) {
		if (isDebug) {
			Log.i(getLogTag(tag), msg);
		}
	}
	
	public static void w(String tag, String msg) {
		if (isDebug) {
			Log.w(getLogTag(tag), msg);
		}
	}
	
	private static String getLogTag(String tag) {
		return String.format("%s[%s]", prefixName, tag);
	}
}
