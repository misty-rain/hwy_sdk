package com.bojoy.bjsdk_mainland_new.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * 账号工具类
 *
 * @author sunhaoyang
 */
public class AccountUtil {

    /**
     * 将 用户token 保存在本地
     * @param context 上下文
     * @param uid 用户id
     * @param passPort 用户信息 包括 uid、userName、token
     */
    public static void saveAccount(Context context, String uid, String passPort) {
            AccountSharePUtils.put(context, uid, passPort + "," + System.currentTimeMillis());
    }

    /**
     * 获取本地所有用户
     *
     * @param context
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getAllPassport(Context context) {
        return (Map<String, String>) AccountSharePUtils.getAll(context);
    }

    /**
     * 清除所有本地信息
     *
     * @param context
     */
    public static void clearInfos(Context context) {
        Map<String, String> maps = getAllPassport(context);
        for (Map.Entry<String, String> entry : maps.entrySet()) {
            remove(context, entry.getKey());
        }
    }

    /**
     * 清除用户本地信息
     * *
     * @param context
     * @param uid 用户id
     */
    public static void remove(Context context, String uid) {
        AccountSharePUtils.remove(context, uid);
    }

    /**
     * 倒序排序登录账号
     *
     * @param arraylist
     */
    public static void sort(ArrayList<Long> arraylist) {
        // 冒泡排序
        for (int i = 0; i < arraylist.size(); i++) {
            for (int j = 1; j < arraylist.size(); j++) {
                Long a;
                if ((arraylist.get(j - 1)).compareTo(arraylist.get(j)) > 0) { // 比较两个整数的大小
                    a = arraylist.get(j - 1);
                    arraylist.set((j - 1), arraylist.get(j));
                    arraylist.set(j, a);
                }
            }
        }
        Collections.reverse(arraylist);
    }

}
