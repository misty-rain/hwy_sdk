package com.bojoy.bjsdk_mainland_new.eventhandler.event;


/**
 * @author xuxiaobin
 * BaseReceiveEvent 服务器回应数据事件基类（也是通用的服务器回应事件），
 * 用于EvnetBus传递的事件
 */
public class BaseReceiveEvent<T> {

	public static final int Flag_Success = 1;
	public static final int Flag_Fail = 2;


	protected int flag;
	protected T respMsg;
	protected int requestType;
	
	public int getFlag() {
		return flag;
	}

	public T getRespMsg() {
		return respMsg;
	}

	public int getRequestType() {
		return requestType;
	}


	public BaseReceiveEvent(int flag,T message){
		this.flag=flag;
		this.respMsg=message;
	}


}
