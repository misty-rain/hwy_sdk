package com.bojoy.bjsdk_mainland_new.utils;

import android.content.Context;
import android.text.TextUtils;

import com.bojoy.bjsdk_mainland_new.model.entity.BaseAppPassport;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 14-2-26. 字符串帮助类
 */
public class StringUtility {

    private static final String TAG = StringUtility.class.getSimpleName();

    private static final String ACCOUNT_REGEX = "^\\w*[a-zA-Z]{1}\\w*$";// 必须包1个字母
    // \w
    // ：表示数字字母下划线
    private static final String ACCOUNT_REGEX_SIMPLE = "^[0-9a-zA-Z_]{4,20}$";// (!_)(!.*_$)[\\w]{4,20}
    private static final String PASSWROD_REGEX = "[a-zA-Z0-9_]*";
    private static final String PHONE_REGEX = "^(1[3458][0-9]{9})$";
    private static final String PHONE_REGEX_SIMPLE = "^(1[0-9]{10})$";// 目前已经有17开头的手机号码，所以号码第二位不再限制
    private static final String ACCOUNT_ONLY_NUMBER = "^([0-9]+)$";// 目前已经有17开头的手机号码，所以号码第二位不再限制
    private static final String MAC_STR_REGULAR = "^([0-9a-fA-F]{2})(([:][0-9a-fA-F]{2}){5})$";// mac地址正则表达式
    private static final String EMAIL_REGEX = "^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$";
    private static final String PASSWORD_REGEX_OTHER = "[^%$&]{1,}";

    private StringUtility() {
    }

    private final static Pattern emailer = Pattern
              .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

