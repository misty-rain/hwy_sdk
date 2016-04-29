package com.bojoy.bjsdk_mainland_new.ui.adapter;

import java.util.List;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.widget.ViewHolder;

/**
 * adapter基础类
 * 功能：
 * 简化操作
 * @author sunhaoyang
 * @param <T>
 * @param <T>
 *
 */
public abstract class BasicAdapter<T> extends BaseAdapter {

	protected List<T> mData;
	protected Context mContext;
	protected String mLayoutId;
	protected ViewHolder holder;

	public BasicAdapter(List<T> data, Context context, String layoutId) {
		this.mData = data;
		this.mContext = context;
		this.mLayoutId = layoutId;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		holder = new ViewHolder(mContext, parent,
				ReflectResourceId.getLayoutId(mContext, mLayoutId), position);
		setView(position, convertView, parent);
		return holder.getConvertView();

	}
	
	protected <T extends View> T getView(String viewId) {
		return holder.getView(ReflectResourceId.getViewId(mContext, viewId));
	}

	protected abstract void setView(int position, View convertView, ViewGroup parent);

}
