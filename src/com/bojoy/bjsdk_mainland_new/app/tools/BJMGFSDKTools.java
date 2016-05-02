package com.bojoy.bjsdk_mainland_new.app.tools;

import android.content.Context;

import com.bojoy.bjsdk_mainland_new.api.BaseApi;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.model.entity.BackResultBean;
import com.bojoy.bjsdk_mainland_new.model.entity.BaseAppPassport;
import com.bojoy.bjsdk_mainland_new.model.entity.PassPort;
import com.bojoy.bjsdk_mainland_new.model.entity.UserData;
import com.bojoy.bjsdk_mainland_new.support.fastjson.JSON;
import com.bojoy.bjsdk_mainland_new.ui.page.base.BaseDialogPage;
import com.bojoy.bjsdk_mainland_new.utils.DomainUtility;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.SpUtil;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;
import com.bojoy.bjsdk_mainland_new.utils.Utility;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMGFDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wutao on 2015/12/16.
 * 将配置SDK 属性 方法 独立至此类
 * ps.请后续开发者将配置SDK 属性 相关业务公共代码，放在此类中 ，
 */
public class BJMGFSDKTools {

    private static final String TAG = BJMGFSDKTools.class.getSimpleName();

    //当前用户passPort
    public PassPort currentPassPort;

    //标识当前用户是否注册
    public boolean isRegister = false;

    //是否从登录列表中切换至输入账号密码界面
    public boolean isNeedSetAccount = false;

    //是否从注册页面进入
    public boolean isByRegister = false;

    //小游戏标志 3：小游戏
    public int mPlatform = 0;

    //记录对应页面
    public BaseDialogPage baseDialogPage = null;

    // 是否启用wap 充值
    private boolean wapRechargeFlag = true;

    //是否在线
    private boolean online;

    //屏幕方向 横屏 or 竖屏
    private int screenOrientation;

    //全局用户数据
    private UserData userData;
    //当前用户状态是否在线，主要用于判断登出
    public boolean isCurrUserStatusOnLine;
    //是否网页充值
    public boolean isWapRechargeFlag = true;
    // 当前用户UUID
    public String currentUserUUID;

    private String offlineMsgFlag = "";
    private String newWishMsgFlag = "";
    private String offlineTime = "";

    public boolean getOfflineMsgFlag() {
        if (offlineMsgFlag.equals("1")) {
            return true;
        } else {
            return false;
        }
    }

    public void setOfflineMsgFlag(String offlineMsgFlag) {
        this.offlineMsgFlag = offlineMsgFlag;
    }

    public String getNewWishMsgFlag() {
        return newWishMsgFlag;
    }

    public void setNewWishMsgFlag(String newWishMsgFlag) {
        this.newWishMsgFlag = newWishMsgFlag;
    }

    public String getOfflineTime() {
        return offlineTime;
    }

    public void setOfflineTime(String offlineTime) {
        this.offlineTime = offlineTime;
    }

    private static BJMGFSDKTools bjmgfsdkTools;


    private BJMGFSDKTools() {
    }

    public static BJMGFSDKTools getInstance() {
        if (bjmgfsdkTools == null) {
            synchronized (BJMGFSDKTools.class) {
                if (bjmgfsdkTools == null) {
                    bjmgfsdkTools = new BJMGFSDKTools();

                }
            }
        }
        return bjmgfsdkTools;
    }


    /**
     * 读取是否联网运行
     *
     * @return
     */
    public boolean isOnline() {
        return online;
    }

    /**
     * 设置游戏或应用是否为必须联网类型
     *
     * @param online
     */
    public void setOnline(boolean online) {
        this.online = online;
    }

    /**
     * 得到屏幕方向
     *
     * @return
     */
    public int getScreenOrientation() {
        return screenOrientation;
    }

    /**
     * 设置当前游戏或应用的横竖屏参数
     *
     * @param screenOrientation BJMGF_Screen_Orientation_Landscape / BJMGF_Screen_Orientation_Portrait
     */
    public void setScreenOrientation(int screenOrientation) {
        this.screenOrientation = screenOrientation;
    }

    /**
     * 得到当前用户passport
     *
     * @return
     */
    public PassPort getCurrentPassPort() {
        return currentPassPort;
    }

    /**
     * 设置当前用户Passport
     *
     * @param currentPassPort
     */
    public void setCurrentPassPort(PassPort currentPassPort) {
        this.currentPassPort = currentPassPort;
    }

    public boolean isCurrUserStatusOnLine() {
        return isCurrUserStatusOnLine;
    }

    public void setCurrUserStatusOnLine(boolean currUserStatusOnLine) {
        isCurrUserStatusOnLine = currUserStatusOnLine;
    }

