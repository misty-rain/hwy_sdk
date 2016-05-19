package com.bojoy.bjsdk_mainland_new.model.entity;


import com.bojoy.bjsdk_mainland_new.app.BJMGFSdk;
import com.bojoy.bjsdk_mainland_new.congfig.DockTypeConstants;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;

/**
 * 
 * @author xuxiaobin
 * 悬浮窗和开关数据层
 *
 */
public class DockSdkType {
	
	private final String TAG = DockSdkType.class.getSimpleName();
	
	private int value;
	private int type;
	private boolean openWish;
	private boolean openPollMsg;
	private boolean isWish;
	private boolean isNoWish;
	
	// int数据，低4位用于标识悬浮窗类型，第5位表示心愿开关，第6位表示离线消息轮询查询开关
	public void setSdkValue(int value) {
		this.value = value;
		int type = value & 0xf;
		if (type >= DockTypeConstants.SDK_NORMAL_TYPE &&
				type <= DockTypeConstants.SDK_DOCK_SNS_TYPE) {
			this.type = type;
		} else {
			this.type = DockTypeConstants.SDK_DOCK_TYPE;
		}
		openWish = (value & 0x10) == 0x10;
		openPollMsg = (value & 0x20) == 0x20;
		LogProxy.i(TAG, "type = " + this.type + ", openWish = " + openWish +
				", openPollMsg = " + openPollMsg);
	}

	public int getValue() {
		return value;
	}

	public int getType() {
		return type;
	}

	public boolean isOpenWish() {
		return openWish;
	}

	public boolean isOpenPollMsg() {
		return openPollMsg;
	}
	
}
