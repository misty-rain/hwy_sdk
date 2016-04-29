package com.bojoy.bjsdk_mainland_new.ui.view.about.impl;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseActivityPage;
import com.bojoy.bjsdk_mainland_new.ui.view.about.IAboutView;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;

/**
 * 
 * @author wutao
 * AboutPage 关于界面
 * 
 * */

public class AboutPage extends BaseActivityPage implements IAboutView{

	@SuppressWarnings("unused")
	private final String TAG = AboutPage.class.getSimpleName();
	private RelativeLayout mBackLayout = null;
	private TextView versionTextView;
	public AboutPage(Context context, PageManager manager,
					 BJMGFActivity activity) {
		super(ReflectResourceId.getLayoutId(context,
				Resource.layout.bjmgf_sdk_dock_about_page), context, manager, activity);
	}
	
	@Override
	public void onCreateView(View view) {
		mBackLayout = (RelativeLayout)view.findViewById(ReflectResourceId.getViewId(context,
				Resource.id.bjmgf_sdk_about_closeLlId));
		versionTextView = (TextView)view.findViewById(ReflectResourceId.getViewId(context, 
				Resource.id.bjmgf_sdk_version_TvId));
		
		mBackLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				manager.previousPage();
			}
		});
		versionTextView.setText("V " + getString(Resource.string.bjmgf_sdk_version));
		super.onCreateView(view);
	}


	@Override
	public void setView() {}

	@Override
	public void showVersion() {

	}

	@Override
	public void showError(String message) {

	}

	@Override
	public void showSuccess() {

	}
}
