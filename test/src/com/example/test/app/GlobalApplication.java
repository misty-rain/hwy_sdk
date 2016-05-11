package com.example.test.app;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by wutao on 2016/5/11.
 */
public class GlobalApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext());
    }

}
