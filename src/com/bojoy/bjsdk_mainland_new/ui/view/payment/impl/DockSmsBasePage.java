package com.bojoy.bjsdk_mainland_new.ui.view.payment.impl;

import android.content.Context;
import android.view.View;

import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;

public abstract class DockSmsBasePage {

	protected View view = null;
	protected Context context = null;

	public DockSmsBasePage(Context context, String layoutId) {
		this.context = context;
		view = View.inflate(context,
				ReflectResourceId.getLayoutId(context, layoutId), null);
		setView();
	}
	
	public abstract void setView();

	/**
	 * 泛型简化findviewById方法
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected final <E extends View> E getView(String id) {
		try {
			return (E) view.findViewById(ReflectResourceId.getViewId(context,
					id));
		} catch (ClassCastException ex) {
			LogProxy.e("BJMEngine", "Could not cast View to concrete class."
					+ ex);
			throw ex;
		}
	}

	public View getView() {
		return view;
	}
}
