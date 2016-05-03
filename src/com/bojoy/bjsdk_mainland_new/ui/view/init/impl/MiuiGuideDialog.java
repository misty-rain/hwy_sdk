package com.bojoy.bjsdk_mainland_new.ui.view.init.impl;

import android.content.Context;
import android.view.MotionEvent;

import com.bojoy.bjsdk_mainland_new.ui.page.base.ViewPage;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMGFDialog;

/**
 * Created by wutao on 2016/5/3.
 */
public class MiuiGuideDialog extends BJMGFDialog {

    public MiuiGuideDialog(Context context) {
        super(context, null, Page_MiuiGuide);
    }

    public MiuiGuideDialog(Context context, int theme) {
        super(context, theme, null, Page_MiuiGuide);
    }

    @Override
    protected void createPage() {
        LogProxy.d("", "create MiuiGuidePage");
        MiuiGuidePage page = new MiuiGuidePage(getContext(), manager, this);
        manager.addPage(page);
    }

    @Override
    public void show() {
        fitScreenMode();
        super.show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ViewPage page = manager.getCurrentPage();
        LogProxy.d("", "hide MiuiGuidePage＝" +page);
        if (page != null) {
            ((MiuiGuidePage) page).hide();
        }
        return false;
//		return super.onTouchEvent(event);
    }
}
