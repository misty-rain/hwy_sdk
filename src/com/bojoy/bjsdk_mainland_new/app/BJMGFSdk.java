package com.bojoy.bjsdk_mainland_new.app;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.bojoy.bjsdk_mainland_new.app.tools.BJMGFSDKTools;
import com.bojoy.bjsdk_mainland_new.app.tools.DockTypeTools;
import com.bojoy.bjsdk_mainland_new.app.tools.PayTools;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.eventhandler.EventHandler;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BJMGFSdkEvent;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BJMGFSdkListener;
import com.bojoy.bjsdk_mainland_new.model.entity.BaseAppPassport;
import com.bojoy.bjsdk_mainland_new.model.entity.PayOrderData;
import com.bojoy.bjsdk_mainland_new.model.entity.RechargeData;
import com.bojoy.bjsdk_mainland_new.presenter.account.IAccountCenterPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.account.impl.AccountCenterPresenterImpl;
import com.bojoy.bjsdk_mainland_new.presenter.init.IInitPresenter;
import com.bojoy.bjsdk_mainland_new.presenter.init.impl.InitSDKPresenterImpl;
import com.bojoy.bjsdk_mainland_new.support.eventbus.EventBus;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.BJMGFActivity;
import com.bojoy.bjsdk_mainland_new.ui.activity.base.WebViewActivity;
import com.bojoy.bjsdk_mainland_new.ui.dock.DockManagerBeta;
import com.bojoy.bjsdk_mainland_new.ui.view.cs.impl.CustomerServiceView;
import com.bojoy.bjsdk_mainland_new.ui.view.payment.impl.PaymentCenterView;
import com.bojoy.bjsdk_mainland_new.utils.DialogUtil;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.MessagePollingTool;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;
import com.bojoy.bjsdk_mainland_new.utils.Utility;
import com.bojoy.bjsdk_mainland_new.utils.payment.SKStartSmsPay;
import com.bojoy.bjsdk_mainland_new.widget.dialog.BJMGFDialog;
import com.bojoy.bjsdk_mainland_new.widget.dialog.ExitDialog;
import com.skymobi.pay.sdk.normal.zimon.EpsApplication;

/**
 * Created by wutao on 2015/12/16. * 直接与开发者对接的API接口类
 */
public class BJMGFSdk {

    private final String TAG = BJMGFSdk.class.getSimpleName();

    private static BJMGFSdk instance;
    private static final Object block = new Object();
    private BJMGFSdkListener listener;
    private EventHandler eventHandler;
    private BJMGFDialog bjmgfDialog;
    public Activity activity;
    private DockManagerBeta dockManager = new DockManagerBeta();
    private final int NETWORK_REQUESTCODE = 100;
    EventBus eventBus = EventBus.getDefault();

    EpsApplication epsApplication = new EpsApplication();
    private PayTools payTools = PayTools.getInstance();
    private PayOrderData payOrderData;



    private MessagePollingTool messageReceiver = null;

    private BJMGFSdk() {
        LogProxy.setPrefixName("BJMGFSdk");
        eventHandler = new EventHandler();
        payTools = PayTools.getInstance();
        LogProxy.i(TAG, "BJMGFSdk created");
    }

    public static BJMGFSdk getDefault() {
        if (instance == null) {
            synchronized (block) {
                if (instance == null) {
                    instance = new BJMGFSdk();
                }
            }
        }
        return instance;
    }

    /**
     * 是否开启 debug 日志 模式
     *
     * @param debug
     */
    public static void setDebugMode(boolean debug) {
        LogProxy.setDebugMode(debug);
    }

    public DockManagerBeta getDockManager() {
        return dockManager;
    }

