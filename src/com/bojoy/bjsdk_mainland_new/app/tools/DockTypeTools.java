package com.bojoy.bjsdk_mainland_new.app.tools;

import com.bojoy.bjsdk_mainland_new.app.BJMGFSdk;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.model.entity.DockSdkType;

/**
 * Created by wutao on 2016/1/6.
 * 悬浮窗类型工具类
 * ps.请后续开发者将悬浮窗相关业务公共代码，放在此类中 ，
 */
public class DockTypeTools {

    private static DockTypeTools dockTypeTools;
    //悬浮窗类型
    private DockSdkType sdkType;

    private DockTypeTools() {
        sdkType=new DockSdkType();
    }

    public static DockTypeTools getInstance() {
        if (dockTypeTools == null) {
            synchronized (BJMGFSDKTools.class) {
                if (dockTypeTools == null) {
                    dockTypeTools = new DockTypeTools();
                }
            }
        }
        return dockTypeTools;
    }


    /**
     * 设置 悬浮窗类型
     * @param type
     *  @param type -- 0 不带悬浮窗   1普通悬浮窗 2社交版悬浮窗
     *             默认为1，该方法在initSdk之前调用
     */
    public void setType(int type) {
        this.sdkType.setSdkValue(type);
    }

    /**
     * 获得悬浮穿类型
     *  0 不带悬浮窗   1普通悬浮窗 2社交版悬浮窗
     * 				      默认为1，该方法在initSdk之前调用
     */
    public int getType() {
        return this.sdkType.getType();
    }

    /**
     * 正常类型
     * @return
     */
    public boolean isNormalType() {
        return this.sdkType.getType() == SysConstant.SDK_NORMAL_TYPE;
    }

    /**
     * 正常悬浮窗
     * @return
     */
    public boolean isNormalDockType() {
        return  this.sdkType.getType() == SysConstant.SDK_DOCK_TYPE;
    }

    /**
     * 消息悬浮窗
     * @return
     */
    public boolean isDockSnsType() {
        return  this.sdkType.getType() == SysConstant.SDK_DOCK_SNS_TYPE;
    }

    /**
     * 开启许愿
     * @return
     */
    public boolean isOpenWish() {
        return this.sdkType.isOpenWish();
    }

    /**
     * 轮训消息
     * @return
     */
    public boolean isOpenPollMsg() {
        return this.sdkType.isOpenPollMsg();
    }



}
