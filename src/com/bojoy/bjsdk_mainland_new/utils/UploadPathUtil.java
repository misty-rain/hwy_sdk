package com.bojoy.bjsdk_mainland_new.utils;



/**
 * 上传路径规则工具类封装
 *
 * @author songdawei
 */
public class UploadPathUtil {
    private static final String TAG = UploadPathUtil.class.getSimpleName();
    /**
     * 上传的图片后缀（统一)
     */
    private final static String DEFAULT_PIC_SUFFIX = ".jpg";
    /**
     * 上传的语音文件后缀（统一)
     */
    private final static String DEFAULT_VOICE_SUFFIX = ".amr";
    /**
     * 大图标志
     */
    private final static String BIG_IMG_FLAG = "_B";

    /**
     * 小图标志
     */
    private final static String SMALL_IMG_FLAG = "_S";

    // 头像存在目录-根据用户ID计算出路径算法
    private static PathConfig facePathConfig;
    // 相册存在目录-根据用户ID计算出路径算法
    private static PathConfig photoPathConfig;
    // IM聊天图片存在目录-根据md5字符串计算出路径算法
    private static PathConfig imgPathConfig;
    // IM聊天语音存在目录-根据md5字符串计算出路径算法
    private static PathConfig voicePathConfig;
    // 备份删除的数据
    private static String delBackPath;
    // OSS图片存在目录-根据md5字符串计算出路径算法
    private static PathConfig ossImgPathConfig;
    // 动态图片存在目录-根据md5字符串计算出路径算法
    private static PathConfig dynamicImgPathConfig;

    public void reload(Object arg0) {
        reload();
    }

    public void reload() {

        LogProxy.d(TAG, "[facePathConfig] root/layer/step->" + facePathConfig.getRootPath() + "/" + facePathConfig.getLayer() + "/" + facePathConfig.getStep() + " init success");
        LogProxy.d(TAG, "[photoPathConfig] root/layer/step->" + photoPathConfig.getRootPath() + "/" + photoPathConfig.getLayer() + "/" + photoPathConfig.getStep() + " init success");
        LogProxy.d(TAG, "[imgPathConfig] root/layer/step->" + imgPathConfig.getRootPath() + "/" + imgPathConfig.getLayer() + "/" + imgPathConfig.getStep() + " init success");
        LogProxy.d(TAG, "[voicePathConfig] root/layer/step->" + voicePathConfig.getRootPath() + "/" + voicePathConfig.getLayer() + "/" + voicePathConfig.getStep() + " init success");
        LogProxy.d(TAG, "[delBackPath]" + delBackPath + " init succe");
        LogProxy.d(TAG, "[ossImgPathConfig] root/layer/step->" + ossImgPathConfig.getRootPath() + "/" + ossImgPathConfig.getLayer() + "/" + ossImgPathConfig.getStep() + " init success");
        LogProxy.d(TAG, "[dynamicImgPathConfig] root/layer/step->" + dynamicImgPathConfig.getRootPath() + "/" + dynamicImgPathConfig.getLayer() + "/" + dynamicImgPathConfig.getStep() + " init success");
    }

    private UploadPathUtil() {

    }

    /**
     * 备份文件全路径
     *
     * @param filePath
     * @return
     */
    public static String buildDelPath(String filePath) {
        return new StringBuilder().append(delBackPath).append(filePath).toString();
    }

    /**
     * 按用户ID生成的大图片文件全路径
     *
     * @param configPath
     * @param filename
     * @return
     */
    public static String buildBigImgPath(String configPath, String filename) {
        return new StringBuilder().append(configPath).append("/").append(filename).append(BIG_IMG_FLAG).append(DEFAULT_PIC_SUFFIX).toString();
    }

    /**
     * 按用户ID生成的小图片文件全路径
     *
     * @param configPath
     * @param filename
     * @return
     */
    public static String buildSmallImgPath(String configPath, String filename) {
        return new StringBuilder().append(configPath).append("/").append(filename).append(SMALL_IMG_FLAG).append(DEFAULT_PIC_SUFFIX).toString();
    }

    /**
     * 按MD5路径算法生成的大图片文件全路径（聊天图片）
     *
     * @param md5Str
     * @return
     */
    public static String buildBigImgPathByMd5(String md5Str) {
        return new StringBuilder().append(getImgPathByMd5(md5Str)).append(BIG_IMG_FLAG).append(DEFAULT_PIC_SUFFIX).toString();
    }

    /**
     * 按MD5路径算法生成的小图片文件全路径（聊天图片）
     *
     * @param md5Str
     * @return
     */
    public static String buildSmallImgPathByMd5(String md5Str) {
        return new StringBuilder().append(getImgPathByMd5(md5Str)).append(SMALL_IMG_FLAG).append(DEFAULT_PIC_SUFFIX).toString();
    }