    public void onEventMainThread(BJMGFSdkEvent event) {
        LogProxy.i(TAG, "BJMGFSdkEvent " + event.getEventId());
        switch (event.getEventId()) {
            case BJMGFSdkEvent.App_Need_Wifi:
                if (activity != null) {
                    activity.startActivityForResult(new Intent(
                              Settings.ACTION_WIFI_SETTINGS), NETWORK_REQUESTCODE);
                }
                break;
            case BJMGFSdkEvent.App_Init_Offline:
                break;
            case BJMGFSdkEvent.App_Login_Success:
                messageReceiver.start();
                break;
            case BJMGFSdkEvent.App_Closed:
                dockManager.removeDock();
                break;
            case BJMGFSdkEvent.APP_WELCOME_SHOW:
                dockManager.openDock();
                break;
            case BJMGFSdkEvent.App_Logout:
                LogProxy.i(TAG, "close dock");
                dockManager.closeDock();
                break;
            case BJMGFSdkEvent.App_Exit: // SDK 退出
                dockManager.removeDock();
                messageReceiver.stop();
                break;
            case BJMGFSdkEvent.App_Switch_Account://切换账号or 登出
                dockManager.removeDock();
                break;
            case BJMGFSdkEvent.RECHARGE_SUCCESS:
                break;
            case BJMGFSdkEvent.RECHARGE_FAIL:
                break;
            case BJMGFSdkEvent.Get_Offline_Message:
                if (BJMGFSDKTools.getInstance().getOfflineMsgFlag()) {
                    dockManager.notifyMessage();
                }
                break;
            case BJMGFSdkEvent.APP_PERFECT_DATA: //完善资料事件
                DialogUtil.showTryChangeDialog(dockManager.getActivity(), 200, bjmgfDialog);
                break;
            default:
                break;
        }
    }

    /**
     * 初始化BJMGFSDK
     *
     * @param activity          —— 应用Activity
     * @param appId             —— 应用id号
     * @param appKey            —— 应用key
     * @param platformId        平台ID
     * @param version           sdk 版本号
     * @param channel           —— 应用渠道
     * @param online            —— 设置游戏或应用是否为必须联网类型
     * @param screenOrientation —— 游戏或应用的横竖屏参数
     *                          BJMGFGlobalData.BJMGF_Screen_Orientation_Landscape /
     *                          BJMGFGlobalData.BJMGF_Screen_Orientation_Portrait
     * @param listener          —— 回调接口
     */
    private void initSdk(Activity activity, String appId, String appKey,
                         String platformId, String version, String channel, boolean online,
                         int screenOrientation, BJMGFSdkListener listener) {
        this.listener = listener;
        messageReceiver = new MessagePollingTool(activity);
        BJMGFSDKTools.getInstance().setScreenOrientation(screenOrientation);
        /** Get appId, appKey and channel from Application meta data */
        ApplicationInfo appInfo = null;
        String channelMeta = null;
        String appIdMeta = null;
        String appKeyMeta = null;
        /** SK SMS Pay */
        String SKMerchantId = null;
        String SKAppId = null;
        String SKMerchantPasswd = null;
        try {
            appInfo = activity.getPackageManager().getApplicationInfo(
                      activity.getPackageName(), PackageManager.GET_META_DATA);

            channelMeta = Utility.getAppMetaData(appInfo, "BJMGF_CHANNEL");
            appIdMeta = Utility.getAppMetaData(appInfo, "BJMGF_APPID");
            appKeyMeta = Utility.getAppMetaData(appInfo, "BJMGF_APPKEY");
            SKMerchantId = Utility.getAppMetaData(appInfo, "ZMMerchantId");
            SKAppId = Utility.getAppMetaData(appInfo, "ZMAppId");
            SKMerchantPasswd = Utility.getAppMetaData(appInfo, "ZMAppSecret");

        } catch (PackageManager.NameNotFoundException e) {
            LogProxy.i(TAG, "Not get appInfo " + activity.getPackageName());
        }
        if (StringUtility.isEmpty(appId)) {
            appId = StringUtility.isEmpty(appIdMeta) ? "" : appIdMeta;
        }
        if (StringUtility.isEmpty(channel)) {
            channel = StringUtility.isEmpty(channelMeta) ? "" : channelMeta;
        }
        if (StringUtility.isEmpty(appKey)) {
            appKey = StringUtility.isEmpty(appKeyMeta) ? "" : appKeyMeta;
        }
        if (StringUtility.isEmpty(SKMerchantId) || StringUtility.isEmpty(SKAppId) || StringUtility.isEmpty(SKMerchantPasswd)) {
            LogProxy.e(TAG, "SKStartSmsPay.init : get SKStartSmsPay init info failed from Application meta data!");
        } else {
            SKStartSmsPay.init(SKMerchantId, SKAppId, SKMerchantPasswd);
        }

        BaseAppPassport.setAppId(appId);
        BaseAppPassport.setAppKey(appKey);
        BaseAppPassport.setChannel(channel);
        BaseAppPassport.setPlatformId(platformId);
        BaseAppPassport.setVersion(version);

        dockManager.createDock(activity.getApplicationContext(), activity);

        if (BJMGFSDKTools.getInstance().isCurrUserStatusOnLine())
            dockManager.openDock();
        else
            dockManager.closeDock();

        if (!BJMGFSDKTools.getInstance().isCurrUserStatusOnLine()) {
            if (!eventBus.isRegistered(this)) {
                eventBus.register(this);
                //epsApplication.onStart(activity.getApplicationContext());
            }

            IInitPresenter initPresenter = new InitSDKPresenterImpl(activity,
                      null);
            initPresenter.initSDK(activity);
        }

    }

