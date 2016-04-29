package com.bojoy.bjsdk_mainland_new.support.module;

import android.os.Bundle;

/**
 * 功能模块接口
 * 必须由具体的功能页面实现
 * 自定义窗体必须有一个默认
 * 的构造方法
 * 
 * @author xuxiaobin
 *
 */
public interface IBnyModule {
	
	/**
	 * 
	 * @return 功能模块名
	 */
	public String getTag();
	
	/**
	 * 功能模块之间的数据传递
	 * 
	 * 该方法在Activity是不会被调用的，直接通过
	 * getIntent().getExtras()传递bundle
	 * 
	 * @param bundle	-- 传递的数据
	 * @return
	 */
	public void transmitData(Bundle bundle);
}
