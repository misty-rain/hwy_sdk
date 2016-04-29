package com.bojoy.bjsdk_mainland_new.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

/**
 * 过滤非UTF8编码字节数大于3个的字符
 *
 * @author qiuzhiyuan
 */
public class BJMInputFilter implements InputFilter {

    private static final String TAG = BJMInputFilter.class.getCanonicalName();

    public BJMInputFilter() {

    }

    /*CharSequence source;  //输入的文字
    int start;  //开始位置
    int end;  //结束位置
    Spanned dest; //当前显示的内容
    int dstart;  //当前开始位置
    int dend; //当前结束位置    */
    public CharSequence filter(CharSequence src, int start, int end,
                               Spanned dest, int dstart, int dend) {
        String srcStr = src.toString();
        String dstStr = dest.toString();
        LogProxy.d(TAG, "srcStr=" + srcStr);
        LogProxy.d(TAG, "dstStr=" + dstStr);
        String addedStr = "";
        String temp = "";
        if (TextUtils.isEmpty(dstStr)) {
            temp = srcStr;
            LogProxy.d(TAG, "  1 temp=" + temp);
        }
        if (srcStr.length() > dstStr.length()) {
            addedStr = srcStr.substring(srcStr.indexOf(dstStr)
                      + dstStr.length());
            LogProxy.d(TAG, "addedStr=" + addedStr);
            temp = Utility.filterUTF8(addedStr);
            LogProxy.d(TAG, "  2 temp=" + temp);
            if (!TextUtils.isEmpty(temp)) {
                return null;
            }
            return temp;
        } else {
            LogProxy.d(TAG, "  3 temp=" + temp);
            return null;
        }
    }

}