    /**
     * @param activity
     * @param appId
     * @param appKey
     * @param channel
     * @param online
     * @param screenOrientation
     * @param listener
     * @param userInner         -- 1-测试环境 0-外网环境
     * @param isInReview        -- 1-正在审核 0-不在审核 （主要是IOS使用）
     * @param sdkType           -- 类型（同setSdkType）
     * @param wapRecharge       -- wap充值标识 1-wap充值
     * @param platform          -- 1-标识好玩友平台
     * @param productId         -- 产品ID号，用来做数据采集
     * @param gameVersion       -- 产品版本号
     * @param operator          -- 产品运营
     * @param gameDomain        -- 产品数据采集域名
     */
    public void initSdk(Activity activity, String appId, String appKey,
                        String channel, boolean online, int screenOrientation,
                        BJMGFSdkListener listener, int userInner, int isInReview,
                        int sdkType, int wapRecharge, String platform, String productId,
                        String gameVersion, String operator, String gameDomain) {

        messageReceiver = new MessagePollingTool(activity);
        DockTypeTools.getInstance().setType(sdkType);
        BJMGFSDKTools.getInstance().setWapRecharge(wapRecharge);
        BaseAppPassport.setPlatformId(platform);

        // for test 外网暂时只不支持社交版悬浮窗 因为平台的服务器还么在外网发布
        LogProxy.i(TAG, "testEnv " + userInner + ", type "
                  + DockTypeTools.getInstance().getType());

        initSdk(activity, appId, appKey, platform, gameVersion, channel,
                  online, screenOrientation, listener);
        LogProxy.i(TAG, "productId = " + productId);


        if (!BJMGFSDKTools.getInstance().isCurrUserStatusOnLine()) {
            if (!BJMGFSDKTools.getInstance().checkDialogType(bjmgfDialog,
                      BJMGFDialog.Page_Init)) {
                bjmgfDialog = new BJMGFDialog((Context) activity, activity,
                          BJMGFDialog.Page_Init);
                bjmgfDialog.show();
            }
        }
    }

