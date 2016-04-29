package com.bojoy.bjsdk_mainland_new.utils;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 
 * @author xuxiaobin
 * PollingTimeOutTask 轮询超时任务
 * 
 */
public class PollingTimeoutTask {

	@SuppressWarnings("unused")
	private final String TAG = PollingTimeoutTask.class.getSimpleName();
	
	private boolean start;
	private int timeCounter;
	private int period;
	private int maxTime;
	private int delay;
	private Handler handler = new Handler();
	private PollingListener listener;
	private Timer timer;
	private TimerTask task = new TimerTask() {
		
		@Override
		public void run() {
//			LogProxy.i(TAG, "task run");
			if (handler == null) {
				pollingTask();
				return;
			}
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					pollingTask();
				}
			});
		}
	};
	
	private void pollingTask() {
		/** 不超时处理 */
		if (maxTime == 0) {
			listener.onExecute();
		} else {
			if (timeCounter < maxTime) {
				listener.onExecute();
				timeCounter += period;
			} else {
				listener.onTimeout();
				suspendPolling();
			}
		}
	}
	
	/**
	 * 
	 * @param period —— 间隔时间（单位：毫秒）
	 * @param delay —— 延迟时间（单位：毫秒）
	 * @param maxTime —— 超时时间（单位：毫秒） 0-表示不超时处理
	 * @param listener —— 轮询监听接口
	 */
	public PollingTimeoutTask(int period, int delay, int maxTime, PollingListener listener) {
		this(period, delay, maxTime, listener, null);
	}
	
	/**
	 * 
	 * @param period —— 间隔时间（单位：毫秒）
	 * @param delay —— 延迟时间（单位：毫秒）
	 * @param maxTime —— 超时时间（单位：毫秒） 0-表示不超时处理
	 * @param listener —— 轮询监听接口
	 * @param handler
	 */
	public PollingTimeoutTask(int period, int delay, int maxTime, PollingListener listener, Handler handler) {
		this.period = period;
		this.maxTime = maxTime;
		this.delay = delay;
		if (handler != null) {
			this.handler = handler;
		}
		if (listener == null) {
			throw new IllegalArgumentException("PollingListener can not null");
		}
		this.listener = listener;
		timer = new Timer();
	}
	
	/**
	 * 
	 * @param period —— 间隔时间（单位：毫秒）
	 * @param delay —— 延迟时间（单位：毫秒）
	 * @param maxTime —— 超时时间（单位：毫秒） 0-表示不超时处理
	 * @param listener —— 轮询监听接口
	 * @param isInHandler —— 是否执行放在handler处理
	 */
	public PollingTimeoutTask(int period, int delay, int maxTime, PollingListener listener, boolean isInHandler) {
		this(period, delay, maxTime, listener, null);
		if (!isInHandler) {
			handler = null;
		}
	}
	
	public void startPolling() {
		start = true;
		timeCounter = 0;
		timer.schedule(task, delay, period);
	}
	
	public void suspendPolling() {
		if (start) {
			task.cancel();
			timer.cancel();
			task = null;
			timer = null;
			start = false;
		}
	}

	public boolean isStart() {
		return start;
	}

	public interface PollingListener {
		
		public void onExecute();
		public void onTimeout();
		
	}
}
