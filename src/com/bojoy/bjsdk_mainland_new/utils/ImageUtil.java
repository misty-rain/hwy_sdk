package com.bojoy.bjsdk_mainland_new.utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bojoy.bjsdk_mainland_new.app.GlobalContext;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;

import java.io.*;
import java.lang.reflect.Field;

/**
 * Created by xiniu_wutao on 15/6/30.
 * 图片帮助类
 */
public class ImageUtil {

    private static AppLogger logger = AppLogger.getLogger(ImageUtil.class);

    public static Bitmap getBigBitmapForDisplay(String imagePath,
                                                Context context) {
        if (null == imagePath || !new File(imagePath).exists())
            return null;
        try {
            int degeree = readPictureDegree(imagePath);
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            if (bitmap == null)
                return null;
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
            float scale = bitmap.getWidth() / (float) dm.widthPixels;
            Bitmap newBitMap = null;
            if (scale > 1) {
                newBitMap = zoomBitmap(bitmap, (int) (bitmap.getWidth() / scale), (int) (bitmap.getHeight() / scale));
                bitmap.recycle();
                Bitmap resultBitmap = rotaingImageView(degeree, newBitMap);
                return resultBitmap;
            }
            Bitmap resultBitmap = rotaingImageView(degeree, bitmap);
            return resultBitmap;
        } catch (Exception e) {
            logger.e(e.getMessage());
            return null;
        }
    }

