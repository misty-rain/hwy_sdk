package com.bojoy.bjsdk_mainland_new.presenter.init.impl;

import android.content.Context;

import com.bojoy.bjsdk_mainland_new.app.tools.BJMGFSDKTools;
import com.bojoy.bjsdk_mainland_new.congfig.ErrorCodeConstants;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BJMGFSdkEvent;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseReceiveEvent;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseRequestEvent;
import com.bojoy.bjsdk_mainland_new.eventhandler.event.BaseResultCallbackListener;
import com.bojoy.bjsdk_mainland_new.model.IInitSDKModel;
import com.bojoy.bjsdk_mainland_new.model.entity.BackResultBean;
import com.bojoy.bjsdk_mainland_new.model.impl.InitSDKModelImpl;
import com.bojoy.bjsdk_mainland_new.presenter.init.IInitPresenter;
import com.bojoy.bjsdk_mainland_new.support.eventbus.EventBus;
import com.bojoy.bjsdk_mainland_new.support.fastjson.JSON;
import com.bojoy.bjsdk_mainland_new.ui.view.init.IInitView;
import com.bojoy.bjsdk_mainland_new.utils.LogProxy;
import com.bojoy.bjsdk_mainland_new.utils.SpUtil;
import com.bojoy.bjsdk_mainland_new.utils.Utility;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by wutao on 2015/12/21.
 * 初始化SDK 控制器 实现
 */
public class InitSDKPresenterImpl implements IInitPresenter, BaseResultCallbackListener {


    private IInitSDKModel initSDKModel;
    private IInitView iInitView;
    EventBus eventBus = EventBus.getDefault();
    Context context;
    private final String TAG = InitSDKPresenterImpl.class.getSimpleName();


    public InitSDKPresenterImpl(Context context, IInitView iInitView) {
        initSDKModel = new InitSDKModelImpl();
        this.iInitView = iInitView;
        this.context = context;
    }


    /**
     * 初始化SDk
     *
     * @param context 山下文变量
     */
    @Override
    public void initSDK(Context context) {
        initSDKModel.getAppCollocation(context, this);

    }

    /**
     * APP 检查
     *
     * @param context 上下文环境
     */
    @Override
    public void appCheck(Context context) {
        initSDKModel.appCheck(context, this);
    }

    /**
     * 获得基本信息
     *
     * @param context
     */
    @Override
    public void appCollocation(Context context) {
        initSDKModel.getAppCollocation(context, this);
    }

    @Override
    public void getOfflineMsg(Context context) {
        initSDKModel.getOfflineMsg(context, this);
    }


    @Override
    public void onSuccess(Object response, int requestSessionEvent) {
        try {
            BackResultBean backResultBean = JSON.parseObject((String) response, BackResultBean.class);
            switch (requestSessionEvent) {
                case BaseRequestEvent.REQUEST_INIT: //初始化SDK  事件
                    if (backResultBean.getCode() == ErrorCodeConstants.ERROR_CODE_SUCCESS) {
                        if (backResultBean.getObj().indexOf("uuid") > -1) {
                            String cUUID = JSON
                                      .parseObject(backResultBean.getObj(), Map.class)
                                      .get("uuid").toString();
                            BJMGFSDKTools.getInstance().currentUserUUID = cUUID;
                            SpUtil.setStringValue(context, "uuid", cUUID); // 初始化成功后，将UUID
                            // 存储在本地
                        }
                        eventBus.post(new BaseReceiveEvent(
                                  BaseReceiveEvent.Flag_Success, ""));
                    } else {
                        eventBus.post(new BaseReceiveEvent(
                                  BaseReceiveEvent.Flag_Fail, backResultBean
                                  .getMsg()));
                    }

                    break;
                case BaseRequestEvent.REQUEST_APP_CHECK_UPDATE: //检查更新SDK 事件
                    if (backResultBean.getCode() == ErrorCodeConstants.ERROR_CODE_SUCCESS)
                        iInitView.openUpdateView();
                    else
                        iInitView.showError(backResultBean.getMsg());

                    break;
                case BaseRequestEvent.REQUEST_FIRST_DEPLOY: //第一次读取cdn配置事件,读取成功后 ，将文件保存，执行 初始化 操作
                    SpUtil.setStringValue(context, SysConstant.CDN_JSON_FILE_NAME, response.toString());
                    initSDKModel.initSDK(context, this);
                    break;

                case BaseRequestEvent.REQUEST_MESSAGE_OFFLINE:
                    BJMGFSDKTools.getInstance().setOfflineMsgFlag("");
                    BJMGFSDKTools.getInstance().setNewWishMsgFlag("");
                    BJMGFSDKTools.getInstance().setOfflineTime("");
                    if (backResultBean.getCode() == ErrorCodeConstants.ERROR_CODE_SUCCESS) {
                        String strR = JSON.parseObject(backResultBean.getObj(), Map.class).get("r").toString();
                        String strT = JSON.parseObject(backResultBean.getObj(), Map.class).get("t").toString();
                        String strRw = JSON.parseObject(backResultBean.getObj(), Map.class).get("rw").toString();
                        if (!Utility.stringIsEmpty(strR)) {
                            BJMGFSDKTools.getInstance().setOfflineMsgFlag(strR);
                        }
                        if (!Utility.stringIsEmpty(strRw)) {
                            BJMGFSDKTools.getInstance().setNewWishMsgFlag(strRw);
                        }
                        if (!Utility.stringIsEmpty(strT)) {
                            BJMGFSDKTools.getInstance().setOfflineTime(strT);
                        }
                        eventBus.post(new BJMGFSdkEvent(BJMGFSdkEvent.Get_Offline_Message));
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogProxy.e(TAG, e.getMessage());
        }

    }

    @Override
    public void onError(Call call, Exception exception, int requestSessionEvent) {
        eventBus.post(new BaseReceiveEvent(BaseReceiveEvent.Flag_Fail, exception.getMessage()));
    }
}
