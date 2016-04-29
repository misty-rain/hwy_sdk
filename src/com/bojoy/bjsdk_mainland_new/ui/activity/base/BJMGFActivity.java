package com.bojoy.bjsdk_mainland_new.ui.activity.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Toast;
import com.bojoy.bjsdk_mainland_new.app.tools.BJMGFSDKTools;
import com.bojoy.bjsdk_mainland_new.app.BJMGFSdk;
import com.bojoy.bjsdk_mainland_new.app.tools.PayTools;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BJMGFSdkEvent;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.FileRevEvent;
import com.bojoy.bjsdk_mainland_new.support.eventbus.EventBus;
import com.bojoy.bjsdk_mainland_new.ui.page.PageManager;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseActivityPage;
import com.bojoy.bjsdk_mainland_new.utils.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by wutao on 2016/1/7.
 *  BJMGFActivity sdk主要的Activity，通过Page去切换内容
 */
public class BJMGFActivity extends Activity {
    private final String TAG = BJMGFActivity.class.getSimpleName();

    public static final String Page_Class_Name_Key = "Page_Class_Name_Key";
    public static final String Custom_Service_Tab_Id = "Custom_Service_Tab_Id";

    protected PageManager manager;
    protected ViewGroup root;
    protected EventBus eventBus = EventBus.getDefault();
    protected boolean isNeedOpenDock = true;
    protected int themeId;
    //头像尺寸：宽和高
    protected int faceImageSize = 640;
    protected String imageName = "/croptmp"+ System.currentTimeMillis() +".jpg";
    //图片地址为sd卡中的bjmFace文件夹中
    protected String outputPath=Environment.getExternalStorageDirectory().getAbsolutePath() + imageName;

    public static boolean canLandscape = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setScreenOrientation();
        /** 处理Light主题效果 保证默认字体为黑色 */
        int themeResId = 0;
        try {
            Class<?> clazz = ContextThemeWrapper.class;
            Method method = clazz.getMethod("getThemeResId");
            method.setAccessible(true);
            themeResId = (Integer) method.invoke(this);
        } catch (NoSuchMethodException e) {
            LogProxy.e(TAG, "Failed to get theme resource ID" + e.toString());
        } catch (IllegalAccessException e) {
            LogProxy.e(TAG, "Failed to get theme resource ID" + e.toString());
        } catch (IllegalArgumentException e) {
            LogProxy.e(TAG, "Failed to get theme resource ID" + e.toString());
        } catch (InvocationTargetException e) {
            LogProxy.e(TAG, "Failed to get theme resource ID" + e.toString());
        }
        if (themeResId == android.R.style.Theme_Black_NoTitleBar
                || themeResId == android.R.style.Theme_NoTitleBar) {
            setTheme(android.R.style.Theme_Light_NoTitleBar);
            themeId = android.R.style.Theme_Light_NoTitleBar;
        } else
        if (themeResId == android.R.style.Theme_Black_NoTitleBar_Fullscreen
                || themeResId == android.R.style.Theme_NoTitleBar_Fullscreen) {
            setTheme(android.R.style.Theme_Light_NoTitleBar_Fullscreen);
            themeId = android.R.style.Theme_Light_NoTitleBar_Fullscreen;
        } else
        if (themeResId != android.R.style.Theme_Dialog && themeResId != android.R.style.Theme_Translucent
                && themeResId != android.R.style.Theme_Translucent_NoTitleBar
                && themeResId != android.R.style.Theme_Translucent_NoTitleBar_Fullscreen
                && themeResId != android.R.style.Theme_Light_NoTitleBar
                && themeResId != android.R.style.Theme_Light_NoTitleBar_Fullscreen) {
            setTheme(android.R.style.Theme_Light);
            themeId = android.R.style.Theme_Light;
        } else {
            themeId = themeResId;
        }
        LogProxy.i(TAG, "themeId=" + themeId);
        /*******************************/
        setContentView(ReflectResourceId.getLayoutId(this,
                Resource.layout.bjmgf_sdk_activity));
        root = (ViewGroup) findViewById(ReflectResourceId.getViewId(this,
                Resource.id.bjmgf_sdk_activity_root));

