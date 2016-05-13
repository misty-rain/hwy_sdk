package com.bojoy.bjsdk_mainland_new.eventhandler.event;

/**
 * @author xuxiaobin
 * BJMGFSdkEvent 需返回给开发者的一些sdk事件
 */
public class BJMGFSdkEvent {
	
	/** offline应用初始化完成 */
	public static final int APP_INIT_OFFLINE = 1;
	/** 应用强制关闭 */
	public static final int APP_CLOSED = 2;
	/** 登录成功 */
	public static final int APP_LOGIN_SUCCESS = 3;
	/** 退出登录 */
	public static final int APP_LOGOUT = 4;
	/** 仅退出应用 */
	public static final int APP_EXIT = 5;
	/** 需要打开wifi设置 */
	public static final int APP_NEED_WIFI = 6;
	/** 登录失败 */
	public static final int APP_LOGIN_FAIL = 7;
	/** 切换账号 */
	public static final int APP_SWITCH_ACCOUNT = 8;
	/** 进入提交问题 */
	public static final int APP_BEFORE_SEND_QUESTION = 9;
	/** 注册成功 */
	public static final int APP_REGISTER_SUCCESS = 10;
	/** 获取离线消息 */
	public static final int GET_OFFLINE_MESSAGE = 13;
	/** 欢迎页显示消失之后再显示悬浮窗 */
	public static final int APP_WELCOME_SHOW = 14; 
	/** 初始化成功事件*/
	public static final int APP_INIT_SUCCESS = 15;
	/** 完善资料事件 */
	public static final int APP_PERFECT_DATA =  16;
	/** 用户游客账号转正成功事件 */
	public static final int APP_POSITIVE_USER =  17;
	/** 用户修改密码事件 */
	public static final int APP_CHANGE_PASSWORD = 18;
	/** 充值成功 */
	public static final int RECHARGE_SUCCESS = 19;
	/** 充值失败 */
	public static final int RECHARGE_FAIL = 20;
	
	
	
	private int eventId;
	
	public BJMGFSdkEvent(int eventId) {
		this.eventId = eventId;
	}

	public int getEventId() {
		return eventId;
	}

}
