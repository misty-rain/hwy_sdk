package com.bojoy.bjsdk_mainland_new.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bojoy.bjsdk_mainland_new.app.GlobalContext;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.widget.ClearEditText;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.internal.Util;


/**
 * 工具类
 */
@SuppressLint("NewApi")
public class Utility {


    private final static String TAG = Utility.class.getSimpleName();
    private final static Pattern onlyDitital = Pattern
            .compile("^[0-9]+(.[0-9]{0,1})?$");
    private static Boolean isExit = false;
    private static long exitTime = 0;

    private Hashtable<String, Object> memCacheRegion = new Hashtable<String, Object>();
    private static Boolean _isTablet = null;
    public final static int Pick_Picture_From_Gallery = 1000;
    private static long lastClickTime;

    private final static int Size_Max = 800;
    private final static int JPG_Quality = 80;
    public final static String Temp_Dir_Relative_Path = "/gfsdktemp";
    public final static String Temp_attach_Image_Name = "attah_image.jpg";
    // 验证手机号码正则
    public static final String PHONE_REGEX_SIMPLE = "^(1[0-9]{10})$";// 目前已经有17开头的手机号码，所以号码第二位不再限制


    private Utility() {
        // Forbidden being instantiated.
    }

    public static String encodeUrl(Map<String, String> param) {
        if (param == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        Set<String> keys = param.keySet();
        boolean first = true;

        for (String key : keys) {
            String value = param.get(key);
            if (!TextUtils.isEmpty(value)) {
                if (first)
                    first = false;
                else
                    sb.append("&");
                // try {
                // sb.append(URLEncoder.encode(key, "UTF-8")).append("=")
                // .append(URLEncoder.encode(param.get(key), "UTF-8"));

                sb.append(key).append("=").append(param.get(key));

                // } catch (UnsupportedEncodingException e) {

                // }
            }

        }

        return sb.toString();
    }



    public static Bundle decodeUrl(String s) {
        Bundle params = new Bundle();

        if (s != null) {
            String array[] = s.split("&");
            for (String parameter : array) {
                String v[] = parameter.split("=");
                try {

                    if (v.length == 2)
                        params.putString(URLDecoder.decode(v[0], "UTF-8"),
                                URLDecoder.decode(v[1], "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();

                }
            }
        }
        return params;
    }

    public static void closeSilently(Closeable closeable) {
        if (closeable != null)
            try {
                closeable.close();
            } catch (IOException ignored) {

            }
    }

    /**
     * 将图片变为圆角
     *
     * @param bitmap 原Bitmap图片
     * @param pixels 图片圆角的弧度(单位:像素(px))
     * @return 带有圆角的图片(Bitmap 类型)
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * Parse a URL query and fragment parameters into a key-value bundle.
     */
    public static Bundle parseUrl(String url) {
        // hack to prevent MalformedURLException
        url = url.replace("weiboconnect", "http");
        try {
            URL u = new URL(url);
            Bundle b = decodeUrl(u.getQuery());
            b.putAll(decodeUrl(u.getRef()));
            return b;
        } catch (MalformedURLException e) {
            return new Bundle();
        }
    }


    public static void stopListViewScrollingAndScrollToTop(ListView listView) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.BASE) {
            listView.setSelection(0);
            listView.dispatchTouchEvent(MotionEvent.obtain(
                    SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                    MotionEvent.ACTION_CANCEL, 0, 0, 0));

        } else {
            listView.dispatchTouchEvent(MotionEvent.obtain(
                    SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                    MotionEvent.ACTION_DOWN, 0, 0, 0));
            listView.dispatchTouchEvent(MotionEvent.obtain(
                    SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                    MotionEvent.ACTION_UP, 0, 0, 0));
            listView.setSelection(0);
        }
    }

    public static int dip2px(Context context, int dipValue) {
        float reSize = context.getResources()
                .getDisplayMetrics().density;
        return (int) ((dipValue * reSize) + 0.5);
    }

    /**
     * dp 转像素
     *
     * @param context
     * @param dp
     * @return
     */
    public static float dpToPixel(Context context, float dp) {
        return dp
                * (context.getResources()
                .getDisplayMetrics().densityDpi / 160F);
    }

    public static int px2dip(Context context, int pxValue) {
        float reSize = context.getResources()
                .getDisplayMetrics().density;
        return (int) ((pxValue / reSize) + 0.5);
    }

    public static float sp2px(Context context, int spValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue,
                context.getResources().getDisplayMetrics());
    }

