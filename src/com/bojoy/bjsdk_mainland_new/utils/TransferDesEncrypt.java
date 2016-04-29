package com.bojoy.bjsdk_mainland_new.utils;

/**
 * 游戏服务器中对所有的消息进行加密与解密的简单算法以及密钥的封装
 * 
 * @version 1.0
 * @author Administrator
 * 
 */
public abstract class TransferDesEncrypt {
    private static final String DEF_KEY = "D7f3y1Ef@U46Q2u8^Fy2M";
    public static final char SPLIT = '|';
    private static final String REG_SPLIT = "\\|";

    /**
     * 加密
     * 
     * @param str
     * @param key
     * @return
     */
    protected static String encrypt(String str, String key) {
        StringBuilder sb = new StringBuilder();
        int ch;
        if (!"".equals(str)) {
            int j = 0;
            for (int i = 0; i < str.length(); i++) {
                if (j > (key.length() - 1)) {
                    j = j % key.length();
                }
                ch = (int) str.charAt(i) + (int) key.charAt(j);
                if (ch > 65535) {
                    ch = ch % 65535;
                }
                sb.append((char) ch);
                j++;
            }
        }
        return sb.toString();
    }

    /**
     * 解密
     * 
     * @param str
     * @param key
     * @return
     */
    protected static String decrypt(String str, String key) {
        StringBuilder sb = new StringBuilder();
        int ch;
        int j = 0;
        for (int i = 0; i < str.length(); i++) {
            if (j > (key.length() - 1)) {
                j = j % key.length();
            }
            ch = ((int) str.charAt(i) + 65535 - (int) key.charAt(j));
            if (ch > 65535) {
                ch = ch % 65535;
            }
            sb.append((char) ch);
            j++;
        }
        return sb.toString();
    }

    /**
     * 加密
     * 
     * @param str
     * @return
     */
    public static String encryptDef(String... str) {
        if (str != null && str.length > 0) {
            if (str.length == 1) {
                return encrypt(str[0], DEF_KEY);
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < str.length - 1; i++) {
                    sb.append(str[i]).append(SPLIT);
                }
                return encrypt(sb.append(str[str.length - 1]).toString(), DEF_KEY);
            }
        }
        return null;
    }

    /**
     * 解密
     * 
     * @param str
     * @return
     */
    public static String decryptDef(String str) {
        return decrypt(str, DEF_KEY);
    }

    /**
     * 解密并分割转字符串数组
     * 
     * @param str
     * @return
     */
    public static String[] decryptDefAndSplit(String str) {
        return decrypt(str, DEF_KEY).split(REG_SPLIT, -1);
    }
}