    /**
     * 判断当前账号是否为试玩账号
     *
     * @return
     */
    public boolean isCurrUserTryPlay(Context context) {
        String accountName = BJMGFSDKTools.getInstance().getCurrentPassPort().getPp();
        if (!StringUtility.isEmpty(accountName)) {
            if (accountName.substring(accountName.length() - 2, accountName.length()).equals(SysConstant.TRY_LOGIN_PASSPORT_POSTFIX))
                return true;
        }
        return false;
    }


    /**
     * 设置是否使用原wap充值功能
     * 必须在调用充值接口之前设置
     *
     * @param flag
     */
    public void setWapRecharge(int flag) {
        LogProxy.i(TAG, "wap recharge = " + flag);
        setWapRechargeFlag(flag == 1);

    }

    /**
     * 设置是否网页充值
     *
     * @param isWapRechargeFlag 1代表网页充值 0代表非网页充值
     */
    public void setWapRechargeFlag(boolean isWapRechargeFlag) {
        this.isWapRechargeFlag = isWapRechargeFlag;
    }

    public boolean isWapRechargeFlag() {
        return wapRechargeFlag;
    }

    /**
     * 验证Dialog 类型
     *
     * @param type
     * @return
     */

    public boolean checkDialogType(BJMGFDialog bjmgfDialog, int type) {
        if (bjmgfDialog == null) {
            return false;
        }
        if (type == BJMGFDialog.Page_Init) {
            return false;
        }
        if (type == BJMGFDialog.Page_Login) {
            return false;
        }
        if (type == BJMGFDialog.Page_TryChange) {
            return false;
        }
        if (bjmgfDialog.getPageType() == type) {
            LogProxy.i(TAG, "has open same dialog " + type);
            return true;
        }
        if (bjmgfDialog.isShowing()) {
            if (bjmgfDialog.getPageType() == BJMGFDialog.Page_Init) {
                LogProxy.i(TAG, "has open init dialog");
                return true;
            }
            if (bjmgfDialog.getPageType() == BJMGFDialog.Page_Login) {
                LogProxy.i(TAG, "has open login dialog");
                return true;
            } else if (bjmgfDialog.getPageType() == BJMGFDialog.Page_Logout) {
                LogProxy.i(TAG, "has open logout dialog");
                bjmgfDialog.dismiss();
                return false;
            }
        }
        return false;
    }

    /**
     * 获得mac 地址，如果mac 地址为空 or  000000 时 ，那就取IMEI号
     * 此方法为
     *
     * @param context
     * @return
     */
    public String getBJMGFMac(Context context) {
        String macAddress = Utility.getMac(context);
        if (macAddress != "") {

            String[] macArrays = macAddress.split("\\D+");
            int sum = 0;
            for (String s : macArrays) {
                if (!s.equals(""))
                    sum += Integer.parseInt(s);
            }
            if (sum == 0) {
                macAddress = Utility.getDeviceIMEI(context);
            }
        }
        return macAddress;

    }

  /*  *//**
     * 获得基本参数集合 ，主要用来组装基本参数的集合，避免重复在每个方法内重复组装
     *
     * @param context             上下文
     * @param isContainDeviceInfo 此变量 主要用来判断 是否组合设备信息的参数，主要包括mac、device、model 参数
     * @return
     *//*
    public Map<String, String> getBaseParamsMap(Context context, boolean isContainDeviceInfo) {
        Map<String, String> params = new HashMap<String, String>();
        final String time = String.valueOf(System.currentTimeMillis());
        String uuid = SpUtil.getStringValue(context, "uuid", "");
        if (uuid != "") {
            params.put("uuid", uuid);
        }
        if (getCurrentPassPort() != null)
            params.put("token", getCurrentPassPort().getToken());
        params.put("appid", BaseAppPassport.appId);
        params.put("platformid", BaseAppPassport.platformId);
        params.put("version", BaseAppPassport.version);
        params.put("sign", StringUtility.md5(uuid + BaseAppPassport.appId + time + BaseAppPassport.appKey + BaseAppPassport.APP_SECRET));
        //  params.put("formatObjType", "");
        params.put("vtime", time);
        if (isContainDeviceInfo) {
            params.put("mac", BJMGFSDKTools.getInstance().getBJMGFMac(context));
            params.put("device", Utility.getDeviceIMEI(context));
            params.put("model", Build.MODEL);
        }

        return params;
    }*/