    public static int length(String paramString) {
        int i = 0;
        for (int j = 0; j < paramString.length(); j++) {
            if (paramString.substring(j, j + 1).matches("[Α-￥]"))
                i += 2;
            else
                i++;
        }

        if (i % 2 > 0) {
            i = 1 + i / 2;
        } else {
            i = i / 2;
        }

        return i;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }

    public static int getNetType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return networkInfo.getType();
        }
        return -1;
    }

    public static boolean isGprs(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() != ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSystemRinger(Context context) {
        AudioManager manager = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        return manager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
    }

    public static String getPicPathFromUri(Uri uri, Activity activity) {
        String value = uri.getPath();

        if (value.startsWith("/external")) {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = activity.managedQuery(uri, proj, null, null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else {
            return value;
        }
    }

    public static boolean isAllNotNull(Object... obs) {
        for (int i = 0; i < obs.length; i++) {
            if (obs[i] == null) {
                return false;
            }
        }
        return true;
    }

    public static boolean isIntentSafe(Activity activity, Uri uri) {
        Intent mapCall = new Intent(Intent.ACTION_VIEW, uri);
        PackageManager packageManager = activity.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(
                mapCall, 0);
        return activities.size() > 0;
    }

    public static boolean isIntentSafe(Activity activity, Intent intent) {
        PackageManager packageManager = activity.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(
                intent, 0);
        return activities.size() > 0;
    }

    public static boolean isGooglePlaySafe(Activity activity) {
        Uri uri = Uri
                .parse("http://play.google.com/store/apps/details?id=com.google.android.gms");
        Intent mapCall = new Intent(Intent.ACTION_VIEW, uri);
        mapCall.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        mapCall.setPackage("com.android.vending");
        PackageManager packageManager = activity.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(
                mapCall, 0);
        return activities.size() > 0;
    }

    public static String buildTabText(int number) {

        if (number == 0) {
            return null;
        }

        String num;
        if (number < 99) {
            num = "(" + number + ")";
        } else {
            num = "(99+)";
        }
        return num;

    }

    /**
     * 弹出Toast消息
     *
     * @param msg
     */
    public static void ToastMessage(Context cont, String msg) {
        Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
    }


    public static void ToastMessage(Context cont, int msg) {
        Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
    }


    public static void ToastMessage(Context cont, String msg, int time) {
        Toast.makeText(cont, msg, time).show();
    }

    /**
     * 初始化动画
     *
     * @param context
     * @param img
     */
    public static void initAnim(Context context, ImageView img, int animId) {
        Animation anim = AnimationUtils.loadAnimation(context, animId);
        img.startAnimation(anim);

    }

    /**
     * 判断只能输入数字
     *
     * @param text
     * @return
     */
    public static boolean isOnlyDigital(String text) {
        if (text == null || text.trim().length() == 0)
            return false;
        return onlyDitital.matcher(text).matches();

    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    /**
     * 退出登录 注销
     *
     * @param cont
     */
    /*
     * public static void Logout(final Context cont) { final AlertDialog alg =
	 * new AlertDialog.Builder(cont).create(); alg.show(); Window window =
	 * alg.getWindow(); window.setContentView(R.layout.dialog); Button btnEntry
	 * = (Button) window .findViewById(R.id.dialog_button_entry); Button
	 * btnCancle = (Button) window .findViewById(R.id.dialog_button_cancle);
	 * btnEntry.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { // TODO Auto-generated method
	 * stub alg.cancel(); // 注销 Intent intent = new Intent(cont,
	 * LoginActivity.class); cont.startActivity(intent);
	 * 
	 * } }); btnCancle.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { // TODO Auto-generated method
	 * stub alg.dismiss(); } });
	 * 
	 * }
	 */

    /**
     * 获取assets 目录下 属性文件
     *
     * @return
     */
    public static Properties getContextProperties() {
        Properties pro = new Properties();
        InputStream in = Utility.class
                .getResourceAsStream("/assets/context.properties");
        try {
            pro.load(in);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return pro;

    }


    // 校验Tag Alias 只能是数字,英文字母和中文
    public static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    // 取得版本号
    public static String getVersionName(Context context) {
        try {
            PackageInfo manager = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return manager.versionName;
        } catch (NameNotFoundException e) {
            return "Unknown";
        }
    }

    /**
     * 获得设别IMEI号码
     *
     * @param context
     * @return
     */
    public static String getDeviceIMEI(Context context) {
        TelephonyManager tm = ((TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE));
        return tm.getDeviceId();

    }

    /**
     * 获取本机mac 地址
     *
     * @param context
     * @return
     */
    public static String getMac(Context context) {
        String macStr = "";
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getMacAddress() != null) {
            macStr = wifiInfo.getMacAddress();// MAC地址
        }

        return macStr;
    }

    /**
     * 获得手机型号
     *
     * @return
     */
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }


    /**
     * 获得手机型号
     *
     * @return
     */
    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }

    /**
     * 获得UUID
     *
     * @return
     */
    public static String getUUID() {
        UUID ud = UUID.randomUUID();
        return ud.toString();
    }


    public static boolean isTablet(Context context) {
        LogProxy.d(TAG, "screenLayout=" + context.getResources().getConfiguration().screenLayout);
        LogProxy.d(TAG, "Configuration.SCREENLAYOUT_SIZE_MASK=" + Configuration.SCREENLAYOUT_SIZE_MASK);

        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static DisplayMetrics getDisplayMetrics() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((WindowManager) GlobalContext.getInstance().getSystemService(
                Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(
                displaymetrics);
        return displaymetrics;
    }

    public static float getScreenHeight() {
        return getDisplayMetrics().heightPixels;
    }

    public static float getScreenWidth() {
        return getDisplayMetrics().widthPixels;
    }

    /**
     * 检测Sdcard是否存在
     *
     * @return
     */
    public static boolean isExitsSdcard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    /**
     * 生成 六位随机数
     *
     * @return
     */
    public static long createSixBitRandom() {

        return (long) ((Math.random() * 9 + 1) * 100000);
    }

    /**
     * 退出应用程序
     */
    @SuppressWarnings("deprecation")
    public static void AppExit(Context context) {
        try {

            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }


    public static String intToIp(int i) {

        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }


    /**
     * 获得 IP 地址
     *
     * @param context
     * @return
     */
    public static String getLocalIPAddress(Context context) {
        if (isWifi(context)) {
            //获取wifi服务
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            //判断wifi是否开启
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            return intToIp(ipAddress);
        } else {
            try {
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                    NetworkInterface intf = en.nextElement();
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            return inetAddress.getHostAddress().toString();
                        }
                    }
                }
            } catch (SocketException ex) {

            }
            return null;
        }

    }

    /**
     * 获取View的尺寸大小
     *
     * @param view
     * @return
     */
    public final static int[] getViewSize(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int height = view.getMeasuredHeight();
        int width = view.getMeasuredWidth();
        return new int[]{width, height};
    }


    /**
     * 隐藏输入法
     *
     * @param context
     * @param focusView
     * @return
     */

    public final static boolean hideInputMethod(Context context, View focusView) {
        if (focusView == null) {
            return false;
        }
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (focusView instanceof EditText || focusView instanceof ClearEditText) {
            return im.hideSoftInputFromWindow(focusView.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//			return true;
        }
        return false;
    }


    /**
     * 、
     * 获得App meta data
     *
     * @param appInfo
     * @param key
     * @return
     */
    public static final String getAppMetaData(ApplicationInfo appInfo, String key) {
        if (appInfo == null) {
            return null;
        }
        try {
            return appInfo.metaData.get(key).toString();
        } catch (Exception e) {
            LogProxy.i(TAG, "Can not find " + key + " meta");
        }
        return null;
    }

    /**
     * 手机是否有sim卡
     *
     * @return true:有	false:没有
     */
    public static boolean isHaveSimCard(Context context) {
        try {
            TelephonyManager mgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return TelephonyManager.SIM_STATE_READY == mgr.getSimState();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断当前系统是小米V5或者V6
     *
     * @return
     */
    public static String invalidateSystem() {
        try {
            return Utility.getSystemVersion("ro.miui.ui.version.name");
        } catch (Exception e) {
            return "";
        }
    }


    /**
     * 判断当前系统版本（主要判断是小米系统为V5或者V6）
     * 参数：ro.miui.ui.version.name
     *
     * @param propName
     * @return
     */
    public static String getSystemVersion(String propName) {
        String line = "";
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return "";
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return line;
    }


    private static int getCharLength(int b) {
        int nbytes = 1;

        if (b >= 0xc0 && b <= 0xdf)
            nbytes = 2;// -- 1100 0000 to 1101 1111
        else if (b >= 0xe0 && b <= 0xef)
            nbytes = 3;//-- 1110 0000 to 1110 1111
        else if (b >= 0xf0 && b <= 0xf7)
            nbytes = 4;// -- 1111 0000 to 1111 0111
        else if (b >= 0xf8 && b <= 0xfb)
            nbytes = 5;// -- 1111 1000 to 1111 1011
        else if (b >= 0xfc && b <= 0xfd)
            nbytes = 6;// -- 1111 1100 to 1111 1101
        else if (b < 0x00 || b > 0x7f)
            nbytes = -1;

        return nbytes;
    }


    /**
     * 判断字符串是否是utf8格式
     *
     * @param text
     * @return
     */
    public static String filterUTF8(String text) {
        byte[] textBytes = null;
//		LogProxy.d(TAG, "text=" + text);
        try {
            textBytes = text.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
        StringBuilder sb = new StringBuilder();
//		LogProxy.d(TAG, "len:" + textBytes.length);
        int ch = 0;
        int len = 1;
        for (int i = 0; i < textBytes.length; i += len) {
            int temp = ((int) textBytes[i]) & 0xff;
            len = getCharLength(temp);
//			LogProxy.d(TAG, "textBytes[" + i + "]=" + textBytes[i] + "," + temp + "," + len);
            if (len < 0 || len > 3) {
            } else {
                sb.append(text.charAt(ch));
            }
            ch++;
        }
        return sb.toString();
    }


    public static int getStatusBarHeight(Activity activity) {
        /** 判断是否全屏  */
//		int flag = activity.getWindow().getAttributes().flags;
//		if ((flag & WindowManager.LayoutParams.FLAG_FULLSCREEN) ==
//				WindowManager.LayoutParams.FLAG_FULLSCREEN) {
//			return 0;
//		}
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object o = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = (Integer) field.get(o);
            return activity.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 新增获取bjmgf_sdk_strings.xml中的文字
     */

    public static String getString(String resourceIdName, Context context) {
        return context.getResources().getString(ReflectResourceId.getStringId(context, resourceIdName));
    }

    /**
     * 判断是否重复点击
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 检查路径是否有效
     *
     * @param str
     * @return
     */
    public final static boolean checkFilePath(String pathStr) {
        boolean valid = false;
        File file = new File(pathStr);
        if (file.exists()) {
            valid = true;
        } else {
            valid = false;
        }
        return valid;
    }


    /**
     * 获取文件扩展名
     *
     * @param file
     * @return
     */
    public static String getFileExtension(File file) {
        if (file != null && file.exists()) {
            String fileName = file.getName();
            if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
                return fileName.substring(fileName.lastIndexOf(".") + 1);
            } else {
                return "";
            }
        }
        return "";
    }

    /**
     * 判断是否是png or jpg图片
     *
     * @param path
     * @return
     */
    public final static boolean checkImagePath(String path) {
        if (!checkFilePath(path)) {
            return false;
        }
        File file = new File(path);
        String extenName = getFileExtension(file);
        if (StringUtility.isEmpty(extenName)) {
            return false;
        }
//		LogProxy.i(TAG, "extenName = " + extenName);
        if (extenName.toLowerCase().equals("jpg") || extenName.toLowerCase().equals("png")) {
            return true;
        }
        return false;
    }

    /**
     * 验证手机号码格式
     *
     * @param phoneNumber
     * @return
     */
    public static boolean checkPhoneNumberFormat(Context context, String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            ToastUtil.showMessage(context, getString(Resource.string.bjmgf_sdk_InputPhoneNumberNullStr,context));
            return false;
        } else if (!phoneNumber.matches(PHONE_REGEX_SIMPLE)) {
            ToastUtil.showMessage(context,getString(Resource.string.bjmgf_sdk_InputPhoneNumberErrorStr,context));
            return false;
        }
        return true;
    }

    /**
     * 判断应用是否安装
     * @param context - Context
     * @param packageName - string
     * @return true/false
     * */
    public static boolean isInstall(Context context, String packageName) {
        if (StringUtility.isEmpty(packageName)) {
            return false;
        }
        packageName = packageName.trim();
        boolean flag = false;
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> packageInfoList = packageManager
                  .getInstalledPackages(0);
        for (int i = 0; i < packageInfoList.size(); i++) {
            PackageInfo pak = (PackageInfo) packageInfoList.get(i);
            if (packageName.equals(pak.packageName.trim())) {
                flag = true;
                break;
            } else {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * 调用浏览器打开网页
     * @param activity
     * @param url
     */
    public final static void openUrl(Activity activity, String url) {
        if (StringUtility.isEmpty(url)) {
            return;
        }
        LogProxy.i(TAG, url);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        activity.startActivity(Intent.createChooser(intent,
                  activity.getResources().getString(ReflectResourceId.getStringId(activity,
                            Resource.string.bjmgf_sdk_open_webview))));
    }

    /**
     * 调用系统的选取图片
     */
    public final static void pickPicture(Activity activity) {
        /**
         * 	         部分使用联发科芯片的手机rom，会报AssertionError，源码如下
         *
         *     Cursor cursor = mContext.getContentResolver().query(
         CONTENT_URI, COUNT_PROJECTION ,
         String. format("%s in (%s)", Media._ID, builder.toString()),
         null, null);
         if (cursor == null) return false ;
         Utils.assertTrue(cursor.moveToNext());

         *    cursor.moveToNext()为false时，表示只有1张图片，但是Utils.assertTrue就会抛出
         *    AssertionError
         *    也就是说只有1张图片时就无法调用图库
         *    做异常处理
         *    但代码是在打开Gallery应用的时候才报错误，无法捕获，所以只能做手工判断，图库中是否有1张图片，
         *    其他设备都会如此处理 */
        LogProxy.i(TAG, "pick picture from gallery");
        Cursor cursor = activity.getContentResolver().query(
                Media.EXTERNAL_CONTENT_URI,
                new String[] {Media._ID},
                null,
                null,
                null);
        if (cursor != null) {
            if (cursor.getCount() == 1) {
                LogProxy.i(TAG, "have only one picture");
                Toast.makeText(activity,
                        activity.getResources().getString(ReflectResourceId.getStringId(activity,
                                Resource.string.bjmgf_sdk_photo_exception_hint)),
                        Toast.LENGTH_SHORT).show();
                cursor.close();
                return;
            }
            cursor.close();
        }
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(i, Pick_Picture_From_Gallery);
    }

    /**
     * 保存图片到指定路径 大图片要进行压缩
     * @param activity
     * @param srcPath
     * @return 返回图片路径
     */
    public final static String saveBitmap(Activity activity, String srcPath) {
        return saveBitmap(activity, srcPath, false);
    }

    /**
     * 保存图片到指定路径 扩展存储卡 大图片要进行压缩
     * @param activity
     * @param srcPath
     * @return 返回图片路径
     */
    public final static String saveBitmap(Activity activity, String srcPath, boolean saveToExpandStoreage) {
        if (!checkFilePath(srcPath)) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath, options);
        Bitmap bitmap = null;
        if (options.outWidth < Size_Max && options.outHeight < Size_Max) {
            bitmap = BitmapFactory.decodeFile(srcPath);
        } else {
            int width = 0, height = 0;
            float temp = 0f;
            if (options.outWidth > options.outHeight) {
                width = Size_Max;
                temp = width * options.outHeight / options.outWidth;
                height = (int)temp;
            } else {
                height = Size_Max;
                temp = height * options.outWidth / options.outHeight;
                width = (int)temp;
            }
            bitmap = getScaleBitmapFromFile(srcPath, width, height);
        }
        String appPath = "";
        if (saveToExpandStoreage) {
            appPath = activity.getApplicationContext().getExternalCacheDir().getAbsolutePath();
            LogProxy.d(TAG, "appPath=" + appPath);
        } else {
            appPath = activity.getApplicationContext().getFilesDir().getAbsolutePath();
        }
        String tempPath = appPath + Temp_Dir_Relative_Path;
        File tempFileDir = new File(tempPath);
        if (!tempFileDir.exists()) {
            tempFileDir.mkdirs();
        }
        String outputPath = String.format("%s/%s", tempFileDir, Temp_attach_Image_Name);
        if (compressBitmapToFile(bitmap, outputPath)) {
            LogProxy.i(TAG, "out put Path = " + outputPath);
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
        if (!checkFilePath(imagePath)) {
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
            bitmap.compress(Bitmap.CompressFormat.JPEG, JPG_Quality, fileStream);
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

    public static final boolean stringIsEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }


    /**
     * 判断悬浮窗权限是否打开
     * @param context
     * @return
     */
    public static boolean isSystemAlertWindowOpAllowed(Context context) {
        final int version = Build.VERSION.SDK_INT;
        LogProxy.i(TAG, "version = " + version);
        if (version >= 19) {
            int op = 24;  // 自己写就是24  为什么是24?看AppOpsManager (默认值)
            try {
                Class<?> clazz = AppOpsManager.class;
                Field field = clazz.getDeclaredField("OP_SYSTEM_ALERT_WINDOW");
                op = field.getInt(null);
                LogProxy.i(TAG, "op = " + op);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            return checkOp(context, op);
        } else {
            LogProxy.i(TAG, "permission = " + context.getApplicationInfo().permission);
            if ((context.getApplicationInfo().flags & 1 << 27) == 1) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 判断某一权限是否打开
     * @param context
     * @param op       权限ID
     * @return
     */
    public static boolean checkOp(Context context, int op) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            AppOpsManager manager = (AppOpsManager) context
                      .getSystemService(Context.APP_OPS_SERVICE);
            try {
                Class<?> clazz = AppOpsManager.class;
                Method method = clazz.getMethod("checkOp", int.class, int.class, String.class);
                int permisson = (Integer) method.invoke(manager, op,
                          Binder.getCallingUid(), context.getPackageName());
                LogProxy.i(TAG, "permission = " + permisson);
                if (AppOpsManager.MODE_ALLOWED == permisson) { //
                    LogProxy.i(TAG, "allowed");
                    return true;
                } else {
                    LogProxy.i(TAG, "ignored");
                }
            } catch (Exception e) {
                LogProxy.w(TAG, e.getMessage());
            }
        } else {
            LogProxy.w(TAG, "Below API 19 cannot invoke!");
        }
        return false;
    }

    /**
     * 跳转到应用详情页面
     */
    public static void goToMiuiSettingPage(Context context){
        try {
            Log.i(TAG, "com.miui.securitycenter");
            Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            localIntent.setClassName("com.miui.securitycenter","com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            localIntent.putExtra("extra_pkgname", context.getPackageName());
            context.startActivity(localIntent);
        } catch (ActivityNotFoundException localActivityNotFoundException) {
            Log.i(TAG, "ACTION_APPLICATION_DETAILS_SETTINGS");
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
            intent.setData(uri);
            context.startActivity(intent);
        }
    }



}
