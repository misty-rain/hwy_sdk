package com.bojoy.bjsdk_mainland_new.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;
import com.bojoy.bjsdk_mainland_new.support.imageloader.cache.disc.naming.Md5FileNameGenerator;
import com.bojoy.bjsdk_mainland_new.support.imageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.bojoy.bjsdk_mainland_new.support.imageloader.core.DisplayImageOptions;
import com.bojoy.bjsdk_mainland_new.support.imageloader.core.ImageLoader;
import com.bojoy.bjsdk_mainland_new.support.imageloader.core.ImageLoaderConfiguration;
import com.bojoy.bjsdk_mainland_new.support.imageloader.core.assist.QueueProcessingType;
import com.bojoy.bjsdk_mainland_new.support.imageloader.core.listener.ImageLoadingListener;

/**
 * Created by wutao on 2016/1/8.
 * Imageloader 框架 工具类
 */
public class ImageLoaderUtil {

    private final String TAG = ImageLoaderUtil.class.getSimpleName();

    private static Object block = new Object();
    private static ImageLoaderUtil instance;
    private final static int Size_Max = 1024;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private boolean init;

    private ImageLoaderUtil() {

    }

    public static ImageLoaderUtil getInstance() {
        if (instance == null) {
            synchronized (block) {
                if (instance == null) {
                    instance = new ImageLoaderUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化缓存
     * @param context
     */
    public void init(Context context) {
        if (!init) {
            init = true;
          /*  ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                    .threadPriority(Thread.NORM_PRIORITY - 2)
                    .denyCacheImageMultipleSizesInMemory()
                    .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                    .diskCacheSize(50 * Size_Max * Size_Max) // 50 Mb
                    .tasksProcessingOrder(QueueProcessingType.LIFO)
                    .writeDebugLogs() // Remove for release app
                    .build();*/

            int maxMemory = (int) (Runtime.getRuntime().maxMemory());
            // 使用最大可用内存值的1/8作为缓存的大小。
            int cacheSize = maxMemory / 8;
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager mWm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            mWm.getDefaultDisplay().getMetrics(metrics);

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                      .threadPriority(Thread.NORM_PRIORITY - 2)
                      .denyCacheImageMultipleSizesInMemory()
                      .memoryCache(new UsingFreqLimitedMemoryCache(cacheSize))
                      .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                      .tasksProcessingOrder(QueueProcessingType.LIFO)
                      .diskCacheExtraOptions(metrics.widthPixels, metrics.heightPixels, null)
                      .diskCacheSize(1024 * 1024 * 1024)
                      .diskCacheFileCount(1000)
                      .build();

            // Initialize ImageLoader with configuration.
            ImageLoader.getInstance().init(config);
        }
    }

    public boolean isInit() {
        return init;
    }

    /**
     * 动态加载图片 (Android-Universal-Image-Loader v1.9.3)
     * @param context
     * @param imageView
     * @param imageUrl
     */
    public final void loadImageUrl(final Context context, ImageView imageView, String imageUrl,
                                   ImageLoadingListener listener) {
        loadImageUrl(context, imageView, imageUrl, listener, 0);
    }

    public final void loadImageUrl(final Context context, ImageView imageView, String imageUrl,
                                   ImageLoadingListener listener, int roundRect) {
        loadImageUrl(context, imageView, imageUrl, listener, roundRect, false);
    }

    /**
     *
     * 动态加载图片 (Android-Universal-Image-Loader v1.9.3)
     * @param context
     * @param imageView
     * @param imageUrl
     * @param listener
     * @param roundRect > 0 则图片做圆角处理
     */
    public final void loadImageUrl(final Context context, ImageView imageView, String imageUrl,
                                   ImageLoadingListener listener, int roundRect, boolean needReDownload) {
        if (!init) {
            throw new IllegalAccessError("You need init befor load image!");
        }
        DisplayImageOptions options = null;
        if (roundRect > 0) {
            options = new DisplayImageOptions.Builder(roundRect)
//			.showImageOnLoading(0)
//			.showImageForEmptyUri(0)
//			.showImageOnFail(0)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        } else {
            options = new DisplayImageOptions.Builder()
//			.showImageOnLoading(0)
//			.showImageForEmptyUri(0)
//			.showImageOnFail(0)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }

        //LogProxy.i(TAG, "image url = " + imageUrl);
        try {
            imageLoader.displayImage(imageUrl, imageView, options, listener, needReDownload);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void cancelLoadingImage(ImageView imageView) {
        imageLoader.cancelDisplayTask(imageView);
    }

    public void clearCache() {
        imageLoader.clearDiskCache();
        imageLoader.clearMemoryCache();
    }

}