    /**
     * 获取游客试玩接口参数trykey
     *
     * @param context
     */
    public String getTryKey(Context context) {
        String MAC_STR_REGULAR = "^([0-9a-fA-F]{2})(([:][0-9a-fA-F]{2}){5})$";
        //下个版本需要将trykey 保存到SD卡
        String tempStr = Utility.getMac(context);
        LogProxy.d(TAG, "tempStr=" + tempStr);
        if (tempStr.matches(MAC_STR_REGULAR)) {
           /* String macFilterStr = BJMGFCommon.getAndroidMacFilter();
            String[] filter = macFilterStr.split("\\|");
            if (filter.length > 0) {//当需要过滤时才过滤
                for (int i = 0; i < filter.length; i++) {
                    if (filter[i].endsWith(tempStr)) {
                        tempStr = SpUtil.getStringValue(context, "uuid", "");
                        break;
                    }
                }
            }*/
        } else {
            tempStr = SpUtil.getStringValue(context, "uuid", "");
        }
        return tempStr;

    }

    /**
     * 获取个人头像URL地址
     *
     * @param uid
     * @param faceName
     * @return
     */
    public String getPFFaceUrl(Context context, long uid, String faceName) {
        StringBuffer rootStr = new StringBuffer(DomainUtility.getInstance().getImageResDomain(context) + "/face");
        int layer = 5;
        int step = 500;
        long gene = 1;

        for (int i = 1; i < layer; i++) {
            gene *= step;
        }
        long tempUserID = uid;
        for (int i = 0; i < layer; i++) {
            if (layer != i + 1) {
                int temp = (int) (tempUserID / gene);
                tempUserID = tempUserID % gene;
                rootStr.append("/").append(temp);
                gene /= step;
            } else {
                rootStr.append("/").append(uid);
            }
        }
        rootStr.append("/").append(faceName).append("_S.jpg");
        return rootStr.toString();
    }

    /**
     * 设置 当前 用户 信息
     *
     * @param userData
     */
    public void setCurrUserData(UserData userData) {
        this.userData = userData;
    }

    /**
     * 得到当前用户信息
     *
     * @return
     */
    public UserData getCurrUserData() {
        return userData;
    }


    public UserData cloneUserData(UserData data) {
        UserData ud = new UserData();
        if (data == null) {
            return null;
        }
        ud.setUid(data.getUid());
        ud.setNick(data.getNick());
        ud.setBirth(data.getBirth());
        ud.setSex(data.getSex());
        ud.setFaceUrl(data.getFaceUrl());
        return ud;
    }

    /**
     * 拼装带身份认证的URL
     *
     * @return
     */
    public String getIdentityUrl(Context context,int sign) {
        Map<String, String> params = new HashMap<String, String>();
        final String time = String.valueOf(System.currentTimeMillis());
        String uuid = SpUtil.getStringValue(context, "uuid", "");
        if (uuid != "") {
            params.put("uuid", uuid);
        }
        params.put("appid", BaseAppPassport.appId);
        params.put("platformid", BaseAppPassport.platformId);
        params.put("token", getCurrentPassPort().getToken());
        params.put("redirect", String.valueOf(sign));

        String url = DomainUtility.getInstance().getServiceSDKDomain(context) + BaseApi.APP_WEB_SIGN + params.toString().replaceAll(", ", "&").replaceAll("\\{", "").replaceAll("\\}", "").trim();

        return url;
    }

    /**
     * 将保存 用户个人信息方法剥离 ，以此公用
     *
     * @param context        上下文
     * @param backResultBean 返回结果实体
     * @param json           json 结果
     */
    public void saveCurrentUserInfo(Context context, BackResultBean backResultBean, String json) {
        UserData userData = new UserData();
        if (!backResultBean.getObj().equals("{}")) {
            Map<String, com.bojoy.bjsdk_mainland_new.support.fastjson.JSONObject> baseParams = JSON.parseObject(json, Map.class);
            String jsonSt = baseParams.get("obj").getString("userInfo");
            Map<String, String> jsObjParams = JSON.parseObject(jsonSt, Map.class);
            userData = JSON.parseObject(String.valueOf(jsObjParams.get("baseInfo")), UserData.class);
            String url = BJMGFSDKTools.getInstance().getPFFaceUrl(context, Long.parseLong(String.valueOf(jsObjParams.get("uid"))), userData.getFaceUrl());
            userData.setFaceUrl(url);
        } else {
            userData.setUid(Long.valueOf(BJMGFSDKTools.getInstance().getCurrentPassPort().getUid()));
        }
        BJMGFSDKTools.getInstance().setCurrUserData(userData);
    }

    /**
     * 问题详情追加回复
     *
     * @param context
     * @param fid     问题记录ID(不能为空)
     * @param content 追加回复的内容(不能为空。最多100个汉字)
     * @return
     */
    public Map<String, String> getAppendQuestionParamsMap(Context context, String fid, String content) {
//        Map<String, String> params = getBaseParamsMap(context, true);
        Map<String, String> params = new HashMap<>();
        params.put("fid", fid);
        params.put("content", content);
        return params;
    }

}
