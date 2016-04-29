package com.bojoy.bjsdk_mainland_new.utils;

import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseActivityPage;

/**
 * page线程，用于回调事件
 * Created by sunhaoyang on 2016/1/29.
 */
public class PageThread implements Runnable {

//    private Thread mThread = null;
    private BaseActivityPage mPage = null;
    private int mSign = -1;

    /**
     * 关联
     * @param page
     * @param sign
     */
    public void attach(BaseActivityPage page, int sign) {
        mPage = page;
//        mThread = new Thread(this);
        mSign = sign;
    }

    /**
     * 启动
     */
    public void start() {
//        if (mThread != null) {
//            mThread.start();
//        }
        if (mPage != null) {
            mPage.onResult(mSign);
        }
    }

    @Override
    public void run() {
//        if (mPage != null) {
//            mPage.onResult(mSign);
//        }
    }

    /**
     * 销毁
     */
    public void destroy() {
//        if (mThread != null) {
//            mThread.interrupt();
//        }
    }
}