    private static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        if (null == bitmap) {
            return null;
        }
        try {
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            Matrix matrix = new Matrix();
            float scaleWidth = ((float) width / w);
            float scaleHeight = ((float) height / h);
            matrix.postScale(scaleWidth, scaleHeight);
            Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
            return newbmp;
        } catch (Exception e) {
            logger.e(e.getMessage());
            return null;
        }
    }


    public Intent getPhotoPickIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        return intent;
    }


    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);// 压缩位图
            byte[] bytes = baos.toByteArray();// 创建分配字节数组
            return bytes;
        } catch (Exception e) {
            AppLogger.getLogger(ImageUtil.class).e(e.getMessage());
            return null;
        } finally {
            if (null != baos) {
                try {
                    baos.flush();
                    baos.close();
                } catch (IOException e) {
                    AppLogger.getLogger(ImageUtil.class).e(e.getMessage());
                }
            }
        }
    }

    /**
     * @param path
     * @return
     * @throws IOException
     * @Description 上传服务器前调用该方法进行压缩
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap revitionImage(String path) throws IOException {
        if (null == path || TextUtils.isEmpty(path) || !new File(path).exists())
            return null;
        BufferedInputStream in = null;
        try {
            int degree = readPictureDegree(path);
            in = new BufferedInputStream(new FileInputStream(new File(path)));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            options.inSampleSize = calculateInSampleSize(options, 400, 600);

            in.close();
            in = new BufferedInputStream(new FileInputStream(new File(path)));
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
            Bitmap newbitmap = rotaingImageView(degree, bitmap);
            return newbitmap;
        } catch (Exception e) {
            AppLogger.getLogger(ImageUtil.class).e(e.getMessage());
            return null;
        } finally {
            if (null != in) {
                in.close();
                in = null;
            }
        }
    }

    public String getImagePathFromUri(Uri uri) {
        // 如果是file，直接拿
        if (uri.getScheme().equalsIgnoreCase("file")) {
            return uri.getPath();
        }

        String[] projection = {
                MediaStore.Images.Media.DATA
        };
        Cursor cursor = GlobalContext.getInstance().getContentResolver().query(uri, projection,
                null, null, null);
        int column_index = cursor.getColumnIndex(projection[0]);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();

        return path;
    }


    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            AppLogger.getLogger(ImageUtil.class).e(e.getMessage());
        }
        return degree;
    }

    /*
     * 旋转图片
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        if (null == bitmap) {
            return null;
        }
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }
    
    

    /**
     * 根据InputStream获取图片实际的宽度和高度
     *
     * @param imageStream
     * @return
     */
    public static ImageSize getImageSize(InputStream imageStream)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(imageStream, null, options);
        return new ImageSize(options.outWidth, options.outHeight);
    }

    public static class ImageSize
    {
        int width;
        int height;

        public ImageSize()
        {
        }

        public ImageSize(int width, int height)
        {
            this.width = width;
            this.height = height;
        }

        @Override
        public String toString()
        {
            return "ImageSize{" +
                    "width=" + width +
                    ", height=" + height +
                    '}';
        }
    }

    public static int calculateInSampleSize(ImageSize srcSize, ImageSize targetSize)
    {
        // 源图片的宽度
        int width = srcSize.width;
        int height = srcSize.height;
        int inSampleSize = 1;

        int reqWidth = targetSize.width;
        int reqHeight = targetSize.height;

        if (width > reqWidth && height > reqHeight)
        {
            // 计算出实际宽度和目标宽度的比率
            int widthRatio = Math.round((float) width / (float) reqWidth);
            int heightRatio = Math.round((float) height / (float) reqHeight);
            inSampleSize = Math.max(widthRatio, heightRatio);
        }
        return inSampleSize;
    }

    /**
     * 根据ImageView获适当的压缩的宽和高
     *
     * @param view
     * @return
     */
    public static ImageSize getImageViewSize(View view)
    {

        ImageSize imageSize = new ImageSize();

        imageSize.width = getExpectWidth(view);
        imageSize.height = getExpectHeight(view);

        return imageSize;
    }

    /**
     * 根据view获得期望的高度
     *
     * @param view
     * @return
     */
    private static int getExpectHeight(View view)
    {

        int height = 0;
        if (view == null) return 0;

        final ViewGroup.LayoutParams params = view.getLayoutParams();
        //如果是WRAP_CONTENT，此时图片还没加载，getWidth根本无效
        if (params != null && params.height != ViewGroup.LayoutParams.WRAP_CONTENT)
        {
            height = view.getWidth(); // 获得实际的宽度
        }
        if (height <= 0 && params != null)
        {
            height = params.height; // 获得布局文件中的声明的宽度
        }

        if (height <= 0)
        {
            height = getImageViewFieldValue(view, "mMaxHeight");// 获得设置的最大的宽度
        }

        //如果宽度还是没有获取到，憋大招，使用屏幕的宽度
        if (height <= 0)
        {
            DisplayMetrics displayMetrics = view.getContext().getResources()
                    .getDisplayMetrics();
            height = displayMetrics.heightPixels;
        }

        return height;
    }

    /**
     * 根据view获得期望的宽度
     *
     * @param view
     * @return
     */
    private static int getExpectWidth(View view)
    {
        int width = 0;
        if (view == null) return 0;

        final ViewGroup.LayoutParams params = view.getLayoutParams();
        //如果是WRAP_CONTENT，此时图片还没加载，getWidth根本无效
        if (params != null && params.width != ViewGroup.LayoutParams.WRAP_CONTENT)
        {
            width = view.getWidth(); // 获得实际的宽度
        }
        if (width <= 0 && params != null)
        {
            width = params.width; // 获得布局文件中的声明的宽度
        }

        if (width <= 0)

        {
            width = getImageViewFieldValue(view, "mMaxWidth");// 获得设置的最大的宽度
        }
        //如果宽度还是没有获取到，憋大招，使用屏幕的宽度
        if (width <= 0)

        {
            DisplayMetrics displayMetrics = view.getContext().getResources()
                    .getDisplayMetrics();
            width = displayMetrics.widthPixels;
        }

        return width;
    }

    /**
     * 通过反射获取imageview的某个属性值
     *
     * @param object
     * @param fieldName
     * @return
     */
    private static int getImageViewFieldValue(Object object, String fieldName)
    {
        int value = 0;
        try
        {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = field.getInt(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE)
            {
                value = fieldValue;
            }
        } catch (Exception e)
        {
        }
        return value;

    }



    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            // 取 drawable 的长宽
            int w = drawable.getIntrinsicWidth();
            int h = drawable.getIntrinsicHeight();

            // 取 drawable 的颜色格式
            Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                    : Bitmap.Config.RGB_565;
            // 建立对应 bitmap
            Bitmap bitmap = Bitmap.createBitmap(w, h, config);
            // 建立对应 bitmap 的画布
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, w, h);
            // 把 drawable 内容画到画布中
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }


    /**
     * 保存图片到指定路径 大图片要进行压缩
     * @param activity
     * @param srcPath
     * @param destFileName
     * @return 返回图片路径
     */
    public final static String saveBitmap(Activity activity, String srcPath) {
        return saveBitmap(activity, srcPath, false);
    }

    /**
     * 保存图片到指定路径 扩展存储卡 大图片要进行压缩
     * @param activity
     * @param srcPath
     * @param destFileName
     * @return 返回图片路径
     */
    public final static String saveBitmap(Activity activity, String srcPath, boolean saveToExpandStoreage) {
        if (!Utility.checkFilePath(srcPath)) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath, options);
        Bitmap bitmap = null;
        if (options.outWidth < SysConstant.SIZE_MAX && options.outHeight < SysConstant.SIZE_MAX) {
            bitmap = BitmapFactory.decodeFile(srcPath);
        } else {
            int width = 0, height = 0;
            float temp = 0f;
            if (options.outWidth > options.outHeight) {
                width = SysConstant.SIZE_MAX;
                temp = width * options.outHeight / options.outWidth;
                height = (int)temp;
            } else {
                height = SysConstant.SIZE_MAX;
                temp = height * options.outWidth / options.outHeight;
                width = (int)temp;
            }
            bitmap = getScaleBitmapFromFile(srcPath, width, height);
        }
        String appPath = "";
        if (saveToExpandStoreage) {
            appPath = activity.getApplicationContext().getExternalCacheDir().getAbsolutePath();
            LogProxy.d("ImageUtil", "appPath=" + appPath);
        } else {
            appPath = activity.getApplicationContext().getFilesDir().getAbsolutePath();
        }
        String tempPath = appPath + Utility.Temp_Dir_Relative_Path;
        File tempFileDir = new File(tempPath);
        if (!tempFileDir.exists()) {
            tempFileDir.mkdirs();
        }
        String outputPath = String.format("%s/%s", tempFileDir, Utility.Temp_attach_Image_Name);
        if (compressBitmapToFile(bitmap, outputPath)) {
            LogProxy.i("ImageUtil", "out put Path = " + outputPath);
            return outputPath;
        }
        return srcPath;
    }

    /**
     * 获取缩小的图片
     * @param imagePath
     * @param outWidth
     * @param outHeight
     * @return
     */
    public final static Bitmap getScaleBitmapFromFile(String imagePath, int outWidth, int outHeight) {
        if (!Utility.checkFilePath(imagePath)) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
//		LogProxy.i(TAG, "size = " + outWidth + ", " + outHeight
//				+ ", out = " + options.outWidth + ", " + options.outHeight);
        float scaleX = (float)options.outWidth / (float)outWidth;
        float scaleY = (float)options.outHeight / (float)outHeight;
        float scale = scaleX < scaleY ? scaleX : scaleY;
//		LogProxy.i(TAG, "scaleX = " + scaleX + ", scaleY = " + scaleY);
        options.inSampleSize = getNear2Power((int)scale);
        options.inInputShareable = true;
        options.inPurgeable = true;
        options.inJustDecodeBounds = false;
        Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);
//		LogProxy.i(TAG, "inSampleSize = " + options.inSampleSize);
//		LogProxy.i("BJM", "bitmap = (" + bmp.getWidth() + ", " + bmp.getHeight() + ")");
        return bmp;
    }

    public final static int getNear2Power(int num) {
        int index = 0;
        while(true) {
            int value = (int)Math.pow(2, index);
            if (value > num) {
                return value;
            }
            if (value < 0 || value > Integer.MAX_VALUE) {
                return Integer.MAX_VALUE;
            }
            index++;
        }
    }

    public final static boolean compressBitmapToFile(Bitmap bitmap, String outputPath) {
        FileOutputStream fileStream;
        boolean success = true;
        try {
            fileStream = new FileOutputStream(outputPath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, SysConstant.JPG_QUALITY, fileStream);
        } catch (FileNotFoundException e) {
            success = false;
            e.printStackTrace();
        }
        bitmapRecycle(bitmap);
        return success;
    }

    public final static boolean bitmapRecycle(Bitmap bitmap) {
        if(bitmap != null && !bitmap.isRecycled()){
            // 回收并且置为null
            bitmap.recycle();
            bitmap = null;
            return true;
        }
        return false;
    }

}
