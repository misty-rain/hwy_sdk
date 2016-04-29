package com.bojoy.bjsdk_mainland_new.eventhandler.event;

/**
 * 
 * @author xuxiaobin
 * NormalEvent 普通事件 用tag来区分内容
 *
 */
public class NormalEvent {
	
	public static final String Go_To_My_Question_Event = "Go_To_My_Question_Event";

	private String tag;
	
	public NormalEvent(String tag) {
		this.tag = tag;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
}