    /**
     * 获得字符创文本的个数
     *
     * @param content
     * @param word
     * @param preCount
     * @return
     */
    public static int countWord(String content, String word, int preCount) {
        int count = preCount;
        int index = content.indexOf(word);
        if (index == -1) {
            return count;
        } else {
            count++;
            return countWord(content.substring(index + word.length()), word,
                      count);
        }
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return emailer.matcher(email).matches();
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    public static String getSmsText(Context context){
        return "onekey_" + SpUtil.getStringValue(context, "uuid", "") + "#" + BaseAppPassport.getAppId()
                + "#" + BaseAppPassport.getChannel() + "#" + Utility.getLocalIPAddress(context);
    }


    /**
     * 用来判断，字符串中是否含有null 字符  主要用在判断用户头像
     *
     * @param str
     * @return
     */
    public static boolean isExitsNullStr(String str) {
        if (!isEmpty(str)) {
            if (str.indexOf("null_") > -1)
                return true;
        }
        return false;

    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 字符串转布尔值
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 根据key得到列表
     *
     * @param json
     * @param key
     * @return
     */

    public static String getJsonByKey(String json, String key) {
        int index = json.indexOf(key + ":");
        int index1 = json.indexOf("]", index);
        String result = "";
        if (index != -1) {
            result = json.substring(key.length() + index + 1, index1 + 1);
        }

        return result;
    }

    /**
     * 根据开始，结束，周期解析拼接服务时间串
     *
     * @param startTime
     * @param endTime
     * @param week
     * @return
     */
    public static String getServiceTimeByTimeWithWeek(String startTime,
                                                      String endTime, String week) {
        String serviceTime = "";
        if (startTime != null && endTime != null && week != null) {
            String serviceWeek = "";
            String[] weekDay = week.split(",");
            for (int i = 0; i < weekDay.length; i++) {
                switch (Integer.parseInt(weekDay[i])) {
                    case 1:
                        serviceWeek += "周一" + ",";
                        break;

                    case 2:
                        serviceWeek += "周二" + ",";
                        break;

                    case 3:

                        serviceWeek += "周三" + ",";
                        break;

                    case 4:
                        serviceWeek += "周四" + ",";
                        break;

                    case 5:
                        serviceWeek += "周五" + ",";
                        break;

                    case 6:
                        serviceWeek += "周六" + ",";
                        break;
                    default:
                        serviceWeek += "周日" + ",";
                        break;
                }
            }

            String timeStr = startTime + "--" + endTime;
            serviceTime = serviceWeek
                      .substring(0, serviceWeek.lastIndexOf(","))
                      + "\t"
                      + timeStr;

        }
        return serviceTime;
    }

    /**
     * 按位置替换替换字符串
     *
     * @param source
     * @param index
     * @param before
     * @param after
     * @return
     */
    public static String replace(String source, int index, String before,
                                 String after) {
        Matcher matcher = Pattern.compile(before).matcher(source);
        for (int counter = 0; matcher.find(); counter++)
            if (counter == index)
                return source.substring(0, matcher.start()) + after
                          + source.substring(matcher.end(), source.length());
        return source;
    }

    /**
     * md5 加密
     *
     * @param string
     * @return
     */
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                      string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * md5 加密
     *
     * @param string
     * @return
     */
    public static String md5(byte[] string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);

        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public static byte[] int2Bytes(int num) {
        byte[] byteNum = new byte[2];
        for (int ix = 0; ix < 4; ++ix) {
            int offset = 32 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((num >> offset) & 0xff);
        }
        return byteNum;

    }

    /**
     * MD5加密
     */
    public static String MD5(String input) {
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(input.getBytes());
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            StringBuilder hexString = new StringBuilder();
            // 字节数组转换为 十六进制 数
            for (byte aMd : md) {
                String shaHex = Integer.toHexString(aMd & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * SHA1加密
     */
    public static String SHA1(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(input.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            // 字节数组转换为 十六进制 数
            for (byte aMessageDigest : messageDigest) {
                String shaHex = Integer.toHexString(aMessageDigest & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 判断字符串的长度在范围之内
     *
     * @param text
     * @param min
     * @param max
     * @return
     */
    public static final boolean stringInRange(String text, int min, int max) {
        if (isEmpty(text)) {
            return false;
        }
        if (min <= 0 || max <= 0 || min > max) {
            return false;
        }
        return text.length() >= min && text.length() <= max;
    }

    /**
     * 判断字符是否相同
     *
     * @param text
     * @return
     */
    public static final boolean stringIsSameLetter(String text) {
        if (isEmpty(text)) {
            return false;
        }
        char[] letters = text.toCharArray();
        char ch = letters[0];
        for (int i = 1; i < letters.length; i++) {
            if (letters[i] != ch) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否为简单字符串
     *
     * @param text
     * @param diff - 差值
     * @return
     */
    private static final boolean stringIsSimple(String text, int diff) {
        if (isEmpty(text)) {
            return false;
        }
        char[] letters = text.toCharArray();
        for (int i = 0; i < letters.length - 1; i++) {
            if (letters[i] - letters[i + 1] != diff) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符是否为升序
     *
     * @param text
     * @return
     */
    public static final boolean stringIsASC(String text) {
        return stringIsSimple(text, -1);
    }

    /**
     * 判断字符是否为降序
     *
     * @param text
     * @return
     */
    public static final boolean stringIsDESC(String text) {
        return stringIsSimple(text, 1);
    }

    /**
     * 新增获取bjmgf_sdk_strings.xml中的文字
     */

    public static String getString(Context mContext, String resourceIdName) {
        return mContext.getResources().getString(
                  ReflectResourceId.getStringId(mContext, resourceIdName));
    }

    /**
     * 新增验证验证码长度方法
     */
    public static boolean checkVerifyCodeLength(String code) {
        return code.length() == 6;

    }

    /**
     * 转换占位符字符串
     *
     * @param str
     * @param reg
     * @return
     */
    public static String convertStringFormat(String str, String reg) {
        return String.format(reg, str);
    }

    /**
     * 判断字符串是否还有1个字母
     *
     * @param text
     * @return
     */
    public static final boolean stringIncludeLetter(String text) {
        if (isEmpty(text)) {
            return false;
        }
        char[] letters = text.toCharArray();
        for (char c : letters) {
            if ((c >= 'a' && c <= 'z') ||
                      (c >= 'A' && c <= 'Z')) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkAccountNameValid(Context mContext, String account) {
        if (!StringUtility.stringInRange(account, 4, 20)) {
            ToastUtil
                      .showMessage(
                                mContext,
                                getString(
                                          mContext, getString(mContext, Resource.string.bjmgf_sdk_login_dialog_invalid_account_length)));
            return false;
        }
        if (!account.matches(ACCOUNT_REGEX)) {
            if (StringUtility.stringIncludeLetter(account)) {
                ToastUtil
                          .showMessage(
                                    mContext,
                                    getString(
                                              mContext, getString(mContext, Resource.string.bjmgf_sdk_login_dialog_invalid_account_special_symbol)));
            } else {
                ToastUtil
                          .showMessage(
                                    mContext,
                                    getString(
                                              mContext, getString(mContext, Resource.string.bjmgf_sdk_login_dialog_invalid_account_no_letter)));
            }
            return false;
        }
        if (StringUtility.stringIsSameLetter(account)) {
            ToastUtil
                      .showMessage(
                                mContext,
                                getString(
                                          mContext, getString(mContext, Resource.string.bjmgf_sdk_login_dialog_invalid_account_same_letter)));
            return false;
        }
        if (StringUtility.stringIsASC(account) || StringUtility.stringIsDESC(account)) {
            ToastUtil
                      .showMessage(
                                mContext,
                                getString(
                                          mContext, getString(mContext, Resource.string.bjmgf_sdk_login_dialog_invalid_account_simple)));
            return false;
        }
        return true;
    }

    /**
     * 检查找回密码输入框中的字符串 4-20位数字字母下划线 或 手机号
     *
     * @param account
     * @return
     */
    public static boolean checkFindPasswordPageAccountValid(Context mContext,
                                                            String account) {
        if (isEmpty(account)) {
            LogProxy.d(TAG, "checkFindPasswordPageAccountValid  ---------  1");
            ToastUtil
                      .showMessage(
                                mContext,
                                getString(
                                          mContext,
                                          Resource.string.bjmgf_sdk_login_dialog_invalid_findPassword_account_prompt));
            return false;
        } else if (account.matches(ACCOUNT_ONLY_NUMBER)) {// 手机号的判断
            if (!account.matches(PHONE_REGEX_SIMPLE)) {
                LogProxy.d(TAG,
                          "checkFindPasswordPageAccountValid  ---------  2");
                ToastUtil
                          .showMessage(
                                    mContext,
                                    getString(
                                              mContext,
                                              Resource.string.bjmgf_sdk_login_dialog_invalid_findPassword_account_prompt));
                return false;
            } else {
                return true;
            }
        } else if (!stringInRange(account, 4, 20)) {
            LogProxy.d(TAG, "checkFindPasswordPageAccountValid  ---------  3");
            ToastUtil
                      .showMessage(
                                mContext,
                                getString(
                                          mContext,
                                          Resource.string.bjmgf_sdk_login_dialog_invalid_findPassword_account_prompt));
            return false;
        } else if (!account.matches(ACCOUNT_REGEX)) {
            LogProxy.d(TAG, "checkFindPasswordPageAccountValid  ---------  4");
            ToastUtil
                      .showMessage(
                                mContext,
                                getString(
                                          mContext,
                                          Resource.string.bjmgf_sdk_login_dialog_invalid_findPassword_account_prompt));
            return false;
        } else if (stringIsSameLetter(account)) {
            LogProxy.d(TAG, "checkFindPasswordPageAccountValid  ---------  5");
            ToastUtil
                      .showMessage(
                                mContext,
                                getString(
                                          mContext,
                                          Resource.string.bjmgf_sdk_login_dialog_invalid_findPassword_account_prompt));
            return false;
        } else if (stringIsASC(account)) {
            LogProxy.d(TAG, "checkFindPasswordPageAccountValid  ---------  6");
            ToastUtil
                      .showMessage(
                                mContext,
                                getString(
                                          mContext,
                                          Resource.string.bjmgf_sdk_login_dialog_invalid_findPassword_account_prompt));
            return false;
        } else if (stringIsDESC(account)) {
            LogProxy.d(TAG, "checkFindPasswordPageAccountValid  ---------  7");
            ToastUtil
                      .showMessage(
                                mContext,
                                getString(
                                          mContext,
                                          Resource.string.bjmgf_sdk_login_dialog_invalid_findPassword_account_prompt));
            return false;
        } else if (!account.matches(ACCOUNT_REGEX_SIMPLE)) {
            LogProxy.d(TAG, "checkFindPasswordPageAccountValid  ---------  8");
            ToastUtil
                      .showMessage(
                                mContext,
                                getString(
                                          mContext,
                                          Resource.string.bjmgf_sdk_login_dialog_invalid_findPassword_account_prompt));
            return false;
        }

        return true;
    }

    public static boolean checkPasswordValid(Context mContext, String password) {
        return checkPasswordValid(mContext, password, "");
    }

    public static boolean checkPasswordValid(Context mContext, String password, String prefix) {
        if (!StringUtility.stringInRange(password, 6, 20)) {
            ToastUtil
                      .showMessage(
                                mContext, prefix
                                          + getString(mContext, Resource.string.bjmgf_sdk_login_dialog_invalid_password_length));
            return false;
        }
        if (!password.matches(PASSWROD_REGEX)) {
            ToastUtil
                      .showMessage(
                                mContext, prefix
                                          + getString(mContext, Resource.string.bjmgf_sdk_login_dialog_invalid_password_special_symbol));
            return false;
        }
        if (!password.matches(PASSWORD_REGEX_OTHER)) {
            ToastUtil
                      .showMessage(
                                mContext, prefix
                                          + getString(mContext, Resource.string.bjmgf_sdk_login_dialog_invalid_password_special_symbol));
            return false;
        }
        if (StringUtility.stringIsSameLetter(password) || StringUtility.stringIsASC(password)
                  || StringUtility.stringIsDESC(password)) {
            ToastUtil
                      .showMessage(
                                mContext, prefix
                                          + getString(mContext, Resource.string.bjmgf_sdk_login_dialog_invalid_password_simple));
            return false;
        }
        return true;
    }

    public static String getMoblieKey(String strMobileKey, int num) {
        String[] str = strMobileKey.split("\\|");
        if (str.length < 2) {
            return "";
        }
        return str[num];
    }

    /**
     * 新增检查手机格式
     * */
    public static boolean checkPhoneNumberFormat(Context context,String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            ToastUtil.showMessage(context,getString(context, Resource.string.bjmgf_sdk_InputPhoneNumberNullStr));
            return false;
        } else if (!phoneNumber.matches(PHONE_REGEX_SIMPLE)) {
            ToastUtil.showMessage(context,getString(context, Resource.string.bjmgf_sdk_InputPhoneNumberErrorStr));
            return false;
        }
        return true;
    }


    /**
     * 连接字符串Wap充值使用
     *
     * @param url    网络请求Url地址
     * @param params 网络请求参数
     * @return
     */
    public static String getAppendUrlAndParams(String url, Map<String, String> params) {
        StringBuffer sb = new StringBuffer();
        sb.append(url + "?");
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                sb.append(key).append("=").append(params.get(key)).append("&");

            }
        }
        sb = sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * 新增将手机号中间四位转换为“*”
     *
     * */
    public static String transformPhoneNumberType(String phoneNumbre) {
        StringBuffer stringBuffer = new StringBuffer(phoneNumbre);
        if (stringBuffer.length() < 8) {
            return phoneNumbre;
        }
        stringBuffer.replace(3, 7, "****");
        return stringBuffer.toString();
    }

}
