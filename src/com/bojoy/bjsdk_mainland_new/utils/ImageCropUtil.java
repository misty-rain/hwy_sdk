package com.bojoy.bjsdk_mainland_new.utils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images.Media;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**
 * 相册图片裁剪
 *
 * @author sunhaoyang
 */
public class ImageCropUtil {
    private static final String TAG = ImageCropUtil.class.getSimpleName();
    private static Handler sSystemHandler = null;
    private static WeakReference<Activity> sActivity = null;
    public static final int MSG_OPEN_IMAGE_SELECTOR_ACTIVITY = 0;
    /**
     * 遇到37wan在银联支付返回时，跟原有的INTENT_IMAGE_SELECTOR_ACTIVITY（10）一样了，
     * 逻辑跳到了onActivityResult
     **/
    //选择
    public static final int INTENT_IMAGE_SELECTOR_ACTIVITY = 12340; // 10
    //裁剪
    public static final int INTENT_IMAGE_CLIPPING_ACTIVITY = 12341;

    private static final ImageCropUtil INSTANCE = new ImageCropUtil();

    private ImageCropUtil() {
        InitHandler();
    }

    public static ImageCropUtil getInstance(Activity activity) {
        sActivity = new WeakReference<Activity>(activity);
        return INSTANCE;
    }

    private static void InitHandler() {
        sSystemHandler = new Handler() {
            public void handleMessage(Message msg) {
                synchronized (this) {
                    switch (msg.what) {
                        case MSG_OPEN_IMAGE_SELECTOR_ACTIVITY:
                            /**
                             * 部分使用联发科芯片的手机rom，会报AssertionError，源码如下
                             *
                             * Cursor cursor =
                             * mContext.getContentResolver().query( CONTENT_URI,
                             * COUNT_PROJECTION , String. format("%s in (%s)",
                             * Media._ID, builder.toString()), null, null); if
                             * (cursor == null) return false ;
                             * Utils.assertTrue(cursor.moveToNext());
                             *
                             * cursor.moveToNext()为false时，表示只有1张图片，但是Utils.
                             * assertTrue就会抛出 AssertionError 也就是说只有1张图片时就无法调用图库
                             * 做异常处理 但代码是在打开Gallery应用的时候才报错误，无法捕获，所以只能做手工判断，
                             * 图库中是否有1张图片， 其他设备都会如此处理
                             */
                            LogProxy.d(TAG, "pick picture from gallery");
                            Cursor cursor = sActivity
                                    .get()
                                    .getContentResolver()
                                    .query(Media.EXTERNAL_CONTENT_URI,
                                            new String[]{Media._ID}, null,
                                            null, null);
                            LogProxy.d(TAG, "start action");
                            if (cursor != null) {
                                if (cursor.getCount() == 1) {
                                    LogProxy.d(TAG, "have only one picture");
                                    Toast.makeText(sActivity.get(),
                                            "请您添加照片到图库", Toast.LENGTH_SHORT)
                                            .show();
                                    cursor.close();
                                    return;
                                }
                                cursor.close();
                            }
                            Intent intent = new Intent(
                                    Intent.ACTION_PICK,
                                    Media.EXTERNAL_CONTENT_URI);
                            sActivity.get().startActivityForResult(intent,
                                    INTENT_IMAGE_SELECTOR_ACTIVITY);
                            break;
                    }
                }
            }
        };
    }

    /**
     * 选取图片
     */
    public void InvokeOpenImageSelectorMessage() {
        Message message = new Message();
        message.what = MSG_OPEN_IMAGE_SELECTOR_ACTIVITY;
        sSystemHandler.sendMessage(message);
    }

}
