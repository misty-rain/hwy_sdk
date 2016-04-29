package com.bojoy.bjsdk_mainland_new.widget.dialog;


import android.content.Context;

/**
 * 好玩友 协议dialog
 */
import com.bojoy.bjsdk_mainland_new.ui.view.init.impl.ProtocolPage;

public class ProtocolDialog extends BJMGFDialog {

	public ProtocolDialog(Context context) {
		super(context, null, Page_Protocol);
	}

	public ProtocolDialog(Context context, int theme) {
		super(context, theme, null, Page_Protocol);
	}
	
	@Override
	protected void createPage() {
		ProtocolPage page = new ProtocolPage(getContext(), manager, this);
		manager.addPage(page);
	}
	
	@Override
	protected void fitScreenMode() {
		fitScreen(true);
	}
	
}