        manager = new PageManager(root);
    }

    public void hideInputMethod() {
        Utility.hideInputMethod(this, getCurrentFocus());
//		InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//		if (getCurrentFocus() != null) {
//			im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//		}
    }

    public void setScreenOrientation() {
        if (BJMGFSDKTools.getInstance().getScreenOrientation() == SysConstant.BJMGF_Screen_Orientation_Landscape && canLandscape ) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (BJMGFSDKTools.getInstance().getScreenOrientation() == SysConstant.BJMGF_Screen_Orientation_Portrait) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        BJMGFSdk.getDefault().getDockManager().hideDock(); // 隐藏悬浮窗
        String pageClassName = getIntent().getStringExtra(Page_Class_Name_Key);
        openPage(pageClassName);
    }
    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "activity ondestroy");
        PayTools.getInstance().clearPayDatas();//清除支付订单数据
        if (isNeedOpenDock) {
            BJMGFSdk.getDefault().getDockManager().openDock(); // 打开悬浮窗
        }
        manager.clean();
        super.onDestroy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /**
         * 点击输入框外使输入框隐藏
         */
        hideInputMethod();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utility.Pick_Picture_From_Gallery && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();// 选中图片
            LogProxy.i(TAG, "uri = " + data.getData().toString());
            String picturePath = null;
            /** 从图库中选取的图片返回的URI进行判断，如果直接是文件的URI，则直接返回文件路径 */
            if (URLUtil.isFileUrl(data.getData().toString())) {
                picturePath = data.getData().getPath();
                LogProxy.i(TAG, "return data is direct file path:" + picturePath);
            } else if (URLUtil.isContentUrl(data.getData().toString())) {
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);	// 图片的路径
                LogProxy.i(TAG, "picture path:" + picturePath);
                cursor.close();
            } else {
                LogProxy.i(TAG, "Do not support uri = " + data.getData().toString());
                Toast.makeText(this, ReflectResourceId.getStringId(this,
                        Resource.string.bjmgf_sdk_question_image_pick_error), Toast.LENGTH_SHORT).show();
                return;
            }
            eventBus.post(new FileRevEvent(picturePath));
        }
        else if(requestCode == ImageCropUtil.INTENT_IMAGE_SELECTOR_ACTIVITY && resultCode == RESULT_OK ){
            if (data == null) {
                return;
            }
            Uri imagUri = data.getData();
            Intent intent = new Intent("com.android.camera.action.CROP", null);
            intent.setDataAndType(imagUri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", faceImageSize);
            intent.putExtra("outputY", faceImageSize);
            intent.putExtra("scale", true);//黑边
            intent.putExtra("scaleUpIfNeeded", true);//黑边
            intent.putExtra("return-data", false);
            Uri uritempFile = Uri.parse("file://" + outputPath);
            //判断外部存储设备不对应的时候，采用另一种路径
            if (!Environment.getExternalStorageState().equals(
                    android.os.Environment.MEDIA_MOUNTED)) {
                outputPath="/mnt/sdcard2" + imageName;
                uritempFile = Uri.parse("file://" + outputPath);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
            intent.putExtra("outputFormat",
                      Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true);

            startActivityForResult(intent,
                    ImageCropUtil.INTENT_IMAGE_CLIPPING_ACTIVITY);
        }
        else if (requestCode == ImageCropUtil.INTENT_IMAGE_CLIPPING_ACTIVITY && resultCode == RESULT_OK ) {
            eventBus.post(new FileRevEvent(outputPath));
        }
        else if (resultCode == RESULT_OK && data != null) {
            String str =  data.getExtras().getString("pay_result");
            if( str.equalsIgnoreCase("success") ){
                eventBus.post(new BJMGFSdkEvent(BJMGFSdkEvent.RECHARGE_SUCCESS));
            } else if( str.equalsIgnoreCase("fail") ){
                eventBus.post(new BJMGFSdkEvent(BJMGFSdkEvent.RECHARGE_FAIL));
            } else {
                eventBus.post(new BJMGFSdkEvent(BJMGFSdkEvent.RECHARGE_FAIL));
            }
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (manager != null && manager.getCurrentPage() != null) {
                if (!manager.getCurrentPage().canBack()) {
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public ViewGroup getRoot() {
        return root;
    }

    private void openPage(String pageClassName) {
        LogProxy.i(TAG, pageClassName);
        if (manager.getCurrentPage() != null
                && pageClassName.equals(manager.getCurrentPage().getClass()
                .getCanonicalName())) {
            return;
        }
        /**
         * 如果当前在次级界面的时候也不需要重新更换界面，
         * 保证返回Activity的时候还是在当前页面
         */
        if (manager.pageCount() >= 1) {
//			LogProxy.i(TAG, "page count=" + manager.pageCount());
            return;
        }
        BaseActivityPage page;
        try {
            Class<?> c = Class.forName(pageClassName);
            Constructor<?> constructor = c.getConstructor(Context.class,
                    PageManager.class, BJMGFActivity.class);
            page = (BaseActivityPage) constructor.newInstance(this, manager,
                    this);
      /*      if (pageClassName.equals(CustomerServiceCenterView.class.getCanonicalName())) {
                int currId = getIntent().getIntExtra(Custom_Service_Tab_Id, 0);
                ((CustomerServiceCenterView)page).setCurrentId(currId);
            }*/
            manager.clearTopPage(page);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        // if (pageClassName.equals(Dock.class.getSimpleName())) {
        // page = new Dock(this, manager, this);
        //manager.clearTopPage(page);
        // }
    }

    public int getThemeId() {
        return themeId;
    }

    public void setNeedOpenDock(boolean flag) {
        this.isNeedOpenDock = flag;
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "activity onpause");
        super.onPause();
    }

}
