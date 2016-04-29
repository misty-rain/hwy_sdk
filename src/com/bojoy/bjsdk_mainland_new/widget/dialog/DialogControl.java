package com.bojoy.bjsdk_mainland_new.widget.dialog;

/**
 * Created by xiniu_wutao on 15/6/30.
 */
public interface DialogControl {

    void hideWaitDialog();

    WaitDialog showWaitDialog();

    WaitDialog showWaitDialog(int resid);

    WaitDialog showWaitDialog(String text);
}
