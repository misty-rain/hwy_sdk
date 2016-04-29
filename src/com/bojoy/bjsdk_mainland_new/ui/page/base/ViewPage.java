package com.bojoy.bjsdk_mainland_new.ui.page.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.bojoy.bjsdk_mainland_new.support.eventbus.EventBus;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xuxiaobin ViewPage 自定页面布局，用来在一个控件里面任意替换原有的布局
 */
public abstract class ViewPage {

	protected Context context;
	/**
	 * 新的布局layout.xml的id号
	 */
	protected int layoutId;
	protected View pageView;
	protected PageManager manager;
	private String tag = "";
	//用来存放 viewpage 切换 之间的传值
	private Map<String,Object> paramsMap=new HashMap<String,Object>();

	public ViewPage(int layoutId, Context context, PageManager manager) {
		this.layoutId = layoutId;
		this.context = context;
		this.manager = manager;
	}

	public Context getContext() {
		return context;
	}

	public PageManager getManager() {
		return manager;
	}



	public void onAttach(ViewGroup parent) {
		if (pageView == null) {
			pageView = View.inflate(context, layoutId, null);
			onCreateView(pageView);
		}
		onResume();
	}

	/**
	 * 初始化新的View
	 * 
	 * @param view
	 */
	public abstract void onCreateView(View view);

	public void onDetach() {
		onPause();
	}

	public View getPageView() {
		return pageView;
	}

	public void onResume() {

	}


	public void onPause() {

	}

	public void onEvent(Object event) {
		return;
	}

	/**
	 * 是否按返回键可以返回
	 * 
	 * @return
	 */
	public boolean canBack() {
		return true;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * 添加需要传递的参数
	 * @param paramsMap
     */
	public void putParams(Map<String,Object> paramsMap){
		this.paramsMap.clear();
		this.paramsMap = paramsMap;
	}

	/**
	 * 
	 * 得到传递的参数
	 * @return
     */
	public Map<String,Object> getParams(){
		return paramsMap;
	}
}