    /**
     * 退出BJMGFSDK窗口
     */
    public void sdkExit(Activity activity) {
        final ExitDialog dialog = new ExitDialog(activity);
        dialog.setTitle(Utility.getString(
                  Resource.string.bjmgf_sdk_login_out_quitGameBtnStr, activity));
        dialog.setMessage(Utility
                  .getString(
                            Resource.string.bjmgf_sdk_login_out_quitGameBtnStrMsg,
                            activity));
        dialog.setPositiveButton(Utility.getString(
                  Resource.string.bjmgf_sdk_dock_dialog_btn_ok_str, activity),
                  new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          eventBus.post(new BJMGFSdkEvent(BJMGFSdkEvent.App_Exit));
                          dialog.dismiss();
                          android.os.Process.killProcess(android.os.Process
                                    .myPid());
                          System.exit(0);
                      }
                  });

        dialog.setNegativeButton(
                  Utility.getString(
                            Resource.string.bjmgf_sdk_dock_dialog_btn_cancel_str,
                            activity), new View.OnClickListener() {

                      @Override
                      public void onClick(View v) {
                          dialog.dismiss();
                      }
                  });
        dialog.show();
    }

    public void login(Context context) {
        if (!BJMGFSDKTools.getInstance().isCurrUserStatusOnLine) {
            BJMGFDialog bjmgfDialog = new BJMGFDialog(context, (Activity) context, BJMGFDialog.Page_Login);
            bjmgfDialog.show();
        }
    }

    /**
     * 切换账号 or 登出
     *
     * @param context
     */
    public void logout(Context context) {
        if (BJMGFSDKTools.getInstance().isCurrUserStatusOnLine) {
            IAccountCenterPresenter iAccountCenterPresenter = new AccountCenterPresenterImpl(context, null);
            iAccountCenterPresenter.logout(context);
            BJMGFDialog bjmgfDialog = new BJMGFDialog(context, (Activity) context, BJMGFDialog.Page_Login);
            bjmgfDialog.show();
        }
    }

    public void startSMSPay() {

    }

    /**
     * 充值账号
     *
     * @param activity
     * @param orderSerial —— 订单序列号
     * @param productId   —— 产品编号（目前暂时用不到）
     * @param productName —— 产品名称
     * @param count       —— 产品数量（目前暂时用不到）
     * @param money       —— 价格总额
     * @param serverId    —— 应用充值透传参数
     * @param roleId      ——  游戏角色Id
     */

    public void rechargeProduct(Activity activity, String orderSerial,
                                    String productId, String productName, int count, int money,
                                String serverId, String roleId) {
        //判断是否需要网页充值 1 - 网页充值 0 - 非网页充值
        if (!BJMGFSDKTools.getInstance().isWapRechargeFlag) {
            rechargeProductSDK(activity, productName, money, count,
                      orderSerial, "", serverId, "", roleId);
        } else {

            rechargeProductOriginal(activity, orderSerial, productId,
                      productName, count, money, serverId, roleId);

        }
    }

    /**
     * 用户充值网页充值
     *
     * @param activity    ——  启动界面
     * @param orderSerial —— 订单号
     * @param productId   —— 商品Id
     * @param productName ——  商品名称
     * @param count       —— 商品数量
     * @param money       —— 充值金额
     * @param serverId    —— 服务器Id
     * @param roleId      ——  角色Id
     */
    public void rechargeProductOriginal(Activity activity, String orderSerial,
                                        String productId, String productName, int count, double money,
                                        String serverId, String roleId) {

        RechargeData rechargeData = new RechargeData(orderSerial, productId, productName, count, money, serverId, roleId);
        payTools.setRechargeData(rechargeData);
        BJMGFActivity.canLandscape = true;
        Intent intent = new Intent(activity, WebViewActivity.class);

        activity.startActivity(intent);


    }

    /**
     * 用户充值非网页充值
     *
     * @param activity    —— 启动页面
     * @param productName ——  商品名
     * @param cash        —— 充值金额
     * @param count       —— 充值数量
     * @param orderSerial —— 订单号
     * @param sendUrl     —— 平台应用充值回调（发货）地址
     * @param appServerID —— 服务器Id
     * @param extend      —— 其他扩展信息
     * @param roleId      —— 角色Id
     */
    public void rechargeProductSDK(Activity activity, String productName,
                                   double cash, int count, String orderSerial, String sendUrl,
                                   String appServerID, String extend, String roleId) {
        if (BJMGFSDKTools.getInstance().getCurrentPassPort() == null) {
            Toast.makeText(
                      activity,
                      activity.getResources()
                                .getString(
                                          ReflectResourceId
                                                    .getStringId(
                                                              activity,
                                                              Resource.string.bjmgf_sdk_please_login_first)),
                      Toast.LENGTH_SHORT).show();
        } else {
            BJMGFActivity.canLandscape = false;
            payOrderData = new PayOrderData(productName, cash, (int) (cash * payTools.PAY_RATIO),
                      orderSerial, appServerID, sendUrl, roleId, roleId, "", extend,
                      SysConstant.PAYMENT_ORDERTYPE, "", appServerID);
            payTools.setPayOrderData(payOrderData);
            Intent intent = new Intent(activity, BJMGFActivity.class);
            intent.putExtra(BJMGFActivity.Page_Class_Name_Key,
                      PaymentCenterView.class.getCanonicalName());
            activity.startActivity(intent);
        }
    }

    /**
     * 设置服务器和角色信息
     *
     * @param serverID
     * @param serverName
     * @param roleID
     * @param roleName
     */
    public void setServerAndRole(String serverID, String serverName,
                                 String roleID, String roleName, String roleLevel) {
    /*    BJMGFSDKTools.getInstance().setServerAndRole(serverID, serverName, roleID, roleName,
                  roleLevel);*/
    }

    /**
     * 打开提交问题页面
     *
     * @param activity
     */
    public void sendQuestion(Activity activity) {
        if (!BJMGFSDKTools.getInstance().isCurrUserStatusOnLine) {
            Toast.makeText(
                      activity,
                      activity.getResources()
                                .getString(
                                          ReflectResourceId
                                                    .getStringId(
                                                              activity,
                                                              Resource.string.bjmgf_sdk_please_login_first)),
                      Toast.LENGTH_SHORT).show();
            return;
        }
        BJMGFActivity.canLandscape = true;
        Intent intent = new Intent(activity, BJMGFActivity.class);
        intent.putExtra(BJMGFActivity.Page_Class_Name_Key, CustomerServiceView.class.getCanonicalName());
        intent.putExtra(BJMGFActivity.Custom_Service_Tab_Id, ReflectResourceId
                  .getViewId(activity, Resource.id.bjmgf_sdk_sendQuestionBtnId));
        activity.startActivity(intent);
    }

    /**
     * 打开客服页面
     *
     * @param activity
     */
    public void openCustomService(Activity activity) {
        if (!BJMGFSDKTools.getInstance().isCurrUserStatusOnLine) {
            Toast.makeText(
                      activity,
                      activity.getResources()
                                .getString(
                                          ReflectResourceId
                                                    .getStringId(
                                                              activity,
                                                              Resource.string.bjmgf_sdk_switch_account_success)),
                      Toast.LENGTH_SHORT).show();
            return;
        }
        BJMGFActivity.canLandscape = true;
        Intent intent = new Intent(activity, BJMGFActivity.class);
        intent.putExtra(BJMGFActivity.Page_Class_Name_Key,
                  CustomerServiceView.class.getCanonicalName());
        activity.startActivity(intent);
    }

    public void onPause(Activity activity) {
        LogProxy.i(TAG, "onPause");
        if (!BJMGFSDKTools.getInstance().isCurrUserStatusOnLine())
            return;
        dockManager.hideDock();
    }

    public void onResume(Activity activity) {
        LogProxy.i(TAG, "onResume");
        if (!BJMGFSDKTools.getInstance().isCurrUserStatusOnLine())
            return;
        dockManager.openDock();
    }

    public void onDestroy(Activity activity) {
        LogProxy.i(TAG, "onDestroy");
        eventBus.unregister(this);
    }

    public void closeSdk() {
        messageReceiver.stop();
    }
}