    /**
     * 按MD5路径算法生成的语音文件全路径（聊天语音）
     *
     * @param md5Str
     * @return
     */
    public static String buildVoicePath(String md5Str) {
        return new StringBuilder().append(getVoicePathByMd5(md5Str)).append(DEFAULT_VOICE_SUFFIX).toString();
    }

    /**
     * 按MD5路径算法生成的圈子动态上传图片文件全路径（圈子动态图片）
     *
     * @param md5Str
     * @return
     */
    public static String buildDynamicImgPathByMd5(String md5Str) {
        return new StringBuilder().append(getDynamicImgPathByMd5(md5Str)).append(DEFAULT_PIC_SUFFIX).toString();
    }

    /**
     * 按MD5路径算法生成的大图片文件全路径（OSS图片）
     *
     * @param md5Str
     * @return
     */
    public static String buildBigOssImgPathByMd5(String md5Str) {
        return new StringBuilder().append(getOssImgPathByMd5(md5Str)).append(BIG_IMG_FLAG).append(DEFAULT_PIC_SUFFIX).toString();
    }

    /**
     * 按MD5路径算法生成的小图片文件全路径（OSS图片）
     *
     * @param md5Str
     * @return
     */
    public static String buildSmallOssImgPathByMd5(String md5Str) {
        return new StringBuilder().append(getOssImgPathByMd5(md5Str)).append(SMALL_IMG_FLAG).append(DEFAULT_PIC_SUFFIX).toString();
    }

    /**
     * 获取头像存在目录
     *
     * @param uid
     * @return
     */
    public static String getFacePathByUid(long uid) {
        facePathConfig=new PathConfig();
        facePathConfig.setLayer(5);
        facePathConfig.setStep(500);
        facePathConfig.setRootPath("/AECDN/AppData/face");

        return getPath(uid, facePathConfig);
    }

    /**
     * 获取头像存在目录
     *
     * @param uid
     * @return
     */
    public static String getPhotoPathByUid(long uid) {
        return getPath(uid, photoPathConfig);
    }

    /**
     * IM聊天图片存在目录
     *
     * @param md5Str
     * @return
     */
    private static String getImgPathByMd5(String md5Str) {
        return getMd5Path(md5Str, imgPathConfig);
    }

    /**
     * OSS图片存在目录
     *
     * @param md5Str
     * @return
     */
    private static String getDynamicImgPathByMd5(String md5Str) {
        return getMd5Path(md5Str, dynamicImgPathConfig);
    }

    /**
     * OSS图片存在目录
     *
     * @param md5Str
     * @return
     */
    private static String getOssImgPathByMd5(String md5Str) {
        return getMd5Path(md5Str, ossImgPathConfig);
    }

    /**
     * IM聊天语音存在目录
     *
     * @param md5Str
     * @return
     */
    private static String getVoicePathByMd5(String md5Str) {
        return getMd5Path(md5Str, voicePathConfig);
    }

    private static String getMd5Path(String md5Str, PathConfig config) {
        return getMd5Path(md5Str, config.getRootPath(), config.getLayer(), config.getStep());
    }

    private static String getMd5Path(String md5Str, String rootPath, int layer, int step) {
        StringBuilder sb = new StringBuilder(rootPath);
        for (int i = 0; i < layer; i++) {
            sb.append("/").append(md5Str.substring(i * step, (i + 1) * step));
        }
        sb.append("/").append(md5Str.substring(layer * step));
        return sb.toString();
    }

    private static String getPath(long infoId, PathConfig config) {
        return getPath(infoId, config.getRootPath(), config.getLayer(), config.getStep());
    }

    /**
     * 根据具体资源的ID,算出最后的路径
     *
     * @param infoId   -对应资源ID
     * @param rootPath -访问资源的地址前缀
     * @param layer    -几层目录数
     * @param step     -每层文件数量
     * @return 返回算出来的对应该资源的地址
     */
    private static String getPath(long infoId, String rootPath, int layer, int step) {
        StringBuilder rootStr = new StringBuilder(rootPath);
        long gene = 1;
        for (int i = 1; i < layer; i++) {
            gene *= step;
        }
        long tempUserID = infoId;
        for (int i = 0; i < layer; i++) {
            if (layer != i + 1) {
                int temp = (int) (tempUserID / gene); // 取整数部分
                tempUserID = tempUserID % gene; // 取余数部分
                rootStr.append("/").append(temp);
                gene /= step;
            } else {
                rootStr.append("/").append(infoId);
            }
        }
        return rootStr.toString();
    }


}
