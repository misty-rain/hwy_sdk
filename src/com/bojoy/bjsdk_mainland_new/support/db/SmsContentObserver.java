package com.bojoy.bjsdk_mainland_new.support.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wutao on 2016/1/15.
 * 监听短信数据库 ，主要要在 发送短信验证码后 ，自动填充验证码，无需手动填写
 */
public class SmsContentObserver extends ContentObserver {

    private static final String TAG = SmsContentObserver.class.getSimpleName();
    private Context mContext;
    private Handler mHandler;

    public SmsContentObserver(Context context, Handler handler) {
        super(handler);
        this.mContext = context;
        this.mHandler = handler;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        //读取收件箱中指定号码的短信

        Cursor cursor = mContext.getContentResolver().query(Uri.parse("content://sms/inbox"), new String[]{"_id", "address", "read", "body"},
                " address=? and read=?", new String[]{"106901332929", "0"}, "_id desc");//按id排序，如果按date排序的话，修改手机时间后，读取的短信就不准了
        LogProxy.d(TAG, "cursor.isBeforeFirst() " + cursor.isBeforeFirst() + " cursor.getCount()  " + cursor.getCount());
        if (cursor != null && cursor.getCount() > 0) {
            ContentValues values = new ContentValues();
            values.put("read", "1");        //修改短信为已读模式
            cursor.moveToNext();
            int smsbodyColumn = cursor.getColumnIndex("body");
            String smsBody = cursor.getString(smsbodyColumn);
            LogProxy.d(TAG, "smsBody = " + smsBody);
            String smsCode = getDynamicPassword(smsBody);
            LogProxy.d(TAG, "smsCode = " + smsCode);
            Message message = Message.obtain();
            message.what = 1;
            message.obj = smsCode;
            mHandler.sendMessage(message);


        }

        //在用managedQuery的时候，不能主动调用close()方法， 否则在Android 4.0+的系统上， 会发生崩溃
        if (Build.VERSION.SDK_INT < 14) {
            cursor.close();
        }
    }


    /**
     * 从字符串中截取连续6位数字
     * 用于从短信中获取动态密码
     *
     * @param str 短信内容
     * @return 截取得到的6位动态密码
     */
    private String getDynamicPassword(String str) {
        Pattern continuousNumberPattern = Pattern.compile("[0-9\\.]+");
        Matcher m = continuousNumberPattern.matcher(str);
        String dynamicPassword = "";
        while (m.find()) {
            if (m.group().length() == 6) {
                dynamicPassword = m.group();
            }
        }
        return dynamicPassword;
    }


}
