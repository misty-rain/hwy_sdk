package com.bojoy.bjsdk_mainland_new.app;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import com.bojoy.bjsdk_mainland_new.support.eventbus.EventBus;
import com.bojoy.bjsdk_mainland_new.utils.ImageLoaderUtil;


public class GlobalContext extends Application {

    //singleton
    private static GlobalContext globalContext = null;
    private SharedPreferences sharedPref = null;
    private Activity activity = null;


   

    public static GlobalContext getInstance() {
        return globalContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        globalContext = this;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }


}
